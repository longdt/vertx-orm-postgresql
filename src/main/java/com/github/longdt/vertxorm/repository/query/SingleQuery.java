package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.List;

public abstract class SingleQuery<E> extends RawQuery<E> {
    protected String fieldName;

    public SingleQuery(String fieldName, String querySql, Object value) {
        this(fieldName, querySql, Tuple.of(value));
    }

    public SingleQuery(String fieldName, String querySql, Object... value) {
        this(fieldName, querySql, Tuple.wrap(value));
    }

    public SingleQuery(String fieldName, String querySql, List<?> value) {
        this(fieldName, querySql, Tuple.wrap(value));
    }

    public SingleQuery(String fieldName, String querySql, Tuple params) {
        super(querySql,params);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
