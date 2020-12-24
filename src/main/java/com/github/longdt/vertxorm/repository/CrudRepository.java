package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<ID, E> {

    default Future<E> save(E entity) {
        return getPool().withConnection(conn -> save(conn, entity));
    }

    Future<E> save(SqlConnection conn, E entity);

    default Future<E> insert(E entity) {
        return getPool().withConnection(conn -> insert(conn, entity));
    }

    Future<E> insert(SqlConnection conn, E entity);

    default Future<E> update(E entity) {
        return getPool().withConnection(conn -> update(conn, entity));
    }

    Future<E> update(SqlConnection conn, E entity);

    default Future<Void> delete(ID id) {
        return getPool().withConnection(conn -> delete(conn, id));
    }

    Future<Void> delete(SqlConnection conn, ID id);

    default Future<Optional<E>> find(ID id) {
        return getPool().withConnection(conn -> find(conn, id));
    }

    Future<Optional<E>> find(SqlConnection conn, ID id);

    default Future<List<E>> findAll() {
        return getPool().withConnection(this::findAll);
    }

    Future<List<E>> findAll(SqlConnection conn);

    default Future<List<E>> findAll(Query<E> query) {
        return getPool().withConnection(conn -> findAll(conn, query));
    }

    Future<List<E>> findAll(SqlConnection conn, Query<E> query);

    default Future<Optional<E>> find(Query<E> query) {
        return getPool().withConnection(conn -> find(conn, query));
    }

    Future<Optional<E>> find(SqlConnection conn, Query<E> query);

    default Future<Page<E>> findAll(PageRequest pageRequest) {
        return findAll(QueryFactory.emptyQuery(), pageRequest);
    }

    default Future<Page<E>> findAll(Query<E> query, PageRequest pageRequest) {
        return getPool().withTransaction(conn -> findAll(conn, query, pageRequest));
    }

    default Future<Page<E>> findAll(SqlConnection conn, PageRequest pageRequest) {
        return findAll(conn, QueryFactory.emptyQuery(), pageRequest);
    }

    Future<Page<E>> findAll(SqlConnection conn, Query<E> query, PageRequest pageRequest);

    default Future<Long> count(Query<E> query) {
        return getPool().withConnection(conn -> count(conn, query));
    }

    Future<Long> count(SqlConnection conn, Query<E> query);

    default Future<Boolean> exists(ID id) {
        return getPool().withConnection(conn -> exists(conn, id));
    }

    Future<Boolean> exists(SqlConnection conn, ID id);

    default Future<Boolean> exists(Query<E> query) {
        return getPool().withConnection(conn -> exists(conn, query));
    }

    Future<Boolean> exists(SqlConnection conn, Query<E> query);

    Pool getPool();
}
