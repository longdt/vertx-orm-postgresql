package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.List;

public class In<E, V> extends SingleQuery<E> {
    private List<V> values;

    public In(String fieldName, List<V> values) {
        super(fieldName);
        this.values = values;
    }

    @Override
    public String getConditionSql() {
        StringBuilder builder = new StringBuilder();
        builder.append('"').append(fieldName).append("\" IN (");
        values.forEach(v -> builder.append("?,"));
        builder.deleteCharAt(builder.length() - 1);
        return builder.append(')').toString();
    }

    @Override
    public JsonArray getConditionParams() {
        return new JsonArray(values);
    }
}
