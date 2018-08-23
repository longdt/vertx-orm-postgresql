package longdt.repository.query;

import io.vertx.core.json.JsonArray;

public interface Query<E> {

    String getQuerySql();

    JsonArray getParams();
}