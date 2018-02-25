package com.haitao.model;

import com.haitao.common.Enum.CalanderType;
import com.haitao.utils.calendar.CustomDate;

/**
 * 广告图
 * Created by tqy on 15/12/2.
 */
public class CalanderCellObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public CustomDate date;
	public CalanderType state;
	public int i;
	public int j;

	public CalanderCellObject(CustomDate date, CalanderType state, int i, int j) {
		super();
		this.date = date;
		this.state = state;
		this.i = i;
		this.j = j;
	}
	
}