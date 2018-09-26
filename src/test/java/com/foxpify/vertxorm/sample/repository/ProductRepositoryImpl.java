package com.foxpify.vertxorm.sample.repository;

import com.foxpify.vertxorm.repository.impl.Config;
import io.vertx.ext.sql.SQLClient;
import com.foxpify.vertxorm.repository.impl.AbstractCrudRepository;

public class ProductRepositoryImpl extends AbstractCrudRepository<Long, Product> implements ProductRepository {
    public ProductRepositoryImpl(SQLClient sqlClient) {
        Config<Long, Product> conf = new Config.Builder<Long, Product>("bar", Product::new)
                .pk("id", Product::getId, Product::setId, true)
                .addField("name", Product::getName, Product::setName)
                .addField("description", Product::getDescription, Product::setDescription)
                .addTimestampTzField("created_date", Product::getCreatedDate, Product::setCreatedDate)
                .addTimestampTzField("updated_date", Product::getUpdatedDate, Product::setUpdatedDate)
                .build();
        init(sqlClient, conf);
    }
}
