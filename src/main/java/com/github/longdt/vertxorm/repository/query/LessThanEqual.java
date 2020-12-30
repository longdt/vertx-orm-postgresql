package com.github.longdt.vertxorm.repository.query;

/**
 * <p>LessThanEqual class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class LessThanEqual<O, A extends Comparable<A>> extends SingleQuery<O> {

    /**
     * <p>Constructor for LessThanEqual.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a A object.
     */
    public LessThanEqual(String fieldName, A value) {
        super(fieldName, value);
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('\"')
                .append(fieldName)
                .append("\"<=$")
                .append(++index);
        return index;
    }
}
