package com.haitao.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {

    private static ProgressDialog mDialog;

    public static void show(Context ctx, String text) {
        // 全部改为可以取消的dialog
        show(ctx, text, true);
    }

    public static void show(Context ctx, String text, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(ctx);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }
            mDialog.setMessage(text);
            if (!((Activity) ctx).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public static void show(Context ctx, int resId) {
        show(ctx, resId, false);
    }

    public static void show(Context ctx, int resId, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(ctx);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }

            mDialog.setMessage(ctx.getResources().getString(resId));
            if (!((Activity) ctx).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public static boolean isShowing() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    public static void dismiss() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
        }
    }
}
