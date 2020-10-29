package com.github.longdt.vertxorm.util;

public interface QuadConsumer<T, U, V, K> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     * @param k the 4th input argument
     */
    void accept(T t, U u, V v, K k);
}