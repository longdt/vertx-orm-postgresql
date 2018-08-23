package longdt.sample.repository;

import io.vertx.ext.sql.SQLClient;
import longdt.repository.impl.AbstractCrudRepository;
import longdt.repository.impl.Config;

public class ProductRepositoryImpl extends AbstractCrudRepository<Long, Product> implements ProductRepository {
    public ProductRepositoryImpl(SQLClient sqlClient) {
        Config<Long, Product> conf = new Config.Builder<Long, Product>("bar", Product::new)
                .pk("id", Product::getId, Product::setId, true)
                .addField("name", Product::getName, Product::setName)
                .addField("description", Product::getDescription, Product::setDescription)
                .addTimestampzField("created_date", Product::getCreatedDate, Product::setCreatedDate)
                .addTimestampzField("updated_date", Product::getUpdatedDate, Product::setUpdatedDate)
                .build();
        init(sqlClient, conf);
    }
}
