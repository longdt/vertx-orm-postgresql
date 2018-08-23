package longdt.vertxorm.util;


import java.util.List;

public class Page<E> {
   private PageRequest pageRequest;
   private int totalPage;
   private int totalCount;
   private List<E> content;

   public Page(PageRequest pageRequest, int totalCount, List<E> content) {
      this.pageRequest = pageRequest;
      this.totalPage = (totalCount + (pageRequest.getSize() - 1)) / pageRequest.getSize();
      this.totalCount = totalCount;
      this.content = content;
   }

   public int getCurrentPage() {
      return pageRequest.getIndex();
   }

   public int getTotalPage() {
      return totalPage;
   }

   public int getTotalCount() {
      return totalCount;
   }

   public int getPageSize() {
      return pageRequest.getSize();
   }

   public List<E> getContent() {
      return content;
   }

}
