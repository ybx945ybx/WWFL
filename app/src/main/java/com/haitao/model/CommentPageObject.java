package com.haitao.model;

import java.util.ArrayList;

/**
 * 分页的post
 * Created by tqy on 15/12/2.
 */
public class CommentPageObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String _total = "";
	public String _pagecount = "";
	public ArrayList<CommentObject> _data = null;
	
}