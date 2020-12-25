package com.github.longdt.vertxorm.repository;

import io.vertx.sqlclient.Row;

import java.util.List;
import java.util.function.Function;

public class Configuration<ID, E> {
    private String tableName;
    private List<String> columnNames;
    private IdAccessor<ID, E> idAccessor;
    protected Function<Row, E> rowMapper;
    protected Function<E, Object[]> parametersMapper;

    public String getTableName() {
        return tableName;
    }

    public Configuration<ID, E> setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Configuration<ID, E> setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    public IdAccessor<ID, E> getIdAccessor() {
        return idAccessor;
    }

    public Configuration<ID, E> setIdAccessor(IdAccessor<ID, E> idAccessor) {
        this.idAccessor = idAccessor;
        return this;
    }

    public Function<Row, E> getRowMapper() {
        return rowMapper;
    }

    public Configuration<ID, E> setRowMapper(Function<Row, E> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    public Function<E, Object[]> getParametersMapper() {
        return parametersMapper;
    }

    public Configuration<ID, E> setParametersMapper(Function<E, Object[]> parametersMapper) {
        this.parametersMapper = parametersMapper;
        return this;
    }
}
