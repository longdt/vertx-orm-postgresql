package com.github.longdt.vertxorm.util;

import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.impl.TupleInternal;

public class ShiftedArrayTuple implements TupleInternal {
    private Object[] values;
    private int offset;
    private int size;

    public ShiftedArrayTuple(Object[] values, int offset) {
        this.values = values;
        this.offset = offset;
        size = values.length - offset;
    }

    @Override
    public void setValue(int pos, Object value) {
        values[pos + offset] = value;
    }

    @Override
    public Object getValue(int pos) {
        return values[pos + offset];
    }

    @Override
    public Tuple addValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
