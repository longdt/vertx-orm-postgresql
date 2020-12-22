package com.github.longdt.vertxorm.repository.query;

public class IsNull<E> extends SingleQuery<E> {

    public IsNull(String fieldName) {
        super(fieldName, QueryFactory.EMPTY_PARAMS);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" is null");
        return index;
    }
}
