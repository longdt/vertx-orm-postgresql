package com.github.longdt.vertxorm.model;

public class ArgumentDescription {
    private String name;
    private ValueType type;
    private String description;

    public enum ValueType {
        INTEGER, DECIMAL, STRING, DATE
    }

    public String getName() {
        return name;
    }

    public ArgumentDescription setName(String name) {
        this.name = name;
        return this;
    }

    public ValueType getType() {
        return type;
    }

    public ArgumentDescription setType(ValueType type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "ArgumentDescription{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
