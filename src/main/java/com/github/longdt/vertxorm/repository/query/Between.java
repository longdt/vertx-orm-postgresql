package com.github.longdt.vertxorm.repository.query;

/**
 * <p>Between class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Between<O, A extends Comparable<A>> extends SingleQuery<O> {

    /**
     * <p>Constructor for Between.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param lowerValue a A object.
     * @param upperValue a A object.
     */
    public Between(String fieldName, A lowerValue, A upperValue) {
        super(fieldName, lowerValue, upperValue);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\" BETWEEN $")
                .append(++index)
                .append(" AND $")
                .append(++index);
        return index;
    }
}
