package com.haitao.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.db.StoreDB;
import com.haitao.framework.http.okhttp.callback.ResultCallback;
import com.haitao.framework.utils.HTTPUtil;
import com.haitao.imp.URILocatorHelper;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.StoreResponseObject;
import com.haitao.utils.SPUtils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

/**
 * Created by apple on 17/3/23.
 */

public class CustomService extends Service {
    public static final  String TAG          = "CustomService";
    private static final String ACTION_START = "CustomService" + ".START";
    private static final String ACTION_STOP  = "CustomService" + ".STOP";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 静态方法开启服务。
     *
     * @param mContext 上下文环境
     */
    public static void actionStart(Context mContext) {
        Intent i = new Intent(mContext, CustomService.class);
        i.setAction(ACTION_START);
        mContext.startService(i);
        //        Log.e(TAG, "----------------CustomService开启-----------------");
    }

    /**
     * 静态方法停止服务。
     *
     * @param mContext 上下文环境
     */
    public static void actionStop(Context mContext) {
        Intent i = new Intent(mContext, CustomService.class);
        i.setAction(ACTION_STOP);
        mContext.startService(i);
        //        Logger.d("----------------CustomService关闭-----------------");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null) {// 根据意图做出相应的行动
            Logger.d(intent.getAction());
            if (ACTION_START.equals(intent.getAction())) {
                new Thread(this::initStore).start();
            } else if (ACTION_STOP.equals(intent.getAction())) {
                stopSelf();
            }
        }
    }


    private void initStore() {
        HTTPUtil.postAsyn(URILocatorHelper.getUrlBase().getBaseURI() + "/" + MethodConstant.STORE_LIST, MethodConstant.STORE_LIST, null, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                // FLog.e("=====response",response);
                if (!TextUtils.isEmpty(response)) {
                    StoreResponseObject storeResponseObject = JSON.parseObject(response, StoreResponseObject.class);
                    if ("0".equals(storeResponseObject.code)) {
                        StoreFilterObject entity = storeResponseObject.data;
                        if (null != entity) {

                            if (null != entity.list && entity.list.size() > 0 && StoreDB.count() <= 0) {
                                StoreDB.add(entity.list);
                            }
                            if (null != entity.char_list) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_CHAR, JSON.toJSONString(entity.char_list));
                            }
                            if (null != entity.version) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_VERSION, entity.version);
                            }

                            if (null != entity.super_rebate) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_SUPER_REBATE, JSON.toJSONString(entity.super_rebate));
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("Custom Service onCreate");
    }
}
