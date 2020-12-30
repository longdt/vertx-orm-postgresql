package com.github.longdt.vertxorm.repository.query;

/**
 * <p>NotEqual class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class NotEqual<E> extends SingleQuery<E> {

    /**
     * <p>Constructor for NotEqual.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public NotEqual(String fieldName, Object value) {
        super(fieldName, value);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\"!=$")
                .append(++index);
        return index;
    }
}
