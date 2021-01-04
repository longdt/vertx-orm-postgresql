package com.github.longdt.vertxorm.util;

import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.impl.TupleInternal;

public class SubArrayTuple implements TupleInternal {
    private Object[] values;
    private int offset;
    private int size;

    /**
     * <p>Constructor for SubArrayTuple.</p>
     *
     * @param values an array of {@link Object} objects.
     * @param offset a int.
     */
    public SubArrayTuple(Object[] values, int offset, int length) {
        this.values = values;
        this.offset = offset;
        size = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(int pos, Object value) {
        values[pos + offset] = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(int pos) {
        return values[pos + offset];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple addValue(Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
