package com.haitao.common.Constant;

/**
 * api常量
 * Created by tqy on 15/11/18.
 */
public class MethodConstant {
    ///////////////////////////////////////////////////////////////////
    //首页
    //////////////////////////////////////////////////////////////////
    //首页
    public static final String INDEX               = "?m=index&c=home";
    // 弹窗广告
    public static final String POPUP_AD            = "?m=index&c=popup_ad";
    //发现首页
    public static final String DISCOVERY_INDEX     = "?m=find&c=index";
    ///////////////////////////////////////////////////////////////////
    //个人中心
    //////////////////////////////////////////////////////////////////
    //登录
    public static final String LOGIN               = "?m=my&c=login";
    //自动刷新token
    public static final String REFRESH_TOKEN       = "?m=my&c=refresh_token";
    //第三方登录
    public static final String SSO_LOGIN           = "?m=my&c=sso_login";
    //第三方登录绑定
    public static final String BIND_OLD_ACCOUNT    = "?m=my&c=bind_old_account";
    //第三方注册
    public static final String BIND_NEW_ACCOUNT    = "?m=my&c=bind_new_account";
    //发送验证码
    public static final String GET_MOBILE_VERIFY   = "?m=index&c=get_mobile_verify";
    //获取图片验证码
    public static final String GET_IMAGE_VERIFY    = "?m=index&c=get_image_verify";
    //检测手机验证码
    public static final String CHECK_MOBILE_VERIFY = "?m=index&c=check_verify";
    //找回密码（重置密码）
    public static final String LOGIN_PWD_UPDATE    = "?m=my&c=login_pwd_update";
    //找回密码(发邮件)
    public static final String SEND_MAIL_CODE      = "?m=my&c=send_mail_code";
    //注册验证
    public static final String REGISTER_VERIFY     = "?m=my&c=register_verify";
    //注册
    public static final String REGIST              = "?m=my&c=register";
    //绑定手机号码
    public static final String BIND_TEL            = "?m=my&c=bind_tel";
    //修改登录密码
    public static final String UPDATE_LOGIN_PWD    = "?m=my&c=update_loginpwd";
    //修改提现密码
    public static final String UPDATE_CASH_PWD     = "?m=my&c=update_withdrawpwd";
    //用户信息
    public static final String ACCOUNT_INFO        = "?m=my&c=account_info";
    //我的收藏(优惠)
    public static final String CELLECT_LIST        = "?m=my&c=collect_list";
    //我的收藏（商家）
    public static final String CELLECT_STORE       = "?m=cashback&c=my_attention_store";
    //我的收藏(贴子)
    public static final String MY_COLLECT_THREAD   = "?m=my&c=collect_thread";
    //我的收藏（版块）
    public static final String CELLECT_FORUM       = "?m=bbs&c=collection_forum_list";
    //我发布的贴子
    public static final String MY_THREAD           = "?m=my&c=thread";
    //我的回贴
    public static final String MY_REPLY            = "?m=my&c=post";

    //修改用户信息（性别，省，市）
    public static final String UPDATE_USER      = "?m=my&c=save_user";
    //修改用户头像
    public static final String UPDATE_AVATOR    = "?m=my&c=save_user_avator";
    //我的订单
    public static final String ORDER_LIST       = "?m=my&c=order_list";
    //丢单商家
    public static final String ORDER_LOST_STORE = "?m=my&c=order_lost_store";
    //丢单申请
    public static final String ORDER_LOST_APPLY = "?m=my&c=order_lost_apply";
    //订单详情
    public static final String ORDER_VIEW       = "?m=my&c=order_view";
    //意见反馈
    public static final String FEEDBACK         = "?m=my&c=user_feedback";
    //我的金币
    public static final String GOLD_LIST        = "?m=my&c=gold_list";
    //资金流水
    public static final String MONEY_LIST       = "?m=my&c=money_list";
    //提现列表
    public static final String WITHDRAWAL_LIST  = "?m=my&c=withdrawal_list";
    //提现页面初始数据
    public static final String WITHDRAWAL       = "?m=my&c=withdrawal";
    //申请提现
    public static final String WITHDRAWAL_APPLY = "?m=my&c=withdrawal_apply";
    //提现详情
    public static final String WITHDRAWAL_VIEW  = "?m=my&c=withdrawal_view";
    //邀请好友
    public static final String INVITE           = "?m=my&c=invite";
    //获取好友邀请列表
    public static final String INVITE_LIST      = "?m=my&c=invite_list";
    //消息
    public static final String MY_MESSAGE       = "?m=my&c=pm";
    //通知
    public static final String MY_NOTICE        = "?m=my&c=notice";
    //未读通知数
    public static final String MY_UNREAD        = "?m=my&c=unread";
    //新消息
    public static final String NEW_NOTIFIES     = "?m=my&c=get_new_notifies";
    //获取type消息
    public static final String PM_BYTYPE        = "?m=my&c=get_pm_bytype";
    //发送消息
    public static final String SEND_PM          = "?m=my&c=send_pm";

    ///////////////////////////////////////////////////////////////////
    //物流查询
    //////////////////////////////////////////////////////////////////
    //转运公司
    public static final String TRANSFER_COMPANY         = "?m=my&c=transport_company";
    //物流订单
    public static final String LOGISTICS_ORDER          = "?m=my&c=transport_order";
    //查询
    public static final String LOGISTICS_SEARCH         = "?m=my&c=transport_way_bill_search";
    //查询记录
    public static final String LOGISTICS_HIS            = "?m=my&c=transport_way_bill_search_history";
    ///////////////////////////////////////////////////////////////////
    //转运
    //////////////////////////////////////////////////////////////////
    //转运列表
    public static final String TRANSPORT_LIST           = "?m=cashback&c=shipping_list";
    //转运国家
    public static final String TRANSPORT_COUNTRY        = "?m=cashback&c=shipping_country";
    //转运详情
    public static final String TRANSPORT_DETAIL         = "?m=cashback&c=shipping_view";
    //转运收藏
    public static final String TRANSPORT_FAV            = "?m=cashback&c=shipping_collect";
    //取消转运收藏
    public static final String TRANSPORT_FAV_REMOVE     = "?m=cashback&c=un_shipping_collect";
    //转运评价
    public static final String TRANSPORT_COMMENT_LIST   = "?m=cashback&c=shipping_comments";
    //转运评价点赞
    public static final String TRANSPORT_COMMENT_PRAISE = "?m=cashback&c=shipping_praise";
    //添加转运评价
    public static final String TRANSPORT_COMMENT_ADD    = "?m=cashback&c=shippoing_add_comments";

    ///////////////////////////////////////////////////////////////////
    //优惠、商家
    //////////////////////////////////////////////////////////////////
    //优惠首页
    public static final String DEAL_INDEX                    = "?m=cashback&c=index";
    //热门商家
    public static final String STORE_HOT                     = "?m=cashback&c=store_hot";
    //加倍商家
    public static final String STORE_SUPER_REBATE            = "?m=cashback&c=store_super_rebate";
    //商家详情
    public static final String STORE_VIEW                    = "?m=cashback&c=store_view";
    //收藏商家
    public static final String ATTENTION_STORE               = "?m=cashback&c=attention_store";
    //取消收藏商家
    public static final String UN_ATTENTION_STORE            = "?m=cashback&c=un_attention_store";
    //全部商家
    public static final String STORE_LIST                    = "?m=cashback&c=store_list";
    //优惠折扣
    public static final String DEAL_LIST                     = "?m=cashback&c=deal_list";
    //24小时排行
    public static final String HOT_DEALS                     = "?m=index&c=hot_deals";
    //24小时排行的商家和分类字典
    public static final String HOT_DICS                      = "?m=index&c=hot_deals_conditions";
    //优惠详情
    public static final String DEAL_VIEW                     = "?m=cashback&c=deal_view";
    //添加收藏
    public static final String CELLECT_ADD                   = "?m=cashback&c=collect_add";
    //删除收藏
    public static final String CELLECT_DEL                   = "?m=cashback&c=collect_del";
    //添加赞
    public static final String PRAISE_ADD                    = "?m=cashback&c=praise_add";
    //评论列表
    public static final String COMMENT_LIST                  = "?m=cashback&c=comment_list";
    //评论添加
    public static final String COMMENT_ADD                   = "?m=cashback&c=comment_add";
    ///////////////////////////////////////////////////////////////////
    //论坛
    //////////////////////////////////////////////////////////////////
    //论坛首页
    public static final String BBS_INDEX                     = "?m=bbs&c=index";
    //签到列表
    public static final String SIGN_LIST                     = "?m=my&c=sign_list";
    //签到
    public static final String SIGN                          = "?m=my&c=sign_add";
    //自动签到
    public static final String AUTOMATIC_SIGN                = "?m=my&c=automatic_sign";
    //今日话题
    public static final String SIGN_FACEICON                 = "?m=my&c=get_sign_faceicon";
    //话题签到
    public static final String TOPIC_SIGN                    = "?m=my&c=topic_sign";
    //全部版块
    public static final String FORUM                         = "?m=bbs&c=forum";
    //版块详情
    public static final String FORUM_VIEW                    = "?m=bbs&c=forum_view";
    //收藏版块
    public static final String FORUM_FAV_ADD                 = "?m=bbs&c=section_collection";
    //取消收藏版块
    public static final String FORUM_FAV_REMOVE              = "?m=bbs&c=un_section_collection";
    //主题列表
    public static final String THREAD_LIST                   = "?m=bbs&c=thread_list";
    //贴子详情
    public static final String THREAD_VIEW                   = "?m=bbs&c=get_thread";
    //贴子列表
    public static final String POST_REPLIES                  = "?m=bbs&c=post_replies";
    //达人列表
    public static final String TALENT                        = "?m=bbs&c=talent";
    //达人详情
    public static final String TALENT_VIEW                   = "?m=bbs&c=space_view";
    //活动列表
    public static final String SHIPPING_ACTIVITY             = "?m=bbs&c=shipping_activity";
    //获取所有主题分类
    public static final String THREAD_CATEGORY               = "?m=bbs&c=thread_all_class";
    //获取所有发贴主题分类
    public static final String THREAD_CATEGORY_BY_UID        = "?m=bbs&c=thread_all_class_by_uid";
    //发布贴子
    public static final String THREAD_ADD                    = "?m=bbs&c=thread_add";
    //贴子添加收藏
    public static final String THREAD_FAV                    = "?m=bbs&c=favorite_thread";
    //贴子取消收藏
    public static final String DEL_THREAD_FAV                = "?m=my&c=del_thread_favorite";
    //回贴
    public static final String THREAD_POST                   = "?m=bbs&c=thread_post";
    //免费试用
    public static final String SAMPLE_LIST                   = "?m=bbs&c=freetrial_list";
    //试用详情
    public static final String SAMPLE_DETAIL                 = "?m=bbs&c=freetrial_view";
    //试用名单
    public static final String SAMPLE_MEMBER                 = "?m=bbs&c=freetrial_apply";
    //试用报告
    public static final String SAMPLE_REPORT                 = "?m=bbs&c=freetrial_report";
    //精选报告
    public static final String SAMPLE_SELECTED_REPORT        = "?m=find&c=free_trial";
    //提交申请
    public static final String SAMPLE_APPLY                  = "?m=bbs&c=freetrial_save";
    //我的试用-申请中
    public static final String MY_SAMPLE_APPLICATION_IN      = "?m=bbs&c=my_freetrial";
    //我的试用-申请成功
    public static final String MY_SAMPLE_APPLICATION_SUCCESS = "?m=bbs&c=my_freetrial_success";
    //我的试用-申请失败
    public static final String MY_SAMPLE_APPLICATION_FAIL    = "?m=bbs&c=my_freetrial_fail";
    //我的试用-试用报告
    public static final String MY_SAMPLE_REPORT              = "?m=bbs&c=my_freetrial_report";
    //金币兑换
    public static final String EXCHANGE_LIST                 = "?m=find&c=auction_list";
    //兑换详情
    public static final String EXCHANGE_DETAIL               = "?m=find&c=auction_view";
    //兑换申请
    public static final String EXCHANGE_APPLY                = "?m=find&c=auction_apply";

    ///////////////////////////////////////////////////////////////////
    //搜索
    //////////////////////////////////////////////////////////////////
    //搜索商家
    public static final String SEARCH_STORE     = "?m=search&c=store";
    //搜索优惠
    public static final String SEARCH_DEAL      = "?m=search&c=deal";
    //搜索贴子
    public static final String SEARCH_POST      = "?m=search&c=post";
    //搜索引导页
    public static final String SEARCH_INDEX     = "?m=search&c=search_index";
    //转运搜索
    public static final String SEARCH_TRANSPORT = "?m=search&c=transports";
    ///////////////////////////////////////////////////////////////////
    //关于
    //////////////////////////////////////////////////////////////////
    //使用帮助
    public static final String HELP             = "?m=index&c=help";

    ///////////////////////////////////////////////////////////////////
    //缓存数据
    //////////////////////////////////////////////////////////////////
    //省市
    public static final String AREA          = "?m=my&c=area";
    //各种分类
    public static final String CATEGORY_LIST = "?m=index&c=data_dict";
    //各版本号
    public static final String VERSION       = "?m=index&c=version";
    //闪屏广告
    public static final String SPLASH_AD     = "?m=index&c=screen_ad";

    ///////////////////////////////////////////////////////////////////
    //数据统计
    //////////////////////////////////////////////////////////////////
    //分享数据统计
    public static final String SHARE_LOG = "?m=my&c=share_log";

}
