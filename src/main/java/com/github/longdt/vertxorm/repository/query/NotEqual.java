package com.github.longdt.vertxorm.repository.query;

public class NotEqual<E> extends SingleQuery<E> {

    public NotEqual(String fieldName, Object value) {
        super(fieldName, value);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\"!=$")
                .append(startIdx);
    }
}
