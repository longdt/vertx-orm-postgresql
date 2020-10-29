package com.github.longdt.vertxorm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.longdt.vertxorm.repository.base.BuilderImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RowMapper<ID, E> {

    static <ID, E> Builder<ID, E> builder(String tableName, Supplier<E> supplier) {
        return new BuilderImpl<>(tableName, supplier);
    }

    E map(Row row);

    interface Builder<ID, E> {
        Builder<ID, E> pk(String pkName, Function<E, ID> pkGetter, BiConsumer<E, ID> pkSetter);

        Builder<ID, E> pk(String pkName, Function<E, ID> pkGetter, BiConsumer<E, ID> pkSetter, boolean autogen);

        <T> Builder<ID, E> pkConverter(Function<ID, T> pkGetConverter, Function<T, ID> pkSetConverter);

        <T> Builder<ID, E> addField(String fieldName, Function<? super E, T> getter, BiConsumer<? super E, T> setter);

        <T, D> Builder<ID, E> addField(String fieldName, Function<? super E, T> getter, BiConsumer<? super E, T> setter, Function<T, D> getConverter, Function<D, T> setConverter);

        Builder<ID, E> addUuidField(String fieldName, Function<E, UUID> getter, BiConsumer<E, UUID> setter);

        <T> Builder<ID, E> addJsonField(String fieldName, Function<E, T> getter, BiConsumer<E, T> setter, Class<T> clazz);

        <T> Builder<ID, E> addJsonField(String fieldName, Function<E, T> getter, BiConsumer<E, T> setter, TypeReference<T> type);

        Builder<ID, E> addDecimalField(String fieldName, Function<E, BigDecimal> getter, BiConsumer<E, BigDecimal> setter);

        Builder<ID, E> addJsonObjectField(String fieldName, Function<E, JsonObject> getter, BiConsumer<E, JsonObject> setter);

        RowMapper<ID, E> build();
    }

}
