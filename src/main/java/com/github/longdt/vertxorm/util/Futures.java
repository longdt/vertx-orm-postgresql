package com.github.longdt.vertxorm.util;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <p>Futures class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public final class Futures {
    private Futures() {
    }

    /**
     * <p>toFuture.</p>
     *
     * @param consumer a {@link com.github.longdt.vertxorm.util.QuadConsumer} object.
     * @param arg1 a T object.
     * @param arg2 a V object.
     * @param arg3 a U object.
     * @param <T> a T object.
     * @param <V> a V object.
     * @param <U> a U object.
     * @param <R> a R object.
     * @return a {@link io.vertx.core.Future} object.
     */
    public static <T, V, U, R> Future<R> toFuture(QuadConsumer<T, V, U, Handler<AsyncResult<R>>> consumer, T arg1, V arg2, U arg3) {
        Promise<R> promise = Promise.promise();
        consumer.accept(arg1, arg2, arg3, promise);
        return promise.future();
    }

    /**
     * <p>toFuture.</p>
     *
     * @param consumer a {@link com.github.longdt.vertxorm.util.TriConsumer} object.
     * @param arg1 a T object.
     * @param arg2 a V object.
     * @param <T> a T object.
     * @param <V> a V object.
     * @param <R> a R object.
     * @return a {@link io.vertx.core.Future} object.
     */
    public static <T, V, R> Future<R> toFuture(TriConsumer<T, V, Handler<AsyncResult<R>>> consumer, T arg1, V arg2) {
        Promise<R> promise = Promise.promise();
        consumer.accept(arg1, arg2, promise);
        return promise.future();
    }

    /**
     * <p>toFuture.</p>
     *
     * @param consumer a {@link java.util.function.BiConsumer} object.
     * @param arg a T object.
     * @param <T> a T object.
     * @param <R> a R object.
     * @return a {@link io.vertx.core.Future} object.
     */
    public static <T, R> Future<R> toFuture(BiConsumer<T, Handler<AsyncResult<R>>> consumer, T arg) {
        Promise<R> promise = Promise.promise();
        consumer.accept(arg, promise);
        return promise.future();
    }

    /**
     * <p>toFuture.</p>
     *
     * @param consumer a {@link java.util.function.Consumer} object.
     * @param <R> a R object.
     * @return a {@link io.vertx.core.Future} object.
     */
    public static <R> Future<R> toFuture(Consumer<Handler<AsyncResult<R>>> consumer) {
        Promise<R> promise = Promise.promise();
        consumer.accept(promise);
        return promise.future();
    }

    /**
     * <p>joinNow.</p>
     *
     * @param future a {@link io.vertx.core.Future} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T joinNow(Future<T> future) {
        if (future.succeeded()) {
            return future.result();
        } else if (future.failed()) {
            throw new CompletionException(future.cause());
        }
        return null;
    }

    /**
     * <p>join.</p>
     *
     * @param future a {@link io.vertx.core.Future} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T join(Future<T> future) {
        return join(future, -1, TimeUnit.NANOSECONDS);
    }

    /**
     * <p>join.</p>
     *
     * @param future a {@link io.vertx.core.Future} object.
     * @param time a long.
     * @param unit a {@link java.util.concurrent.TimeUnit} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T join(Future<T> future, long time, TimeUnit unit) {
        if (future.succeeded()) {
            return future.result();
        } else if (future.failed()) {
            throw new CompletionException(future.cause());
        }
        var thread = Thread.currentThread();
        future.onComplete(event -> LockSupport.unpark(thread));
        if (time > 0) {
            LockSupport.parkNanos(future, unit.toNanos(time));
        } else {
            LockSupport.park(future);
        }
        return joinNow(future);
    }

    /**
     * <p>sync.</p>
     *
     * @param consumer a {@link com.github.longdt.vertxorm.util.QuadConsumer} object.
     * @param arg1 a T object.
     * @param arg2 a V object.
     * @param arg3 a S object.
     * @param <T> a T object.
     * @param <V> a V object.
     * @param <S> a S object.
     * @param <R> a R object.
     * @return a R object.
     */
    public static <T, V, S, R> R sync(QuadConsumer<T, V, S, Handler<AsyncResult<R>>> consumer, T arg1, V arg2, S arg3) {
        return join(toFuture(consumer, arg1, arg2, arg3));
    }

    /**
     * <p>sync.</p>
     *
     * @param consumer a {@link com.github.longdt.vertxorm.util.TriConsumer} object.
     * @param arg1 a T object.
     * @param arg2 a V object.
     * @param <T> a T object.
     * @param <V> a V object.
     * @param <R> a R object.
     * @return a R object.
     */
    public static <T, V, R> R sync(TriConsumer<T, V, Handler<AsyncResult<R>>> consumer, T arg1, V arg2) {
        return join(toFuture(consumer, arg1, arg2));
    }

    /**
     * <p>sync.</p>
     *
     * @param consumer a {@link java.util.function.BiConsumer} object.
     * @param arg a T object.
     * @param <T> a T object.
     * @param <R> a R object.
     * @return a R object.
     */
    public static <T, R> R sync(BiConsumer<T, Handler<AsyncResult<R>>> consumer, T arg) {
        return join(toFuture(consumer, arg));
    }

    /**
     * <p>sync.</p>
     *
     * @param consumer a {@link java.util.function.Consumer} object.
     * @param <R> a R object.
     * @return a R object.
     */
    public static <R> R sync(Consumer<Handler<AsyncResult<R>>> consumer) {
        return join(toFuture(consumer));
    }
}
