package com.foxpify.vertxorm.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import com.foxpify.vertxorm.repository.query.Query;
import com.foxpify.vertxorm.repository.query.RawQuery;
import com.foxpify.vertxorm.util.Futures;
import com.foxpify.vertxorm.util.Page;
import com.foxpify.vertxorm.util.PageRequest;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<ID, E> {
    void save(E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> save(E entity) {
        return Futures.toFuture(this::save, entity);
    }

    void insert(E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> insert(E entity) {
        return Futures.toFuture(this::insert, entity);
    }

    void update(E entity, Handler<AsyncResult<E>> resultHandler);

    default Future<E> update(E entity) {
        return Futures.toFuture(this::update, entity);
    }

    void delete(ID id, Handler<AsyncResult<Void>> resultHandler);

    default Future<Void> delete(ID id) {
        return Futures.toFuture(this::delete, id);
    }

    void find(ID id, Handler<AsyncResult<Optional<E>>> resultHandler);

    default Future<Optional<E>> find(ID id) {
        return Futures.toFuture(this::find, id);
    }

    void findAll(Handler<AsyncResult<List<E>>> resultHandler);

    default Future<List<E>> findAll() {
        return Futures.toFuture(this::findAll);
    }

    void query(Query<E> query, Handler<AsyncResult<List<E>>> resultHandler);

    default Future<List<E>> query(Query<E> query) {
        return Futures.toFuture(this::query, query);
    }

    void querySingle(Query<E> query, Handler<AsyncResult<Optional<E>>> resultHandler);

    default Future<Optional<E>> querySingle(Query<E> query) {
        return Futures.toFuture(this::querySingle, query);
    }

    default void getPage(PageRequest pageRequest, Handler<AsyncResult<Page<E>>> resultHandler) {
        getPage(pageRequest, new RawQuery<>(null), resultHandler);
    }

    default Future<Page<E>> getPage(PageRequest pageRequest) {
        return Futures.toFuture(this::getPage, pageRequest);
    }

    void getPage(PageRequest pageRequest, Query<E> query, Handler<AsyncResult<Page<E>>> resultHandler);

    default Future<Page<E>> getPage(PageRequest pageRequest, Query<E> query) {
        return Futures.toFuture(this::getPage, pageRequest, query);
    }
}
