package com.foxpify.vertxorm.repository.query;

public class Equal<E> extends SingleQuery<E> {

    public Equal(String fieldName, Object value) {
        super(fieldName, "\"" + fieldName + "\"=?", value);
    }
}
