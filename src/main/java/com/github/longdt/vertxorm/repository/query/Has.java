package com.github.longdt.vertxorm.repository.query;

/**
 * <p>Has class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Has<E> extends SingleQuery<E> {

    /**
     * <p>Constructor for Has.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     */
    public Has(String fieldName) {
        super(fieldName, QueryFactory.EMPTY_PARAMS);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" is not null");
        return index;
    }
}
