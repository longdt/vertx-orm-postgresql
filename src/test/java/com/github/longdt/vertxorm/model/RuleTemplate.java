package com.github.longdt.vertxorm.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RuleTemplate {
    private Integer id;
    private String name;
    private Map<String, ArgumentDescription> arguments;
    private String flinkJob;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RuleTemplate() {
    }

    public Integer getId() {
        return id;
    }

    public RuleTemplate setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RuleTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, ArgumentDescription> getArguments() {
        return arguments;
    }

    public RuleTemplate setArguments(Map<String, ArgumentDescription> arguments) {
        this.arguments = arguments;
        return this;
    }

    public String getFlinkJob() {
        return flinkJob;
    }

    public RuleTemplate setFlinkJob(String flinkJob) {
        this.flinkJob = flinkJob;
        return this;
    }

    public boolean getActive() {
        return active;
    }

    public RuleTemplate setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public RuleTemplate setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public RuleTemplate setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return "RuleTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", arguments=" + arguments +
                ", flinkJob='" + flinkJob + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
