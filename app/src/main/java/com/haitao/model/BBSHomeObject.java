package com.haitao.model;

import java.util.ArrayList;

/**
 * 论坛首页
 * Created by tqy on 15/12/2.
 */
public class BBSHomeObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 广告下方的版块
	 */
	public ArrayList<TagObject> top_forum = null;
	/**
	 * 无利转让
	 */
	public ArrayList<PostObject> sales = null;
	/**
	 * 广告
	 */
	public ArrayList<AdObject> banner = null;
	/**
	 * 热贴
	 */
	public ArrayList<PostObject> hot_thread = null;
	/**
	 * 达人
	 */
	public ArrayList<TalentObject> doyens = null;
	/**
	 * 活动
	 */
	public ArrayList<PostObject> actives = null;
}