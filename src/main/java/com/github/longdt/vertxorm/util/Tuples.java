package com.github.longdt.vertxorm.util;

import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.impl.ArrayTuple;

public class Tuples {
    /**
     * add all element from t2 to t1
     * @param t1
     * @param t2
     * @return t1
     */
    public static Tuple addAll(Tuple t1, Tuple t2) {
        for (int i = 0; i < t2.size(); ++i) {
            t1.addValue(t2.getValue(i));
        }
        return t1;
    }

    public static Tuple tuple(Tuple src) {
        return new ArrayTuple(src);
    }

    public static Tuple tuple(int capacity) {
        return new ArrayTuple(capacity);
    }
}
