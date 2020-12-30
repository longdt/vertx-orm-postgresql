package com.github.longdt.vertxorm.repository;

/**
 * <p>IdAccessor interface.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public interface IdAccessor<ID, E> {

    /**
     * <p>getId.</p>
     *
     * @param entity a E object.
     * @return a ID object.
     */
    ID getId(E entity);

    /**
     * <p>setId.</p>
     *
     * @param entity a E object.
     * @param id a ID object.
     */
    void setId(E entity, ID id);

    /**
     * <p>id2DbValue.</p>
     *
     * @param id a ID object.
     * @return a {@link java.lang.Object} object.
     */
    default Object id2DbValue(ID id) {
        return id;
    }

    /**
     * <p>db2IdValue.</p>
     *
     * @param value a {@link java.lang.Object} object.
     * @return a ID object.
     */
    @SuppressWarnings("unchecked")
    default ID db2IdValue(Object value) {
        return (ID) value;
    }
}
