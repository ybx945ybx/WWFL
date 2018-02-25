package com.haitao.imp;


import com.haitao.common.Constant.MethodConstant;
import com.haitao.framework.codec.ParameterConvertor;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.ServiceDefineBase;
import com.haitao.framework.service.ServiceFactory;
import com.haitao.model.AdObject;
import com.haitao.model.CommentObject;
import com.haitao.model.DiscountObject;
import com.haitao.model.ExchangeApplyObject;
import com.haitao.model.ExchangeObject;
import com.haitao.model.HomeObject;
import com.haitao.model.InviteObject;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.LogisticsObject;
import com.haitao.model.MobileVerifyObject;
import com.haitao.model.NoticeObject;
import com.haitao.model.OrderObject;
import com.haitao.model.PostObject;
import com.haitao.model.ProvinceObject;
import com.haitao.model.RebateObject;
import com.haitao.model.SampleApplyObject;
import com.haitao.model.SampleObject;
import com.haitao.model.SampleReportObject;
import com.haitao.model.SectionObject;
import com.haitao.model.ShareObject;
import com.haitao.model.SignListObject;
import com.haitao.model.StoreObject;
import com.haitao.model.TalentObject;
import com.haitao.model.TransportCommentItemObject;
import com.haitao.model.TransportCommentObject;
import com.haitao.model.UserObject;
import com.haitao.model.WithdrawObject;

public class StartUP {

    public static void StartUp() {
        ///////////////////////////////////////////////////////////////////
        //个人中心
        //////////////////////////////////////////////////////////////////
        //登录
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.LOGIN)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"account", "password", "code"})));
        //刷新token
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.REFRESH_TOKEN)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"refresh_token"})));
        //第三方登录
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.SSO_LOGIN)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"type", "open_id", "open_token", "open_unionid"})));
        //第三方登录绑定
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.BIND_OLD_ACCOUNT)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"account", "password", "type", "open_id", "open_token", "open_unionid"})));
        //获取手机验证码
        ServiceFactory.addServiceDefine(new ServiceDefineBase<MobileVerifyObject, IEntityService<MobileVerifyObject>>(MobileVerifyObject.class, MethodConstant.GET_MOBILE_VERIFY)
                .setRequestConverter(new ParameterConvertor<MobileVerifyObject>(new String[]{"mobile", "type"})));
        //检测手机验证码
        ServiceFactory.addServiceDefine(new ServiceDefineBase<MobileVerifyObject, IEntityService<MobileVerifyObject>>(MobileVerifyObject.class, MethodConstant.CHECK_MOBILE_VERIFY)
                .setRequestConverter(new ParameterConvertor<MobileVerifyObject>(new String[]{"mobile", "type", "code"})));
        //找回密码（重置密码）
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.LOGIN_PWD_UPDATE)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"mobile", "password", "code"})));
        //绑定手机号
        ServiceFactory.addServiceDefine(new ServiceDefineBase<MobileVerifyObject, IEntityService<MobileVerifyObject>>(MobileVerifyObject.class, MethodConstant.BIND_TEL)
                .setRequestConverter(new ParameterConvertor<MobileVerifyObject>(new String[]{"mobile", "code", "token", "is_rebind", "updateBind", "phoneCode"})));
        //通过邮箱找回密码
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.SEND_MAIL_CODE)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"email"})));
        //注册验证
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.REGISTER_VERIFY)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"username", "mobile", "password", "invite"})));
        //注册
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.REGIST)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"username", "mobile", "password", "invite", "code", "checked"})));
        //第三方注册
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.BIND_NEW_ACCOUNT)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"username", "mobile", "password", "invite", "code", "type", "open_id", "open_token", "open_unionid", "checked"})));
        //修改登录密码
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.UPDATE_LOGIN_PWD)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"old_password", "new_password", "confirmpwd"})));
        //修改提现密码
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.UPDATE_CASH_PWD)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"password", "mobile", "code", "confirmpwd"})));
        //修改个人资料
        ServiceFactory.addServiceDefine(new ServiceDefineBase<UserObject, IEntityService<UserObject>>(UserObject.class, MethodConstant.UPDATE_USER)
                .setRequestConverter(new ParameterConvertor<UserObject>(new String[]{"gender", "province", "city"})));
        //消息类型
        ServiceFactory.addServiceDefine(new ServiceDefineBase<NoticeObject, IEntityService<NoticeObject>>(NoticeObject.class, MethodConstant.PM_BYTYPE)
                .setRequestConverter(new ParameterConvertor<NoticeObject>(new String[]{"type", "type_id"})));
        //我的订单
        ServiceFactory.addServiceDefine(new ServiceDefineBase<OrderObject, IEntityService<OrderObject>>(OrderObject.class, MethodConstant.ORDER_LIST)
                .setRequestConverter(new ParameterConvertor<OrderObject>(new String[]{"status"})));
        //丢单申请
        ServiceFactory.addServiceDefine(new ServiceDefineBase<OrderObject, IEntityService<OrderObject>>(OrderObject.class, MethodConstant.ORDER_LOST_APPLY)
                .setRequestConverter(new ParameterConvertor<OrderObject>(new String[]{"store_id", "time", "order", "price", "email"})));
        //订单详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<OrderObject, IEntityService<OrderObject>>(OrderObject.class, MethodConstant.ORDER_VIEW)
                .setRequestConverter(new ParameterConvertor<OrderObject>(new String[]{"id"})));
        //资金列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<RebateObject, IEntityService<RebateObject>>(RebateObject.class, MethodConstant.MONEY_LIST)
                .setRequestConverter(new ParameterConvertor<RebateObject>(new String[]{"type", "status"})));
        //提现列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<WithdrawObject, IEntityService<WithdrawObject>>(WithdrawObject.class, MethodConstant.WITHDRAWAL_LIST)
                .setRequestConverter(new ParameterConvertor<WithdrawObject>(new String[]{"status"})));
        //提现申请
        ServiceFactory.addServiceDefine(new ServiceDefineBase<WithdrawObject, IEntityService<WithdrawObject>>(WithdrawObject.class, MethodConstant.WITHDRAWAL_APPLY)
                .setRequestConverter(new ParameterConvertor<WithdrawObject>(new String[]{"money", "account_type", "account", "account_confirm", "account_date_m", "account_date_y", "account_name", "account_bank", "account_branch", "code", "password"})));
        //提现详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<WithdrawObject, IEntityService<WithdrawObject>>(WithdrawObject.class, MethodConstant.WITHDRAWAL_VIEW)
                .setRequestConverter(new ParameterConvertor<WithdrawObject>(new String[]{"id"})));
        //意见反馈
        ServiceFactory.addServiceDefine(new ServiceDefineBase<CommentObject, IEntityService<CommentObject>>(CommentObject.class, MethodConstant.FEEDBACK)
                .setRequestConverter(new ParameterConvertor<CommentObject>(new String[]{"content"})));

        //邀请好友
        ServiceFactory.addServiceDefine(new ServiceDefineBase<InviteObject, IEntityService<InviteObject>>(InviteObject.class, MethodConstant.INVITE)
                .setRequestConverter(new ParameterConvertor<InviteObject>(new String[]{"page", "lpp"})));
        ///////////////////////////////////////////////////////////////////
        //转运
        //////////////////////////////////////////////////////////////////
        //转运查询
        ServiceFactory.addServiceDefine(new ServiceDefineBase<LogisticsObject, IEntityService<LogisticsObject>>(LogisticsObject.class, MethodConstant.LOGISTICS_SEARCH)
                .setRequestConverter(new ParameterConvertor<LogisticsObject>(new String[]{"tid", "tracking_no", "order_id"})));
        //转运详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>>(LogisticsCompanyObject.class, MethodConstant.TRANSPORT_DETAIL)
                .setRequestConverter(new ParameterConvertor<LogisticsCompanyObject>(new String[]{"id"})));
        //转运收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>>(LogisticsCompanyObject.class, MethodConstant.TRANSPORT_FAV)
                .setRequestConverter(new ParameterConvertor<LogisticsCompanyObject>(new String[]{"id"})));
        //取消转运收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>>(LogisticsCompanyObject.class, MethodConstant.TRANSPORT_FAV_REMOVE)
                .setRequestConverter(new ParameterConvertor<LogisticsCompanyObject>(new String[]{"id"})));
        //转运评价列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<TransportCommentItemObject, IEntityService<TransportCommentItemObject>>(TransportCommentItemObject.class, MethodConstant.TRANSPORT_COMMENT_LIST)
                .setRequestConverter(new ParameterConvertor<TransportCommentItemObject>(new String[]{"id", "page", "lpp"})));
        //转运评价点赞
        ServiceFactory.addServiceDefine(new ServiceDefineBase<TransportCommentItemObject, IEntityService<TransportCommentItemObject>>(TransportCommentItemObject.class, MethodConstant.TRANSPORT_COMMENT_PRAISE)
                .setRequestConverter(new ParameterConvertor<TransportCommentItemObject>(new String[]{"id"})));
        //添加转运评价
        ServiceFactory.addServiceDefine(new ServiceDefineBase<TransportCommentObject, IEntityService<TransportCommentObject>>(TransportCommentObject.class, MethodConstant.TRANSPORT_COMMENT_ADD)
                .setRequestConverter(new ParameterConvertor<TransportCommentObject>(new String[]{"id", "contents", "score", "orders"})));
        ///////////////////////////////////////////////////////////////////
        //优惠、商家
        //////////////////////////////////////////////////////////////////
        //首页
        ServiceFactory.addServiceDefine(new ServiceDefineBase<HomeObject, IEntityService<HomeObject>>(HomeObject.class, MethodConstant.INDEX)
                .setRequestConverter(new ParameterConvertor<HomeObject>(new String[]{"lpp"})));
        // 弹窗广告
        ServiceFactory.addServiceDefine(new ServiceDefineBase<AdObject, IEntityService<AdObject>>(AdObject.class, MethodConstant.POPUP_AD)
                .setRequestConverter(new ParameterConvertor<AdObject>(new String[]{})));
        //商家
        ServiceFactory.addServiceDefine(new ServiceDefineBase<StoreObject, IEntityService<StoreObject>>(StoreObject.class, MethodConstant.STORE_VIEW)
                .setRequestConverter(new ParameterConvertor<StoreObject>(new String[]{"store_id", "source_type", "source_value"})));
        //收藏商家
        ServiceFactory.addServiceDefine(new ServiceDefineBase<StoreObject, IEntityService<StoreObject>>(StoreObject.class, MethodConstant.ATTENTION_STORE)
                .setRequestConverter(new ParameterConvertor<StoreObject>(new String[]{"store_id"})));
        //取消商家收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<StoreObject, IEntityService<StoreObject>>(StoreObject.class, MethodConstant.UN_ATTENTION_STORE)
                .setRequestConverter(new ParameterConvertor<StoreObject>(new String[]{"store_id"})));
        //优惠列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.DEAL_LIST)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"cate", "tag", "sid", "expire", "transports", "alipay", "is_single"})));
        //24小时人气排行
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.HOT_DEALS)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"cate", "tag", "sid", "expire", "transports", "alipay"})));
        //优惠详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.DEAL_VIEW)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"deal_id", "source_type", "source_value"})));
        //添加收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.CELLECT_ADD)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"id"})));
        //删除收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.CELLECT_DEL)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"id"})));
        //添加赞
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.PRAISE_ADD)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"id", "type"})));
        //评论列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<CommentObject, IEntityService<CommentObject>>(CommentObject.class, MethodConstant.COMMENT_LIST)
                .setRequestConverter(new ParameterConvertor<CommentObject>(new String[]{"id", "type"})));
        //评论添加
        ServiceFactory.addServiceDefine(new ServiceDefineBase<CommentObject, IEntityService<CommentObject>>(CommentObject.class, MethodConstant.COMMENT_ADD)
                .setRequestConverter(new ParameterConvertor<CommentObject>(new String[]{"id", "type", "comment", "reply_username", "op"})));

        ///////////////////////////////////////////////////////////////////
        //论坛
        //////////////////////////////////////////////////////////////////
        //话题签到
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SignListObject, IEntityService<SignListObject>>(SignListObject.class, MethodConstant.TOPIC_SIGN)
                .setRequestConverter(new ParameterConvertor<SignListObject>(new String[]{"topic"})));
        //达人列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<TalentObject, IEntityService<TalentObject>>(TalentObject.class, MethodConstant.TALENT)
                .setRequestConverter(new ParameterConvertor<TalentObject>(new String[]{"category"})));
        //达人详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<TalentObject, IEntityService<TalentObject>>(TalentObject.class, MethodConstant.TALENT_VIEW)
                .setRequestConverter(new ParameterConvertor<TalentObject>(new String[]{"uid", "page", "lpp"})));
        //发布贴子
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.THREAD_ADD)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"fid", "typeid", "subject", "content"})));
        //版块详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SectionObject, IEntityService<SectionObject>>(SectionObject.class, MethodConstant.FORUM_VIEW)
                .setRequestConverter(new ParameterConvertor<SectionObject>(new String[]{"fid", "typeid", "page", "lpp", "order"})));
        //版块添加收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SectionObject, IEntityService<SectionObject>>(SectionObject.class, MethodConstant.FORUM_FAV_ADD)
                .setRequestConverter(new ParameterConvertor<SectionObject>(new String[]{"fid"})));
        //版块取消收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SectionObject, IEntityService<SectionObject>>(SectionObject.class, MethodConstant.FORUM_FAV_REMOVE)
                .setRequestConverter(new ParameterConvertor<SectionObject>(new String[]{"fid"})));
        //主题列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.THREAD_LIST)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"fid", "typeid", "authorid", "order"})));
        //贴子详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.THREAD_VIEW)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"id", "page", "lpp", "uid", "source_type", "source_value"})));
        //贴子收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.THREAD_FAV)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"tid"})));
        //删除贴子收藏
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.DEL_THREAD_FAV)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"ids"})));
        //回贴
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.THREAD_POST)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"tid", "pid", "message"})));
        //贴子回复列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<CommentObject, IEntityService<CommentObject>>(CommentObject.class, MethodConstant.POST_REPLIES)
                .setRequestConverter(new ParameterConvertor<CommentObject>(new String[]{"id", "uid"})));
        //活动列表
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<CommentObject>>(PostObject.class, MethodConstant.SHIPPING_ACTIVITY)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"category"})));
        ServiceFactory.addServiceDefine(new ServiceDefineBase<NoticeObject, IEntityService<NoticeObject>>(NoticeObject.class, MethodConstant.SEND_PM)
                .setRequestConverter(new ParameterConvertor<NoticeObject>(new String[]{"message", "touid"})));
        //免费试用
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SampleObject, IEntityService<SampleObject>>(SampleObject.class, MethodConstant.SAMPLE_LIST)
                .setRequestConverter(new ParameterConvertor<SampleObject>(new String[]{"page", "lpp"})));
        //试用详情
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SampleObject, IEntityService<SampleObject>>(SampleObject.class, MethodConstant.SAMPLE_DETAIL)
                .setRequestConverter(new ParameterConvertor<SampleObject>(new String[]{"id"})));
        //试用申请名单
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SampleApplyObject, IEntityService<SampleApplyObject>>(SampleApplyObject.class, MethodConstant.SAMPLE_MEMBER)
                .setRequestConverter(new ParameterConvertor<SampleApplyObject>(new String[]{"page", "lpp", "id"})));
        //试用报告
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SampleReportObject, IEntityService<SampleReportObject>>(SampleReportObject.class, MethodConstant.SAMPLE_REPORT)
                .setRequestConverter(new ParameterConvertor<SampleReportObject>(new String[]{"id"})));
        //试用申请
        ServiceFactory.addServiceDefine(new ServiceDefineBase<SampleApplyObject, IEntityService<SampleApplyObject>>(SampleApplyObject.class, MethodConstant.SAMPLE_APPLY)
                .setRequestConverter(new ParameterConvertor<SampleApplyObject>(new String[]{"trial_id", "reason", "real_name", "mobile", "province", "city", "address", "postcode"})));
        //金币兑换首页
        //免费试用
        ServiceFactory.addServiceDefine(new ServiceDefineBase<ExchangeObject, IEntityService<ExchangeObject>>(ExchangeObject.class, MethodConstant.EXCHANGE_LIST)
                .setRequestConverter(new ParameterConvertor<ExchangeObject>(new String[]{"page", "lpp", "type"})));
        ServiceFactory.addServiceDefine(new ServiceDefineBase<ExchangeObject, IEntityService<ExchangeObject>>(ExchangeObject.class, MethodConstant.EXCHANGE_DETAIL)
                .setRequestConverter(new ParameterConvertor<ExchangeObject>(new String[]{"tid"})));
        ServiceFactory.addServiceDefine(new ServiceDefineBase<ExchangeApplyObject, IEntityService<ExchangeApplyObject>>(ExchangeApplyObject.class, MethodConstant.EXCHANGE_APPLY)
                .setRequestConverter(new ParameterConvertor<ExchangeApplyObject>(new String[]{"tid", "revicename", "mobile", "address", "info", "other"})));
        ///////////////////////////////////////////////////////////////////
        //搜索
        //////////////////////////////////////////////////////////////////
        //优惠
        ServiceFactory.addServiceDefine(new ServiceDefineBase<DiscountObject, IEntityService<DiscountObject>>(DiscountObject.class, MethodConstant.SEARCH_DEAL)
                .setRequestConverter(new ParameterConvertor<DiscountObject>(new String[]{"keywords"})));
        //贴子
        ServiceFactory.addServiceDefine(new ServiceDefineBase<PostObject, IEntityService<PostObject>>(PostObject.class, MethodConstant.SEARCH_POST)
                .setRequestConverter(new ParameterConvertor<PostObject>(new String[]{"keywords"})));
        //商家
        ServiceFactory.addServiceDefine(new ServiceDefineBase<StoreObject, IEntityService<StoreObject>>(StoreObject.class, MethodConstant.SEARCH_STORE)
                .setRequestConverter(new ParameterConvertor<StoreObject>(new String[]{"keywords"})));
        //转运
        ServiceFactory.addServiceDefine(new ServiceDefineBase<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>>(LogisticsCompanyObject.class, MethodConstant.SEARCH_TRANSPORT)
                .setRequestConverter(new ParameterConvertor<LogisticsCompanyObject>(new String[]{"keywords"})));
        ///////////////////////////////////////////////////////////////////
        //缓存数据
        //////////////////////////////////////////////////////////////////
        //获取省市
        ServiceFactory.addServiceDefine(new ServiceDefineBase<ProvinceObject, IEntityService<ProvinceObject>>(ProvinceObject.class, MethodConstant.AREA)
                .setRequestConverter(new ParameterConvertor<ProvinceObject>(new String[]{})));
        /////////////////////////////////////////////////////////////////
        //数据统计
        ////////////////////////////////////////////////////////////////
        ServiceFactory.addServiceDefine(new ServiceDefineBase<ShareObject, IEntityService<ShareObject>>(ShareObject.class, MethodConstant.SHARE_LOG)
                .setRequestConverter(new ParameterConvertor<ShareObject>(new String[]{"share_type", "share_content"})));

    }

}
