package com.haitao.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.haitao.R;

public class PopWindowUtils {
    static PopupWindow popupWindow = null;

    /**
     * 在某个view下方显示，分类下载
     *
     * @param mContext
     * @param locView
     * @param contentView
     */
    public static void show(final Context mContext, View locView, final View contentView) {
        if (null == popupWindow) {
            popupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor((v, event) -> false);
        ColorDrawable halfTransparent = (ColorDrawable)
                mContext.getResources().getDrawable(R.color.half_transparent);
        //        ColorDrawable halfTransparent = new ColorDrawable(Color.WHITE);
        popupWindow.setBackgroundDrawable(halfTransparent);
        popupWindow.showAsDropDown(locView, 0, DensityUtil.dip2px(mContext, 10));
    }

    public static void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        popupWindow.setOnDismissListener(listener);
    }

    public static void dismiss() {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 弹窗是否显示
     *
     * @return
     */
    public static boolean isShown() {
        return null != popupWindow && popupWindow.isShowing();
    }

    /**
     * 相对某个view显示
     *
     * @param mContext
     * @param locView
     * @param contentView
     */
    public static void showAtLocation(final Context mContext, View locView, final View contentView) {
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.ReturnTips);
        popupWindow.showAtLocation(locView, Gravity.TOP | Gravity.CENTER, 0, (int) mContext.getResources().getDimension(R.dimen.px30));
    }

    /**
     * 相对某个view显示
     *
     * @param mContext
     * @param locView
     * @param contentView
     */
    public static void showLocation(final Context mContext, View locView, final View contentView) {
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor((v, event) -> false);
        ColorDrawable halfTransparent = (ColorDrawable) ContextCompat.getDrawable(mContext, R.color.half_transparent);
        popupWindow.setBackgroundDrawable(halfTransparent);
        popupWindow.showAtLocation(locView, Gravity.BOTTOM | Gravity.RIGHT, (int) mContext.getResources().getDimension(R.dimen.px30), (int) mContext.getResources().getDimension(R.dimen.px105));
    }


}