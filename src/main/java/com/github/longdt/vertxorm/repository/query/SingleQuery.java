package com.github.longdt.vertxorm.repository.query;

import io.vertx.sqlclient.Tuple;

import java.util.List;

/**
 * <p>Abstract SingleQuery class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public abstract class SingleQuery<E> extends AbstractQuery<E> {
    protected String fieldName;

    /**
     * <p>Constructor for SingleQuery.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public SingleQuery(String fieldName, Object value) {
        this(fieldName, Tuple.of(value));
    }

    /**
     * <p>Constructor for SingleQuery.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public SingleQuery(String fieldName, Object... value) {
        this(fieldName, Tuple.wrap(value));
    }

    /**
     * <p>Constructor for SingleQuery.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param value a {@link java.util.List} object.
     */
    public SingleQuery(String fieldName, List<Object> value) {
        this(fieldName, Tuple.wrap(value));
    }

    /**
     * <p>Constructor for SingleQuery.</p>
     *
     * @param fieldName a {@link java.lang.String} object.
     * @param params a {@link io.vertx.sqlclient.Tuple} object.
     */
    public SingleQuery(String fieldName, Tuple params) {
        super(params);
        this.fieldName = fieldName;
    }

    /**
     * <p>Getter for the field <code>fieldName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFieldName() {
        return fieldName;
    }
}
