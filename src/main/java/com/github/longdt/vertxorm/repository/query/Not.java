package com.github.longdt.vertxorm.repository.query;

public class Not<E> extends AbstractQuery<E> {
    private Query<E> negatedQuery;

    public Not(Query<E> negatedQuery) {
        super(negatedQuery.getConditionParams());
        this.negatedQuery = negatedQuery;
    }

    public Query<E> getNegatedQuery() {
        return negatedQuery;
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append("NOT (");
        ((AbstractQuery<E>) negatedQuery).buildSQL(sqlBuilder, startIdx);
        sqlBuilder.append(')');
    }
}
