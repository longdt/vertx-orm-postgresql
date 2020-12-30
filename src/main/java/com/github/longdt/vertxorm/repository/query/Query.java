package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Query interface.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public interface Query<E> {

    /**
     * <p>appendQuerySql.</p>
     *
     * @param sqlBuilder a {@link java.lang.StringBuilder} object.
     * @param index a int.
     * @return a int.
     */
    int appendQuerySql(StringBuilder sqlBuilder, int index);

    /**
     * <p>appendQueryParams.</p>
     *
     * @param tuple a {@link io.vertx.sqlclient.Tuple} object.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    Tuple appendQueryParams(Tuple tuple);

    /**
     * <p>getQueryParams.</p>
     *
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    Tuple getQueryParams();

    /**
     * <p>isConditional.</p>
     *
     * @return a boolean.
     */
    default boolean isConditional() {
        return true;
    }

    /**
     * <p>orderBy.</p>
     *
     * @param orders a {@link com.github.longdt.vertxorm.repository.query.Order} object.
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    @SuppressWarnings({"unchecked"})
    default Query<E> orderBy(Order<E>... orders) {
        return orderBy(Arrays.asList(orders));
    }

    /**
     * <p>orderBy.</p>
     *
     * @param order a {@link com.github.longdt.vertxorm.repository.query.Order} object.
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    default Query<E> orderBy(Order<E> order) {
        return orderBy(Collections.singletonList(order));
    }

    /**
     * <p>orderBy.</p>
     *
     * @param orders a {@link java.util.List} object.
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    Query<E> orderBy(List<Order<E>> orders);

    /**
     * <p>orderBy.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<Order<E>> orderBy();

    /**
     * <p>limit.</p>
     *
     * @param limit a int.
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    Query<E> limit(int limit);

    /**
     * <p>limit.</p>
     *
     * @return a int.
     */
    int limit();

    /**
     * <p>offset.</p>
     *
     * @param offset a long.
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    Query<E> offset(long offset);

    /**
     * <p>offset.</p>
     *
     * @return a long.
     */
    long offset();
}
