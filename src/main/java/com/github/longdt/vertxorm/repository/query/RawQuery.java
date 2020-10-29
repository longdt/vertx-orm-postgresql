package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

public class RawQuery<E> extends AbstractQuery<E> {
    protected String querySql;
    protected Tuple params;

    public RawQuery(String querySql) {
        this(querySql, QueryFactory.EMPTY_PARAMS);
    }

    public RawQuery(String querySql, Tuple params) {
        this.querySql = querySql;
        this.params = params;
    }

    @Override
    public String getConditionSql() {
        return querySql;
    }

    @Override
    public Tuple getConditionParams() {
        return params;
    }
}
