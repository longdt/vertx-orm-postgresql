package com.github.longdt.vertxorm.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;

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
        try {
            DatabindCodec.mapper().updateValue(this, jsonObject);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
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

    @JsonIgnore
    public long getOffset() {
        return (index - 1) * size;
    }
}
