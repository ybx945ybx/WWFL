package com.haitao.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 优惠首页
 */
public class DiscountHomeObject implements Serializable {

	/**
	 * 首页返回数据
	 */
	private static final long serialVersionUID = 1L;
	//热门返利商家
	public ArrayList<StoreObject> hot_store = null;
	//deal分页
	public DiscountPageObject new_deal = null;
}