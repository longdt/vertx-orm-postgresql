package com.github.longdt.vertxorm.repository.query;

/**
 * <p>Order class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class Order<E> {
    private String fieldName;
    private final boolean descending;

    /**
     * <p>Constructor for Order.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     */
    public Order(String fieldName) {
        this(fieldName, false);
    }

    /**
     * <p>Constructor for Order.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param descending a boolean.
     */
    public Order(String fieldName, boolean descending) {
        this.fieldName = fieldName;
        this.descending = descending;
    }

    /**
     * <p>Getter for the field <code>fieldName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     *
     * @return a boolean.
     */
    public boolean isAscending() {
        return !descending;
    }

    /**
     * Returns whether sorting for this property shall be descending.
     *
     * @return a boolean.
     */
    public boolean isDescending() {
        return descending;
    }
}
