package com.github.longdt.vertxorm.repository;

/**
 * <p>EntityNotFoundException class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param cause a {@link java.lang.Throwable} object.
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
