package com.haitao.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;
import com.orhanobut.logger.Logger;

/**
 * 线下返利导航
 *
 * Created by a55 on 2018/1/5.
 */

public class NavigationDialog extends BottomSheetDialog implements View.OnClickListener {

    private Context mContext;
    private String  latitude;
    private String  longitude;

    public NavigationDialog(@NonNull Context context, String latitude, String longitude, boolean isBaiduShow, boolean isGaodeShow, boolean isGoogleShow, boolean isTengxunShow) {
        super(context);
        mContext = context;
        this.latitude = latitude;
        this.longitude = longitude;
        initDlg(context, isBaiduShow, isGaodeShow, isGoogleShow, isTengxunShow);

    }

    private void initDlg(Context context, boolean isBaiduShow, boolean isGaodeShow, boolean isGoogleShow, boolean isTengxunShow) {
        View layout      = View.inflate(context, R.layout.dlg_navigation, null);
        View llytBaidu   = layout.findViewById(R.id.llyt_baidu);
        View llytGaode   = layout.findViewById(R.id.llyt_gaode);
        View llytGoogle  = layout.findViewById(R.id.llyt_google);
        View llytTengxun = layout.findViewById(R.id.llyt_tengxun);

        llytBaidu.setVisibility(isBaiduShow ? View.VISIBLE : View.GONE);
        llytGaode.setVisibility(isGaodeShow ? View.VISIBLE : View.GONE);
        llytGoogle.setVisibility(isGoogleShow ? View.VISIBLE : View.GONE);
        llytTengxun.setVisibility(isTengxunShow ? View.VISIBLE : View.GONE);

        llytBaidu.setOnClickListener(this);
        llytGaode.setOnClickListener(this);
        llytGoogle.setOnClickListener(this);
        llytTengxun.setOnClickListener(this);

        setContentView(layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llyt_baidu:
                try {
                    Intent intent = Intent.getIntent("intent://map/direction?" +
                            "destination=latlng:" + latitude + "," + longitude + "|name:我的目的地" +    //终点
                            "&mode=driving&" +     //导航路线方式
                            "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    mContext.startActivity(intent); //启动调用
                } catch (Exception e) {
                    Logger.e("intent", e.getMessage());
                }
                break;
            case R.id.llyt_gaode:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    //将功能Scheme以URI的方式传入data
                    Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2");
                    intent.setData(uri);
                    //启动该页面即可
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Logger.e("intent", e.getMessage());
                }
                break;
            case R.id.llyt_google:
                break;
            case R.id.llyt_tengxun:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    //将功能Scheme以URI的方式传入data
                    Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to=我的目的地&tocoord=" + latitude + "," + longitude);
                    intent.setData(uri);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Logger.e("intent", e.getMessage());
                }
                break;
        }

        dismiss();
    }
}
