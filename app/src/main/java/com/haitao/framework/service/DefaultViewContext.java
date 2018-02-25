package com.haitao.framework.service;


import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;

public class DefaultViewContext<T, B extends IEntityService<T>> implements IViewContext<T, B> {

    private T entity;
    private B service;
    private PageResult page = new PageResult();

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public B getService() {
        return service;
    }

    public void setService(B service) {
        this.service = service;
    }

    public PageResult getPage() {
        return page;
    }

    @Override
    public void asynQuery(String method, T entity, IAsynServiceHandler<T> handler) {
        service.asynQuery(method, entity, handler);
    }

    @Override
    public void asynQuery(int pageSize, String method, T entity, IAsynServiceHandler<T> handler) {
        page.pagesize = pageSize;
        service.asynQuery(page, method, entity, handler);
    }

    @Override
    public void asynQueryNext(String method, T entity, IAsynServiceHandler<T> handler) {
        page.page++;
        service.asynQuery(page, method, entity, handler);
    }
}
