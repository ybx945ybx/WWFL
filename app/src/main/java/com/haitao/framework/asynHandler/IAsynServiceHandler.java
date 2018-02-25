package com.haitao.framework.asynHandler;


import com.haitao.framework.codec.result.PageResult;

public interface IAsynServiceHandler<T> {
	
	void onSuccess(T entity) throws Exception;
	void onSuccessPage(PageResult<T> entity) throws Exception;
	void onFailed(String error);

}
