package longdt.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class IsNull<E> extends SingleQuery<E> {
    public IsNull(String fieldName) {
        super(fieldName);
    }

    @Override
    public String getConditionSql() {
        return "\"" + fieldName + "\" is null";
    }

    @Override
    public JsonArray getConditionParams() {
        return new JsonArray(Collections.emptyList());
    }
}
