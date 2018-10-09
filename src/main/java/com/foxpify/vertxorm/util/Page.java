package com.foxpify.vertxorm.util;

import com.dslplatform.json.CompiledJson;

import java.util.List;

public class Page<E> {
   private int currentPage;
   private int pageSize;
   private int totalPage;
   private int totalCount;
   private List<E> content;

   @CompiledJson
   Page(int currentPage, int pageSize, int totalPage, int totalCount, List<E> content) {
      this.currentPage = currentPage;
      this.pageSize = pageSize;
      this.totalPage = totalPage;
      this.totalCount = totalCount;
      this.content = content;
   }

   public Page(PageRequest pageRequest, int totalCount, List<E> content) {
      this(pageRequest.getIndex(), pageRequest.getSize(), totalCount,content);
   }

   public Page(int currentPage, int pageSize, int totalCount, List<E> content) {
      this.currentPage = currentPage;
      this.pageSize = pageSize;
      this.totalPage = (totalCount + (pageSize - 1)) / pageSize;
      this.totalCount = totalCount;
      this.content = content;
   }

   public int getCurrentPage() {
      return currentPage;
   }

   public int getPageSize() {
      return pageSize;
   }

   public int getTotalPage() {
      return totalPage;
   }

   public int getTotalCount() {
      return totalCount;
   }

   public List<E> getContent() {
      return content;
   }
}
