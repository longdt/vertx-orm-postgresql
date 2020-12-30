package com.github.longdt.vertxorm.repository.query;

import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.sqlclient.Tuple;

import java.util.List;

/**
 * <p>Abstract AbstractQuery class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public abstract class AbstractQuery<E> implements Query<E> {
    protected List<Order<E>> orders;
    protected int limit = -1;
    protected long offset = -1;
    protected Tuple params;

    /**
     * <p>Constructor for AbstractQuery.</p>
     *
     * @param params a {@link io.vertx.sqlclient.Tuple} object.
     */
    public AbstractQuery(Tuple params) {
        this.params = params;
    }

    /** {@inheritDoc} */
    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        return Tuples.addAll(tuple, params);
    }

    /** {@inheritDoc} */
    @Override
    public Tuple getQueryParams() {
        return params;
    }

    /** {@inheritDoc} */
    @Override
    public Query<E> orderBy(List<Order<E>> orders) {
        this.orders = orders;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public List<Order<E>> orderBy() {
        return orders;
    }

    /** {@inheritDoc} */
    @Override
    public Query<E> limit(int limit) {
        this.limit = limit;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int limit() {
        return limit;
    }

    /** {@inheritDoc} */
    @Override
    public Query<E> offset(long offset) {
        this.offset = offset;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public long offset() {
        return offset;
    }
}
