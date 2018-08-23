package longdt.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collection;
import java.util.stream.Collectors;

public class And<E> implements Query<E> {
    private final Collection<Query<E>> childQueries;

    public And(Collection<Query<E>> childQueries) {
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'And' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
    }


    @Override
    public String getQuerySql() {
        return childQueries.stream().map(Query::getQuerySql)
                .collect(Collectors.joining(") AND (", "(", ")"));
    }

    @Override
    public JsonArray getParams() {
        return childQueries.stream().map(Query::getParams).collect(JsonArray::new, JsonArray::addAll, JsonArray::addAll);
    }
}
