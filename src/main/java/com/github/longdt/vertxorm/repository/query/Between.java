package com.github.longdt.vertxorm.repository.query;

public class Between<O, A extends Comparable<A>> extends SingleQuery<O> {

    public Between(String fieldName, A lowerValue, A upperValue) {
        super(fieldName, lowerValue, upperValue);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" BETWEEN $")
                .append(startIdx++)
                .append(" AND $")
                .append(startIdx);
    }
}
