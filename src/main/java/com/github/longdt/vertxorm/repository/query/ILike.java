package com.github.longdt.vertxorm.repository.query;

public class ILike<E> extends SingleQuery<E> {

    public ILike(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" ILIKE $")
                .append(startIdx);
    }
}