package com.haitao.model;

/**
 * 标签/分类通用
 * Created by tqy on 15/12/2.
 */
public class TagObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public String id = "";

	public String text = "";
	/**
	 * 名称
	 */
	public String name = "";
	/**
	 * 是否需要开户行（用户提现的银行卡）
	 */
	public String branch = "";
	/**
	 * 支付方式的图标（用户提现的支付方式）
	 */
	public String pic = "";
	/**
	 * 主题类别的所属板块id
	 */
	public String fid = "";
	/**
	 * 优惠详情标签：type为cate表示分类，为tag表示标签，同时也是标记数据字典的类型
	 */
	public String type = "cate";
	/**
	 * 分类对应的图片
	 */
	public String img = "";

	////////////////////////////
	//标签
	///////////////////////////
	/**
	 * 标签id
	 */
	public String tag_id = "";
	/**
	 * 标签名称
	 */
	public String tag_name = "";
	/**
	 * 来源ID
	 */
	public String data_id = "";
	/**
	 * 来源类型 - 1：品牌 2：商家
	 */
	public String data_type = "";
	/**
	 * 排序
	 */
	public String sort = "";
	
}