package com.haitao.model;

import java.util.ArrayList;

/**
 * 分页的转运评价
 * Created by tqy on 15/12/2.
 */
public class TransportPageObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String _total = "";
	public String _pagecount = "";
	public ArrayList<TransportCommentItemObject> _data = null;
	
}