package com.foxpify.vertxorm.repository.query;

public class Not<E> extends RawQuery<E> {
    private Query<E> negatedQuery;

    public Not(Query<E> negatedQuery) {
        super("NOT (" + negatedQuery.getConditionSql() + ")", negatedQuery.getConditionParams());
        this.negatedQuery = negatedQuery;
    }

    public Query<E> getNegatedQuery() {
        return negatedQuery;
    }
}
