## Vertx-Orm-PostgresSQL
Simple API focusing on scalability and low overhead.

Reactive and non blocking which able to handle many database connections with a single thread by use `vertx-pg-client`
## Developers
### Testing
Out of the box, the test suite runs a Docker container using TestContainers.
### Maven dependency
```
<dependency>
    <groupId>com.github.longdt</groupId>
    <artifactId>vertx-orm-postgresql</artifactId>
    <version>1.2.0</version>
</dependency>
```
### Example
##### Define Entity class:

```
public class RuleTemplate {
    private Integer id;
    private String name;
    private Map<String, ArgumentDescription> arguments;
    private String flinkJob;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    ...
    setter/getter methods
    ...
}
```
##### Define Repository:
```
public interface RuleTemplateRepository extends CrudRepository<Integer, RuleTemplate> {
}
```
##### Implement Repository and declare field mapping
```
public class RuleTemplateRepositoryImpl extends AbstractCrudRepository<Integer, RuleTemplate> implements RuleTemplateRepository {
    public RuleTemplateRepositoryImpl(Pool pool) {
        var mapperBuilder = RowMapper.<Integer, RuleTemplate>builder("rule_template", RuleTemplate::new)
                .pk("id", RuleTemplate::getId, RuleTemplate::setId, true)
                .addField("name", RuleTemplate::getName, RuleTemplate::setName)
                .addJsonField("arguments", RuleTemplate::getArguments, RuleTemplate::setArguments, new TypeReference<>() {
                })
                .addField("flink_job", RuleTemplate::getFlinkJob, RuleTemplate::setFlinkJob)
                .addBooleanField("active", RuleTemplate::getActive, RuleTemplate::setActive)
                .addField("created_at", RuleTemplate::getCreatedAt, RuleTemplate::setCreatedAt)
                .addField("updated_at", RuleTemplate::getUpdatedAt, RuleTemplate::setUpdatedAt);

        init(pool, (RowMapperImpl<Integer, RuleTemplate>) mapperBuilder.build());
    }
}
```
##### Create repository instance:
```
RuleTemplateRepository repository = new RuleTemplateRepositoryImpl(pool);
```
##### Now it's time to use. Let's try some simple methods:
###### insert
```
var template = new RuleTemplate();
...
repository.insert(template, ar -> {
    if (ar.succeeded()) {
        System.out.println(ar.result());
    } else {
        ar.cause().printStackTrace();
    }
});
```
###### update
```
var template = new RuleTemplate().setId(1);
...
repository.update(template, ar -> {
    if (ar.succeeded()) {
        System.out.println(ar.result());
    } else {
        ar.cause().printStackTrace();
    }
});
```
###### find by id
```
repository.find(id, ar -> {
    if (ar.succeeded()) {
        System.out.println(ar.result());
    } else {
        ar.cause().printStackTrace();
    }
});
```
###### find by query
```
import static com.github.longdt.vertxorm.repository.query.QueryFactory.*;

var query = QueryFactory.<RuleTemplate>and("active", 1);
repository.findAll(query, ar -> {
    if (ar.succeeded()) {
        System.out.println(ar.result());
    } else {
        ar.cause().printStackTrace();
    }
});
```
###### find with paging
```
import static com.github.longdt.vertxorm.repository.query.QueryFactory.*;

var pageRequest = new PageRequest(1, 20);
var query = QueryFactory.<RuleTemplate>and("active", 1);
repository.findAll(query, pageRequest, ar -> {
    if (ar.succeeded()) {
        System.out.println(ar.result());
    } else {
        ar.cause().printStackTrace();
    }
});
```
###### transaction with SQLHelper
```
//find then update example
var id = 1;
SQLHelper.inTransactionSingle(repository.getPool()
        , conn -> repository.find(conn, id)     //find entity by id
                .map(entityOpt -> entityOpt.orElseThrow(() -> new EntityNotFoundException("id: " + id + " is not found")))
                .compose(entity -> {
                    //update entity
                    entity.setUpdatedAt(LocalDateTime.now());
                    return repository.update(conn, entity);
                })
        , ar -> {   //handle result of transaction
            if (ar.succeeded()) {
                System.out.println(ar.result());
            } else {
                ar.cause().printStackTrace();
            }
        });
```