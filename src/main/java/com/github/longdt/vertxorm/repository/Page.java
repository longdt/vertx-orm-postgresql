package com.github.longdt.vertxorm.repository;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@DataObject
public class Page<E> {
    private int currentPage;
    private int pageSize;
    private long totalPage;
    private long totalElements;
    private List<E> content;

    Page(int currentPage, int pageSize, long totalPage, long totalElements, List<E> content) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalElements = totalElements;
        this.content = content;
    }

    public Page(PageRequest pageRequest, long totalElements, List<E> content) {
        this(pageRequest.getIndex(), pageRequest.getSize(), totalElements, content);
    }

    public Page(int currentPage, int pageSize, long totalElements, List<E> content) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = (totalElements + (pageSize - 1)) / pageSize;
        this.totalElements = totalElements;
        this.content = content;
    }

    public Page(JsonObject jsonObject) {
        try {
            DatabindCodec.mapper().updateValue(this, jsonObject);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    public <R> Page<R> map(Function<E, R> mapFn) {
        List<R> result = new ArrayList<>(content.size());
        content.forEach(e -> result.add(mapFn.apply(e)));
        return new Page<>(currentPage, pageSize, totalPage, totalElements, result);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<E> getContent() {
        return content;
    }
}
