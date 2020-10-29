package com.github.longdt.vertxorm.repository.query;

public class GreaterThan<O, A extends Comparable<A>> extends SingleQuery<O> {

    public GreaterThan(String fieldName, A value) {
        super(fieldName, "`" + fieldName + "` > ?", value);
    }
}
