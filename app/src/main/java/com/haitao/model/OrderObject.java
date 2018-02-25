package com.haitao.model;
/**
 * 订单
 * Created by tqy on 15/12/2.
 */
public class OrderObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String id = "";
	public String status = "";//订单状态码
	public String amount = "";//订单金额
	public String trans_date = "";//交易时间
	public String store_name = "";//商家名称
	public String alliance_code = "";//商家在联盟的唯一码
	public String logo = "";//商家的logo
	public String add = "";//返利金额
	public String store_id = "";//(商家的ID) 必填
	public String status_text = "";//状态描述
	public String cashback = "";
	public String valid_time = "";//预计生效时间
	public String order_id = "";//订单号
	public String[] order_picture = null;//订单截图
	public String order_notes = "";//备注

	public String time = "";// (订单的交易时间(yyyy-mm-dd)) 必填
	public String order = "";// (订单号) 必填
	public String price = "";// (交易金额)
	public String email = ""; //购物商家注册邮箱




}