package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.model.ArgumentDescription;
import com.github.longdt.vertxorm.model.RuleTemplate;
import com.github.longdt.vertxorm.util.DatabaseTestCase;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RuleTemplateRepositoryImplTest extends DatabaseTestCase {
    private final RuleTemplateRepository repository = new RuleTemplateRepositoryImpl(pool);

    @Test
    void insert(Vertx vertx, VertxTestContext testContext) {
        var arguements = new HashMap<String, ArgumentDescription>();
        arguements.put("max_txn_cnt", new ArgumentDescription().setName("max_txn_cnt").setType(ArgumentDescription.ValueType.INTEGER));
        arguements.put("max_txn_amount", new ArgumentDescription().setName("max_txn_amount").setType(ArgumentDescription.ValueType.INTEGER));
        var now = LocalDateTime.now();
        var template = new RuleTemplate()
                .setActive(true)
                .setName("Sample Rule Template")
                .setFlinkJob("Flink Job")
                .setArguments(arguements)
                .setCreatedAt(now)
                .setUpdatedAt(now);
        repository.insert(template, testContext.succeeding(entity -> testContext.verify(() -> {
            assertNotNull(entity);
            assertEquals(entity.getId(), 1);
            assertEquals(entity.getFlinkJob(), template.getFlinkJob());
            assertTrue(entity.getActive());
            testContext.completeNow();
        })));
    }

    @Test
    void update(Vertx vertx, VertxTestContext testContext) {
        awaitCompletion(this::insert, vertx);
        var now = LocalDateTime.now();
        var template = new RuleTemplate()
                .setActive(true)
                .setName("Sample Rule Template")
                .setFlinkJob("Updated Flink Job")
                .setArguments(Collections.emptyMap())
                .setUpdatedAt(now)
                .setId(1);
        repository.update(template, testContext.succeeding(entity -> testContext.verify(() -> {
            assertNotNull(entity);
            assertEquals(entity.getId(), 1);
            assertEquals(entity.getFlinkJob(), template.getFlinkJob());
            assertTrue(entity.getArguments().isEmpty());
            assertTrue(entity.getActive());
            testContext.completeNow();
        })));
    }

    @Test
    void find(Vertx vertx, VertxTestContext testContext) {
        awaitCompletion(this::insert, vertx);
        repository.find(1, testContext.succeeding(rs -> testContext.verify(() -> {
            var entity = rs.orElseThrow();
            assertNotNull(entity);
            assertEquals(entity.getId(), 1);
            assertTrue(entity.getActive());
            testContext.completeNow();
        })));
    }

    @Test
    void findAll(Vertx vertx, VertxTestContext testContext) {
        awaitCompletion(this::insert, vertx);
        repository.findAll(testContext.succeeding(rs -> testContext.verify(() -> {
            assertEquals(rs.size(), 1);
            var entity = rs.get(0);
            assertNotNull(entity);
            assertEquals(entity.getId(), 1);
            assertTrue(entity.getActive());
            testContext.completeNow();
        })));
    }

    @Test
    void findPage(Vertx vertx, VertxTestContext testContext) {
        awaitCompletion(this::insert, vertx);
        repository.findAll(new PageRequest(1, 20), testContext.succeeding(rs -> testContext.verify(() -> {
            assertEquals(rs.getTotalElements(), 1);
            var entity = rs.getContent().get(0);
            assertNotNull(entity);
            assertEquals(entity.getId(), 1);
            assertTrue(entity.getActive());
            testContext.completeNow();
        })));
    }
}