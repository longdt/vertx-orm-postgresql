package com.github.longdt.vertxorm.repository.query;

import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public abstract class AbstractQuery<E> implements Query<E> {
    protected List<Order<E>> orders;
    protected int limit = -1;
    protected long offset = -1;
    protected Tuple params;

    public AbstractQuery(Tuple params) {
        this.params = params;
    }

    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        return Tuples.addAll(tuple, params);
    }

    @Override
    public Tuple getQueryParams() {
        return params;
    }

    @Override
    public Query<E> orderBy(List<Order<E>> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public List<Order<E>> orderBy() {
        return orders;
    }

    @Override
    public Query<E> limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public Query<E> offset(long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public long offset() {
        return offset;
    }
}
