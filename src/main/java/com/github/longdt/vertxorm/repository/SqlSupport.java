package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;

/**
 * <p>SqlSupport interface.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public interface SqlSupport {
    /**
     * <p>getInsertSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getInsertSql();

    /**
     * <p>getAutoIdInsertSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getAutoIdInsertSql();

    /**
     * <p>getUpsertSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getUpsertSql();

    /**
     * <p>getUpdateSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getUpdateSql();

    /**
     * <p>getQuerySql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getQuerySql();

    /**
     * <p>getQueryByIdSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getQueryByIdSql();

    /**
     * <p>getQuerySql.</p>
     *
     * @param sql a {@link java.lang.String} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @param <E> a E object.
     * @return a {@link java.lang.String} object.
     */
    default <E> String getQuerySql(String sql, Query<E> query) {
        if (query.isConditional()) {
            StringBuilder sqlBuilder = new StringBuilder(sql)
                    .append(" WHERE ");
            query.appendQuerySql(sqlBuilder, 0);
            return sqlBuilder.toString();
        }
        return sql;
    }

    /**
     * <p>getSql.</p>
     *
     * @param sql a {@link java.lang.String} object.
     * @param query a {@link com.github.longdt.vertxorm.repository.query.Query} object.
     * @param <E> a E object.
     * @return a {@link java.lang.String} object.
     */
    <E> String getSql(String sql, Query<E> query);

    /**
     * <p>getCountSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getCountSql();

    /**
     * <p>getExistSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getExistSql();

    /**
     * <p>getExistByIdSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getExistByIdSql();

    /**
     * <p>getDeleteSql.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getDeleteSql();
}
