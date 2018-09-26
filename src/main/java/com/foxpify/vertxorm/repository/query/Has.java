package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class Has<E> extends SingleQuery<E> {

    public Has(String fieldName) {
        super(fieldName);
    }

    @Override
    public String getConditionSql() {
        return "\"" + fieldName + "\" is not null";
    }

    @Override
    public JsonArray getConditionParams() {
        return new JsonArray(Collections.emptyList());
    }
}
