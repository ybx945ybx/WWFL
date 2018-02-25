package com.haitao.model;

import com.haitao.db.v2.TransportModel;

import java.util.ArrayList;

/**
 * 转运列表，包含热门转运
 */
public class TransportFilterObject extends BaseObject {
	public String char_name = "";
	public ArrayList<LogisticsCompanyObject> hot = null;
	public ArrayList<TransportModel> list = null;
}