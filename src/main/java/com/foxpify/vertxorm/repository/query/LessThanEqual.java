package com.foxpify.vertxorm.repository.query;

public class LessThanEqual<O, A extends Comparable<A>> extends SingleQuery<O> {

    public LessThanEqual(String fieldName, A value) {
        super(fieldName, "\"" + fieldName + "\" <= ?", value);
    }
}
