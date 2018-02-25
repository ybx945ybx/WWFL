package com.haitao.model;

import java.util.ArrayList;

/**
 * 版块
 * Created by tqy on 15/12/2.
 */
public class SectionObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 版块ID
	 */
	public String fid = "";
	/**
	 * 父版块ID
	 */
	public String pid = "";
	/**
	 * 标题
	 */
	public String name = "";
	/**
	 * 主题量
	 */
	public String threads = "";
	/**
	 * 贴子量
	 */
	public String posts = "";
	/**
	 * 今天贴量
	 */
	public String todayposts = "";
	/**
	 * 图标
	 */
	public String icon = "";
	/**
	 * 是否收藏
	 */
	public String is_favorite = "0";

	/**
	 * 版块
	 */
	public ArrayList<SectionObject> forums = null;

	public PostPageObject list = null;
	public ArrayList<PostObject> tops = null;

	public String typeid = "";
	public String categoryName = "";
	public String page = "1";
	public String lpp = "20";
	public String order = "0";
	public boolean isSelected = false;

}