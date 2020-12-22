package com.github.longdt.vertxorm.repository.query;

public class Between<O, A extends Comparable<A>> extends SingleQuery<O> {

    public Between(String fieldName, A lowerValue, A upperValue) {
        super(fieldName, lowerValue, upperValue);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" BETWEEN $")
                .append(++index)
                .append(" AND $")
                .append(++index);
        return index;
    }
}
