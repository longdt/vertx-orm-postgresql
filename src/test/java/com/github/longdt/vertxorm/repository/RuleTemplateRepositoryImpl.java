package com.github.longdt.vertxorm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.longdt.vertxorm.model.RuleTemplate;
import com.github.longdt.vertxorm.repository.base.RowMapperImpl;
import com.github.longdt.vertxorm.repository.postgresql.AbstractCrudRepository;
import io.vertx.sqlclient.Pool;

public class RuleTemplateRepositoryImpl extends AbstractCrudRepository<Integer, RuleTemplate> implements RuleTemplateRepository {
    public RuleTemplateRepositoryImpl(Pool pool) {
        var mapperBuilder = RowMapper.<Integer, RuleTemplate>builder("rule_template", RuleTemplate::new)
                .pk("id", RuleTemplate::getId, RuleTemplate::setId, true)
                .addField("name", RuleTemplate::getName, RuleTemplate::setName)
                .addJsonField("arguments", RuleTemplate::getArguments, RuleTemplate::setArguments, new TypeReference<>() {
                })
                .addField("flink_job", RuleTemplate::getFlinkJob, RuleTemplate::setFlinkJob)
                .addField("active", RuleTemplate::getActive, RuleTemplate::setActive)
                .addField("created_at", RuleTemplate::getCreatedAt, RuleTemplate::setCreatedAt)
                .addField("updated_at", RuleTemplate::getUpdatedAt, RuleTemplate::setUpdatedAt);

        init(pool, (RowMapperImpl<Integer, RuleTemplate>) mapperBuilder.build());
    }
}
