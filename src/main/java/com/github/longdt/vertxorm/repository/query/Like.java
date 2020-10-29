package com.github.longdt.vertxorm.repository.query;

public class Like<E> extends SingleQuery<E> {

    public Like(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" LIKE $")
                .append(startIdx);
    }
}
