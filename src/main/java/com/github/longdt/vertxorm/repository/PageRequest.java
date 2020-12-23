package com.github.longdt.vertxorm.repository;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * index start 1
 */
@DataObject
public class PageRequest {
    private int index;
    private int size;

    public PageRequest(int index, int size) {
        this.index = index;
        this.size = size;
    }

    public PageRequest(JsonObject jsonObject) {
        this.index = jsonObject.getInteger("index");
        this.size = jsonObject.getInteger("size");
    }

    public JsonObject toJson() {
        return new JsonObject().put("index", index).put("size", size);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getOffset() {
        return (index - 1) * (long) size;
    }
}
