package com.github.longdt.vertxorm.repository.query;

public class GreaterThan<O, A extends Comparable<A>> extends SingleQuery<O> {

    public GreaterThan(String fieldName, A value) {
        super(fieldName, value);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\">$")
                .append(++index);
        return index;
    }
}
