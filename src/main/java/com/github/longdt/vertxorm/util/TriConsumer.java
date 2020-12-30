package com.github.longdt.vertxorm.util;

/**
 * <p>TriConsumer interface.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public interface TriConsumer<T, U, V> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);
}
