package com.haitao.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制输入edittext
 * Created by a55 on 2017/11/22.
 */

public class LengthFilter implements InputFilter {
    int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
    String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字
    private Context mContext;

    public LengthFilter(Context context, int mAX_EN) {
        super();
        mContext = context;
        MAX_EN = mAX_EN;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int destCount = dest.toString().length()
                + getChineseCount(dest.toString());
        int sourceCount = source.toString().length()
                + getChineseCount(source.toString());
        if (destCount + sourceCount > MAX_EN) {
            //            ToastUtils.showToast(mContext, String.format("不能超过%d字", MAX_EN));
            return "";
        } else {
            return source;
        }
    }

    public int getChineseCount(String str) {
        int     count = 0;
        Pattern p     = Pattern.compile(regEx);
        Matcher m     = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

}
