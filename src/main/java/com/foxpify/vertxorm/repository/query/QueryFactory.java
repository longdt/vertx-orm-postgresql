package com.foxpify.vertxorm.repository.query;

import io.netty.util.Attribute;
import io.vertx.core.json.JsonArray;

import java.util.*;

public class QueryFactory {
    public static final JsonArray EMPTY_PARAMS = new JsonArray(Collections.emptyList());

    public static <E> Equal<E> equal(String fieldName, Object value) {
        return new Equal<>(fieldName, value);
    }

    /**
     * Creates a {@link LessThanEqual} query which asserts that an attribute is less than or equal to an upper bound
     * (i.e. less than, inclusive).
     *
     * @param fieldName The attribute to which the query refers
     * @param value The upper bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link LessThan} query
     */
    public static <O, A extends Comparable<A>> LessThanEqual<O, A> lessThanOrEqualTo(String fieldName, A value) {
        return new LessThanEqual<O, A>(fieldName, value);
    }

    /**
     * Creates a {@link LessThan} query which asserts that an attribute is less than (but not equal to) an upper
     * bound (i.e. less than, exclusive).
     *
     * @param fieldName The attribute to which the query refers
     * @param value The upper bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link LessThan} query
     */
    public static <O, A extends Comparable<A>> LessThan<O, A> lessThan(String fieldName, A value) {
        return new LessThan<O, A>(fieldName, value);
    }

    /**
     * Creates a {@link GreaterThan} query which asserts that an attribute is greater than or equal to a lower
     * bound (i.e. greater than, inclusive).
     *
     * @param fieldName The attribute to which the query refers
     * @param value The lower bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> GreaterThanEqual<O, A> greaterThanOrEqualTo(String fieldName, A value) {
        return new GreaterThanEqual<O, A>(fieldName, value);
    }

    /**
     * Creates a {@link LessThan} query which asserts that an attribute is greater than (but not equal to) a lower
     * bound (i.e. greater than, exclusive).
     *
     * @param fieldName The attribute to which the query refers
     * @param value The lower bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> GreaterThan<O, A> greaterThan(String fieldName, A value) {
        return new GreaterThan<O, A>(fieldName, value);
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

    /**
     * Creates a {@link Not} query, representing a logical negation of a child query, which when evaluated
     * yields the <u>set complement</u> of the result set from the child query.
     *
     * @param query The child query to be logically negated
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return A {@link Not} query, representing a logical negation of a child query
     */
    public static <O> Not<O> not(Query<O> query) {
        return new Not<O>(query);
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

    /**
     * <p> Creates a {@link In} query which asserts that an attribute has at least one value matching any value in a set of values.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues The set of values to match
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link In} query
     */
    public static <O, A> Query<O> in(String attribute, A... attributeValues) {
        return in(attribute, Arrays.asList(attributeValues));
    }

    /**
     * <p> Creates a {@link In} query which asserts that an attribute has at least one value matching any value in a set of values.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues TThe set of values to match
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link In} query
     */
    public static <O, A> Query<O> in(String attribute, List<A> attributeValues) {
        int n = attributeValues.size();
        switch (n) {
            case 0:
                return none();
            case 1:
                A singleValue = attributeValues.iterator().next();
                return equal(attribute, singleValue);
            default:
                return new In<>(attribute, attributeValues);
        }
    }

    /**
     * Creates a query which matches no objects in the collection.
     * <p/>
     * This is equivalent to a literal boolean 'false'.
     *
     * @param <O> The type of the objects in the collection
     * @return A query which matches no objects in the collection
     */
    public static <O> Query<O> none() {
        return raw("FALSE");
    }

    /**
     * Creates a query which matches all objects in the collection.
     * <p/>
     * This is equivalent to a literal boolean 'true'.
     *
     * @param <O> The type of the objects in the collection
     * @return A query which matches all objects in the collection
     */
    public static <O> Query<O> all() {
        return raw("TRUE");
    }

    /**
     * Creates a {@link Like} query which asserts that an attribute starts with a certain string fragment.
     *
     * @param fieldName The attribute to which the query refers
     * @param value The value to be asserted by the query
     * @param <O> The type of the object containing the attribute
     * @return An {@link Like} query
     */
    public static <O> Like<O> startsWith(String fieldName, String value) {
        return new Like<>(fieldName, "%" + value);
    }

    /**
     * Creates a {@link Like} query which asserts that an attribute ends with a certain string fragment.
     *
     * @param fieldName The attribute to which the query refers
     * @param value The value to be asserted by the query
     * @param <O> The type of the object containing the attribute
     * @return An {@link Like} query
     */
    public static <O> Like<O> endsWith(String fieldName, String value) {
        return new Like<>(fieldName, value + "%");
    }

    /**
     * Creates a {@link Like} query which asserts that an attribute contains with a certain string fragment.
     *
     * @param fieldName The attribute to which the query refers
     * @param value The value to be asserted by the query
     * @param <O> The type of the object containing the attribute
     * @return An {@link Like} query
     */
    public static <O> Like<O> contains(String fieldName, String value) {
        return new Like<>(fieldName, "%" + value + "%");
    }

    /**
     * Creates a {@link Like} query which asserts that an attribute contains with a certain string fragment.
     *
     * @param fieldName The attribute to which the query refers
     * @param value The value to be asserted by the query
     * @param <O> The type of the object containing the attribute
     * @return An {@link Like} query
     */
    public static <O> Like<O> like(String fieldName, String value) {
        return new Like<>(fieldName, value);
    }

    /**
     * Creates a {@link Query} query which asserts that an attribute is contained in a certain string
     * fragment.
     *
     * @param fieldName The attribute to which the query refers
     * @param value The value to be asserted by the query
     * @param <O> The type of the object containing the attribute
     * @return An {@link Query} query
     */
    public static <O> Query<O> isContainedIn(String fieldName, String value) {
        return raw("?  LIKE '%' || \"" + fieldName + "\" || '%'", value);
    }

    public static JsonArray emptyParams() {
        return EMPTY_PARAMS;
    }
}
