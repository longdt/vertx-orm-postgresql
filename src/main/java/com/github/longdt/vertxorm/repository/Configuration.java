package com.github.longdt.vertxorm.repository;

import io.vertx.sqlclient.Row;

import java.util.List;
import java.util.function.Function;

/**
 * <p>Configuration class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Configuration<ID, E> {
    private String tableName;
    private List<String> columnNames;
    private IdAccessor<ID, E> idAccessor;
    protected Function<Row, E> rowMapper;
    protected Function<E, Object[]> parametersMapper;

    /**
     * <p>Getter for the field <code>tableName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * <p>Setter for the field <code>tableName</code>.</p>
     *
     * @param tableName a {@link java.lang.String} object.
     * @return a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public Configuration<ID, E> setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * <p>Getter for the field <code>columnNames</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * <p>Setter for the field <code>columnNames</code>.</p>
     *
     * @param columnNames a {@link java.util.List} object.
     * @return a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public Configuration<ID, E> setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    /**
     * <p>Getter for the field <code>idAccessor</code>.</p>
     *
     * @return a {@link com.github.longdt.vertxorm.repository.IdAccessor} object.
     */
    public IdAccessor<ID, E> getIdAccessor() {
        return idAccessor;
    }

    /**
     * <p>Setter for the field <code>idAccessor</code>.</p>
     *
     * @param idAccessor a {@link com.github.longdt.vertxorm.repository.IdAccessor} object.
     * @return a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public Configuration<ID, E> setIdAccessor(IdAccessor<ID, E> idAccessor) {
        this.idAccessor = idAccessor;
        return this;
    }

    /**
     * <p>Getter for the field <code>rowMapper</code>.</p>
     *
     * @return a {@link java.util.function.Function} object.
     */
    public Function<Row, E> getRowMapper() {
        return rowMapper;
    }

    /**
     * <p>Setter for the field <code>rowMapper</code>.</p>
     *
     * @param rowMapper a {@link java.util.function.Function} object.
     * @return a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public Configuration<ID, E> setRowMapper(Function<Row, E> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    /**
     * <p>Getter for the field <code>parametersMapper</code>.</p>
     *
     * @return a {@link java.util.function.Function} object.
     */
    public Function<E, Object[]> getParametersMapper() {
        return parametersMapper;
    }

    /**
     * <p>Setter for the field <code>parametersMapper</code>.</p>
     *
     * @param parametersMapper a {@link java.util.function.Function} object.
     * @return a {@link com.github.longdt.vertxorm.repository.Configuration} object.
     */
    public Configuration<ID, E> setParametersMapper(Function<E, Object[]> parametersMapper) {
        this.parametersMapper = parametersMapper;
        return this;
    }
}
