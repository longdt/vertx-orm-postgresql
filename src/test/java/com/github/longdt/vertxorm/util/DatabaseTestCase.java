package com.github.longdt.vertxorm.util;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public abstract class DatabaseTestCase {
    private static final int DEFAULT_AWAIT_TIME_SECONDS = 10;
    private static final String SQL_FOLDER = "src/test/resources/migration/";
    private static final String SQL_TEST_FOLDER = "src/test/resources/script/";
    protected static final Pool pool;
    protected static final String database;

    static {
        var pgSQLContainer = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
                .withTmpFs(Collections.singletonMap("/var/lib/mysql", "rw"));
        pgSQLContainer.start();

        database = pgSQLContainer.getDatabaseName();
        var connectOptions = new PgConnectOptions()
                .setHost(pgSQLContainer.getContainerIpAddress())
                .setPort(pgSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                .setDatabase(database)
                .setUser(pgSQLContainer.getUsername())
                .setPassword(pgSQLContainer.getPassword());
        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(10);
        pool = PgPool.pool(connectOptions, poolOptions);
    }

    @BeforeEach
    protected void setUp(Vertx vertx, VertxTestContext testContext) {
        try {
            Files.list(Path.of(SQL_FOLDER)).sorted()
                    .forEach(p -> Futures.join(prepareDB(p)));
            testContext.completeNow();
        } catch (Exception e) {
            e.printStackTrace();
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @AfterEach
    protected void tearDown(Vertx vertx, VertxTestContext testContext) {
        pool.withTransaction(conn -> conn.query("SELECT concat('DROP TABLE IF EXISTS \"', table_name, '\";')\n" +
                        "FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'")
                        .collecting(Collectors.mapping(r -> r.getString(0), Collectors.toList()))
                        .execute()
                        .compose(sqlResult -> {
                            StringBuilder sql = new StringBuilder("SET session_replication_role = 'replica';");
                            sqlResult.value().forEach(sql::append);
                            sql.append("SET session_replication_role = 'origin';");
                            return conn.query(sql.toString()).execute();
                        })
                , testContext.succeedingThenComplete());
    }

    protected void awaitCompletion(BiConsumer<Vertx, VertxTestContext> consumer, Vertx vertx) {
        VertxTestContext testContext = new VertxTestContext();
        try {
            consumer.accept(vertx, testContext);
            assertThat(testContext.awaitCompletion(DEFAULT_AWAIT_TIME_SECONDS, TimeUnit.SECONDS)).isTrue();
            if (testContext.failed()) {
                throw testContext.causeOfFailure();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected void awaitCompletion(VertxTestContext testContext) {
        try {
            assertThat(testContext.awaitCompletion(DEFAULT_AWAIT_TIME_SECONDS, TimeUnit.SECONDS)).isTrue();
            if (testContext.failed()) {
                throw testContext.causeOfFailure();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RowSet<Row>> prepareDB(Path script) {
        try {
            var content = Files.readString(script, StandardCharsets.UTF_8);
            return pool.query(content).execute();
        } catch (IOException e) {
            return Future.failedFuture(e);
        }
    }

    public Future<RowSet<Row>> prepareDB(String sqlFile) {
        return prepareDB(Paths.get(SQL_TEST_FOLDER, sqlFile));
    }
}
