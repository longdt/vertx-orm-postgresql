package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.Collection;

public class And<E> extends AbstractQuery<E> {
    private final Collection<Query<E>> childQueries;

    public And(Collection<Query<E>> childQueries) {
        super(null);
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'And' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
    }

    public Collection<Query<E>> getChildQueries() {
        return childQueries;
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append('(');
        var iter = childQueries.iterator();
        var query = iter.next();
        index = query.appendQuerySql(sqlBuilder, index);
        while (iter.hasNext()) {
            sqlBuilder.append(") AND (");
            index = iter.next().appendQuerySql(sqlBuilder, index);
        }
        sqlBuilder.append(')');
        return index;
    }

    @Override
    public Tuple getQueryParams() {
        if (params == null) {
            params = appendQueryParams(Tuple.tuple());
        }
        return params;
    }

    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        childQueries.forEach(q -> q.appendQueryParams(tuple));
        return tuple;
    }
}