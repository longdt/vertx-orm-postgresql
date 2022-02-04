package com.github.longdt.vertxorm.repository.postgresql;

import com.github.longdt.vertxorm.repository.*;
import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;
import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.core.Future;
import io.vertx.sqlclient.*;
import io.vertx.sqlclient.impl.ArrayTuple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>Abstract AbstractCrudRepository class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public abstract class AbstractCrudRepository<ID, E> implements CrudRepository<ID, E> {
    protected Pool pool;
    private IdAccessor<ID, E> idAccessor;
    protected Function<Row, E> rowMapper;
    protected Function<E, Object[]> parametersMapper;
    protected Collector<Row, ?, List<E>> collector;
    protected SqlSupportImpl sqlSupport;


    /**
     * <p>init.</p>
     *
     * @param pool          a {@link io.vertx.sqlclient.Pool} object.
     * @param configuration a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public void init(Pool pool, Configuration<ID, E> configuration) {
        this.pool = pool;
        this.rowMapper = Objects.requireNonNull(configuration.getRowMapper());
        this.collector = Collectors.mapping(rowMapper, Collectors.toList());
        this.parametersMapper = Objects.requireNonNull(configuration.getParametersMapper());
        this.idAccessor = Objects.requireNonNull(configuration.getIdAccessor());
        this.sqlSupport = new SqlSupportImpl(configuration.getTableName(), configuration.getColumnNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<E> save(SqlConnection conn, E entity) {
        if (idAccessor.getId(entity) == null) {
            return insert(conn, entity, true);
        }
        return upsert(conn, entity);
    }

    @Override
    public Future<Collection<E>> saveAll(SqlConnection conn, Collection<E> entities) {
        if (entities.isEmpty()) {
            return Future.succeededFuture();
        }
        var entity = entities.iterator().next();
        if (idAccessor.getId(entity) == null) {
            return insertAll(conn, entities, true);
        }
        return upsertAll(conn, entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<E> insert(SqlConnection conn, E entity) {
        boolean genPk = idAccessor.getId(entity) == null;
        return insert(conn, entity, genPk);
    }

    private Future<E> insert(SqlConnection conn, E entity, boolean genPk) {
        var params = parametersMapper.apply(entity);
        String sql;
        Tuple paramsTuple;
        if (genPk) {
            sql = sqlSupport.getAutoIdInsertSql();
            paramsTuple = Tuples.shift(params, 1);
        } else {
            sql = sqlSupport.getInsertSql();
            paramsTuple = Tuple.wrap(params);
        }
        return conn.preparedQuery(sql)
                .execute(paramsTuple)
                .map(res -> {
                    try {
                        if (genPk) {
                            idAccessor.setId(entity, idAccessor.db2IdValue(res.iterator().next().getValue(0)));
                        }
                        return entity;
                    } catch (Exception e) {
                        throw new RuntimeException("Can't set id value of entity: " + entity.getClass().getName(), e);
                    }
                });
    }

    @Override
    public Future<Collection<E>> insertAll(SqlConnection conn, Collection<E> entities) {
        if (entities.isEmpty()) {
            return Future.succeededFuture();
        }
        var entity = entities.iterator().next();
        var genPk = idAccessor.getId(entity) == null;
        return insertAll(conn, entities, genPk);
    }

    private Future<Collection<E>> insertAll(SqlConnection conn, Collection<E> entities, boolean genPk) {
        String sql;
        List<Tuple> batch;
        if (genPk) {
            sql = sqlSupport.getAutoIdInsertSql();
            batch = toBatch(entities, params -> Tuples.shift(params, 1));
        } else {
            sql = sqlSupport.getInsertSql();
            batch = toBatch(entities);
        }
        return conn.preparedQuery(sql)
                .executeBatch(batch)
                .map(res -> {
                    try {
                        if (genPk) {
                            setIdAll(entities, res);
                        }
                        return entities;
                    } catch (Exception e) {
                        throw new RuntimeException("Can't set id value of entity in batch mode", e);
                    }
                });
    }

    private void setIdAll(Collection<E> entities, RowSet<Row> rows) {
        for (var iter = entities.iterator(); iter.hasNext() && rows != null; rows = rows.next()) {
            idAccessor.setId(iter.next(), idAccessor.db2IdValue(rows.iterator().next().getValue(0)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<E> update(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        return conn.preparedQuery(sqlSupport.getUpdateSql())
                .execute(Tuple.wrap(params))
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return entity;
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    @Override
    public Future<Collection<Boolean>> updateAll(SqlConnection conn, Collection<E> entities) {
        var batch = toBatch(entities);
        return conn.preparedQuery(sqlSupport.getUpdateSql())
                .executeBatch(batch)
                .map(rows -> checkExistedEntities(rows, new ArrayList<>(entities.size())));
    }

    private Collection<Boolean> checkExistedEntities(RowSet<Row> rows, Collection<Boolean> existedEntities) {
        do {
            existedEntities.add(rows.rowCount() == 1);
        } while ((rows = rows.next()) != null);
        return existedEntities;
    }

    private Collection<E> collectExistedEntities(RowSet<E> rows, Collection<E> existedEntities) {
        do {
            existedEntities.add(rows.rowCount() == 1 ? rows.iterator().next() : null);
        } while ((rows = rows.next()) != null);
        return existedEntities;
    }

    @Override
    public Future<E> update(SqlConnection conn, E entity, Query<E> query) {
        var params = parametersMapper.apply(entity);
        var id = params[0];
        if (id == null) {
            return Future.failedFuture(new IllegalArgumentException("id field must be set"));
        }
        var sqlBuilder = new StringBuilder();
        int index = sqlSupport.getUpdateSql(sqlBuilder, query);
        Tuple paramsTuple;
        if (index > sqlSupport.getColumnNames().size()) {
            paramsTuple = new ArrayTuple(index);
            Tuples.addAll(paramsTuple, params);
            query.appendQueryParams(paramsTuple);
        } else {
            paramsTuple = Tuple.wrap(params);
        }
        return conn.preparedQuery(sqlBuilder.toString())
                .execute(paramsTuple)
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return entity;
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    @Override
    public Future<Void> updateDynamic(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        var id = params[0];
        if (id == null) {
            return Future.failedFuture(new IllegalArgumentException("id field must be set"));
        }
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params);
        if (idx == 1) {
            return Future.succeededFuture();
        }
        idx = 1;
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                params[idx++] = params[i];
            }
        }
        return conn.preparedQuery(sqlBuilder.toString())
                .execute(Tuples.sub(params, 0, idx))
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return null;
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    @Override
    public Future<Collection<Boolean>> updateDynamicAll(SqlConnection conn, Collection<E> entities) {
        if (entities.isEmpty()) {
            return Future.succeededFuture();
        }
        var entity = entities.iterator().next();
        var params = parametersMapper.apply(entity);
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params);
        if (idx == 1) {
            return Future.succeededFuture();
        }

        var batch = toBatch(entities, entityParams -> {
            int nonNullIdx = 1;
            for (int i = 1; i < entityParams.length; ++i) {
                if (entityParams[i] != null) {
                    entityParams[nonNullIdx++] = entityParams[i];
                }
            }
            return Tuples.sub(entityParams, 0, nonNullIdx);
        });

        return conn.preparedQuery(sqlBuilder.toString())
                .executeBatch(batch)
                .map(rows -> checkExistedEntities(rows, new ArrayList<>(entities.size())));
    }

    @Override
    public Future<Integer> updateDynamicAll(SqlConnection conn, E entity, Query<E> query) {
        var params = parametersMapper.apply(entity);
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicAllSql(sqlBuilder, params, query);
        var paramsTuple = new ArrayTuple(idx);
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                paramsTuple.addValue(params[i]);
            }
        }
        if (paramsTuple.size() == 0) {
            return Future.succeededFuture();
        }
        query.appendQueryParams(paramsTuple);
        return conn.preparedQuery(sqlBuilder.toString())
                .execute(paramsTuple)
                .map(RowSet::rowCount);
    }

    @Override
    public Future<Void> updateDynamic(SqlConnection conn, E entity, Query<E> query) {
        var params = parametersMapper.apply(entity);
        var id = params[0];
        if (id == null) {
            return Future.failedFuture(new IllegalArgumentException("id field must be set"));
        }
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params, query);
        var paramsTuple = new ArrayTuple(idx).addValue(id);
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                paramsTuple.addValue(params[i]);
            }
        }
        if (paramsTuple.size() == 1) {
            return Future.succeededFuture();
        }
        query.appendQueryParams(paramsTuple);
        return conn.preparedQuery(sqlBuilder.toString())
                .execute(paramsTuple)
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return null;
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    private Future<E> upsert(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        return conn.preparedQuery(sqlSupport.getUpsertSql())
                .execute(Tuple.wrap(params))
                .map(entity);
    }

    private Future<Collection<E>> upsertAll(SqlConnection conn, Collection<E> entities) {
        var batch = toBatch(entities);
        return conn.preparedQuery(sqlSupport.getUpsertSql())
                .executeBatch(batch)
                .map(entities);
    }

    @Override
    public Future<E> merge(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        var id = params[0];
        if (id == null) {
            return Future.failedFuture(new IllegalArgumentException("id field must be set"));
        }
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params);
        if (idx == 1) {
            return Future.succeededFuture();
        }
        idx = 1;
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                params[idx++] = params[i];
            }
        }
        sqlBuilder.append(sqlSupport.getReturningAllSql());
        return conn.preparedQuery(sqlBuilder.toString())
                .mapping(rowMapper)
                .execute(Tuples.sub(params, 0, idx))
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return rowSet.iterator().next();
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    @Override
    public Future<Collection<E>> mergeAll(SqlConnection conn, Collection<E> entities) {
        if (entities.isEmpty()) {
            return Future.succeededFuture();
        }
        var entity = entities.iterator().next();
        var params = parametersMapper.apply(entity);
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params);
        if (idx == 1) {
            return Future.succeededFuture();
        }

        var batch = toBatch(entities, entityParams -> {
            int nonNullIdx = 1;
            for (int i = 1; i < entityParams.length; ++i) {
                if (entityParams[i] != null) {
                    entityParams[nonNullIdx++] = entityParams[i];
                }
            }
            return Tuples.sub(entityParams, 0, nonNullIdx);
        });
        sqlBuilder.append(sqlSupport.getReturningAllSql());
        return conn.preparedQuery(sqlBuilder.toString())
                .mapping(rowMapper)
                .executeBatch(batch)
                .map(rows -> collectExistedEntities(rows, new ArrayList<>(entities.size())));
    }

    @Override
    public Future<E> merge(SqlConnection conn, E entity, Query<E> query) {
        var params = parametersMapper.apply(entity);
        var id = params[0];
        if (id == null) {
            return Future.failedFuture(new IllegalArgumentException("id field must be set"));
        }
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicSql(sqlBuilder, params, query);
        var paramsTuple = new ArrayTuple(idx).addValue(id);
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                paramsTuple.addValue(params[i]);
            }
        }
        if (paramsTuple.size() == 1) {
            return Future.succeededFuture();
        }
        query.appendQueryParams(paramsTuple);
        sqlBuilder.append(sqlSupport.getReturningAllSql());
        return conn.preparedQuery(sqlBuilder.toString())
                .mapping(rowMapper)
                .execute(paramsTuple)
                .map(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        return rowSet.iterator().next();
                    } else {
                        throw new EntityNotFoundException("Entity with id: " + params[0] + " is not found");
                    }
                });
    }

    @Override
    public Future<List<E>> mergeAll(SqlConnection conn, E entity, Query<E> query) {
        var params = parametersMapper.apply(entity);
        var sqlBuilder = new StringBuilder();
        int idx = sqlSupport.getUpdateDynamicAllSql(sqlBuilder, params, query);
        var paramsTuple = new ArrayTuple(idx);
        for (int i = 1; i < params.length; ++i) {
            if (params[i] != null) {
                paramsTuple.addValue(params[i]);
            }
        }
        if (paramsTuple.size() == 0) {
            return Future.succeededFuture();
        }
        query.appendQueryParams(paramsTuple);
        sqlBuilder.append(sqlSupport.getReturningAllSql());
        return conn.preparedQuery(sqlBuilder.toString())
                .collecting(collector)
                .execute(paramsTuple)
                .map(SqlResult::value);
    }

    /**
     * <p>delete.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param id   a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    @Override
    public Future<Void> delete(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getDeleteByIdSql())
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(res -> {
                    if (res.rowCount() != 1) {
                        throw new EntityNotFoundException("Entity " + id + " is not found");
                    }
                    return null;
                });
    }

    @Override
    public Future<Void> deleteAll(SqlConnection conn, Collection<ID> ids) {
        List<?> queryParams;
        if (ids.getClass() == ArrayList.class) {
            queryParams = (List<?>) ids;
        } else {
            queryParams = new ArrayList<>(ids);
        }
        var query = QueryFactory.in(sqlSupport.getIdName(), queryParams);
        return conn.preparedQuery(sqlSupport.getQuerySql(sqlSupport.getDeleteSql(), query))
                .execute(query.getQueryParams())
                .mapEmpty();
    }

    @Override
    public Future<Void> deleteAll(SqlConnection conn) {
        return conn.preparedQuery(sqlSupport.getDeleteSql())
                .execute()
                .mapEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Optional<E>> find(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getQueryByIdSql())
                .mapping(rowMapper)
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(this::toEntity);
    }

    /**
     * <p>toEntity.</p>
     *
     * @param rowSet a {@link io.vertx.sqlclient.RowSet} object.
     * @return a {@link java.util.Optional} object.
     */
    protected Optional<E> toEntity(RowSet<E> rowSet) {
        var rowIter = rowSet.iterator();
        E entity = rowIter.hasNext() ? rowIter.next() : null;
        return Optional.ofNullable(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<E>> findAll(SqlConnection conn) {
        return conn.query(sqlSupport.getQuerySql())
                .collecting(collector)
                .execute()
                .map(SqlResult::value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<E>> findAll(SqlConnection conn, Query<E> query) {
        String sql = sqlSupport.getSql(sqlSupport.getQuerySql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .collecting(collector)
                .execute(params)
                .map(SqlResult::value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Optional<E>> find(SqlConnection conn, Query<E> query) {
        String sql = sqlSupport.getSql(sqlSupport.getQuerySql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .mapping(rowMapper)
                .execute(params)
                .map(this::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Page<E>> findAll(SqlConnection conn, Query<E> query, PageRequest pageRequest) {
        query.limit(pageRequest.getSize()).offset(pageRequest.getOffset());
        String sql = sqlSupport.getSql(sqlSupport.getQuerySql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .collecting(collector)
                .execute(params)
                .compose(sqlResult -> {
                    var content = sqlResult.value();
                    if (!content.isEmpty() && content.size() < pageRequest.getSize()) {
                        return Future.succeededFuture(new Page<>(pageRequest, pageRequest.getOffset() + content.size(), content));
                    }
                    return count(conn, query).map(cnt -> new Page<>(pageRequest, cnt, content));
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Long> count(SqlConnection conn, Query<E> query) {
        return conn.preparedQuery(sqlSupport.getQuerySql(sqlSupport.getCountSql(), query))
                .execute(query.getQueryParams())
                .map(res -> res.iterator().next().getLong(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> exists(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getExistByIdSql())
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(res -> res.size() > 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> exists(SqlConnection conn, Query<E> query) {
        query.limit(1).offset(-1);
        String sql = sqlSupport.getSql(sqlSupport.getExistSql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .execute(params)
                .map(res -> res.size() > 0);
    }

    /**
     * <p>getSqlParams.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    protected Tuple getSqlParams(Query<E> query) {
        if (query.limit() < 0 && query.offset() < 0) {
            return query.getQueryParams();
        }

        var params = Tuple.tuple();
        params = query.appendQueryParams(params);
        if (query.limit() >= 0) {
            params.addInteger(query.limit());
        }
        if (query.offset() >= 0) {
            params.addLong(query.offset());
        }
        return params;
    }

    protected List<Tuple> toBatch(Collection<E> entities, Function<Object[], Tuple> tupleMapper) {
        var batch = new ArrayList<Tuple>(entities.size());
        for (E e : entities) {
            batch.add(tupleMapper.apply(parametersMapper.apply(e)));
        }
        return batch;
    }

    protected List<Tuple> toBatch(Collection<E> entities) {
        var batch = new ArrayList<Tuple>(entities.size());
        for (E e : entities) {
            batch.add(Tuple.wrap(parametersMapper.apply(e)));
        }
        return batch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pool getPool() {
        return pool;
    }
}
