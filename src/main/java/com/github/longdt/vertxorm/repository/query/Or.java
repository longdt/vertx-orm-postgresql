package com.github.longdt.vertxorm.repository.query;

import com.github.longdt.vertxorm.util.Tuples;
import io.vertx.sqlclient.Tuple;

import java.util.Collection;

public class Or<E> extends AbstractQuery<E> {
    private final Collection<Query<E>> childQueries;

    public Or(Collection<Query<E>> childQueries) {
        super(null);
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'Or' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
        params = childQueries.stream().map(Query::getConditionParams).collect(Tuple::tuple, Tuples::addAll, Tuples::addAll);
    }

    public Collection<Query<E>> getChildQueries() {
        return childQueries;
    }

    @Override
    public void buildSQL(StringBuilder sqlBuilder, int startIdx) {
        sqlBuilder.append('(');
        var iter = childQueries.iterator();
        var query = (AbstractQuery<E>) iter.next();
        query.buildSQL(sqlBuilder, startIdx);
        startIdx += query.getConditionParams().size();
        while (iter.hasNext()) {
            sqlBuilder.append(") OR (");
            query = (AbstractQuery<E>) iter.next();
            query.buildSQL(sqlBuilder, startIdx);
            startIdx += query.getConditionParams().size();
        }
        sqlBuilder.append(')');
    }
}