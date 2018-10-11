package com.foxpify.vertxorm.repository.query;

public class ILike<E> extends SingleQuery<E> {

    public ILike(String fieldName, String value) {
        super(fieldName, "\"" + fieldName + "\" ILIKE ?", value);
    }
}