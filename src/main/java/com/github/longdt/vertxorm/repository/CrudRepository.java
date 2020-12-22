package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;
import com.github.longdt.vertxorm.util.Futures;
import com.github.longdt.vertxorm.util.SQLHelper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface CrudRepository<ID, E> {
    default void save(E entity, Handler<AsyncResult<E>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::save, entity, resultHandler);
    }

    default Future<E> save(E entity) {
        return Futures.toFuture(this::save, entity);
    }

    void save(SqlConnection conn, E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> save(SqlConnection conn, E entity) {
        return Futures.toFuture(this::save, conn, entity);
    }

    default void insert(E entity, Handler<AsyncResult<E>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::insert, entity, resultHandler);
    }

    default Future<E> insert(E entity) {
        return Futures.toFuture(this::insert, entity);
    }

    void insert(SqlConnection conn, E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> insert(SqlConnection conn, E entity) {
        return Futures.toFuture(this::insert, conn, entity);
    }

    default void update(E entity, Handler<AsyncResult<E>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::update, entity, resultHandler);
    }

    default Future<E> update(E entity) {
        return Futures.toFuture(this::update, entity);
    }

    void update(SqlConnection conn, E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> update(SqlConnection conn, E entity) {
        return Futures.toFuture(this::update, conn, entity);
    }

    default void delete(ID id, Handler<AsyncResult<Void>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::delete, id, resultHandler);
    }

    default Future<Void> delete(ID id) {
        return Futures.toFuture(this::delete, id);
    }

    void delete(SqlConnection conn, ID id, Handler<AsyncResult<Void>> resultHandler);

    default Future<Void> delete(SqlConnection conn, ID id) {
        return Futures.toFuture(this::delete, conn, id);
    }

    default void find(ID id, Handler<AsyncResult<Optional<E>>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::find, id, resultHandler);
    }

    default Future<Optional<E>> find(ID id) {
        return Futures.toFuture(this::find, id);
    }

    void find(SqlConnection conn, ID id, Handler<AsyncResult<Optional<E>>> resultHandler);

    default Future<Optional<E>> find(SqlConnection conn, ID id) {
        return Futures.toFuture(this::find, conn, id);
    }

    default void findAll(Handler<AsyncResult<List<E>>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), (BiConsumer<SqlConnection, Handler<AsyncResult<List<E>>>>) this::findAll, resultHandler);
    }

    default Future<List<E>> findAll() {
        return Futures.toFuture(this::findAll);
    }

    void findAll(SqlConnection conn, Handler<AsyncResult<List<E>>> resultHandler);

    default Future<List<E>> findAll(SqlConnection conn) {
        return Futures.toFuture(this::findAll, conn);
    }

    default void findAll(Query<E> query, Handler<AsyncResult<List<E>>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::findAll, query, resultHandler);
    }

    default Future<List<E>> findAll(Query<E> query) {
        return Futures.toFuture(this::findAll, query);
    }

    void findAll(SqlConnection conn, Query<E> query, Handler<AsyncResult<List<E>>> resultHandler);

    default Future<List<E>> findAll(SqlConnection conn, Query<E> query) {
        return Futures.toFuture(this::findAll, conn, query);
    }

    default void find(Query<E> query, Handler<AsyncResult<Optional<E>>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::find, query, resultHandler);
    }

    default Future<Optional<E>> find(Query<E> query) {
        return Futures.toFuture(this::find, query);
    }

    void find(SqlConnection conn, Query<E> query, Handler<AsyncResult<Optional<E>>> resultHandler);

    default Future<Optional<E>> find(SqlConnection conn, Query<E> query) {
        return Futures.toFuture(this::find, conn, query);
    }

    default void findAll(PageRequest pageRequest, Handler<AsyncResult<Page<E>>> resultHandler) {
        findAll(QueryFactory.emptyQuery(), pageRequest, resultHandler);
    }

    default Future<Page<E>> findAll(PageRequest pageRequest) {
        return Futures.toFuture(this::findAll, pageRequest);
    }

    default void findAll(Query<E> query, PageRequest pageRequest, Handler<AsyncResult<Page<E>>> resultHandler) {
        SQLHelper.inTransactionSingle(getPool(), conn -> findAll(conn, query, pageRequest), resultHandler);
    }

    default Future<Page<E>> findAll(Query<E> query, PageRequest pageRequest) {
        return Futures.toFuture(this::findAll, query, pageRequest);
    }

    default void findAll(SqlConnection conn, PageRequest pageRequest, Handler<AsyncResult<Page<E>>> resultHandler) {
        findAll(conn, QueryFactory.emptyQuery(), pageRequest, resultHandler);
    }

    default Future<Page<E>> findAll(SqlConnection conn, PageRequest pageRequest) {
        return Futures.toFuture(this::findAll, conn, pageRequest);
    }

    void findAll(SqlConnection conn, Query<E> query, PageRequest pageRequest, Handler<AsyncResult<Page<E>>> resultHandler);

    default Future<Page<E>> findAll(SqlConnection conn, Query<E> query, PageRequest pageRequest) {
        return Futures.toFuture(this::findAll, conn, query, pageRequest);
    }

    default void count(Query<E> query, Handler<AsyncResult<Long>> resultHandler) {
        SQLHelper.executeInConnection(getPool(), this::count, query, resultHandler);
    }

    default Future<Long> count(Query<E> query) {
        return Futures.toFuture(this::count, query);
    }

    void count(SqlConnection conn, Query<E> query, Handler<AsyncResult<Long>> resultHandler);

    default Future<Long> count(SqlConnection conn, Query<E> query) {
        return Futures.toFuture(this::count, conn, query);
    }

    Pool getPool();
}
