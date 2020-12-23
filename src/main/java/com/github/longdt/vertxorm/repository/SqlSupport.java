package com.github.longdt.vertxorm.repository;

import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;

public interface SqlSupport {
    String getInsertSql();

    String getAutoIdInsertSql();

    String getUpsertSql();

    String getUpdateSql();

    String getQuerySql();

    String getQueryByIdSql();

    default <E> String getQuerySql(String sql, Query<E> query) {
        if (query != QueryFactory.EMPTY_QUERY) {
            StringBuilder sqlBuilder = new StringBuilder(sql)
                    .append(" WHERE ");
            query.appendQuerySql(sqlBuilder, 0);
            return sqlBuilder.toString();
        }
        return sql;
    }

    <E> String getSql(String sql, Query<E> query);

    String getCountSql();

    String getExistSql();

    String getExistByIdSql();

    String getDeleteSql();
}
