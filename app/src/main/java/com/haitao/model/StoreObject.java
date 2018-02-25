package com.haitao.model;

import java.util.ArrayList;

/**
 * 商家
 * Created by tqy on 15/12/2.
 */
public class StoreObject extends BaseObject {
    private static final long   serialVersionUID  = 1L;
    /**
     * 商家ID
     */
    public               String id                = "";
    /**
     * 商家名称
     */
    public               String name              = "";
    /**
     * 商家图片
     */
    public               String pic               = "";
    /**
     * 返利
     */
    public               String cashback          = "";
    /**
     * 返利显示的值
     */
    public               String cashback_view     = "";
    /**
     * 返利说明
     */
    public               String cashback_desc     = "";
    /**
     * 曾返
     */
    public               String old_cashback_view = "";
    /**
     * 推荐语
     */
    public               String one_word          = "";
    /**
     * 国旗
     */
    public               String country_pic       = "";
    /**
     * 国家
     */
    public               String country_id        = "";
    /**
     * 国家名称
     */
    public               String country_name      = "";
    /**
     * 是否支持支付宝
     */
    public               String is_alipay         = "";
    /**
     * 是否支持转运
     */
    public               String is_transports     = "";
    /**
     * 是否支持直邮
     */
    public               String is_direct_mail    = "";
    /**
     * 是否大陆地区受限
     */
    public               String bounded_accessing = "";
    /**
     * 是否支持手机购买
     */
    public               String is_mobile_buy     = "";
    /**
     * 是否加倍返利
     */
    public               String is_super_rebate   = "";
    /**
     * 是否支持paypal
     */
    public               String is_paypal         = "";
    /**
     * 成功下单数
     */
    public               String buy_order_count   = "";
    /**
     * 收藏数
     */
    public               String collect_count     = "";
    /**
     * 购买跳转页
     */
    public               String go_url            = "";
    /**
     * 跳转页的返利说明
     */
    public               String cashback_desc_go  = "";
    /**
     * 关注量
     */
    public               String attention_count   = "0";
    public               String is_attention      = "0";
    /**
     * 0 下架 ；1 上架
     */
    public               String is_disabled       = "0";

    public String               is_credit   = "";
    public String               description = "";
    public DiscountPageObject   deals       = null;
    /**
     * 分类
     */
    public ArrayList<TagObject> category    = null;
    /**
     * 首字母
     */
    public String               character   = "";

    //上传参数
    public String store_id = "";

    public String keywords = "";

    public boolean isSelected = false;

    /**
     * 来源（push/banner）
     */
    public String source_type  = "";
    /**
     * 来源的id
     */
    public String source_value = "";
    /**
     * 丢单反馈是否需要邮箱，1需要，0不需要
     */
    public String is_use_email = "";

    /**
     * 商家信息页url
     */
    public String store_info_url = "";

    /**
     * 商家是否有活动
     */
    public String in_activity = "0";
}