package com.github.longdt.vertxorm.repository.query;

public class Has<E> extends SingleQuery<E> {

    public Has(String fieldName) {
        super(fieldName, QueryFactory.EMPTY_PARAMS);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" is not null");
        return index;
    }
}
