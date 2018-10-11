package com.foxpify.vertxorm.repository.query;

public class IsNull<E> extends SingleQuery<E> {

    public IsNull(String fieldName) {
        super(fieldName, "\"" + fieldName + "\" is null", QueryFactory.EMPTY_PARAMS);
    }
}
