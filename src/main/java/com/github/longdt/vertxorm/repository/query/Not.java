package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

public class Not<E> extends AbstractQuery<E> {
    private Query<E> negatedQuery;

    public Not(Query<E> negatedQuery) {
        super(null);
        this.negatedQuery = negatedQuery;
    }

    public Query<E> getNegatedQuery() {
        return negatedQuery;
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        sqlBuilder.append("NOT (");
        index = negatedQuery.appendQuerySql(sqlBuilder, index);
        sqlBuilder.append(')');
        return index;
    }

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

    @Override
    public Tuple appendQueryParams(Tuple tuple) {
        return negatedQuery.appendQueryParams(tuple);
    }
}
