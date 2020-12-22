package com.github.longdt.vertxorm.repository.query;

public class ILike<E> extends SingleQuery<E> {

    public ILike(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" ILIKE $")
                .append(++index);
        return index;
    }
}