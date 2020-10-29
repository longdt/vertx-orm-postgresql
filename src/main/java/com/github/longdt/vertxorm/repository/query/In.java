package com.github.longdt.vertxorm.repository.query;

import java.util.List;

public class In<E, V> extends SingleQuery<E> {

    public In(String fieldName, List<V> values) {
        super(fieldName, null, values);
        querySql = createQuerySql();
    }

    private String createQuerySql() {
        StringBuilder builder = new StringBuilder();
        builder.append('`').append(fieldName).append("` IN (");
        for (int i = 0; i < getConditionParams().size(); ++i) {
            builder.append("?,");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.append(')').toString();
    }
}
