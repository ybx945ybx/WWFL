package com.haitao.model;
/**
 * 提现
 * Created by tqy on 15/12/2.
 */
public class WithdrawObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 提现ID
	 */
	public String id = "";
	/**
	 * 提现金额
	 */
	public String amount = "";
	/**
	 * 提现类型
	 */
	public String type = "";
	//提现类型
	public String type_text = "";
	/**
	 * 提现状态
	 */
	public String status = "";
	//提现状态
	public String status_text = "";
	/**
	 * 提现时间
	 */
	public String dateline = "";
	/**
	 * 图标
	 */
	public String pic = "";
	/**
	 * 汇率
	 */
	public String rate = "";
	/**
	 * 可用金额
	 */
	public String current_money = "";
	/**
	 * 最低提现金额
	 */
	public String checkout_limit = "";
	/**
	 * 驳回原因
	 */
	public String reason = "";


	public String money = "";// (提现金额) 必填
	public String account_type = "";// (提现方式) 必填
	public String account = "";// (提现账号) 必填
	public String account_confirm = ""; //(确认账号) 必填
	public String account_date_m = "";// (信用卡必填 月份：08) 选填
	public String account_date_y = "";// (信用卡必填 年份：15) 选填
	public String account_name = "";// (非信用卡必填 账号姓名) 选填
	public String account_bank = ""; // (借记卡必填 开户行) 选填
	public String account_branch = "";// (借记卡部分开户行必填 开户网点) 选填
	public String code = "";// (验证码) 必填
	public String password = "";// (提现密码)

	public String waitcashback = "";

}