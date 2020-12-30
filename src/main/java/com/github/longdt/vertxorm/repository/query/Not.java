package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

/**
 * <p>Not class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Not<E> extends AbstractQuery<E> {
    private Query<E> negatedQuery;

    /**
     * <p>Constructor for Not.</p>
     *
     * @param negatedQuery a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    public Not(Query<E> negatedQuery) {
        super(null);
        this.negatedQuery = negatedQuery;
    }

    /**
     * <p>Getter for the field <code>negatedQuery</code>.</p>
     *
     * @return a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     */
    public Query<E> getNegatedQuery() {
        return negatedQuery;
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append("NOT (");
        index = negatedQuery.appendQuerySql(sqlBuilder, index);
        sqlBuilder.append(')');
        return index;
    }

    /** {@inheritDoc} */
    @Override
    public Tuple getQueryParams() {
        if (params == null) {
            if (negatedQuery instanceof And || negatedQuery instanceof Or || negatedQuery instanceof Not) {
                var tuple = Tuple.tuple();
                appendQueryParams(tuple);
                params = tuple;
            } else {
                params = negatedQuery.getQueryParams();
            }
        }
        return params;
    }

    /** {@inheritDoc} */
    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        return negatedQuery.appendQueryParams(tuple);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isConditional() {
        return negatedQuery.isConditional();
    }
}
