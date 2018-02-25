package com.haitao.model;

import java.util.ArrayList;

/**
 * 分页的discount
 * Created by tqy on 15/12/2.
 */
public class DiscountPageObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String _total = "";
	public String _pagecount = "";
	public ArrayList<DiscountObject> _data = null;
	
}