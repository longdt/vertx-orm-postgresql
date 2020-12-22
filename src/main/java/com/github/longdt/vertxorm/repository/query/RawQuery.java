package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.regex.Pattern;

public class RawQuery<E> extends AbstractQuery<E> {
    private static final Pattern pattern = Pattern.compile("\\?");
    protected String querySql;

    public RawQuery(String querySql) {
        this(querySql, QueryFactory.EMPTY_PARAMS);
    }

    public RawQuery(String querySql, Object... params) {
        this(querySql, Tuple.wrap(params));
    }

    public RawQuery(String querySql, Tuple params) {
        super(params);
        this.querySql = querySql;
    }

    @Override
    public int appendQuerySql(StringBuilder sqlBuilder, int index) {
        if (params.size() == 0) {
            sqlBuilder.append(querySql);
            return index;
        }
        var matcher = pattern.matcher(querySql);
        while (matcher.find()) {
            matcher.appendReplacement(sqlBuilder, "\\$");
            sqlBuilder.append(++index);
        }
        matcher.appendTail(sqlBuilder);
        return index;
    }
}
