package com.github.longdt.vertxorm.repository.base;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldMapping<E, T> {
    private String fieldName;
    private Function<? super E, T> getter;
    private BiConsumer<? super E, T> setter;

    @SuppressWarnings("unchecked")
    public FieldMapping(String fieldName, Function<? super E, T> getter, BiConsumer<? super E, T> setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object get(E entity) {
        return getter.apply(entity);
    }

    @SuppressWarnings("unchecked")
    public void set(E entity, Object value) {
        setter.accept(entity, (T) value);
    }
}