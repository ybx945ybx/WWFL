package com.haitao.model;
/**
 * 评论
 * Created by tqy on 15/12/2.
 */
public class CommentObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 贴子评论ID
	 */
	public String id = "";
	/**
	 * 帖子ID
	 */
	public String tid = "";
	public String type = "";//d，优惠 ，s ,商家
	public String username = "";
	public String comments = "";
	public String postdate = "";
	public String praise_count = "";
	public String authorid = "";
	public String avatar = "";
	public String author_pic = "";
	public String release_time = "";
	public String author = "";
	public String[] pic = null;
	public String uid = "";
	public String source = "";
	public String is_praise = "0";


	//上传信息
	public String comment = "";//评论内容
	public String reply_username = "";//被回复用户名
	public String reply_comments = "";//被回复的内容
	public String reply_date = "";//被回复的时间
	public String op = "";
	public String content = "";//意见反馈的内容

	public QuoteCommentObject quote = null;//引入的评论
	public PhotoPickParameterObject mPhotoPickParameterInfo;

}