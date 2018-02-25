package com.haitao.framework.http;


import android.util.Pair;

import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.IConverter;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IServiceDefine;
import com.haitao.framework.service.ServiceFactory;
import com.haitao.framework.utils.HTTPUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

public class HTTPEntityService<T> implements IEntityService<T> {
	private Class<T> entityClass = null;
	private Map<String,IServiceDefine> sds = null;

	public HTTPEntityService(Class<T> entityClass) {
        super();
		this.entityClass = entityClass;
		sds = ServiceFactory.getEntityServiceDefine(entityClass);
    }

	private String getURL(String method)
	{
		return ServiceFactory.getServiceDefine(entityClass, method).getURL();
	}
	private IConverter<T,String[]> getConvertor(String method)
	{
		return ServiceFactory.getServiceDefine(entityClass, method).requestParamConverter();
	}

	private IConverter<String,T> getDecoder(String method)
	{
		return ServiceFactory.getServiceDefine(entityClass, method).decoder();
	}


	@Override
	public void asynFunction(String method, T entity, IAsynServiceHandler<T> handler) {
		HTTPUtil.postAsyn(getURL(method), method, entity, getConvertor(method), getDecoder(method), handler);
	}

	@Override
	public void asynFunctionWidthFile(String method, T entity, Pair<String, File>[] files, IAsynServiceHandler<T> handler) {
		HTTPUtil.postMultypart(getURL(method), method, entity, files, getConvertor(method), getDecoder(method), handler);
	}

	@Override
	public void asynQuery( String method, T entity, IAsynServiceHandler<T> handler) {
		HTTPUtil.postQuery(getURL(method), method, entity, getConvertor(method), sds.get(method).getPageDecoder(), handler,new PageResult());
	}

	@Override
	public void asynQuery(PageResult page, String method, T entity, IAsynServiceHandler<T> handler) {
		HTTPUtil.postQuery(getURL(method), method, entity, getConvertor(method), sds.get(method).getPageDecoder(), handler,page);
	}
}
