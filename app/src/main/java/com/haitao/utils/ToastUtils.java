package com.haitao.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast;

    public static void show(Context ctx, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void show(Context ctx, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, ctx.getResources().getString(resId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(ctx.getResources().getString(resId));
        }
        mToast.show();
    }

}
