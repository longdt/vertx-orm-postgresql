package com.github.longdt.vertxorm.repository.query;

public class Like<E> extends SingleQuery<E> {

    public Like(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" LIKE $")
                .append(++index);
        return index;
    }
}
