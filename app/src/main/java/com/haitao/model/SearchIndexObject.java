package com.haitao.model;


import java.util.ArrayList;

/**
 * 搜索引导页
 * Created by tqy on 15/12/2.
 */
public class SearchIndexObject extends BaseObject {
	//分类
	public ArrayList<TagObject> category_list;
	//热门商家
	public ArrayList<StoreObject> hot_stores;
	//热门标签
	public ArrayList<TagObject> tag_list;

}