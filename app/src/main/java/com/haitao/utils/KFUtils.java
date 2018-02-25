package com.haitao.utils;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;

import com.haitao.common.Constant.XNConstant;
import com.haitao.common.UserManager;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;
import io.swagger.client.model.DealExtraModel;

public class KFUtils {
    /**
     * 开始聊天
     *
     * @param activity
     */
    public static void startChat(Activity activity) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getInstance().getPermissions(activity, 200, permissions);
        getKF5UserInfo();
        Ntalker.getInstance().startChat(activity, XNConstant.SETTING_ID, "", null, null, null);
    }

    /**
     * 开始聊天,带入口标题，入口url
     *
     * @param mContext
     */
    public static void startChat(Activity mContext, DealExtraModel discountObject) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getInstance().getPermissions(mContext, 200, permissions);
        getKF5UserInfo();
        ChatParamsBody chatparams = new ChatParamsBody();
        chatparams.startPageTitle = discountObject.getTitle();  // 咨询发起页标题(必填)
        chatparams.startPageUrl = discountObject.getShareUrl();//咨询发起页URL，
        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        chatparams.itemparams.goods_id = discountObject.getDealId();//示例：ntalker_test，传入商品id*/
        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
        chatparams.itemparams.goods_name = discountObject.getTitle();
        chatparams.itemparams.goods_price = TextUtils.isEmpty(discountObject.getNowPrice()) ? discountObject.getDiscountView() : discountObject.getNowPrice();
        chatparams.itemparams.goods_image = discountObject.getDealPic();     //URL必须以"http://"开头
        chatparams.itemparams.goods_url = discountObject.getShareUrl();
        chatparams.itemparams.itemparam = "2";//1：直购，2：优惠，3：帖子
        Ntalker.getInstance().startChat(mContext, XNConstant.SETTING_ID, "", null, null, chatparams);
    }


    /**
     * 开始聊天
     *
     * @param mContext
     */
    public static void startChat(Activity mContext, String service_title, String service_pic, String service_price, String service_url, String service_id) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getInstance().getPermissions(mContext, 200, permissions);
        getKF5UserInfo();
        ChatParamsBody chatparams = new ChatParamsBody();
        chatparams.startPageTitle = service_title;  // 咨询发起页标题(必填)
        chatparams.startPageUrl = service_url;//咨询发起页URL，
        if (!TextUtils.isEmpty(service_id)) {
            chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
            chatparams.itemparams.goods_id = service_id;//示例：ntalker_test，传入商品id*/
        }
        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
        chatparams.itemparams.goods_name = service_title;
        chatparams.itemparams.goods_price = service_price;
        chatparams.itemparams.goods_image = service_pic;     //URL必须以"http://"开头
        chatparams.itemparams.goods_url = service_url;
        chatparams.itemparams.itemparam = "2";//1：直购，2：优惠，3：帖子
        Ntalker.getInstance().startChat(mContext, XNConstant.SETTING_ID, "", null, null, chatparams);
    }

    /**
     * 登录客服系统
     *
     * @return
     */
    public static int getKF5UserInfo() {
        String uid    = UserManager.getInstance().isLogin() ? "ht_" + UserManager.getInstance().getUserId() : "";
        int    userId = Ntalker.getInstance().login(uid, UserManager.getInstance().getUserName(), 0);
        return userId;
    }

}
