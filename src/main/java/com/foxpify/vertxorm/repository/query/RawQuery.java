package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class RawQuery<E> extends AbstractQuery<E> {
    private String querySql;
    private JsonArray params;

    public RawQuery(String querySql) {
        this(querySql, new JsonArray(Collections.emptyList()));
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
