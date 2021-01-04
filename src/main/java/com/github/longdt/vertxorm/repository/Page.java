package com.github.longdt.vertxorm.repository;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.shareddata.Shareable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>Page class.</p>
 *
 * @author Long Dinh
 * @version $Id: $Id
 */
@DataObject
public class Page<E> implements Shareable {
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

    /**
     * <p>Constructor for Page.</p>
     *
     * @param pageRequest a {@link com.github.longdt.vertxorm.repository.PageRequest} object.
     * @param totalElements a long.
     * @param content a {@link java.util.List} object.
     */
    public Page(PageRequest pageRequest, long totalElements, List<E> content) {
        this(pageRequest.getIndex(), pageRequest.getSize(), totalElements, content);
    }

    /**
     * <p>Constructor for Page.</p>
     *
     * @param currentPage a int.
     * @param pageSize a int.
     * @param totalElements a long.
     * @param content a {@link java.util.List} object.
     */
    public Page(int currentPage, int pageSize, long totalElements, List<E> content) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = (totalElements + (pageSize - 1)) / pageSize;
        this.totalElements = totalElements;
        this.content = content;
    }

    /**
     * <p>Constructor for Page.</p>
     *
     * @param jsonObject a {@link io.vertx.core.json.JsonObject} object.
     */
    public Page(JsonObject jsonObject) {
        try {
            DatabindCodec.mapper().updateValue(this, jsonObject);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>toJson.</p>
     *
     * @return a {@link io.vertx.core.json.JsonObject} object.
     */
    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    /**
     * <p>map.</p>
     *
     * @param mapFn a {@link java.util.function.Function} object.
     * @param <R> a R object.
     * @return a {@link com.github.longdt.vertxorm.repository.Page} object.
     */
    public <R> Page<R> map(Function<E, R> mapFn) {
        List<R> result = new ArrayList<>(content.size());
        content.forEach(e -> result.add(mapFn.apply(e)));
        return new Page<>(currentPage, pageSize, totalPage, totalElements, result);
    }

    /**
     * <p>Getter for the field <code>currentPage</code>.</p>
     *
     * @return a int.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * <p>Getter for the field <code>pageSize</code>.</p>
     *
     * @return a int.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * <p>Getter for the field <code>totalPage</code>.</p>
     *
     * @return a long.
     */
    public long getTotalPage() {
        return totalPage;
    }

    /**
     * <p>Getter for the field <code>totalElements</code>.</p>
     *
     * @return a long.
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * <p>Getter for the field <code>content</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<E> getContent() {
        return content;
    }
}
