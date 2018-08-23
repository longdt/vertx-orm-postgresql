package longdt.vertxorm.repository.impl;

import io.vertx.core.json.JsonArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Config<ID, E> {
    private String tableName;
    private Supplier<E> supplier;
    private String pkName;
    private boolean pkAutoGen;
    private Function<E, ID> pkGetter;
    private BiConsumer<E, ID> pkSetter;
    private List<String> columnNames;
    private List<String> columnNamesPlusPK;
    private List<Mapping<E, ?>> mappings;
    private List<Mapping<E, ?>> mappingsPlusPK;

    public Config(Builder<ID, E> builder) {
        this.tableName = builder.tableName;
        this.supplier = builder.supplier;
        this.pkName = builder.pkName;
        this.pkAutoGen = builder.pkAutoGen;
        this.pkGetter = builder.pkGetter;
        this.pkSetter = builder.pkSetter;
        columnNames = builder.mappings.keySet().stream().filter(c -> !pkName.equals(c)).collect(Collectors.toList());

        columnNamesPlusPK = new ArrayList<>(columnNames.size() + 1);
        columnNamesPlusPK.add(pkName);
        columnNamesPlusPK.addAll(columnNames);

        mappings = builder.mappings.values().stream()
                .filter(m -> !pkName.equals(m.fieldName)).collect(Collectors.toList());
        mappingsPlusPK = new ArrayList<>(mappings.size() + 1);
        mappingsPlusPK.add(new Mapping<>(pkName, pkGetter, pkSetter));
        mappingsPlusPK.addAll(mappings);
    }

    public String tableName() {
        return tableName;
    }

    public E newInstance() {
        return supplier.get();
    }

    public String pkName() {
        return pkName;
    }

    public boolean isPkAutoGen() {
        return pkAutoGen;
    }

    public List<String> getColumnNames() {
        return getColumnNames(true);
    }

    public List<String> getColumnNames(boolean includePK) {
        return includePK ? columnNamesPlusPK : columnNames;
    }

    public List<Mapping<E, ?>> getMappings() {
        return getMappings(true);
    }

    public List<Mapping<E, ?>> getMappings(boolean includePK) {
        return includePK ? mappingsPlusPK : mappings;
    }

    public ID getId(E entity) {
        return pkGetter.apply(entity);
    }

    public void setId(E entity, ID id) {
        pkSetter.accept(entity, id);
    }

    public E toEntity(JsonArray rs) {
        E entity = supplier.get();
        int i = 0;
        for (Mapping<E, ?> m : getMappings()) {
            m.set(entity, rs.getValue(i));
            ++i;
        }
        return entity;
    }

    public JsonArray toJsonArray(E entity) {
        return toJsonArray(entity,true);
    }

    public JsonArray toJsonArray(E entity, boolean includePK) {
        return getMappings(includePK).stream().map(m -> m.get(entity))
                .collect(JsonArray::new, (objects, o) -> {
                    if (o == null) {
                        objects.addNull();
                    } else {
                        objects.add(o);
                    }
                }, JsonArray::addAll);
    }

    public static class Mapping<E, T> {
        private String fieldName;
        private Function<E, ?> getter;
        private BiConsumer<E, Object> setter;

        public Mapping(String fieldName, Function<E, ?> getter, BiConsumer<E, T> setter) {
            this.fieldName = fieldName;
            this.getter = getter;
            this.setter = (e, t) -> setter.accept(e, (T) t);
        }

        public String getFieldName() {
            return fieldName;
        }

        public Object get(E entity) {
            return getter.apply(entity);
        }

        public void set(E entity, Object value) {
            setter.accept(entity, value);
        }
    }

    public static class Builder<ID, E> {
        private String tableName;
        private Supplier<E> supplier;
        private String pkName;
        private boolean pkAutoGen;
        private Function<E, ID> pkGetter;
        private BiConsumer<E, ID> pkSetter;
        private Map<String, Mapping<E, ?>> mappings;

        public Builder(String tableName, Supplier<E> supplier) {
            this.tableName = tableName;
            this.supplier = supplier;
            mappings = new LinkedHashMap<>();
        }

        public Builder<ID, E> pk(String pkName, Function<E, ID> pkGetter, BiConsumer<E, ID> pkSetter) {
            return pk(pkName, pkGetter, pkSetter, false);
        }

        public Builder<ID, E> pk(String pkName, Function<E, ID> pkGetter, BiConsumer<E, ID> pkSetter, boolean autogen) {
            if (this.pkName != null) {
                mappings.remove(pkName);
            }
            this.pkName = pkName;
            this.pkGetter = pkGetter;
            this.pkSetter = pkSetter;
            this.pkAutoGen = autogen;
            mappings.put(pkName, new Mapping<>(pkName, pkGetter, pkSetter));
            return this;
        }

        public <T> Builder<ID, E> addField(String fieldName, Function<E, ?> getter, BiConsumer<E, T> setter) {
            mappings.put(fieldName, new Mapping<>(fieldName, getter, setter));
            return this;
        }

        public Builder<ID, E> addDateField(String fieldName, Function<E, LocalDate> getter, BiConsumer<E, LocalDate> setter) {
            mappings.put(fieldName, new Mapping<>(fieldName, entity ->
                    getter.apply(entity) == null ? null : getter.apply(entity).toString(),
                    (entity, value) -> {
                        if (value != null) setter.accept(entity, LocalDate.parse((CharSequence) value));
                    }));
            return this;
        }

        public Builder<ID, E> addTimeField(String fieldName, Function<E, LocalTime> getter, BiConsumer<E, LocalTime> setter) {
            mappings.put(fieldName, new Mapping<>(fieldName, entity ->
                    getter.apply(entity) == null ? null : getter.apply(entity).toString(),
                    (entity, value) -> {
                        if (value != null) setter.accept(entity, LocalTime.parse((CharSequence) value));
                    }));
            return this;
        }

        public Builder<ID, E> addTimestampField(String fieldName, Function<E, LocalDateTime> getter, BiConsumer<E, LocalDateTime> setter) {
            mappings.put(fieldName, new Mapping<>(fieldName, entity ->
                    getter.apply(entity) == null ? null : getter.apply(entity).toString(),
                    (entity, value) -> {
                        if (value != null) setter.accept(entity, LocalDateTime.parse((CharSequence) value));
                    }));
            return this;
        }

        public Builder<ID, E> addTimestampTzField(String fieldName, Function<E, OffsetDateTime> getter, BiConsumer<E, OffsetDateTime> setter) {
            mappings.put(fieldName, new Mapping<>(fieldName, entity ->
                    getter.apply(entity) == null ? null : getter.apply(entity).toString(),
                    (entity, value) -> {
                        if (value != null) setter.accept(entity, OffsetDateTime.parse((CharSequence) value));
                    }));
            return this;
        }

        public Config<ID, E> build() {
            return new Config<>(Builder.this);
        }
    }
}
