package com.github.longdt.vertxorm.repository.postgresql;

import com.github.longdt.vertxorm.repository.*;
import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.core.Future;
import io.vertx.sqlclient.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class AbstractCrudRepository<ID, E> implements CrudRepository<ID, E> {
    protected Pool pool;
    private IdAccessor<ID, E> idAccessor;
    protected Function<Row, E> rowMapper;
    protected Function<E, Object[]> parametersMapper;
    protected Collector<Row, ?, List<E>> collector;
    protected SqlSupport sqlSupport;


    public void init(Pool pool, Configuration<ID, E> configuration) {
        this.pool = pool;
        this.rowMapper = Objects.requireNonNull(configuration.getRowMapper());
        this.collector = Collectors.mapping(rowMapper, Collectors.toList());
        this.parametersMapper = Objects.requireNonNull(configuration.getParametersMapper());
        this.idAccessor = Objects.requireNonNull(configuration.getIdAccessor());
        this.sqlSupport = new SqlSupportImpl(configuration.getTableName(), configuration.getColumnNames());
    }

    @Override
    public Future<E> save(SqlConnection conn, E entity) {
        if (idAccessor.getId(entity) == null) {
            return insert(conn, entity);
        }
        return upsert(conn, entity);
    }

    @Override
    public Future<E> insert(SqlConnection conn, E entity) {
        boolean genPk = idAccessor.getId(entity) == null;
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
    public Future<E> update(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        return conn.preparedQuery(sqlSupport.getUpdateSql())
                .execute(Tuple.wrap(params))
                .map(entity);
    }

    private Future<E> upsert(SqlConnection conn, E entity) {
        var params = parametersMapper.apply(entity);
        return conn.preparedQuery(sqlSupport.getUpsertSql())
                .execute(Tuple.wrap(params))
                .map(entity);
    }

    public Future<Void> delete(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getDeleteSql())
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(res -> {
                    if (res.rowCount() != 1) {
                        throw new EntityNotFoundException("Entity " + id + " is not found");
                    }
                    return null;
                });
    }

    @Override
    public Future<Optional<E>> find(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getQueryByIdSql())
                .mapping(rowMapper)
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(this::toEntity);
    }

    protected List<E> toList(SqlResult<List<E>> sqlResult) {
        return sqlResult.value();
    }

    protected Optional<E> toEntity(RowSet<E> rowSet) {
        var rowIter = rowSet.iterator();
        E entity = rowIter.hasNext() ? rowIter.next() : null;
        return Optional.ofNullable(entity);
    }

    @Override
    public Future<List<E>> findAll(SqlConnection conn) {
        return conn.query(sqlSupport.getQuerySql())
                .collecting(collector)
                .execute()
                .map(this::toList);
    }

    @Override
    public Future<List<E>> findAll(SqlConnection conn, Query<E> query) {
        String sql = sqlSupport.getSql(sqlSupport.getQuerySql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .collecting(collector)
                .execute(params)
                .map(this::toList);
    }

    @Override
    public Future<Optional<E>> find(SqlConnection conn, Query<E> query) {
        String sql = sqlSupport.getSql(sqlSupport.getQuerySql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .mapping(rowMapper)
                .execute(params)
                .map(this::toEntity);
    }

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
                    if (content.size() < pageRequest.getSize()) {
                        return Future.succeededFuture(new Page<>(pageRequest, pageRequest.getOffset() + content.size(), content));
                    }
                    return count(conn, query).map(cnt -> new Page<>(pageRequest, cnt, content));
                });
    }

    @Override
    public Future<Long> count(SqlConnection conn, Query<E> query) {
        return conn.preparedQuery(sqlSupport.getQuerySql(sqlSupport.getCountSql(), query))
                .execute(query.getQueryParams())
                .map(res -> res.iterator().next().getLong(0));
    }

    @Override
    public Future<Boolean> exists(SqlConnection conn, ID id) {
        return conn.preparedQuery(sqlSupport.getExistByIdSql())
                .execute(Tuple.of(idAccessor.id2DbValue(id)))
                .map(res -> res.size() > 0);
    }

    @Override
    public Future<Boolean> exists(SqlConnection conn, Query<E> query) {
        query.limit(1).offset(-1);
        String sql = sqlSupport.getSql(sqlSupport.getExistSql(), query);
        var params = getSqlParams(query);
        return conn.preparedQuery(sql)
                .execute(params)
                .map(res -> res.size() > 0);
    }

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

    @Override
    public Pool getPool() {
        return pool;
    }
}
