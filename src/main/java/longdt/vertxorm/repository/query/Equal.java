package longdt.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Collections;

public class Equal<E> extends SingleQuery<E> {
    private Object value;

    public Equal(String fieldName, Object value) {
        super(fieldName);
        this.value = value;
    }

    @Override
    public String getConditionSql() {
        return "\"" + fieldName + "\"=?";
    }

    @Override
    public JsonArray getConditionParams() {
        return new JsonArray(Collections.singletonList(value));
    }
}
