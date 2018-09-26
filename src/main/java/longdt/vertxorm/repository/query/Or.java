package longdt.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collection;
import java.util.stream.Collectors;

public class Or<E> extends AbstractQuery<E> {
    private final Collection<Query<E>> childQueries;

    public Or(Collection<Query<E>> childQueries) {
        if (childQueries.size() < 2) {
            throw new IllegalStateException("An 'Or' query cannot have fewer than 2 child queries, "
                    + childQueries.size() + " were supplied");
        }
        this.childQueries = childQueries;
    }

    @Override
    public String getConditionSql() {
        return childQueries.stream().map(Query::getConditionSql)
                .collect(Collectors.joining(") OR (", "(", ")"));
    }

    @Override
    public JsonArray getConditionParams() {
        return childQueries.stream().map(Query::getConditionParams).collect(JsonArray::new, JsonArray::addAll, JsonArray::addAll);
    }
}