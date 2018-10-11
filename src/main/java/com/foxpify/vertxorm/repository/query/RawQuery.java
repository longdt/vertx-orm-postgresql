package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class RawQuery<E> extends AbstractQuery<E> {
    protected String querySql;
    protected JsonArray params;

    public RawQuery(String querySql) {
        this(querySql, QueryFactory.EMPTY_PARAMS);
    }

    public RawQuery(String querySql, JsonArray params) {
        this.querySql = querySql;
        this.params = params;
    }

    @Override
    public String getConditionSql() {
        return querySql;
    }

    @Override
    public JsonArray getConditionParams() {
        return params;
    }
}
