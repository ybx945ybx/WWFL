package com.haitao.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.common.annotation.ToastType;
import com.haitao.event.ShareSuccessEvent;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享工具类
 */
public class ShareUtils {
    public static void sharePost(Context context, String platform, String title, String content, String contentWeibo, String url, String picPath, ShareAnalyticsObject shareAnalyticsObject) {
        sharePost(context, platform, title, content, contentWeibo, url, picPath, shareAnalyticsObject, null);
    }

    public static void sharePost(Context context, String platform, String title, String content, String contentWeibo, String url, String picPath, ShareAnalyticsObject shareAnalyticsObject, PlatformActionListener listener) {
        String filepath = "";
        if (picPath.startsWith("http")) {
            filepath = ImageLoader.getInstance().getDiskCache().get(picPath).getPath();
            if (!new File(filepath).exists()) {
                filepath = "";
            }
        } else {
            if (TextUtils.isEmpty(picPath)) {
                filepath = "";
            } else {
                filepath = picPath;
            }
        }

        // TalkingData分享统计
        if (shareAnalyticsObject != null) {
            if (TextUtils.isEmpty(shareAnalyticsObject.id)) {
                TCAgent.onEvent(context, shareAnalyticsObject.type, getShareAnalyticsPlatform(platform));
            } else {
                // 分享Id
                HashMap<String, String> map = new HashMap<>();
                map.put("id", shareAnalyticsObject.id);
                TCAgent.onEvent(context, shareAnalyticsObject.type, getShareAnalyticsPlatform(platform), map);
            }
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (TextUtils.isEmpty(picPath)) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(filepath);
            if (f != null && f.exists() && f.isFile()) {
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
                intent.setType("image/*");
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, content);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        showSharePost(context, platform, title, content, contentWeibo, url, filepath, listener);
    }

    public static void showSharePost(Context context, String platformName, String title, String content, String contentWeibo, String url, String picPath, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(TextUtils.equals(platformName, SinaWeibo.NAME) ? contentWeibo : content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(picPath);//确保SDcard下面存在此张图片
        oks.setFilePath(picPath);
        // url仅在微信（包括好友和朋友圈）中使用
        if (!TextUtils.equals(platformName, SinaWeibo.NAME)) {
            oks.setUrl(url);
        }
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 是否直接分享（true则直接分享），false是有九格宫，true没有
        oks.setSilent(true);
        oks.setPlatform(platformName);
        if (listener == null) {
            oks.setCallback(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    ((BaseActivity) context).showToast(ToastType.COMMON_SUCCESS, "分享成功");
                    EventBus.getDefault().post(new ShareSuccessEvent(getPlatformName(platformName)));
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    ((BaseActivity) context).showToast("分享失败");
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    ((BaseActivity) context).showToast("分享取消");
                }
            });
        } else {
            oks.setCallback(listener);
        }

        Logger.d("title = " + title + " content = " + content + " url = " + url + " image = " + picPath);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 显示分享弹框
     *
     * @param context mContext
     * @param type    类型
     * @param title   标题
     * @param content 文本
     * @param url     url
     * @param picPath 图片地址
     */
    public static void showShareDialog(final Context context, final int type, final String title, final String content, final String contentWeibo, final String url, final String picPath) {
        DialogUtils.showShareDialog(context, type, R.layout.layout_share, title, content, contentWeibo, url, picPath);
    }

    /**
     * 显示分享弹框(带推广)
     *
     * @param context mContext
     * @param type    类型
     * @param title   标题
     * @param content 文本
     * @param url     url
     * @param picPath 图片地址
     */
    public static void showShareDialog(final Context context, final int type, final String title, final String content, final String contentWeibo, final String url, final String picPath, final DiscountDetailActivity.OnPromotionShareClickListener listener) {
        DialogUtils.showShareDialog(context, type, R.layout.layout_share, title, content, contentWeibo, url, picPath, null, listener, null, null);
    }

    /**
     * 显示分享弹框
     *
     * @param context              mContext
     * @param type                 类型
     * @param title                标题
     * @param content              文本
     * @param url                  url
     * @param picPath              图片地址
     * @param shareAnalyticsObject 分享统计
     */
    public static void showShareDialog(final Context context, final int type, final String title, final String content, final String contentWeibo, final String url, final String picPath, final ShareAnalyticsObject shareAnalyticsObject) {
        DialogUtils.showShareDialog(context, type, R.layout.layout_share, title, content, contentWeibo, url, picPath, shareAnalyticsObject);
    }

    /**
     * 显示分享弹框 - 带标题文本
     *
     * @param context              mContext
     * @param type                 类型
     * @param title                标题
     * @param content              文本
     * @param url                  url
     * @param picPath              图片地址
     * @param shareAnalyticsObject 分享统计
     * @param dlgTilte             对话框标题
     * @param dlgContent           对话框文本
     */
    public static void showShareDialog(final Context context, final int type, final String title, final String content, final String contentWeibo, final String url, final String picPath, final ShareAnalyticsObject shareAnalyticsObject, String dlgTilte, String dlgContent) {
        DialogUtils.showShareDialog(context, type, R.layout.layout_share, title, content, contentWeibo, url, picPath, shareAnalyticsObject, null, dlgTilte, dlgContent);
    }

    /**
     * 分享统计平台名
     *
     * @param platform 平台名
     * @return 转换后的平台名
     */
    public static String getShareAnalyticsPlatform(String platform) {
        String analyticsPlatform = "";
        if (TextUtils.equals(platform, QQ.NAME)) {
            analyticsPlatform = "QQ好友";
        } else if (TextUtils.equals(platform, QZone.NAME)) {
            analyticsPlatform = "QQ空间";
        } else if (TextUtils.equals(platform, Wechat.NAME)) {
            analyticsPlatform = "微信好友";
        } else if (TextUtils.equals(platform, WechatMoments.NAME)) {
            analyticsPlatform = "微信朋友圈";
        } else if (TextUtils.equals(platform, SinaWeibo.NAME)) {
            analyticsPlatform = "新浪微博";
        } else {
            analyticsPlatform = "其他";
        }
        return analyticsPlatform;
    }

    /**
     * shareSDK的platform名 -> 我们用的platform名
     *
     * @param platform shareSDK的platform名
     */
    public static String getPlatformName(String platform) {
        String eventPlatformName = "";
        if (TextUtils.equals(platform, QQ.NAME)) {
            eventPlatformName = "QQ";
        } else if (TextUtils.equals(platform, SinaWeibo.NAME)) {
            eventPlatformName = "Weibo";
        } else if (TextUtils.equals(platform, Wechat.NAME)) {
            eventPlatformName = "Wechat";
        }
        return eventPlatformName;
    }
}
