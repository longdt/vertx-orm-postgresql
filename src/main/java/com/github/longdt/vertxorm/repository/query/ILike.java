package com.github.longdt.vertxorm.repository.query;

/**
 * <p>ILike class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class ILike<E> extends SingleQuery<E> {

    /**
     * <p>Constructor for ILike.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public ILike(String fieldName, String value) {
        super(fieldName, value);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" ILIKE $")
                .append(++index);
        return index;
    }
}
