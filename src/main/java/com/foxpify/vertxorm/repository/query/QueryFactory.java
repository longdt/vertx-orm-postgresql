package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class QueryFactory {
    public static <E> Equal<E> equal(String fieldName, Object value) {
        return new Equal<>(fieldName, value);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param <E> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <E> And<E> and(Query<E> query1, Query<E> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<E>> queries = Arrays.asList(query1, query2);
        return new And<E>(queries);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param additionalQueries Additional child queries to be connected via a logical AND
     * @param <E> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <E> And<E> and(Query<E> query1, Query<E> query2, Query<E>... additionalQueries) {
        Collection<Query<E>> queries = new ArrayList<Query<E>>(2 + additionalQueries.length);
        queries.add(query1);
        queries.add(query2);
        Collections.addAll(queries, additionalQueries);
        return new And<E>(queries);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param additionalQueries Additional child queries to be connected via a logical AND
     * @param <E> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <E> And<E> and(Query<E> query1, Query<E> query2, Collection<Query<E>> additionalQueries) {
        Collection<Query<E>> queries = new ArrayList<Query<E>>(2 + additionalQueries.size());
        queries.add(query1);
        queries.add(query2);
        queries.addAll(additionalQueries);
        return new And<E>(queries);
    }


    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2);
        return new Or<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param additionalQueries Additional child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Query<O>... additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.length);
        queries.add(query1);
        queries.add(query2);
        Collections.addAll(queries, additionalQueries);
        return new Or<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param additionalQueries Additional child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Collection<Query<O>> additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.size());
        queries.add(query1);
        queries.add(query2);
        queries.addAll(additionalQueries);
        return new Or<O>(queries);
    }

    public static <O> Has<O> has(String fieldName) {
        return new Has<>(fieldName);
    }

    public static <O> IsNull<O> isNull(String fieldName) {
        return new IsNull<>(fieldName);
    }

    public static <O> RawQuery<O> raw(String querySql, Object... params) {
        return new RawQuery<>(querySql, new JsonArray(Arrays.asList(params)));
    }

    public static <O> Order<O> ascending(String fieldName) {
        return new Order<>(fieldName);
    }

    public static <O> Order<O> descending(String fieldName) {
        return new Order<>(fieldName, true);
    }
}
