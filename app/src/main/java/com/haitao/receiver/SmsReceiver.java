package com.haitao.receiver;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.haitao.common.Constant.SPConstant;
import com.haitao.utils.SPUtils;
import com.haitao.view.ClearEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tqy on 2016/1/11.
 */
public class SmsReceiver extends ContentObserver {


    public static final String SMS_URI_INBOX = "content://sms/inbox";

    private Activity activity = null;

    private String smsContent = "";

    private ClearEditText verifyText = null;

    public SmsReceiver(Activity activity, Handler handler, ClearEditText verifyText) {
        super(handler);
        this.activity = activity;
        this.verifyText = verifyText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;// 光标
        // 读取收件箱中指定号码的短信
        cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] { "_id", "address", "body", "read" }, "address=? and read=?",
                new String[] {(String)SPUtils.get(activity, SPConstant.SMS_SEND_PHONE,"106575632881"), "0" }, "date desc");

        if (cursor != null) {// 如果短信为未读模式
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {

                String smsbody = cursor.getString(cursor.getColumnIndex("body"));
                String regEx = "验证码为[:]?([0-9]+)";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(smsbody);
                if(m.find()){
                    smsContent = m.group(1);
                    verifyText.setText(smsContent);
                }

            }

        }

    }

}
