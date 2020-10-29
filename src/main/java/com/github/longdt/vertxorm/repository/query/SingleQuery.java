package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.List;

public abstract class SingleQuery<E> extends AbstractQuery<E> {
    protected String fieldName;

    public SingleQuery(String fieldName, Object value) {
        this(fieldName, Tuple.of(value));
    }

    public SingleQuery(String fieldName, Object... value) {
        this(fieldName, Tuple.wrap(value));
    }

    public SingleQuery(String fieldName, List<Object> value) {
        this(fieldName, Tuple.wrap(value));
    }

    public SingleQuery(String fieldName, Tuple params) {
        super(params);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
