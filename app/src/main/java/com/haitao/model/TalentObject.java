package com.haitao.model;

public class TalentObject extends BaseObject {
	/**
	 * 达人
	 */
	private static final long serialVersionUID = 1L;
	public String uid = "";
	public String username = "";
	public String avator = "";
	public String category = "";
	public String level = "";
	public String thread_count = "";
	public String digest_thread_count = "";
	public String gold = "";

	public String page = "1";
	public String lpp = "20";

	public PostPageObject thread = null;

}