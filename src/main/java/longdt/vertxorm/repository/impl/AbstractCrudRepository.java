package longdt.vertxorm.repository.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import longdt.vertxorm.repository.CrudRepository;
import longdt.vertxorm.repository.EntityNotFoundException;
import longdt.vertxorm.util.Page;
import longdt.vertxorm.util.PageRequest;
import longdt.vertxorm.repository.query.Query;
import longdt.vertxorm.util.Futures;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCrudRepository<ID, E> implements CrudRepository<ID, E> {
    protected SQLClient sqlClient;
    protected Config<ID, E> conf;
    private String deleteSql;
    private String upsertSql;
    private String insertSql;
    private String updateSql;
    private String querySql;
    private String pageSql;
    private String countSql;

    public void init(SQLClient sqlClient, Config<ID, E> conf) {
        this.sqlClient = sqlClient;
        this.conf = conf;
        deleteSql = "DELETE FROM \"" + conf.tableName() + "\" WHERE \"" + conf.pkName() + "\" = ?";
        upsertSql = "INSERT INTO \"" + conf.tableName() + "\" "
                + conf.getColumnNames().stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(",", "(", ")"))
                + " VALUES "
                + conf.getColumnNames().stream().map(c -> "?").collect(Collectors.joining(",", "(", ")"))
                + " ON CONFLICT (\"" + conf.pkName() + "\") DO UPDATE SET "
                + conf.getColumnNames(false).stream().map(c -> "\"" + c + "\" = EXCLUDED.\"" + c + "\"").collect(Collectors.joining(", "));
        insertSql = "INSERT INTO \"" + conf.tableName() + "\" "
                + conf.getColumnNames(!conf.isPkAutoGen()).stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(",", "(", ")"))
                + " VALUES "
                + conf.getColumnNames(!conf.isPkAutoGen()).stream().map(c -> "?").collect(Collectors.joining(",", "(", ")"))
                + " RETURNING \"" + conf.pkName() + "\"";
        updateSql = "UPDATE \"" + conf.tableName() + "\""
                + " SET " + conf.getColumnNames(false).stream().map(c -> "\"" + c + "\" = ?").collect(Collectors.joining(","))
                + " WHERE \"" + conf.pkName() + "\" = ?";
        querySql = "SELECT " + conf.getColumnNames().stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(","))
                + " FROM \"" + conf.tableName() + "\"";
        countSql = "SELECT count(*) FROM \"" + conf.tableName() + "\"";
        pageSql = " LIMIT ? OFFSET ?";
    }

    @Override
    public void save(E entity, Handler<AsyncResult<E>> resultHandler) {
        if (conf.getId(entity) != null) {
            upsert(entity, resultHandler);
        } else {
            insert(entity, resultHandler);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insert(E entity, Handler<AsyncResult<E>> resultHandler) {
        JsonArray params = conf.toJsonArray(entity, !conf.isPkAutoGen());
        sqlClient.queryWithParams(insertSql, params, res -> {
            if (res.succeeded()) {
                conf.setId(entity, (ID) res.result().getResults().get(0).getValue(0));
                resultHandler.handle(Future.succeededFuture(entity));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void update(E entity, Handler<AsyncResult<E>> resultHandler) {
        JsonArray params = conf.toJsonArray(entity, false).add(conf.getId(entity));
        sqlClient.queryWithParams(updateSql, params, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture(entity));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    private void upsert(E entity, Handler<AsyncResult<E>> resultHandler) {
        JsonArray params = conf.toJsonArray(entity);
        sqlClient.updateWithParams(upsertSql, params, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture(entity));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    public void delete(ID id, Handler<AsyncResult<Void>> resultHandler) {
        sqlClient.updateWithParams(deleteSql, new JsonArray().add(id), res -> {
            if (res.succeeded()) {
                if(res.result().getUpdated() != 1) {
                    resultHandler.handle(Future.failedFuture(new EntityNotFoundException("Entity " + id + " is not found")));
                    return;
                }
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void find(ID id, Handler<AsyncResult<Optional<E>>> resultHandler) {
        String query = querySql + " WHERE \"" + conf.pkName() + "\"=?";
        sqlClient.querySingleWithParams(query, new JsonArray().add(id), toEntity(resultHandler));
    }

    private Handler<AsyncResult<ResultSet>> toList(Handler<AsyncResult<List<E>>> resultHandler) {
        return res -> {
            if (res.succeeded()) {
                try {
                    List<E> entities = res.result().getResults().stream()
                            .map(conf::toEntity).collect(Collectors.toList());
                    resultHandler.handle(Future.succeededFuture(entities));
                } catch (Exception e) {
                    resultHandler.handle(Future.failedFuture(e));
                }
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        };
    }

    private Handler<AsyncResult<JsonArray>> toEntity(Handler<AsyncResult<Optional<E>>> resultHandler) {
        return res -> {
            if (res.succeeded()) {
                try {
                    E entity = res.result() != null ? conf.toEntity(res.result()) : null;
                    resultHandler.handle(Future.succeededFuture(Optional.ofNullable(entity)));
                } catch (Exception e) {
                    resultHandler.handle(Future.failedFuture(e));
                }
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        };
    }

    @Override
    public void findAll(Handler<AsyncResult<List<E>>> resultHandler) {
        sqlClient.query(querySql, toList(resultHandler));
    }

    @Override
    public void query(Query<E> query, Handler<AsyncResult<List<E>>> resultHandler) {
        String queryStr = toSQL(querySql, query);
        JsonArray params = getSqlParams(query);
        sqlClient.queryWithParams(queryStr, params, toList(resultHandler));
    }

    @Override
    public void querySingle(Query<E> query, Handler<AsyncResult<Optional<E>>> resultHandler) {
        String queryStr = toSQL(querySql, query);
        JsonArray params = getSqlParams(query);
        sqlClient.querySingleWithParams(queryStr, params, toEntity(resultHandler));
    }


    @Override
    public void getPage(PageRequest pageRequest, Query<E> query, Handler<AsyncResult<Page<E>>> resultHandler) {
        Future<Integer> count = Futures.toFuture(sqlClient::querySingleWithParams, where(countSql, query), query.getConditionParams())
                .map(rs -> rs.getInteger(0));
        Future<ResultSet> pageResult = Future.future();
        query.limit(pageRequest.getSize()).offset(pageRequest.getOffset());
        String queryStr = toSQL(querySql, query);
        JsonArray params = getSqlParams(query);
        sqlClient.queryWithParams(queryStr, params, pageResult);
        Future<List<E>> entities = pageResult.map(rs -> rs.getResults().stream()
                .map(conf::toEntity).collect(Collectors.toList()));

        CompositeFuture.all(count, entities).map(cf -> new Page<E>(pageRequest, cf.resultAt(0), cf.resultAt(1))).setHandler(ar -> {
            if (ar.failed()) {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            } else {
                resultHandler.handle(Future.succeededFuture(ar.result()));
            }
        });
    }

    private String where(String sql, Query<E> query) {
        String condition = query.getConditionSql();
        if (condition != null) {
            sql = sql + " WHERE " + query.getConditionSql();
        }
        return sql;
    }

    private String toSQL(String sql, Query<E> query) {
        StringBuilder queryStr = new StringBuilder(sql);
        String condition = query.getConditionSql();
        if (condition != null) {
            queryStr.append(" WHERE ").append(condition);
        }
        if (query.orderBy() != null && !query.orderBy().isEmpty()) {
            queryStr.append(" ORDER BY ");
            query.orderBy().forEach(o -> queryStr.append("\"").append(o.getFieldName()).append("\" ")
                .append(o.isDescending() ? "DESC," : "ASC,"));
            queryStr.deleteCharAt(queryStr.length() - 1);
        }

        if (query.limit() >= 0) {
            queryStr.append(" LIMIT ?");
        }
        if (query.offset() >= 0) {
            queryStr.append(" OFFSET ?");
        }
        return queryStr.toString();
    }


    private JsonArray getSqlParams(Query<E> query) {
        if (query.limit() < 0 && query.offset() < 0) {
            return query.getConditionParams();
        }
        JsonArray params = new JsonArray().addAll(query.getConditionParams());
        if (query.limit() >= 0) {
            params.add(query.limit());
        }
        if (query.offset() >= 0) {
            params.add(query.offset());
        }
        return params;
    }


    public Config<ID, E> getConf() {
        return conf;
    }
}
