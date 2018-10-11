package com.foxpify.vertxorm.repository.query;

public class Like<E> extends SingleQuery<E> {

    public Like(String fieldName, String value) {
        super(fieldName, "\"" + fieldName + "\" LIKE ?", value);
    }
}
