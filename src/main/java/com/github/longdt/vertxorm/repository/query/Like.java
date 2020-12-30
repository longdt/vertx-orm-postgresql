package com.github.longdt.vertxorm.repository.query;

/**
 * <p>Like class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Like<E> extends SingleQuery<E> {

    /**
     * <p>Constructor for Like.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public Like(String fieldName, String value) {
        super(fieldName, value);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" LIKE $")
                .append(++index);
        return index;
    }
}
