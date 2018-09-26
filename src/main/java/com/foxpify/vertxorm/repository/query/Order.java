package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class Order<E> extends SingleQuery<E> {
    private final boolean descending;

    public Order(String fieldName) {
        this(fieldName, false);
    }

    public Order(String fieldName, boolean descending) {
        super(fieldName);
        this.descending = descending;
        orders = Collections.singletonList(this);
    }

    @Override
    public String getConditionSql() {
        return null;
    }

    @Override
    public JsonArray getConditionParams() {
        return null;
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     *
     * @return
     */
    public boolean isAscending() {
        return !descending;
    }

    /**
     * Returns whether sorting for this property shall be descending.
     *
     * @return
     */
    public boolean isDescending() {
        return descending;
    }
}
