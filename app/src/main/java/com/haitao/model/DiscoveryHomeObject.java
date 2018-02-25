package com.haitao.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 发现首页
 */
public class DiscoveryHomeObject implements Serializable {

	/**
	 * 首页返回数据
	 */
	private static final long serialVersionUID = 1L;
	//进行中的试用
	public ArrayList<SampleObject> freetrial_list = null;
	//进行中的活动
	public ArrayList<PostObject> activity_list = null;
	//热门商家
	public ArrayList<StoreObject> hot_store = null;
	//热门转运
	public ArrayList<LogisticsCompanyObject> shipping_list = null;
}