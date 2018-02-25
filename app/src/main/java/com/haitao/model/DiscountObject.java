package com.haitao.model;

import java.util.ArrayList;

/**
 * 优惠折扣
 * Created by tqy on 15/12/2.
 */
public class DiscountObject extends BaseObject {
    /**
     * 优惠id
     */
    public String id = "";
    /**
     * 优惠标题
     */
    public String title = "";
    /**
     * 优惠图片
     */
    public String pic = "";
    /**
     * 价格
     */
    public String price = "";
    /**
     * 原来价格
     */
    public String old_price = "";
    /**
     * 货币符号
     */
    public String symbol = "";
    /**
     * 折扣
     */
    public String discount = "";
    /**
     * 商家名称
     */
    public String store_name = "";
    /**
     * 商家国家图片
     */
    public String country_pic = "";
    /**
     * 发布时间
     */
    public String time_view = "";
    /**
     * 倒计时
     */
    public String surplus_time_view = "";
    /**
     * 赞的数量
     */
    public String praise_count = "";
    /**
     * 评论数量
     */
    public String comment_count = "";
    /**
     * 评论列表
     */
    public ArrayList<CommentObject> comment_list = null;
    /**
     * 收藏数量
     */
    public String collect_count = "";
    /**
     * 温馨提示
     */
    public String warn = "";
    /**
     * 描述
     */
    public String desciption = "";
    /**
     * 购买链接地址
     */
    public String go_url = "";
    /**
     * 折扣码
     */
    public String[] coupons = null;

    public String cashback = "";
    public String cashback_view = "";
    /**
     * 标签
     */
    public ArrayList<TagObject> tags = null;
    /**
     * 分类
     */
    public ArrayList<TagObject> category = null;
    /**
     * 其他优惠
     */
    public ArrayList<DiscountObject> other_deals = null;
    /**
     * 商家
     */
    public StoreObject store = null;
    /**
     * 推荐的deal
     */
    public ArrayList<DiscountObject> recommend_items = null;

    //上传信息
    public String deal_id = "";
    /**
     * 分类ID
     */
    public String cate = "";
    /**
     * 标签ID
     */
    public String tag = "";
    /**
     * 商家ID
     */
    public String sid = "";
    /**
     * 即将过期
     */
    public String expire = "";
    /**
     * 支持转运
     */
    public String transports = "";
    /**
     * 支持支付宝
     */
    public String alipay = "";
    /**
     * 是否单品推荐
     */
    public String is_single = "";
    /**
     * 是否收藏
     */
    public String is_collect = "0";
    /**
     * 是否点赞
     */
    public String is_praise = "0";
    /**
     * 55帮你买,0 不支持，非0 闪淘商品id
     */
    public String st_id = "";

    public String type = "";
    /**
     * 分享内容
     */
    public String share_content = "";
    /**
     * 分享Url
     */
    public String share_url = "";

    public String keywords = "";

    public boolean isSelected = false;
    /**
     * 是否过期
     */
    public String is_expire = "0";
	/**
	* 来源（push/banner/splash_ad/popup_ad/cross_bar）
     * push:推送
     * banner:Banner
     * splash_ad:闪屏广告
     * popup_ad:弹出广告
     * cross_bar:通栏广告
	*/
	public String source_type = "";
	/**
	* 来源的id
	*/
	public String source_value = "";

    /**
     * 快照代码
     */
    public String snap_desciption = "";

}
