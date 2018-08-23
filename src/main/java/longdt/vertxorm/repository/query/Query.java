package longdt.vertxorm.repository.query;

import io.vertx.core.json.JsonArray;

public interface Query<E> {

    String getQuerySql();

    JsonArray getParams();
}