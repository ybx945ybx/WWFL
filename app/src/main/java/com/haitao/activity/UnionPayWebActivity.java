package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.dialog.NavigationDialog;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 线下返利商家位置地图
 * Created by a55 on 2018/1/2.
 */

public class UnionPayWebActivity extends BaseActivity {

    private HtHeadView  htHeadView;
    private WebView     mWebView;
    private ProgressBar mProgress;
    private String mUrl      = "";
    private String centerURL = "";
    private String latitude;
    private String longitude;
    private String title;

    private TextView         tvNavigation;
    private NavigationDialog navigationDialog;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String latitude, String longitude, String title) {
        Intent intent = new Intent(context, UnionPayWebActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra(TransConstant.TITLE, title);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay_web);

        initVars();
        initView();
        initWebView();
        //        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            title = intent.getStringExtra(TransConstant.TITLE);
        }
        Logger.d("latitude = " + latitude + ", longitude = " + longitude);
    }

    private void initView() {
        tvNavigation = getView(R.id.tv_navigation);
        tvNavigation.setOnClickListener(v -> {
            boolean isBaidushow  = isAvilible(mContext, "com.baidu.BaiduMap");
            boolean isGaodeshow  = isAvilible(mContext, "com.autonavi.minimap");
            boolean isGoogleshow = isAvilible(mContext, "com.google.android.apps.maps");
            //            boolean isTengxunshow = isAvilible(mContext, "com.baidu.BaiduMap");
            if (!isBaidushow && !isGaodeshow && !isGoogleshow) {
                ToastUtils.show(mContext, "未安装任何地图应用,无法导航");
            } else {
                if (navigationDialog == null) {
                    navigationDialog = new NavigationDialog(mContext, latitude, longitude, isBaidushow, isGaodeshow, isGoogleshow, false);
                }
                navigationDialog.show();
            }
            //            try {
            //                String uri     = "geo:" + latitude + "," + longitude + "?q=" + "";// + "&z=" + zoom;
            //                Uri    mUri    = Uri.parse(uri);
            //                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            //                startActivity(mIntent);
            //            } catch (Exception e) {
            //                ToastUtils.show(mContext, "未安装任何地图应用,无法导航");
            //            }
        });

        mWebView = getView(R.id.webView_webpage);
        htHeadView = getView(R.id.ht_headview);
        htHeadView.setCenterText(title);
    }

    private void initWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        mWebView.addJavascriptInterface(this, "android");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl(centerURL);
                tvNavigation.postDelayed(() -> tvNavigation.setVisibility(View.VISIBLE), 300);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                } else {
                    if (mProgress.getVisibility() == View.GONE)
                        mProgress.setVisibility(View.VISIBLE);
                    mProgress.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(Constant.IS_DEBUG);
        }
        mProgress = getView(R.id.progress_webpage);
    }

    /**
     * 导航
     */
    @JavascriptInterface
    public void goNavigation() {
        try {
            //            String uri     = "geo:" + lat + "," + lng + "?q=" + des;// + "&z=" + zoom;
            //            Uri    mUri    = Uri.parse(uri);
            //            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            //            startActivity(mIntent);
        } catch (Exception e) {
            ToastUtils.show(mContext, "未安装任何地图应用,无法导航");
        }
    }

    private void initData() {

        centerURL = "javascript:centerAt(" +
                latitude + "," +
                longitude + ")";
        //Wait for the page to load then send the location information

        mWebView.loadUrl("file:///android_asset/map.html");//使用自己自定义布局的webmap页面

    }

    //验证各种导航地图是否安装
    private boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
