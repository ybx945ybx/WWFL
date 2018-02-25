package com.haitao.model;

import java.util.ArrayList;

/**
 * 各种分类
 * Created by tqy on 15/12/2.
 */
public class CategoryListObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	//返利类型
	public ArrayList<TagObject> money_type = null;
	//返利状态
	public ArrayList<TagObject> cashback_status = null;
	//提现方式
	public ArrayList<TagObject> withdrawal_type = null;
	//提现状态
	public ArrayList<TagObject> withdrawal_status = null;
	//提现银行
	public ArrayList<TagObject> withdrawal_bank = null;
	//获取订单状态
	public ArrayList<TagObject> order_status = null;
	//达人类别
	public ArrayList<TagObject> talent_category = null;
	//获取优惠分类
	public ArrayList<TagObject> deal_category = null;
	//商家分类
	public ArrayList<TagObject> store_category = null;
	//商家国家
	public ArrayList<TagObject> store_country = null;
	//商家字典
	public ArrayList<TagObject> store_dict = null;
	
}