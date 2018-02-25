package com.haitao.model;

import java.util.ArrayList;

/**
 * 分页的邀请好友奖励
 * Created by tqy on 15/12/2.
 */
public class InviteRewardPageObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String _total = "";
	public String _pagecount = "";
	public ArrayList<InviteRewardObject> _data = null;
	
}