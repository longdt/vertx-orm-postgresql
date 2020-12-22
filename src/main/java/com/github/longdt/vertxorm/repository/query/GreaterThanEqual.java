package com.github.longdt.vertxorm.repository.query;

public class GreaterThanEqual<O, A extends Comparable<A>> extends SingleQuery<O> {

    public GreaterThanEqual(String fieldName, A value) {
        super(fieldName, value);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\">=$")
                .append(++index);
        return index;
    }
}
