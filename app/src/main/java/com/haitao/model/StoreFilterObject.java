package com.haitao.model;

import com.haitao.db.v2.StoreModel;

import java.util.ArrayList;

/**
 * 全部商家
 * Created by tqy on 15/12/2.
 */
public class StoreFilterObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String version = "";
	public String char_name = "";
	public String[] char_list = null;
	public ArrayList<StoreModel> list = null;
	public ArrayList<StoreObject> hot = null;
	public ArrayList<StoreObject> super_rebate = null;

}