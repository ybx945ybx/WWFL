package com.haitao.utils.verifycode;


import com.haitao.model.BaseObject;

/**
 * 解析响应基类
 */
public class ResponseModel<T> extends BaseObject {
	/**
	 * 响应状态，result为true表示请求成功
	 */
	public String code = "";
	/**
	 * 响应描述，如果请求失败,msg为错误信息
	 */
	public String msg = "";
	/**
	 * 响应的实体类
	 */
	public T data;

}