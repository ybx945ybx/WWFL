package com.haitao.framework.service;

import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;


public interface IViewContext<T,B> {
	PageResult getPage();
	T getEntity();
	B getService();

	void asynQuery(String method,T entity, IAsynServiceHandler<T> handler);
	void asynQuery(int pageSize,String method, T entity, IAsynServiceHandler<T> handler);
	void asynQueryNext(String method ,T entity,IAsynServiceHandler<T> handler);
}
