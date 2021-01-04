package com.github.longdt.vertxorm.util;

import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.impl.ArrayTuple;

/**
 * <p>Tuples class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Tuples {
    /**
     * add all element from t2 to t1
     *
     * @param t1 a {@link io.vertx.sqlclient.Tuple} object.
     * @param t2 a {@link io.vertx.sqlclient.Tuple} object.
     * @return t1
     */
    public static Tuple addAll(Tuple t1, Tuple t2) {
        for (int i = 0; i < t2.size(); ++i) {
            t1.addValue(t2.getValue(i));
        }
        return t1;
    }

    /**
     * <p>addAll.</p>
     *
     * @param tuple a {@link io.vertx.sqlclient.Tuple} object.
     * @param values an array of {@link java.lang.Object} objects.
     * @param offset a int.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    public static Tuple addAll(Tuple tuple, Object[] values, int offset) {
        for (int i = offset; i < values.length; ++i) {
            tuple.addValue(values[i]);
        }
        return tuple;
    }

    /**
     * <p>addAll.</p>
     *
     * @param tuple a {@link io.vertx.sqlclient.Tuple} object.
     * @param values an array of {@link java.lang.Object} objects.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    public static Tuple addAll(Tuple tuple, Object[] values) {
        return addAll(tuple, values, 0);
    }

    /**
     * <p>tuple.</p>
     *
     * @param src a {@link io.vertx.sqlclient.Tuple} object.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    public static Tuple tuple(Tuple src) {
        return new ArrayTuple(src);
    }

    /**
     * <p>tuple.</p>
     *
     * @param capacity a int.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    public static Tuple tuple(int capacity) {
        return new ArrayTuple(capacity);
    }

    /**
     * <p>shift.</p>
     *
     * @param data an array of {@link java.lang.Object} objects.
     * @param offset a int.
     * @return a {@link io.vertx.sqlclient.Tuple} object.
     */
    public static Tuple shift(Object[] data, int offset) {
        return new ShiftedArrayTuple(data, offset);
    }

    public static Tuple sub(Object[] data, int offset, int length) {
        return new SubArrayTuple(data, offset, length);
    }
}
