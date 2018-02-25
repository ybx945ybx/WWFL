package com.haitao.imp;

import com.haitao.common.Constant.MethodConstant;
import com.haitao.framework.uri.BaseURIContext;
import com.haitao.framework.uri.IURILocator;
import com.haitao.framework.uri.URILocatorBase;
import com.haitao.model.AdObject;
import com.haitao.model.BBSHomeObject;
import com.haitao.model.CategoryAllObject;
import com.haitao.model.CategoryListObject;
import com.haitao.model.CommentObject;
import com.haitao.model.DiscountHomeObject;
import com.haitao.model.DiscountObject;
import com.haitao.model.DiscoveryHomeObject;
import com.haitao.model.ExchangeApplyObject;
import com.haitao.model.ExchangeObject;
import com.haitao.model.GoldRecordObject;
import com.haitao.model.HelpObject;
import com.haitao.model.HomeObject;
import com.haitao.model.InviteObject;
import com.haitao.model.InviteRewardObject;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.LogisticsObject;
import com.haitao.model.MobileVerifyObject;
import com.haitao.model.OrderObject;
import com.haitao.model.PostObject;
import com.haitao.model.ProvinceObject;
import com.haitao.model.RebateObject;
import com.haitao.model.SampleApplyObject;
import com.haitao.model.SampleObject;
import com.haitao.model.SampleReportObject;
import com.haitao.model.SearchIndexObject;
import com.haitao.model.SectionObject;
import com.haitao.model.ShareObject;
import com.haitao.model.SignListObject;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.StoreObject;
import com.haitao.model.TagObject;
import com.haitao.model.TalentObject;
import com.haitao.model.ThreadCategoryObject;
import com.haitao.model.TransportCommentItemObject;
import com.haitao.model.TransportCommentObject;
import com.haitao.model.TransportFilterObject;
import com.haitao.model.UserObject;
import com.haitao.model.VersionObject;
import com.haitao.model.WithdrawObject;
import com.haitao.model.NoticeObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tqy on 15/11/18.
 */
public class URILocatorHelper {

    private static BaseURIContext          URL_BASE = new BaseURIContext(null);
    private static Map<Class, IURILocator> uris     = new HashMap<Class, IURILocator>();

    static {
        //        init();
    }

    public static BaseURIContext getUrlBase() {
        return URL_BASE;
    }

    public static IURILocator getIURILocator(Class entityClass) {
        return uris.get(entityClass);
    }

    public static void initUrlBase(String url) {
        URL_BASE.setBaseURI(url);
    }

    public static void init() {
        initURI();
    }

    private static void initURI() {
        ///////////////////////////////////////////////////////////////////
        //首页
        //////////////////////////////////////////////////////////////////
        uris.put(HomeObject.class, new URILocatorBase(URL_BASE).add(MethodConstant.INDEX, MethodConstant.INDEX));
        ///////////////////////////////////////////////////////////////////
        //个人中心
        //////////////////////////////////////////////////////////////////
        //登录
        uris.put(UserObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.LOGIN, MethodConstant.LOGIN)//登录
                .add(MethodConstant.SSO_LOGIN, MethodConstant.SSO_LOGIN)//第三方登录
                .add(MethodConstant.BIND_OLD_ACCOUNT, MethodConstant.BIND_OLD_ACCOUNT)//第三方登录绑定
                .add(MethodConstant.REGISTER_VERIFY, MethodConstant.REGISTER_VERIFY)//注册验证
                .add(MethodConstant.REGIST, MethodConstant.REGIST)//注册
                .add(MethodConstant.BIND_NEW_ACCOUNT, MethodConstant.BIND_NEW_ACCOUNT)//第三方注册
                .add(MethodConstant.SEND_MAIL_CODE, MethodConstant.SEND_MAIL_CODE)//找回密码发送邮件
                .add(MethodConstant.LOGIN_PWD_UPDATE, MethodConstant.LOGIN_PWD_UPDATE)//找回密码重置密码
                .add(MethodConstant.UPDATE_LOGIN_PWD, MethodConstant.UPDATE_LOGIN_PWD)//修改登录密码
                .add(MethodConstant.UPDATE_CASH_PWD, MethodConstant.UPDATE_CASH_PWD)//修改提现密码
                .add(MethodConstant.UPDATE_USER, MethodConstant.UPDATE_USER)//修改用户信息
                .add(MethodConstant.UPDATE_AVATOR, MethodConstant.UPDATE_AVATOR)//修改用户头像
                .add(MethodConstant.ACCOUNT_INFO, MethodConstant.ACCOUNT_INFO)//获取用户信息
                .add(MethodConstant.REFRESH_TOKEN, MethodConstant.REFRESH_TOKEN)//刷新token
        );
        //发送验证码
        uris.put(MobileVerifyObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.GET_MOBILE_VERIFY, MethodConstant.GET_MOBILE_VERIFY)
                .add(MethodConstant.CHECK_MOBILE_VERIFY, MethodConstant.CHECK_MOBILE_VERIFY)
                .add(MethodConstant.GET_IMAGE_VERIFY, MethodConstant.GET_IMAGE_VERIFY)
                .add(MethodConstant.BIND_TEL, MethodConstant.BIND_TEL));

        uris.put(OrderObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.ORDER_LIST, MethodConstant.ORDER_LIST)//我的订单
                .add(MethodConstant.ORDER_LOST_APPLY, MethodConstant.ORDER_LOST_APPLY)//丢单申请
                .add(MethodConstant.ORDER_VIEW, MethodConstant.ORDER_VIEW)//订单详情
                .add(MethodConstant.LOGISTICS_ORDER, MethodConstant.LOGISTICS_ORDER)//物流订单
        );
        uris.put(GoldRecordObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.GOLD_LIST, MethodConstant.GOLD_LIST)//我的金币
        );
        uris.put(RebateObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.MONEY_LIST, MethodConstant.MONEY_LIST)//资金列表
        );
        uris.put(WithdrawObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.WITHDRAWAL_LIST, MethodConstant.WITHDRAWAL_LIST)//提现列表
                .add(MethodConstant.WITHDRAWAL, MethodConstant.WITHDRAWAL)//提现页面
                .add(MethodConstant.WITHDRAWAL_APPLY, MethodConstant.WITHDRAWAL_APPLY)//提现页面
                .add(MethodConstant.WITHDRAWAL_VIEW, MethodConstant.WITHDRAWAL_VIEW)//提现详情
        );
        uris.put(InviteObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.INVITE, MethodConstant.INVITE)//获取邀请信息
        );
        uris.put(InviteRewardObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.INVITE_LIST, MethodConstant.INVITE_LIST)//获取邀请奖励列表
        );
        uris.put(NoticeObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.MY_MESSAGE, MethodConstant.MY_MESSAGE)//消息
                .add(MethodConstant.MY_NOTICE, MethodConstant.MY_NOTICE)//通知
                .add(MethodConstant.MY_UNREAD, MethodConstant.MY_UNREAD)//未读消息通知
                .add(MethodConstant.NEW_NOTIFIES, MethodConstant.NEW_NOTIFIES)//新消息
                .add(MethodConstant.PM_BYTYPE, MethodConstant.PM_BYTYPE)//跟类型获取消息
                .add(MethodConstant.SEND_PM, MethodConstant.SEND_PM)//发送消息
        );
        //转运查询
        uris.put(LogisticsCompanyObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TRANSFER_COMPANY, MethodConstant.TRANSFER_COMPANY)//转运公司
                .add(MethodConstant.TRANSPORT_DETAIL, MethodConstant.TRANSPORT_DETAIL)//转运详情
                .add(MethodConstant.TRANSPORT_FAV, MethodConstant.TRANSPORT_FAV)//转运收藏
                .add(MethodConstant.TRANSPORT_FAV_REMOVE, MethodConstant.TRANSPORT_FAV_REMOVE)//取消转运收藏
                .add(MethodConstant.SEARCH_TRANSPORT, MethodConstant.SEARCH_TRANSPORT)//搜索转运
        );
        uris.put(LogisticsObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.LOGISTICS_SEARCH, MethodConstant.LOGISTICS_SEARCH)//转运查询
                .add(MethodConstant.LOGISTICS_HIS, MethodConstant.LOGISTICS_HIS)//查询历史
        );

        uris.put(TransportFilterObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TRANSPORT_LIST, MethodConstant.TRANSPORT_LIST)//全部转运
        );
        uris.put(TagObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TRANSPORT_COUNTRY, MethodConstant.TRANSPORT_COUNTRY)//转运国家
        );

        uris.put(TransportCommentItemObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TRANSPORT_COMMENT_LIST, MethodConstant.TRANSPORT_COMMENT_LIST)//转运评价列表
                .add(MethodConstant.TRANSPORT_COMMENT_PRAISE, MethodConstant.TRANSPORT_COMMENT_PRAISE)//转运评价点赞
        );
        uris.put(TransportCommentObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TRANSPORT_COMMENT_ADD, MethodConstant.TRANSPORT_COMMENT_ADD)//添加转运评价
        );
        ///////////////////////////////////////////////////////////////////
        //优惠、商家
        //////////////////////////////////////////////////////////////////
        uris.put(DiscountHomeObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.DEAL_INDEX, MethodConstant.DEAL_INDEX)//优惠首页
        );
        uris.put(StoreObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.STORE_HOT, MethodConstant.STORE_HOT)//热门商家
                .add(MethodConstant.STORE_SUPER_REBATE, MethodConstant.STORE_SUPER_REBATE)//加倍返利商家
                .add(MethodConstant.STORE_VIEW, MethodConstant.STORE_VIEW)//商家详情
                .add(MethodConstant.ORDER_LOST_STORE, MethodConstant.ORDER_LOST_STORE)//丢单商家
                .add(MethodConstant.SEARCH_STORE, MethodConstant.SEARCH_STORE)//搜索商家
                .add(MethodConstant.ATTENTION_STORE, MethodConstant.ATTENTION_STORE)//收藏商家
                .add(MethodConstant.UN_ATTENTION_STORE, MethodConstant.UN_ATTENTION_STORE)//取消收藏
                .add(MethodConstant.CELLECT_STORE, MethodConstant.CELLECT_STORE)//我的收藏

        );
        uris.put(StoreFilterObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.STORE_LIST, MethodConstant.STORE_LIST)//全部商家
        );
        uris.put(DiscountObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.DEAL_LIST, MethodConstant.DEAL_LIST)//优惠列表
                .add(MethodConstant.HOT_DEALS, MethodConstant.HOT_DEALS)//24小时人气排行
                .add(MethodConstant.DEAL_VIEW, MethodConstant.DEAL_VIEW)//优惠详情
                .add(MethodConstant.CELLECT_LIST, MethodConstant.CELLECT_LIST)//我的收藏
                .add(MethodConstant.CELLECT_ADD, MethodConstant.CELLECT_ADD)//添加收藏
                .add(MethodConstant.CELLECT_DEL, MethodConstant.CELLECT_DEL)//删除收藏
                .add(MethodConstant.PRAISE_ADD, MethodConstant.PRAISE_ADD)//添加赞
                .add(MethodConstant.SEARCH_DEAL, MethodConstant.SEARCH_DEAL)//搜索优惠
        );

        uris.put(CommentObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.COMMENT_LIST, MethodConstant.COMMENT_LIST)//评论列表
                .add(MethodConstant.COMMENT_ADD, MethodConstant.COMMENT_ADD)//评论添加
                .add(MethodConstant.FEEDBACK, MethodConstant.FEEDBACK)//意见反馈
                .add(MethodConstant.POST_REPLIES, MethodConstant.POST_REPLIES)//贴子回复
                .add(MethodConstant.MY_REPLY, MethodConstant.MY_REPLY)//我的回贴
        );
        ///////////////////////////////////////////////////////////////////
        //发现
        //////////////////////////////////////////////////////////////////
        uris.put(DiscoveryHomeObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.DISCOVERY_INDEX, MethodConstant.DISCOVERY_INDEX)//发现首页
        );

        ///////////////////////////////////////////////////////////////////
        //搜索
        //////////////////////////////////////////////////////////////////
        uris.put(SearchIndexObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SEARCH_INDEX, MethodConstant.SEARCH_INDEX)//搜索引导页
        );

        ///////////////////////////////////////////////////////////////////
        //论坛
        //////////////////////////////////////////////////////////////////
        uris.put(SignListObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SIGN_LIST, MethodConstant.SIGN_LIST)//签到列表
                .add(MethodConstant.SIGN, MethodConstant.SIGN) //签到
                .add(MethodConstant.AUTOMATIC_SIGN, MethodConstant.AUTOMATIC_SIGN) //签到
                .add(MethodConstant.SIGN_FACEICON, MethodConstant.SIGN_FACEICON) //今日话题
                .add(MethodConstant.TOPIC_SIGN, MethodConstant.TOPIC_SIGN) //今日话题
        );
        uris.put(BBSHomeObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.BBS_INDEX, MethodConstant.BBS_INDEX)//论坛首页
        );
        uris.put(SectionObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.FORUM, MethodConstant.FORUM)//全部版块
                .add(MethodConstant.FORUM_VIEW, MethodConstant.FORUM_VIEW)//版块详情
                .add(MethodConstant.FORUM_FAV_ADD, MethodConstant.FORUM_FAV_ADD)//添加收藏版块
                .add(MethodConstant.FORUM_FAV_REMOVE, MethodConstant.FORUM_FAV_REMOVE)//取消收藏版块
                .add(MethodConstant.CELLECT_FORUM, MethodConstant.CELLECT_FORUM)//收藏的版块
        );
        uris.put(ThreadCategoryObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.THREAD_CATEGORY, MethodConstant.THREAD_CATEGORY)//主题分类
                .add(MethodConstant.THREAD_CATEGORY_BY_UID, MethodConstant.THREAD_CATEGORY_BY_UID)//根据用户id来获取对应的主题分类
        );
        uris.put(TalentObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.TALENT, MethodConstant.TALENT)//达人列表
                .add(MethodConstant.TALENT_VIEW, MethodConstant.TALENT_VIEW)//达人详情
        );

        uris.put(PostObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SHIPPING_ACTIVITY, MethodConstant.SHIPPING_ACTIVITY)//活动列表
                .add(MethodConstant.THREAD_ADD, MethodConstant.THREAD_ADD)//发布贴子
                .add(MethodConstant.MY_THREAD, MethodConstant.MY_THREAD)//我发布的贴子
                .add(MethodConstant.MY_COLLECT_THREAD, MethodConstant.MY_COLLECT_THREAD)//我收藏的贴子
                .add(MethodConstant.THREAD_LIST, MethodConstant.THREAD_LIST)//主题列表
                .add(MethodConstant.THREAD_VIEW, MethodConstant.THREAD_VIEW)//贴子详情
                .add(MethodConstant.THREAD_FAV, MethodConstant.THREAD_FAV)//贴子收藏
                .add(MethodConstant.DEL_THREAD_FAV, MethodConstant.DEL_THREAD_FAV)//删除贴子收藏
                .add(MethodConstant.THREAD_POST, MethodConstant.THREAD_POST)//回贴
                .add(MethodConstant.SEARCH_POST, MethodConstant.SEARCH_POST)//搜索贴子
        );

        uris.put(SampleObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SAMPLE_LIST, MethodConstant.SAMPLE_LIST)//免费试用
                .add(MethodConstant.SAMPLE_DETAIL, MethodConstant.SAMPLE_DETAIL)//试用详情
                .add(MethodConstant.MY_SAMPLE_APPLICATION_IN, MethodConstant.MY_SAMPLE_APPLICATION_IN)//申请中
                .add(MethodConstant.MY_SAMPLE_APPLICATION_SUCCESS, MethodConstant.MY_SAMPLE_APPLICATION_SUCCESS)//申请成功
                .add(MethodConstant.MY_SAMPLE_APPLICATION_FAIL, MethodConstant.MY_SAMPLE_APPLICATION_FAIL)//申请失败
        );

        uris.put(SampleApplyObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SAMPLE_MEMBER, MethodConstant.SAMPLE_MEMBER)//试用申请名单
                .add(MethodConstant.SAMPLE_APPLY, MethodConstant.SAMPLE_APPLY)//申请试用
        );

        uris.put(SampleReportObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SAMPLE_REPORT, MethodConstant.SAMPLE_REPORT)//试用报告
                .add(MethodConstant.MY_SAMPLE_REPORT, MethodConstant.MY_SAMPLE_REPORT)//我的试用报告
                .add(MethodConstant.SAMPLE_SELECTED_REPORT, MethodConstant.SAMPLE_SELECTED_REPORT)//精选报告
        );
        //金币兑换
        uris.put(ExchangeObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.EXCHANGE_LIST, MethodConstant.EXCHANGE_LIST)//金币兑换首页
                .add(MethodConstant.EXCHANGE_DETAIL, MethodConstant.EXCHANGE_DETAIL)//金币兑换详情
        );
        uris.put(ExchangeApplyObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.EXCHANGE_APPLY, MethodConstant.EXCHANGE_APPLY)//金币兑换申请
        );

        ///////////////////////////////////////////////////////////////////
        //关于
        //////////////////////////////////////////////////////////////////
        //使用帮助
        uris.put(HelpObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.HELP, MethodConstant.HELP));
        ///////////////////////////////////////////////////////////////////
        //缓存
        //////////////////////////////////////////////////////////////////
        //获取省市
        uris.put(ProvinceObject.class, new URILocatorBase(URL_BASE).add(MethodConstant.AREA, MethodConstant.AREA));
        //各种分类
        uris.put(CategoryAllObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.CATEGORY_LIST, MethodConstant.CATEGORY_LIST));
        uris.put(CategoryListObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.HOT_DICS, MethodConstant.HOT_DICS));
        //各版本号
        uris.put(VersionObject.class, new URILocatorBase(URL_BASE).add(MethodConstant.VERSION, MethodConstant.VERSION));
        ///////////////////////////////////////////////////////////////////
        //闪屏广告
        uris.put(AdObject.class, new URILocatorBase(URL_BASE)
                .add(MethodConstant.SPLASH_AD, MethodConstant.SPLASH_AD)
                .add(MethodConstant.POPUP_AD, MethodConstant.POPUP_AD));
        //数据统计
        //////////////////////////////////////////////////////////////////
        uris.put(ShareObject.class, new URILocatorBase(URL_BASE).add(MethodConstant.SHARE_LOG, MethodConstant.SHARE_LOG));
    }
}
