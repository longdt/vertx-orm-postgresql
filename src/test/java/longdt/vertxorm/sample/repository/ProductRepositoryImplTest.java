package longdt.vertxorm.sample.repository;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import longdt.vertxorm.repository.query.Query;
import longdt.vertxorm.util.Futures;
import longdt.vertxorm.util.Page;
import longdt.vertxorm.util.PageRequest;
import longdt.vertxorm.repository.query.QueryFactory;
import org.junit.Test;

import java.time.OffsetDateTime;

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
        Query<Product> query = QueryFactory.equal("name", "car");
        Page<Product> products = Futures.sync(productRepository::getPage, pageRequest, query);
        System.out.println(products.getTotalCount());
    }
}