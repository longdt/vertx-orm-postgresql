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
}
