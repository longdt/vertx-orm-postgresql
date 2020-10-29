package com.github.longdt.vertxorm.util;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.sqlclient.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;

public class SQLHelper {
    public static <T, V, R> void executeInConnection(Pool pool, QuadConsumer<SqlConnection, T, V, Handler<AsyncResult<R>>> consumer, T arg1, V arg2, Handler<AsyncResult<R>> resultHandler) {
        pool.getConnection(getConn -> {
            if (getConn.failed()) {
                resultHandler.handle(Future.failedFuture(getConn.cause()));
            } else {
                final SqlConnection conn = getConn.result();
                consumer.accept(conn, arg1, arg2, res -> {
                    conn.close();
                    resultHandler.handle(res);
                });
            }
        });
    }

    public static <T, R> void executeInConnection(Pool pool, TriConsumer<SqlConnection, T, Handler<AsyncResult<R>>> consumer, T arg, Handler<AsyncResult<R>> resultHandler) {
        pool.getConnection(getConn -> {
            if (getConn.failed()) {
                resultHandler.handle(Future.failedFuture(getConn.cause()));
            } else {
                final SqlConnection conn = getConn.result();
                consumer.accept(conn, arg, res -> {
                    conn.close();
                    resultHandler.handle(res);
                });
            }
        });
    }

    public static <T, R> void executeInConnection(Pool pool, BiConsumer<SqlConnection, Handler<AsyncResult<R>>> consumer, Handler<AsyncResult<R>> resultHandler) {
        pool.getConnection(getConn -> {
            if (getConn.failed()) {
                resultHandler.handle(Future.failedFuture(getConn.cause()));
            } else {
                final SqlConnection conn = getConn.result();
                consumer.accept(conn, res -> {
                    conn.close();
                    resultHandler.handle(res);
                });
            }
        });
    }

    public static <T> void executeInConnection(Pool pool, Function<SqlConnection, Future<T>> action, Handler<AsyncResult<T>> resultHandler) {
        pool.getConnection(getConn -> {
            if (getConn.failed()) {
                resultHandler.handle(Future.failedFuture(getConn.cause()));
            } else {
                final SqlConnection conn = getConn.result();
                action.apply(conn).onComplete(res -> {
                    conn.close();
                    resultHandler.handle(res);
                });
            }
        });
    }

    public static <T> void inTransactionSingle(Pool pool, Function<SqlConnection, Future<T>> action, Handler<AsyncResult<T>> resultHandler) {
        executeInConnection(pool, conn -> {
            var txn = conn.begin();
            return action.apply(conn).compose(res -> Futures.toFuture((Consumer<Handler<AsyncResult<Void>>>) txn::commit).map(res));
        }, resultHandler);
    }

    public static Future<RowSet<Row>> query(Pool pool, String query) {
        var promise = Promise.<RowSet<Row>>promise();
        pool.query(query).execute(promise);
        return promise.future();
    }

    public static Future<RowSet<Row>> query(SqlConnection conn, String query) {
        var promise = Promise.<RowSet<Row>>promise();
        conn.query(query).execute(promise);
        return promise.future();
    }

    public static <R> Future<SqlResult<R>> query(SqlConnection conn, String query, Collector<Row, ?, R> collector) {
        var promise = Promise.<SqlResult<R>>promise();
        conn.query(query)
                .collecting(collector)
                .execute(promise);
        return promise.future();
    }
}
