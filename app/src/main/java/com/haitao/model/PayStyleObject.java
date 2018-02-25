package com.haitao.model;
/**
 * 支付方式
 * Created by tqy on 15/12/2.
 */
public class PayStyleObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String id = "";
	public String text = "";

	public PayStyleObject(String id,String text){
		this.id = id;
		this.text = text;
	}
	
}