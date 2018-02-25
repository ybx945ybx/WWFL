package com.haitao.framework.codec;


import com.alibaba.fastjson.JSON;
import com.haitao.common.Constant.PageConstant;
import com.haitao.framework.codec.result.PageResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class PageResultDecoder<T> implements IConverter<String, PageResult<T>> {

	private Class entityClass = null;

	public PageResultDecoder(Class entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	public PageResult<T> converter(String entity) throws Exception {
		PageResult<T> result = new PageResult<T>();
		Object data = new JSONTokener(entity).nextValue();
		if (data instanceof JSONArray) {
			result.entityList = (ArrayList<T>) JSON.parseArray(entity.toString(),entityClass);
		}else{
			JSONObject obj = new JSONObject(entity);
			if(obj.has(PageConstant.COUNT)){
				result.total = "".equals(obj.getString(PageConstant.COUNT)) ? 0 : Integer.parseInt(obj.getString(PageConstant.COUNT));
			}
			if(obj.has(PageConstant.PAGE_COUNT)){
				result.pageCount = "".equals(obj.getString(PageConstant.PAGE_COUNT)) ? 0 : Integer.parseInt(obj.getString(PageConstant.PAGE_COUNT));
			}
			if(obj.has(PageConstant.LIST)) {
				result.entityList = (ArrayList<T>) JSON.parseArray(obj.getString(PageConstant.LIST),entityClass);
			}
		}

		return result;

	}
}
