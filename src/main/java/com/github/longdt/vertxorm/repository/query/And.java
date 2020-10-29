package com.github.longdt.vertxorm.repository.query;

import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.sqlclient.Tuple;

import java.util.Collection;
import java.util.stream.Collectors;

public class And<E> extends RawQuery<E> {
    private final Collection<Query<E>> childQueries;

    public And(Collection<Query<E>> childQueries) {
        super(null);
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'And' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
        querySql = childQueries.stream().map(Query::getConditionSql)
                .collect(Collectors.joining(") AND (", "(", ")"));
        params = childQueries.stream().map(Query::getConditionParams).collect(Tuple::tuple, Tuples::addAll, Tuples::addAll);
    }

    public Collection<Query<E>> getChildQueries() {
        return childQueries;
    }
}
