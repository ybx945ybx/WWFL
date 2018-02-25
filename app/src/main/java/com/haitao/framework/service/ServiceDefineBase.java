package com.haitao.framework.service;

import com.haitao.framework.codec.IConverter;
import com.haitao.framework.codec.DefaultResultDecoder;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.codec.PageResultDecoder;
import com.haitao.framework.codec.ParameterConvertor;
import com.haitao.imp.URILocatorHelper;

import org.json.JSONObject;


public class ServiceDefineBase<T, S> implements IServiceDefine<T, S> {
	public String name = "";
	private Class<T> entityClass = null;

	public ServiceDefineBase(Class<T> entityClass, String name) {
		this.setName(name);
		this.entityClass = entityClass;
	}

	public ServiceDefineBase(Class<T> entityClass, String name,
			IConverter<T, String[]> requestConverter,
			IConverter<String, T> objDecoder, S s) {
		ini(entityClass, name, requestConverter, objDecoder, s);
	}

	private void ini(Class<T> entityClass, String name,
			IConverter<T, String[]> requestConverter,
			IConverter<String, T> objDecoder, S s) {
		this.setName(name);
		this.entityClass = entityClass;
		url = URILocatorHelper.getIURILocator(entityClass).getURI("");
		this.requestConverter = requestConverter;
		this.objDecoder = objDecoder;
		this.service = s;
		pageDecoder = new PageResultDecoder<T>(entityClass);
	}

	@SuppressWarnings("unchecked")
	public ServiceDefineBase(Class<T> entityClass, String name,
			IConverter<T, String[]> requestConverter,
			IConverter<String, T> objDecoder) {
		ini(entityClass, name, requestConverter, objDecoder,
				(S) ServiceFactory.getService(entityClass));
	}

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	private String url = null;

	@Override
	public String getURL() {
		return URILocatorHelper.getIURILocator(entityClass).getURI(getName());
	}

	protected IConverter<T, String[]> requestConverter = null;

	@Override
	public IConverter<T, String[]> requestParamConverter() {
		if (null == requestConverter) {
			requestConverter = new ParameterConvertor<T>();
		}
		return requestConverter;
	}

	protected IConverter<String, T> objDecoder = null;

	@Override
	public IConverter<String, T> decoder() {
		if (null == objDecoder) {
			objDecoder = new DefaultResultDecoder<T>(entityClass);
		}
		return objDecoder;
	}

	private S service = null;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public S getService() {
		if (null == service) {
			service = ServiceFactory.getService(entityClass);
		}
		return service;
	}

	private IConverter<String, PageResult<T>> pageDecoder = null;

	@Override
	public IConverter<String, PageResult<T>> getPageDecoder() {
		if (null == pageDecoder) {
			pageDecoder = new PageResultDecoder<T>(entityClass);
		}
		return pageDecoder;
	}

	public ServiceDefineBase setPageDecoder(
			IConverter<String, PageResult<T>> pageDecoder) {
		this.pageDecoder = pageDecoder;
		return this;
	}

	public ServiceDefineBase setUrl(String url) {
		this.url = url;
		return this;
	}

	public ServiceDefineBase setRequestConverter(
			IConverter<T, String[]> requestConverter) {
		this.requestConverter = requestConverter;
		return this;
	}
	
	public ServiceDefineBase setRequestConverter(
			String[] properties) {
		this.requestConverter = new ParameterConvertor(properties);
		return this;
	}

	public ServiceDefineBase setObjDecoder(IConverter<String, T> objDecoder) {
		this.objDecoder = objDecoder;
		return this;
	}

	public ServiceDefineBase setService(S service) {
		this.service = service;
		return this;
	}

}
