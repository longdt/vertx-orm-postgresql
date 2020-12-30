package com.github.longdt.vertxorm.repository.query;

/**
 * <p>IsNull class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class IsNull<E> extends SingleQuery<E> {

    /**
     * <p>Constructor for IsNull.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     */
    public IsNull(String fieldName) {
        super(fieldName, QueryFactory.EMPTY_PARAMS);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" is null");
        return index;
    }
}
