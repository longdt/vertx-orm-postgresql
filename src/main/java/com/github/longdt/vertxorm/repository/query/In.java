package com.github.longdt.vertxorm.repository.query;

import java.util.List;

public class In<E, V> extends SingleQuery<E> {

    @SuppressWarnings("unchecked")
    public In(String fieldName, List<V> values) {
        super(fieldName, (List<Object>) values);
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" IN (");
        for (int i = 0; i < getConditionParams().size(); ++i) {
            sqlBuilder.append('$').append(startIdx++).append(',');
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(')');
    }
}
