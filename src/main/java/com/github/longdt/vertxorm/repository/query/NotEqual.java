package com.github.longdt.vertxorm.repository.query;

public class NotEqual<E> extends SingleQuery<E> {

    public NotEqual(String fieldName, Object value) {
        super(fieldName, value);
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\"!=$")
                .append(++index);
        return index;
    }
}
