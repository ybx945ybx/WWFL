package com.haitao.model;
/**
 *邀请好友
 * Created by tqy on 15/12/8.
 */
public class InviteObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 邀请码
	 */
	public String invite_code = "";
	/**
	 * 累计获得
	 */
	public String amount = "";
	/**
	 * 邀请描述
	 */
	public String invite_msg = "";
	/**
	 * 第一页的邀请奖励
	 */
	public InviteRewardPageObject invite_list = null;
	/**
	 * 分享信息
	 */
	public String share_title = "";
	public String share_content = "";
	public String share_url = "";

	public String page = "1";
	public String lpp = "20";

}