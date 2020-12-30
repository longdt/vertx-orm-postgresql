package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;

import java.util.List;
import java.util.Optional;

/**
 * <p>CrudRepository interface.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public interface CrudRepository<ID, E> {

    /**
     * <p>save.</p>
     *
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<E> save(E entity) {
        return getPool().withConnection(conn -> save(conn, entity));
    }

    /**
     * <p>save.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<E> save(SqlConnection conn, E entity);

    /**
     * <p>insert.</p>
     *
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<E> insert(E entity) {
        return getPool().withConnection(conn -> insert(conn, entity));
    }

    /**
     * <p>insert.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<E> insert(SqlConnection conn, E entity);

    /**
     * <p>update.</p>
     *
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<E> update(E entity) {
        return getPool().withConnection(conn -> update(conn, entity));
    }

    /**
     * <p>update.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param entity a E object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<E> update(SqlConnection conn, E entity);

    /**
     * <p>delete.</p>
     *
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Void> delete(ID id) {
        return getPool().withConnection(conn -> delete(conn, id));
    }

    /**
     * <p>delete.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Void> delete(SqlConnection conn, ID id);

    /**
     * <p>find.</p>
     *
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Optional<E>> find(ID id) {
        return getPool().withConnection(conn -> find(conn, id));
    }

    /**
     * <p>find.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Optional<E>> find(SqlConnection conn, ID id);

    /**
     * <p>findAll.</p>
     *
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<List<E>> findAll() {
        return getPool().withConnection(this::findAll);
    }

    /**
     * <p>findAll.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<List<E>> findAll(SqlConnection conn);

    /**
     * <p>findAll.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<List<E>> findAll(Query<E> query) {
        return getPool().withConnection(conn -> findAll(conn, query));
    }

    /**
     * <p>findAll.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<List<E>> findAll(SqlConnection conn, Query<E> query);

    /**
     * <p>find.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Optional<E>> find(Query<E> query) {
        return getPool().withConnection(conn -> find(conn, query));
    }

    /**
     * <p>find.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Optional<E>> find(SqlConnection conn, Query<E> query);

    /**
     * <p>findAll.</p>
     *
     * @param pageRequest a {@link com.github.longdt.vertxorm.repository.PageRequest} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Page<E>> findAll(PageRequest pageRequest) {
        return findAll(QueryFactory.emptyQuery(), pageRequest);
    }

    /**
     * <p>findAll.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @param pageRequest a {@link com.github.longdt.vertxorm.repository.PageRequest} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Page<E>> findAll(Query<E> query, PageRequest pageRequest) {
        return getPool().withTransaction(conn -> findAll(conn, query, pageRequest));
    }

    /**
     * <p>findAll.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param pageRequest a {@link com.github.longdt.vertxorm.repository.PageRequest} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Page<E>> findAll(SqlConnection conn, PageRequest pageRequest) {
        return findAll(conn, QueryFactory.emptyQuery(), pageRequest);
    }

    /**
     * <p>findAll.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @param pageRequest a {@link com.github.longdt.vertxorm.repository.PageRequest} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Page<E>> findAll(SqlConnection conn, Query<E> query, PageRequest pageRequest);

    /**
     * <p>count.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Long> count(Query<E> query) {
        return getPool().withConnection(conn -> count(conn, query));
    }

    /**
     * <p>count.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Long> count(SqlConnection conn, Query<E> query);

    /**
     * <p>exists.</p>
     *
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Boolean> exists(ID id) {
        return getPool().withConnection(conn -> exists(conn, id));
    }

    /**
     * <p>exists.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param id a ID object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Boolean> exists(SqlConnection conn, ID id);

    /**
     * <p>exists.</p>
     *
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    default Future<Boolean> exists(Query<E> query) {
        return getPool().withConnection(conn -> exists(conn, query));
    }

    /**
     * <p>exists.</p>
     *
     * @param conn a {@link io.vertx.sqlclient.SqlConnection} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @return a {@link io.vertx.core.Future} object.
     */
    Future<Boolean> exists(SqlConnection conn, Query<E> query);

    /**
     * <p>getPool.</p>
     *
     * @return a {@link io.vertx.sqlclient.Pool} object.
     */
    Pool getPool();
}
