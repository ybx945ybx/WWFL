package com.haitao.utils;

/**
 * 页面埋点
 * Created by a55 on 2017/12/13.
 */

public class TraceUtils {
    // 渠道号
    public static final String Chid_Deal_Banner = "DE_B_";   // 优惠banner
    public static final String Chid_Deal_Icon   = "DE_I_";   // 优惠四个icon
    public static final String Chid_Deal_Ads    = "DE_AD_";  // 优惠横栏广告栏
    public static final String Chid_Deal_Hot    = "DE_H_";   // 优惠热门优惠
    public static final String Chid_Deal_Latest = "DE_L_";   // 优惠最新优惠

    public static final String Chid_Discover_Banner             = "DI_B";     // 发现banner
    public static final String Chid_Discover_Store_list         = "DI_SL";    // 发现商家列表
    public static final String Chid_Discover_Direct_China       = "DI_DCSL";  // 发现直邮中国
    public static final String Chid_Discover_Collect_Store_List = "DI_CSL";   // 发现收藏的商家
    public static final String Chid_Discover_Double_Store       = "DI_DS";    // 发现今日加倍返利
    public static final String Chid_Discover_Sale_Store         = "DI_SS";    // 发现限时返利商家

    public static String getFirstTabChidHead(String tabName) {
        return "S-" + tabName + "_";
    }

    public static final String Fid_MW   = "MW";     // 魔窗渠道
    public static final String Fid_Push = "Push";   // 推送渠道

    // event事件类目
    public static final String Event_Category_Click = "c";        // 点击类目
    public static final String Event_Category_Order = "o";        // 购买类目
    public static final String Event_Category_Media = "m";        // 媒介类目

    // event事件动作
    public static final String Event_Action_Click_Goods     = "cg";   // 点击商品
    public static final String Event_Action_Click_Banner    = "cb";   // 点击banner
    public static final String Event_Action_Click_Register  = "cr";   // 点击注册
    public static final String Event_Action_Click_Register3 = "cr3";  // 点击注册（通过第三方账户）

    public static final String Event_Action_Order_Goods_Buy = "ogb";  // 优惠详情页点击去购买

    public static final String Event_Action_Media_Mw   = "mw";        // 魔窗跳转
    public static final String Event_Action_Media_Push = "pu";        // 推送跳转

    // kv参
    public static final String Event_Kv_Jump_url = "kv_url";          // 跳转链接
    public static final String Event_Kv_Value    = "kv_value";        // 魔窗跳转的id
    public static final String Event_Kv_Key      = "kv_key";          // 魔窗跳转的页面key

    public static final String Event_Kv_Type  = "kv_type";            // 推送跳转的类型
    public static final String Event_Kv_Title = "kv_title";           // 推送消息标题
    public static final String Event_Kv_Msg   = "kv_msg";             // 推送消息内容


}
