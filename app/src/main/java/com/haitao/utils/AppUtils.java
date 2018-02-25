package com.haitao.utils;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import com.haitao.R;

/**
 * Created by penley on 15/12/18.
 */
public class AppUtils {

    /**
     * 实现文本复制功能
     *
     * @param mContext
     * @param content
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copyToClipboard(Context mContext, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        ToastUtils.show(mContext, R.string.copy_to_clipboard);
    }
}
