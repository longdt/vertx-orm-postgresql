package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.Collection;

/**
 * <p>Or class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Or<E> extends AbstractQuery<E> {
    private final Collection<Query<E>> childQueries;

    /**
     * <p>Constructor for Or.</p>
     *
     * @param childQueries a {@link java.util.Collection} object.
     */
    public Or(Collection<Query<E>> childQueries) {
        super(null);
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'Or' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
    }

    /**
     * <p>Getter for the field <code>childQueries</code>.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<Query<E>> getChildQueries() {
        return childQueries;
    }

    /** {@inheritDoc} */
    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('(');
        var iter = childQueries.iterator();
        var query = iter.next();
        index = query.appendQuerySql(sqlBuilder, index);
        while (iter.hasNext()) {
            sqlBuilder.append(") OR (");
            index = iter.next().appendQuerySql(sqlBuilder, index);
        }
        sqlBuilder.append(')');
        return index;
    }

    /** {@inheritDoc} */
    @Override
    public Tuple getQueryParams() {
        if (params == null) {
            params = appendQueryParams(Tuple.tuple());
        }
        return params;
    }

    /** {@inheritDoc} */
    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        childQueries.forEach(q -> q.appendQueryParams(tuple));
        return tuple;
    }
}
