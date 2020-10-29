package com.github.longdt.vertxorm.repository.query;

public class GreaterThanEqual<O, A extends Comparable<A>> extends SingleQuery<O> {

    public GreaterThanEqual(String fieldName, A value) {
        super(fieldName, "`" + fieldName + "` >= ?", value);
    }
}
