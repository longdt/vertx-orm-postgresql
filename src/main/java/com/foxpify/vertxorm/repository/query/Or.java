package com.foxpify.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collection;
import java.util.stream.Collectors;

public class Or<E> extends RawQuery<E> {
    private final Collection<Query<E>> childQueries;

    public Or(Collection<Query<E>> childQueries) {
        super(null);
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'Or' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
        querySql = childQueries.stream().map(Query::getConditionSql)
                .collect(Collectors.joining(") OR (", "(", ")"));
        params = childQueries.stream().map(Query::getConditionParams).collect(JsonArray::new, JsonArray::addAll, JsonArray::addAll);
    }

    public Collection<Query<E>> getChildQueries() {
        return childQueries;
    }
}