package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class SingleQuery<E> extends RawQuery<E> {
    protected String fieldName;

    public SingleQuery( String fieldName, String querySql, Object value) {
        this(fieldName, querySql, new JsonArray(Collections.singletonList(value)));
    }

    public SingleQuery(String fieldName, String querySql, Object... value) {
        this(fieldName, querySql, new JsonArray(Arrays.asList(value)));
    }

    public SingleQuery(String fieldName, String querySql, List<?> value) {
        this(fieldName, querySql, new JsonArray(value));
    }

    public SingleQuery(String fieldName, String querySql, JsonArray params) {
        super(querySql,params);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
