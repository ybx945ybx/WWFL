package com.haitao.utils;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.common.Constant.SPConstant;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.view.wheel.adapters.AbstractWheelTextAdapter;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 弹框处理
 *
 * @author penley
 */
public class DialogUtils {

    public static Dialog dialog = null;

    /**
     * 设置字体大小
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setTextviewSize(Context mContext, String curriteItemText, AbstractWheelTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int             size      = arrayList.size();
        String          currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                //                textvew.setTextColor(mContext.getResources().getColor(R.color.darkGrey));
            } else {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                //                textvew.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
            }
        }
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
    public static void showShareDialog(Context context, int type, int parent, String title, String content, String contentWeibo, String url, String picPath) {
        showShareDialog(context, type, R.layout.layout_share, title, content, contentWeibo, url, picPath, null);
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
    public static void showShareDialog(Context context, int type, int parent, String title, String content, String contentWeibo, String url, String picPath, ShareAnalyticsObject shareAnalyticsObject) {
        showShareDialog(context, type, parent, title, content, contentWeibo, url, picPath, shareAnalyticsObject, null, null, null);
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
     * @param dlgTitle             对话框标题
     * @param dlgContent           对话框文本
     */
    public static void showShareDialog(Context context, int type, int parent, String title, String content, String contentWeibo, String url, String picPath,
                                       ShareAnalyticsObject shareAnalyticsObject, DiscountDetailActivity.OnPromotionShareClickListener listener,
                                       String dlgTitle, String dlgContent) {
        if (null == dialog) {
            dialog = new Dialog(context, R.style.st_loading_dialog);// 创建自定义样式dialog
        }
        Window win = dialog.getWindow();
        if (win == null) return;
        win.setGravity(Gravity.BOTTOM);
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View                 v        = inflater.inflate(parent, null);// 得到加载view
        // 点击事件
        OnClickListener clickListener = v1 -> {
            String platform = "";
            switch (v1.getId()) {
                case R.id.btnQQ: // QQ
                    platform = QQ.NAME;
                    break;
                case R.id.btnQzone: // QQ空间
                    platform = QZone.NAME;
                    break;
                case R.id.btnWeChat: // 微信
                    platform = Wechat.NAME;
                    break;
                case R.id.btnWeChatMoments: // 朋友圈
                    platform = WechatMoments.NAME;
                    break;
                case R.id.btnSinaWeiBo: // 微博
                    platform = SinaWeibo.NAME;
                    break;
                case R.id.btnBrowserOpen: // 浏览器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                    break;
                case R.id.btnLinks: // 复制链接
                    AppUtils.copyToClipboard(context, url);
                    break;
                case R.id.btnShareMore: // 更多
                    Intent intentMore = new Intent();
                    intentMore.setAction(Intent.ACTION_SEND);
                    intentMore.putExtra(Intent.EXTRA_TEXT, url);
                    intentMore.setType("text/plain");
                    context.startActivity(Intent.createChooser(intentMore, "分享到"));
                    break;
                case R.id.ll_generate_img_share: // 生成图片分享
                    if (listener != null)
                        listener.onGenerateImgClick();
                    break;
                case R.id.ll_promotion_for_rebate: // 推广赚返利
                    if (listener != null)
                        listener.onPromotionForRebateClick();
                    break;
                case R.id.iv_close: // 取消
                    break;
            }
            if (!TextUtils.isEmpty(platform)) { // 三方分享
                ShareUtils.sharePost(context, platform, title, content, contentWeibo, url, picPath, shareAnalyticsObject);
            } else {
                // TalkingData分享统计
                if (shareAnalyticsObject != null) {
                    if (TextUtils.isEmpty(shareAnalyticsObject.id)) {
                        TCAgent.onEvent(context, shareAnalyticsObject.type, "其他");
                    } else {
                        // 分享Id
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", shareAnalyticsObject.id);
                        TCAgent.onEvent(context, shareAnalyticsObject.type, "其他", map);
                    }
                }
            }
            dismiss();
        };

        // 标题
        TextView tvTitle = v.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(dlgTitle)) {
            tvTitle.setText(dlgTitle);
        } else {
            tvTitle.setText(context.getResources().getString(R.string.share));
        }
        // 文本
        if (!TextUtils.isEmpty(dlgContent)) {
            TextView tvContent = v.findViewById(R.id.tv_content);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(dlgContent);
        }

        // 绑定监听
        v.findViewById(R.id.iv_close).setOnClickListener(clickListener);
        v.findViewById(R.id.btnQQ).setOnClickListener(clickListener);
        v.findViewById(R.id.btnQzone).setOnClickListener(clickListener);
        v.findViewById(R.id.btnWeChat).setOnClickListener(clickListener);
        v.findViewById(R.id.btnWeChatMoments).setOnClickListener(clickListener);
        v.findViewById(R.id.btnSinaWeiBo).setOnClickListener(clickListener);
        v.findViewById(R.id.btnBrowserOpen).setOnClickListener(clickListener);
        v.findViewById(R.id.btnShareMore).setOnClickListener(clickListener);
        // 推广部分
        View llPromotion = v.findViewById(R.id.ll_promotion);
        llPromotion.setVisibility(listener == null ? View.GONE : View.VISIBLE);
        if (listener != null) {
            v.findViewById(R.id.ll_promotion_for_rebate).setOnClickListener(clickListener);
            v.findViewById(R.id.ll_generate_img_share).setOnClickListener(clickListener);
            if (type == 2) {    // 优惠详情没有返利  则只是显示生成图片  不显示分享赚返利
                v.findViewById(R.id.ll_promotion_for_rebate).setVisibility(View.GONE);
                v.findViewById(R.id.view_line).setVisibility(View.GONE);

            } else {  //  显示分享赚返利  第一次显示引导
                if (!(Boolean) SPUtils.get(context, SPConstant.PROMOTION_GUIDE_DLG, false)) {
                    v.findViewById(R.id.llyt_tips).setVisibility(View.VISIBLE);
                    SPUtils.put(context, SPConstant.PROMOTION_GUIDE_DLG, true);
                }

            }
        }
        TextView tvLinks = v.findViewById(R.id.btnLinks);
        tvLinks.setOnClickListener(clickListener);

        if (type == 0) {
            tvLinks.setText("邮件");
            Drawable img_off = ContextCompat.getDrawable(context, R.mipmap.ic_emil);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
            tvLinks.setCompoundDrawables(null, img_off, null, null); // 设置左图标
        }
        dialog.setContentView(v);// 设置布局
        dialog.setOnDismissListener(dialog1 -> dismiss());
        dialog.show();
    }

    public static void dismiss() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
}
