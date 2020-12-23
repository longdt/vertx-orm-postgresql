package com.github.longdt.vertxorm.repository;

public interface IdAccessor<ID, E> {

    ID getId(E entity);

    void setId(E entity, ID id);

    default Object id2DbValue(ID id) {
        return id;
    }

    @SuppressWarnings("unchecked")
    default ID db2IdValue(Object value) {
        return (ID) value;
    }
}
