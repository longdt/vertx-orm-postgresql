# vertx-orm

Usage:

**Step 1: define pojo class**
```
package longdt.vertxorm.sample.repository;

import java.time.OffsetDateTime;

public class Product {
    private Long id;
    private String name;
    private String description;
    private OffsetDateTime createdDate;
    private OffsetDateTime updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public OffsetDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(OffsetDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
```

**Step 2: Define ProductRepository**
```
package longdt.vertxorm.sample.repository;

import longdt.vertxorm.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Long, Product> {

}
```

**Step 3: Implement ProductRepository**
```
package longdt.vertxorm.sample.repository;

import io.vertx.ext.sql.SQLClient;
import longdt.vertxorm.repository.impl.AbstractCrudRepository;
import longdt.vertxorm.repository.impl.Config;

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
```
**Step 4: Use ProductRepository in other class**
```
package longdt.vertxorm.sample.repository;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import longdt.vertxorm.repository.query.Query;
import longdt.vertxorm.util.Futures;
import longdt.vertxorm.util.Page;
import longdt.vertxorm.util.PageRequest;
import org.junit.Test;

import java.time.OffsetDateTime;

import static longdt.vertxorm.repository.query.QueryFactory.*;

public class ProductRepositoryImplTest {
    @Test
    public void createAndGet() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        JsonObject dbConf = new JsonObject()
                .put("host", "10.26.53.155")
                .put("port", 5432)
                .put("database", "sample")
                .put("username", "kyc_uat")
                .put("password", "ILoveKYC");

        AsyncSQLClient sqlClient = PostgreSQLClient.createShared(vertx, dbConf);
        ProductRepository productRepository = new ProductRepositoryImpl(sqlClient);
        Product product = new Product();
        product.setName("car");
        product.setDescription("this is the best car");
        OffsetDateTime now = OffsetDateTime.now();
        product.setCreatedDate(now);
        product.setUpdatedDate(now);
        productRepository.save(product, ar -> {
            if (ar.succeeded()) {
                System.out.println(product.getId());
            } else {
                ar.cause().printStackTrace();
            }
        });
//        Thread.sleep(5000);
        PageRequest pageRequest = new PageRequest(1, 20);
        Query<Product> query = equal("name", "car");
        Page<Product> products = Futures.sync(productRepository::getPage, pageRequest, query);
        System.out.println(products.getTotalCount());
    }
}
```
