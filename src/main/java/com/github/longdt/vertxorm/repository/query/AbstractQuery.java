package com.github.longdt.vertxorm.repository.query;

import java.util.List;

public abstract class AbstractQuery<E> implements Query<E> {
    protected List<Order<E>> orders;
    protected int limit = -1;
    protected long offset = -1;

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
