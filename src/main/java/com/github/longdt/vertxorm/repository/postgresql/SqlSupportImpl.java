package com.github.longdt.vertxorm.repository.postgresql;

import com.github.longdt.vertxorm.repository.SqlSupport;
import com.github.longdt.vertxorm.repository.query.Query;
import com.github.longdt.vertxorm.repository.query.QueryFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SqlSupportImpl implements SqlSupport {
    private final String tableName;
    private final List<String> columnNames;
    private final String insertSql;
    private final String autoIdInsertSql;
    private final String upsertSql;
    private final String updateSql;
    private final String querySql;
    private final String queryByIdSql;
    private final String countSql;
    private final String existSql;
    private final String existByIdSql;
    private final String deleteSql;

    public SqlSupportImpl(String tableName, List<String> columnNames) {
        this.tableName = Objects.requireNonNull(tableName);
        this.columnNames = Objects.requireNonNull(columnNames);
        insertSql = "INSERT INTO \"" + tableName + "\" "
                + columnNames.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(",", "(", ")"))
                + " VALUES "
                + IntStream.rangeClosed(1, columnNames.size()).mapToObj(idx -> "$" + idx).collect(Collectors.joining(",", "(", ")"));
        autoIdInsertSql = "INSERT INTO \"" + tableName + "\" "
                + columnNames.stream().skip(1).map(c -> "\"" + c + "\"").collect(Collectors.joining(",", "(", ")"))
                + " VALUES "
                + IntStream.rangeClosed(1, columnNames.size() - 1).mapToObj(idx -> "$" + idx).collect(Collectors.joining(",", "(", ")"))
                + " RETURNING \"" + getIdName() + "\"";
        upsertSql = "INSERT INTO \"" + tableName + "\" "
                + columnNames.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(",", "(", ")"))
                + " VALUES "
                + IntStream.rangeClosed(1, columnNames.size()).mapToObj(idx -> "$" + idx).collect(Collectors.joining(",", "(", ")"))
                + " ON CONFLICT (\"" + getIdName() + "\") DO UPDATE SET "
                + columnNames.stream().skip(1).map(c -> "\"" + c + "\" = EXCLUDED.\"" + c + "\"").collect(Collectors.joining(", "));
        updateSql = "UPDATE \"" + tableName + "\""
                + " SET " + IntStream.range(1, columnNames.size()).mapToObj(idx -> "\"" + columnNames.get(idx) + "\" = $" + (idx + 1)).collect(Collectors.joining(","))
                + " WHERE \"" + getIdName() + "\" = $1";
        querySql = "SELECT " + columnNames.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(","))
                + " FROM \"" + tableName + "\"";
        queryByIdSql = "SELECT " + columnNames.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(","))
                + " FROM \"" + tableName + "\" WHERE \"" + getIdName() + "\" = $1";
        countSql = "SELECT count(*) FROM \"" + tableName + "\"";
        existSql = "SELECT 1 FROM \"" + tableName + "\"";
        existByIdSql = "SELECT 1 FROM \"" + tableName + "\" WHERE \"" + getIdName() + "\" = $1 LIMIT 1";
        deleteSql = "DELETE FROM \"" + tableName + "\" WHERE \"" + getIdName() + "\" = $1";
    }

    public String getTableName() {
        return tableName;
    }

    private String getIdName() {
        return columnNames.get(0);
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getAutoIdInsertSql() {
        return autoIdInsertSql;
    }

    @Override
    public String getUpsertSql() {
        return upsertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    @Override
    public String getQuerySql() {
        return querySql;
    }

    @Override
    public String getQueryByIdSql() {
        return queryByIdSql;
    }

    @Override
    public <E> String getSql(String sql, Query<E> query) {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        var index = 0;
        if (query != QueryFactory.EMPTY_QUERY) {
            sqlBuilder.append(" WHERE ");
            index = query.appendQuerySql(sqlBuilder, index);
        }
        if (query.orderBy() != null && !query.orderBy().isEmpty()) {
            sqlBuilder.append(" ORDER BY ");
            query.orderBy().forEach(o -> sqlBuilder.append("\"").append(o.getFieldName()).append("\" ")
                    .append(o.isDescending() ? "DESC," : "ASC,"));
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }

        if (query.limit() >= 0) {
            sqlBuilder.append(" LIMIT $").append(++index);
        }
        if (query.offset() >= 0) {
            sqlBuilder.append(" OFFSET $").append(++index);
        }
        return sqlBuilder.toString();
    }

    @Override
    public String getCountSql() {
        return countSql;
    }

    @Override
    public String getExistSql() {
        return existSql;
    }

    @Override
    public String getExistByIdSql() {
        return existByIdSql;
    }

    @Override
    public String getDeleteSql() {
        return deleteSql;
    }
}
