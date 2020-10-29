package com.github.longdt.vertxorm.repository.query;

public class Has<E> extends SingleQuery<E> {

    public Has(String fieldName) {
        super(fieldName, QueryFactory.EMPTY_PARAMS);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" is not null");
    }
}
