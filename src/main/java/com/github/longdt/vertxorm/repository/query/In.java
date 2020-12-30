package com.github.longdt.vertxorm.repository.query;

import java.util.List;

/**
 * <p>In class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class In<E, V> extends SingleQuery<E> {

    @SuppressWarnings("unchecked")
    /**
     * <p>Constructor for In.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param values a {@link java.util.List} object.
     */
    public In(String fieldName, List<V> values) {
        super(fieldName, (List<Object>) values);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" IN (");
        for (int i = 0; i < params.size(); ++i) {
            sqlBuilder.append('$').append(++index).append(',');
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(')');
        return index;
    }
}
