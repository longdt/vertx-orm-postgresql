package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Arrays;
import java.util.List;

public interface Query<E> {

    String getConditionSql();

    JsonArray getConditionParams();

    @SuppressWarnings({"unchecked"})
    default Query<E> orderBy(Order<E>... orders) {
        return orderBy(Arrays.asList(orders));
    }

    Query<E> orderBy(List<Order<E>> orders);

    List<Order<E>> orderBy();

    Query<E> limit(int limit);

    int limit();

    Query<E> offset(long offset);

    long offset();
}