package com.github.longdt.vertxorm.util;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;

import java.util.function.BiFunction;

public class SQLHelper {
    public static <T> Future<T> withTransaction(Pool pool, BiFunction<SqlConnection, Transaction, Future<T>> action) {
        return pool.withConnection(conn -> conn.begin().flatMap(txn -> action.apply(conn, txn)));
    }
}
