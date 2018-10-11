package com.foxpify.vertxorm.repository.query;

public class Order<E> {
    private String fieldName;
    private final boolean descending;

    public Order(String fieldName) {
        this(fieldName, false);
    }

    public Order(String fieldName, boolean descending) {
        this.fieldName = fieldName;
        this.descending = descending;
    }

    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     *
     * @return
     */
    public boolean isAscending() {
        return !descending;
    }

    /**
     * Returns whether sorting for this property shall be descending.
     *
     * @return
     */
    public boolean isDescending() {
        return descending;
    }
}
