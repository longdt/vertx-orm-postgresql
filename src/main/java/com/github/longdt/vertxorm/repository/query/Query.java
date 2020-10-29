package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Query<E> {

    String getConditionSql();

    Tuple getConditionParams();

    @SuppressWarnings({"unchecked"})
    default Query<E> orderBy(Order<E>... orders) {
        return orderBy(Arrays.asList(orders));
    }

    default Query<E> orderBy(Order<E> order) {
        return orderBy(Collections.singletonList(order));
    }

    Query<E> orderBy(List<Order<E>> orders);

    List<Order<E>> orderBy();

    Query<E> limit(int limit);

    int limit();

    Query<E> offset(long offset);

    long offset();
}