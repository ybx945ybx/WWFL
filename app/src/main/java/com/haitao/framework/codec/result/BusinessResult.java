package com.haitao.framework.codec.result;



public class BusinessResult<T> extends OperationResult{
    //返回的对象
    private T entity = null;
    //返回的列表
    private PageResult<T> pageResult = null;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public PageResult<T> getPageResult() {
        return pageResult;
    }

    public void setPageResult(PageResult<T> pageResult) {
        this.pageResult = pageResult;
    }
}
