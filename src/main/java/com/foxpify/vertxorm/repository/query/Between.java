package com.foxpify.vertxorm.repository.query;

public class Between<O, A extends Comparable<A>> extends SingleQuery<O> {

    public Between(String fieldName, A lowerValue, A upperValue) {
        super(fieldName, "\"" + fieldName + "\" BETWEEN ? AND ?", lowerValue, upperValue);
    }
}
