package com.github.longdt.vertxorm.repository.query;

public class LessThanEqual<O, A extends Comparable<A>> extends SingleQuery<O> {

    public LessThanEqual(String fieldName, A value) {
        super(fieldName, value);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\"<=$")
                .append(startIdx);
    }
}
