package com.haitao.model;

import java.util.ArrayList;

/**
 * 贴子
 * Created by tqy on 15/12/2.
 */
public class PostObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	/**
	 * 贴子ID
	 */
	public String id = "";
	/**
	 * 贴子标题
	 */
	public String title = "";
	/**
	 * 贴子图片
	 */
	public String pic = "";
	/**
	 * 多张图片
	 */
	public String[] pics = null;
	/**
	 * 活动图标
	 */
	public String[] pic_info = null;
	/**
	 * 活动副标题
	 */
	public String subtitle = "";
	/**
	 * 发贴人id
	 */
	public String authorid = "";
	/**
	 * 发布人
	 */
	public String author = "";
	/**
	 * 发布人头像
	 */
	public String avator = "";

	public String avatar = "";
	/**
	 * 所属版块ID
	 */
	public String forum_id = "";
	/**
	 * 所属版块名称
	 */
	public String forum_name = "";
	/**
	 * 发布时间
	 */
	public String time_view = "";
	/**
	 * 浏览量
	 */
	public String view_count = "";
	/**
	 * 回复量
	 */
	public String reply_count = "";
	/**
	 * 是否加火
	 */
	public String is_hot = "0";
	/**
	 * 是否加精
	 */
	public String digest = "0";
	/**
	 * 分类名称
	 */
	public String class_name = "";
	/**
	 * 是否收藏
	 */
	public String is_collect = "0";
	/**
	 *  是否有隐藏的内容
	 */
	public String is_hide = "0";
	//分享内容
	public String share_content = "";
	//分享链接
	public String share_url = "";
	public String tid = "";
	public String start_time = "";
	public String end_time = "";
	public String description = "";

	public String category = "";
	public String url = "";
	//发贴上传参数
	/**
	 * 板块ID
	 */
	public String fid = "";
	/**
	 * 主题分类ID
	 */
	public String typeid = "";
	/**
	 * 楼主的主题数
	 */
	//public String threads = "";
	/**
	 * 标题
	 */
	public String subject = "";
	/**
	 * 内容
	 */
	public String content = "";
	/**
	 * 置顶标志
	 */
	public String displayorder = "";

	public String uid = "";
	public String author_pic = "";
	public String grouptitle = "";
	public String release_time = "";
	public String pid = "";
	/**
	 * 回贴内容
	 */
	public String message = "";
	public CommentPageObject replies = null;

	public String page = "1";
	public String lpp = "20";
	public String keywords = "";

	public String ids = "";

	public String order = "0";

	public boolean isSelected = false;
	
	/**
	* 来源（push/banner）
	*/
	public String source_type = "";
	/**
	* 来源的id
	*/
	public String source_value = "";

	public PhotoPickParameterObject mPhotoPickParameterInfo;

	/**
	 * 快照内容
	 */
	public String snap_content = "";

}