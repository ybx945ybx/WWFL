package com.haitao.model;

import com.haitao.common.Constant.PageConstant;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeObject implements Serializable {

	/**
	 * 首页返回数据
	 */
	private static final long serialVersionUID = 1L;
	//广告图
	public ArrayList<AdObject> slide = null;
	//加倍商家
	public ArrayList<StoreObject> hot_store = null;
	//人气排行
	public ArrayList<DiscountObject> hot_deal = null;
	//社区热贴
	public ArrayList<PostObject> hot_thread = null;

	public String lpp = "20";
	//最新优惠
	public DiscountPageObject new_deal = null;
	//横栏广告
	public ArrayList<AdObject> cross_bar_ad = null;
	//弹出广告
	public AdObject popup_ad = null;

	public ArrayList<TagObject> category_list = null;

}