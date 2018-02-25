package com.haitao.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.DiscountDailyActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.activity.ExchangeDetailActivity;
import com.haitao.activity.InviteFriendsActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.SampleDetailActivity;
import com.haitao.activity.SignActivity;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.activity.StoreFilterActivity;
import com.haitao.activity.TagDetailActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.activity.TransportFilterActivity;
import com.haitao.activity.WebActivity;
import com.haitao.common.Constant.PushConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.model.AdObject;

import io.swagger.client.model.SlidePicModel;

/**
 * 专题跳转
 */
public class TopicLink {
    public static class SOURCE_TYPE {
        /**
         * 来源（push/banner/splash_ad/popup_ad/cross_bar）
         * push:推送
         * banner:Banner
         * splash_ad:闪屏广告
         * popup_ad:弹出广告
         * cross_bar:通栏广告
         */
        public static final String PUSH    = "push";
        public static final String BANNER  = "sbanner";
        public static final String SPLASH  = "splash_ad";
        public static final String POPUP   = "popup_ad";
        public static final String CROSS   = "cross_bar";
        public static final String SPECIAL = "special";
    }

    public static String TAG = "banner";

    public static void jump(Context mContext, AdObject adObject) {
        jump(mContext, adObject, "");
    }

    public static void jump(Context mContext, SlidePicModel slidePicModel) {
        jump(mContext, slidePicModel, "");
    }

    /**
     * 根据类型跳转
     *
     * @param context mContext
     * @param data    AdObject data
     * @param type    类型
     */
    public static void jump(Context context, AdObject data, String type) {
        //        Intent intent = null;
        //        Bundle bundle = new Bundle();
        if (data.type.equals(TransConstant.AdType.DEAL) || data.type.equals(PushConstant.DEAL)) {
            // 优惠详情
            DiscountDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.STORE) || data.type.equals(PushConstant.STORE)) {
            // 商家详情
            StoreDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.POST) || data.type.equals(PushConstant.POST)) {
            // 帖子详情
            TopicDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.WEB) || data.type.equals(PushConstant.WEB)) {
            // H5
            if (data.value.contains("?")) {
                data.value = data.value + "&fromapp=1";
            } else {
                data.value = data.value + "?fromapp=1";
            }
            // 不传title
            WebActivity.launch(context, "", data.value, true);
        } else if (data.type.equals(TransConstant.AdType.REGIST)) {
            // 注册
            if (UserManager.getInstance().isLogin()) {
                InviteFriendsActivity.launch(context);
            } else {
                //                LoginActivity.launch(mContext, 1);
                QuickLoginActivity.launch(context);
            }
        } else if (data.type.equals(TransConstant.AdType.BOARD) || data.type.equals(PushConstant.BOARD)) {
            // 版块详情
            BoardDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.EXCHANGE) || data.type.equals(PushConstant.EXCHANGE)) {
            // 兑换详情
            ExchangeDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.TRANSPORT) || data.type.equals(PushConstant.TRANSPORT)) {
            // 转运详情
            TransportDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.TRIAL) || data.type.equals(PushConstant.TRIAL)) {
            // 试用详情
            SampleDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.TAG) || data.type.equals(PushConstant.TAG)) {
            // 标签详情
            TagDetailActivity.launch(context, data.title, data.value);
        } else if (data.type.equals(TransConstant.AdType.INVITE)) {
            // 邀请好友
            InviteFriendsActivity.launch(context);
        } else if (data.type.equals(TransConstant.AdType.USER) || data.type.equals(PushConstant.USER)) {
            // 用户详情
            TalentDetailActivity.launch(context, data.value);
        } else if (data.type.equals(TransConstant.AdType.APP_STORE) || data.type.equals(PushConstant.APP_STORE)) {
            // 应用市场
            try {
                Uri    uri           = Uri.parse("market://details?id=" + context.getPackageName());
                Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(commentIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.type.equals(TransConstant.AdType.OUTAPP) || data.type.equals(PushConstant.OUTAPP)) {
            // 外部浏览器打开
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(data.value);
            intent.setData(content_url);
            context.startActivity(intent);
        } else if (data.type.equals(TransConstant.AdType.SIGN) || data.type.equals(PushConstant.SIGN)) {
            // 每日签到
            SignActivity.launch(context);
        } else if (data.type.equals(TransConstant.AdType.STORE_LIST) || data.type.equals(PushConstant.STORE_LIST)) {
            // 返利商家列表
            StoreFilterActivity.launch(context);
        } else if (data.type.equals(TransConstant.AdType.TRANS_LIST) || data.type.equals(PushConstant.TRANS_LIST)) {
            // 转运商家列表
            TransportFilterActivity.launch(context);
        } else if (data.type.equals(TransConstant.AdType.DEAL_DAILY_LIST) || data.type.equals(PushConstant.DEAL_DAILY_LIST)) {
            // 优惠日报列表
            DiscountDailyActivity.launch(context);
        }
    }

    /**
     * 根据类型跳转
     *
     * @param context mContext
     * @param data    SlidePicModel data
     * @param type    类型
     */
    public static void jump(Context context, SlidePicModel data, String type) {
        if (data.getType().equals(TransConstant.AdType.DEAL) || data.getType().equals(PushConstant.DEAL)) {
            // 优惠详情
            DiscountDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.STORE) || data.getType().equals(PushConstant.STORE)) {
            // 商家详情
            StoreDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.POST) || data.getType().equals(PushConstant.POST)) {
            // 帖子详情
            TopicDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.WEB) || data.getType().equals(PushConstant.WEB)) {
            // H5
            if (data.getLinkData().contains("?")) {
                data.setLinkData(data.getLinkData() + "&fromapp=1");
            } else {
                data.setLinkData(data.getLinkData() + "?fromapp=1");
            }
            // 不传title
            WebActivity.launch(context, "", data.getLinkData(), true);
        } else if (data.getType().equals(TransConstant.AdType.REGIST)) {
            // 注册
            if (UserManager.getInstance().isLogin()) {
                InviteFriendsActivity.launch(context);
            } else {
                //                LoginActivity.launch(mContext, 1);
                QuickLoginActivity.launch(context);
            }
        } else if (data.getType().equals(TransConstant.AdType.BOARD) || data.getType().equals(PushConstant.BOARD)) {
            // 版块详情
            BoardDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.EXCHANGE) || data.getType().equals(PushConstant.EXCHANGE)) {
            // 兑换详情
            ExchangeDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.TRANSPORT) || data.getType().equals(PushConstant.TRANSPORT)) {
            // 转运详情
            TransportDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.TRIAL) || data.getType().equals(PushConstant.TRIAL)) {
            // 试用详情
            SampleDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.TAG) || data.getType().equals(PushConstant.TAG)) {
            // 标签详情
            TagDetailActivity.launch(context, data.getTitle(), data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.INVITE)) {
            // 邀请好友
            InviteFriendsActivity.launch(context);
        } else if (data.getType().equals(TransConstant.AdType.USER) || data.getType().equals(PushConstant.USER)) {
            // 用户详情
            TalentDetailActivity.launch(context, data.getLinkData());
        } else if (data.getType().equals(TransConstant.AdType.APP_STORE) || data.getType().equals(PushConstant.APP_STORE)) {
            // 应用市场
            try {
                Uri    uri           = Uri.parse("market://details?id=" + context.getPackageName());
                Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(commentIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.getType().equals(TransConstant.AdType.OUTAPP) || data.getType().equals(PushConstant.OUTAPP)) {
            // 外部浏览器打开
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(data.getLinkData());
            intent.setData(content_url);
            context.startActivity(intent);
        } else if (data.getType().equals(TransConstant.AdType.SIGN) || data.getType().equals(PushConstant.SIGN)) {
            // 每日签到
            SignActivity.launch(context);
        } else if (data.getType().equals(TransConstant.AdType.STORE_LIST) || data.getType().equals(PushConstant.STORE_LIST)) {
            // 返利商家列表
            StoreFilterActivity.launch(context);
        } else if (data.getType().equals(TransConstant.AdType.TRANS_LIST) || data.getType().equals(PushConstant.TRANS_LIST)) {
            // 转运商家列表
            TransportFilterActivity.launch(context);
        } else if (data.getType().equals(TransConstant.AdType.DEAL_DAILY_LIST) || data.getType().equals(PushConstant.DEAL_DAILY_LIST)) {
            // 优惠日报列表
            DiscountDailyActivity.launch(context);
        }
    }
}