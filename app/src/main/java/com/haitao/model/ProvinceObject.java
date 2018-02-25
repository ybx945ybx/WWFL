package com.haitao.model;

import java.util.ArrayList;

/**
 *省
 * Created by tqy on 15/12/8.
 */
public class ProvinceObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 省id
	 */
	public String id = "";
	/**
	 * 省名称
	 */
	public String province = "";
	/**
	 * 市列表
	 */
	public ArrayList<CityObject> city = null;
}