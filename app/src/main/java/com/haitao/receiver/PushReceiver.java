package com.haitao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.haitao.activity.BaseActivity;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.WithdrawRecordActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.activity.ExchangeDetailActivity;
import com.haitao.activity.InviteFriendsActivity;
import com.haitao.activity.MyRebateActivity;
import com.haitao.activity.SampleDetailActivity;
import com.haitao.activity.SplashActivity;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.activity.TagDetailActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.activity.WebActivity;
import com.haitao.common.Constant.PushConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.JpushObject;
import com.haitao.utils.TraceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
            // send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            intentToActivity(context, extras);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG,
                    "用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void intentToActivity(Context context, String extras) {
        JSONObject  jsonObject;
        Bundle      bundle      = new Bundle();
        JpushObject jpushObject = new JpushObject();
        try {
            jsonObject = new JSONObject(extras);

            if (jsonObject.has("type")) {
                jpushObject.type = jsonObject.getString("type");
            }
            if (jsonObject.has("value")) {
                jpushObject.value = jsonObject.getString("value");
            }
            if (jsonObject.has("id")) {
                jpushObject.id = jsonObject.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 页面埋点 推送事件
        HashMap<String, String> kv = new HashMap<String, String>();
        kv.put(TraceUtils.Event_Kv_Type, jpushObject.type);
        kv.put(TraceUtils.Event_Kv_Value, jpushObject.value);
        YbxTrace.getInstance().event( null, "", "", "", "", "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Push, kv, "", TraceUtils.Fid_Push);

        Intent jpushIntent = null;
        if (jpushObject.type.equals(PushConstant.DEAL)) {
            jpushIntent = new Intent(context, DiscountDetailActivity.class);
            bundle.putString(TransConstant.ID, jpushObject.value);
            bundle.putString(TransConstant.SOURCE, "push");
            bundle.putString(TransConstant.VALUE, jpushObject.id);
        } else if (jpushObject.type.equals(PushConstant.STORE)) {
            jpushIntent = new Intent(context, StoreDetailActivity.class);
            bundle.putString(TransConstant.ID, jpushObject.value);
            bundle.putString(TransConstant.SOURCE, "push");
            bundle.putString(TransConstant.VALUE, jpushObject.id);
        } else if (jpushObject.type.equals(PushConstant.POST)) {
            jpushIntent = new Intent(context, TopicDetailActivity.class);
            bundle.putString(TransConstant.ID, jpushObject.value);
            bundle.putString(TransConstant.SOURCE, "push");
            bundle.putString(TransConstant.VALUE, jpushObject.id);
        } else if (jpushObject.type.equals(PushConstant.WEB)) {
            jpushIntent = new Intent(context, WebActivity.class);
            if (jpushObject.value.contains("?")) {
                jpushObject.value = jpushObject.value + "&fromapp=1";
            } else {
                jpushObject.value = jpushObject.value + "?fromapp=1";
            }
            jpushIntent.putExtra("isShare", true);
            bundle.putString(TransConstant.URL, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.REBATE)) {
            jpushIntent = new Intent(context, MyRebateActivity.class);
        } else if (jpushObject.type.equals(PushConstant.WITHDRAW)) {
            jpushIntent = new Intent(context, WithdrawRecordActivity.class);
        } else if (jpushObject.type.equals(PushConstant.TRANSPORT)) {
            jpushIntent = new Intent(context, TransportDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.BOARD)) {
            jpushIntent = new Intent(context, BoardDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.USER)) {
            jpushIntent = new Intent(context, TalentDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.TAG)) {
            jpushIntent = new Intent(context, TagDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.EXCHANGE)) {
            jpushIntent = new Intent(context, ExchangeDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.TRIAL)) {
            jpushIntent = new Intent(context, SampleDetailActivity.class);
            jpushIntent.putExtra(TransConstant.ID, jpushObject.value);
        } else if (jpushObject.type.equals(PushConstant.INVITE)) {
            jpushIntent = new Intent(context, InviteFriendsActivity.class);
        } else if (jpushObject.type.equals(PushConstant.APP_STORE)) {
            try {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                jpushIntent = new Intent(Intent.ACTION_VIEW, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (jpushObject.type.equals(PushConstant.OUTAPP)) {
            jpushIntent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(jpushObject.value);
            jpushIntent.setData(content_url);
        } else {
            jpushIntent = new Intent(context, SplashActivity.class);
        }
        jpushIntent.putExtras(bundle);
        jpushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(jpushIntent);

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (bundle.get(key) instanceof String) {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
