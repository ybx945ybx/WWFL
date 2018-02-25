package com.haitao.model;
/**
 * 手机验证码
 * Created by tqy on 15/12/2.
 */
public class MobileVerifyObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 手机号码
	 */
	public String mobile = "";
	/**
	 * 类型
	 */
	public String type = "";
	/**
	 * 验证码
	 */
	public String code = "";

	public String token = "";

	public String is_rebind = "0";
	/**
	 * 图片验证码
	 */
	public String url = "";
	/**
	 * 是否修改绑定
	 */
	public String updateBind = "0";
	/**
	 * 修改绑定原手机验证码
	 */
	public String phoneCode = "";

}