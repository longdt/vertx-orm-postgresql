package com.github.longdt.vertxorm.util;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;

import java.util.function.BiFunction;

/**
 * <p>SQLHelper class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class SQLHelper {
    /**
     * <p>withTransaction.</p>
     *
     * @param pool a {@link io.vertx.sqlclient.Pool} object.
     * @param action a {@link java.util.function.BiFunction} object.
     * @param <T> a T object.
     * @return a {@link io.vertx.core.Future} object.
     */
    public static <T> Future<T> withTransaction(Pool pool, BiFunction<SqlConnection, Transaction, Future<T>> action) {
        return pool.withConnection(conn -> conn.begin().flatMap(txn -> action.apply(conn, txn)));
    }
}
