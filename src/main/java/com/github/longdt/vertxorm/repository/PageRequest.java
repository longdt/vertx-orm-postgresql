package com.github.longdt.vertxorm.repository;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * index start 1
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
@DataObject
public class PageRequest {
    private int index;
    private int size;

    /**
     * <p>Constructor for PageRequest.</p>
     *
     * @param index a int.
     * @param size a int.
     */
    public PageRequest(int index, int size) {
        this.index = index;
        this.size = size;
    }

    /**
     * <p>Constructor for PageRequest.</p>
     *
     * @param jsonObject a {@link io.vertx.core.json.JsonObject} object.
     */
    public PageRequest(JsonObject jsonObject) {
        this.index = jsonObject.getInteger("index");
        this.size = jsonObject.getInteger("size");
    }

    /**
     * <p>toJson.</p>
     *
     * @return a {@link io.vertx.core.json.JsonObject} object.
     */
    public JsonObject toJson() {
        return new JsonObject().put("index", index).put("size", size);
    }

    /**
     * <p>Getter for the field <code>index</code>.</p>
     *
     * @return a int.
     */
    public int getIndex() {
        return index;
    }

    /**
     * <p>Setter for the field <code>index</code>.</p>
     *
     * @param index a int.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * <p>Getter for the field <code>size</code>.</p>
     *
     * @return a int.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Setter for the field <code>size</code>.</p>
     *
     * @param size a int.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * <p>getOffset.</p>
     *
     * @return a long.
     */
    public long getOffset() {
        return (index - 1) * (long) size;
    }
}
