package com.haitao.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.event.CardBindChangeEvent;
import com.haitao.event.CommitFaultSuccessEvent;
import com.haitao.event.ShareSuccessEvent;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.model.ShareObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.ToastPopuWindow;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 通用Web页
 */
@MLinkRouter(keys = {"webPageKey"})
public class WebActivity extends BaseActivity {

    private WebView     mWebView;
    private ProgressBar mProgress;

    private String mTitle = "";
    private String mUrl   = "";

    private boolean isShare      = false;
    private boolean isDepreciate = false;

    private String share_title, share_content, share_content_weibo, share_url, shareDlgTitle, shareDlgContent;
    private String share_img = "";

    // 通用吐司
    private ToastPopuWindow mPwToast;
    private boolean         clickNeedLogin;   // 在发生点击事件，并需要登录态的地方设为true   暂时只有线下返利有

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.URL, url);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String title, String url, boolean isShare) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.URL, url);
        intent.putExtra("isShare", isShare);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param mContext mContext
     */
    /*public static void launch(Context mContext, String title, String url, boolean isShare, boolean isDepreciate) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.URL, url);
        intent.putExtra("isShare", isShare);
        intent.putExtra("isDepreciate", isDepreciate);
        mContext.startActivity(intent);
    }*/

    /**
     * 从降价提醒列表页跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, boolean isDepreciate, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.URL, url);
        intent.putExtra("isDepreciate", isDepreciate);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initVars();
        initView();
        initWebView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "H5活动页";
        Intent intent = this.getIntent();
        if (intent.hasExtra(TransConstant.URL)) {
            mUrl = intent.getStringExtra(TransConstant.URL);
        } else if (intent.hasExtra(TransConstant.VALUE)) {
            // 魔窗字段
            mUrl = intent.getStringExtra(TransConstant.VALUE);

            // 页面埋点 魔窗事件
            HashMap<String, String> kv = new HashMap<String, String>();
            kv.put(TraceUtils.Event_Kv_Key, "webPageKey");
            kv.put(TraceUtils.Event_Kv_Value, mUrl);
            YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

        }
        if (intent.hasExtra(TransConstant.TITLE)) {
            mTitle = intent.getStringExtra(TransConstant.TITLE);
        }
        if (intent.hasExtra("isShare")) {
            isShare = intent.getBooleanExtra("isShare", false);
        }
        if (intent.hasExtra("isDepreciate")) {
            isDepreciate = intent.getBooleanExtra("isDepreciate", false);
        }
        EventBus.getDefault().register(this);

    }

    private void initWebView() {
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        mWebView.getSettings().setSupportZoom(false); //可以缩放
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//默认缩放模式
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 允许https http内容混合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new JavaScriptinterface(this), "android");
        mWebView.setVerticalScrollBarEnabled(false);
        tvTitle.setText(mTitle);
    }

    private void initView() {
        initTop();
//        if (isDepreciate) {
//            tvRight.setVisibility(View.VISIBLE);
//            tvRight.setText("保存");
//            tvRight.setTextColor(ContextCompat.getColor(mContext, R.color.brightOrange));
//            tvRight.setOnClickListener(v -> {
//                mWebView.loadUrl("javascript:h5Control.saveRemind()");
//            });
//        }
        mWebView = getView(R.id.webView_webpage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(Constant.IS_DEBUG);
        }
        mProgress = getView(R.id.progress_webpage);
    }

    private void initData() {
        if (isShare) {
            btnLeft.setImageResource(R.mipmap.ic_close);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(R.drawable.ic_share);
            btnRight.setOnClickListener(v -> {
                // 设置默认分享标题
                if (TextUtils.isEmpty(share_title)) {
                    share_title = mTitle;
                }
                // 设置默认分享文案
                if (TextUtils.isEmpty(share_content)) {
                    share_content = mUrl;
                }
                // 设置微博默认分享文案
                if (TextUtils.isEmpty(share_content_weibo)) {
                    share_content_weibo = mUrl;
                }
                // 设置默认分享链接
                if (TextUtils.isEmpty(share_url)) {
                    share_url = mUrl;
                }
                share_url = processShareUrl(share_url);
                // 分享图
                String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(share_img);

                Logger.d("share_title = " + share_title + "\nshare_content = " + share_content + "\nshare_content_weibo = " + share_content_weibo + "\nshare_url = " + share_url + "\nshare_img = " + share_img);

                if (TextUtils.isEmpty(share_img)) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                        bitmap.recycle();
                    } else {
                        picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }
                ShareUtils.showShareDialog(mContext, 1, share_title, share_content, share_content_weibo, share_url, picUrl, new ShareAnalyticsObject("分享_H5活动"));
            });
        }

        if (CommonUtils.isEvent(mUrl) && HtApplication.isLogin()) {
            //            mUrl += "&token=" + UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
            mUrl = processWebUrl(mUrl);
            mWebView.loadUrl(mUrl);
        } else if (CommonUtils.isEventLogin(mUrl)) {
            if (!HtApplication.isLogin()) {
                new AlertDialog.Builder(mContext)
                        .setMessage("登录了才能继续")
                        .setPositiveButton(R.string.login, (dialog, which) -> {
                            dialog.dismiss();
                            QuickLoginActivity.launch(mContext);
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        }).show();

            } else {
                //                mUrl += "&token=" + UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
                mUrl = processWebUrl(mUrl);
                mWebView.loadUrl(mUrl);
            }
        } else {
            //            mUrl += (HtApplication.isLogin() ? "&token=" + UserManager.getInstance().getHtToken() : "") + "&platform=android&fromapp=1";
            mUrl = processWebUrl(mUrl);
            mWebView.loadUrl(mUrl);
        }
        Logger.d("url = " + mUrl);
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @android.webkit.JavascriptInterface
        public void initShare(String ssss) {
            Logger.d("init share: " + ssss);
            ShareObject shareObject;
            try {
                shareObject = JSON.parseObject(ssss, ShareObject.class);
                if (null != shareObject) {
                    btnRight.setVisibility(View.VISIBLE);
                    share_title = shareObject.title;
                    share_content = shareObject.desc;
                    share_content_weibo = shareObject.desc_weibo;
                    share_url = processShareUrl(shareObject.link);
                    share_img = shareObject.imgUrl;
                    Logger.d("share_title = " + share_title + "\nshare_content = " + share_content + "\nshare_content_weibo = " + share_content_weibo + "\nshare_url = " + share_url + "\nshare_img = " + share_img);
                    if (!TextUtils.isEmpty(shareObject.imgUrl)) {
                        ImageLoaderUtils.downloadOnlineImage(mContext, shareObject.imgUrl);
                    }
                }
            } catch (Exception e) {

            }
        }

        /**
         * 带文本分享
         */
        @android.webkit.JavascriptInterface
        public void shareByHtmlInternal(String json) {
            ShareObject shareObject;
            try {
                shareObject = JSON.parseObject(json, ShareObject.class);
                if (null != shareObject) {
                    share_title = shareObject.title;
                    share_content = shareObject.desc;
                    share_content_weibo = shareObject.desc_weibo;
                    share_url = processShareUrl(shareObject.link);
                    share_img = shareObject.imgUrl;
                    Logger.d("share_title = " + share_title + "\nshare_content = " + share_content + "\nshare_content_weibo = " + share_content_weibo + "\nshare_url = " + share_url + "\nshare_img = " + share_img);

                    shareDlgTitle = shareObject.remarkFirst;
                    shareDlgContent = shareObject.remarkTwo;
                    if (!TextUtils.isEmpty(shareObject.imgUrl)) {
                        ImageLoaderUtils.downloadOnlineImage(mContext, shareObject.imgUrl);
                    }

                    String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(share_img);
                    if (TextUtils.isEmpty(share_img)) {
                        if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                            picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                            bitmap.recycle();
                        } else {
                            picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                        }
                    }

                    ShareUtils.showShareDialog(mContext, 1, share_title, share_content, share_content_weibo, share_url, picUrl, new ShareAnalyticsObject("分享_H5活动"), shareDlgTitle, shareDlgContent);

                }
            } catch (Exception e) {

            }
        }

        /**
         * 隐藏分享按钮
         */
        @android.webkit.JavascriptInterface
        public void shareBtnHidden() {
            Logger.d("share hidden");
            runOnUiThread(() -> btnRight.setVisibility(View.GONE));
        }

        /**
         * 登录
         */
        @android.webkit.JavascriptInterface
        public void doLogin() {
            QuickLoginActivity.launch(mContext);
            Logger.d("doLogin");
        }

        /**
         * 绑定手机
         */
        @android.webkit.JavascriptInterface
        public void goSetMobile() {
            //            BindPhoneActivity.launch(mContext, BindPhoneActivity.BING_NONE);
            FirstBindPhoneActivity.launch(mContext);
            Logger.d("doLogin");
        }

        /**
         * 保存成功
         */
        @android.webkit.JavascriptInterface
        public void finishedRemindSaving(String jsonText) {
            finish();
        }

        /**
         * 回到上一页
         */
        @android.webkit.JavascriptInterface
        public void prevView() {
            finish();
        }

        @android.webkit.JavascriptInterface
        public void toast(String text) {
            Logger.d("toast:" + text);
            //praise点赞，collect收藏,collect-cancel取消收藏
            JSONObject jsonObject = JSON.parseObject(text);
            if (!jsonObject.containsKey("type") || !jsonObject.containsKey("code") || !jsonObject.containsKey("msg"))
                return;
            showToast(jsonObject.getString("type"), jsonObject.getString("code"), jsonObject.getString("msg"));
            //            mPwToast = new ToastPopuWindow(WebActivity.this, jsonObject.getString("msg"));
            //            mPwToast.show();
            // 失败情况，返回上一页
            /*if (jsonObject.containsKey("type")) {
                String type = jsonObject.getString("type");
                if (!TextUtils.isEmpty(type) && type.contains("load-remind-failed")) {
                    finish();
                }
            }*/
        }

        /////   线下返利部分接口     /////

        /**
         * 添加银行卡
         */
        @android.webkit.JavascriptInterface
        public void addBankCard() {
            if (!HtApplication.isLogin()) {
                clickNeedLogin = true;
                QuickLoginActivity.launch(mContext);
                return;
            }
            BindBankCardActivity.launch(mContext);
        }

        /**
         * 查看更多商家
         */
        @android.webkit.JavascriptInterface
        public void findMoreBus() {
            UnionPayShopListActivity.launch(mContext);
        }

        /**
         * 立即绑卡
         */
        @android.webkit.JavascriptInterface
        public void bindCardNow() {
            if (!HtApplication.isLogin()) {
                clickNeedLogin = true;
                QuickLoginActivity.launch(mContext);
                return;
            }
            BindBankCardActivity.launch(mContext);
        }

        /**
         * 查看商家详情
         */
        @android.webkit.JavascriptInterface
        public void nineBus(String shopId) {
            UnionPayShopDetailActivity.launch(mContext, shopId);
        }

    }

    private void initEvent() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (CommonUtils.isEventLogin(url)) {
                    if (!HtApplication.isLogin()) {
                        new AlertDialog.Builder(mContext)
                                .setMessage("登录了才能继续")
                                .setPositiveButton(R.string.login, (dialog, which) -> {
                                    dialog.dismiss();
                                    QuickLoginActivity.launch(mContext);
                                })
                                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                                    dialog.dismiss();
                                }).show();

                    } else {
                        mUrl = url + "&token=" + UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
                        view.loadUrl(mUrl);
                    }
                } else if (CommonUtils.isAppUrl(url)) {
                    CommonUtils.parseUrlAndGo(mContext, url);
                } else {
                    mUrl = url;
                    view.loadUrl(mUrl);
                }
                Logger.d(mUrl);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(mTitle)) {
                    tvTitle.setText(title);
                    mTitle = title;
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                // 增加Javascript异常监控
                //                CrashReport.setJavascriptMonitor(view, true);

                mProgress.setProgress(progress);
                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                }

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
    }

    private void showToast(String type, String code, String msg) {
        runOnUiThread(() -> {
            switch (type) {
                case "report-mistake-succeed":
                    EventBus.getDefault().post(new CommitFaultSuccessEvent(msg));
//                    mPwToast = new ToastPopuWindow(WebActivity.this, ToastType.COMMON_SUCCESS, msg);
//                    mPwToast.show();
                    break;
                default:
                    mPwToast = new ToastPopuWindow(WebActivity.this, msg);
                    mPwToast.show();
            }
        });

    }

    /**
     * 处理分享链接
     *
     * @param shareUrl 分享链接
     * @return 去除fromapp和platform字段之后的链接
     */
    public String processShareUrl(String shareUrl) {
        share_url = shareUrl.replaceAll("&platform=android", "");
        return share_url.replaceAll("&fromapp=1", "");
    }

    /**
     * 处理页面url
     *
     * @param webUrl 页面url
     * @return 去除fromapp和platform字段之后的链接
     */
    public String processWebUrl(String webUrl) {
        if (!webUrl.contains("?")) {
            webUrl += "?";
        } else {
            webUrl += "&";
        }
        if (UserManager.getInstance().isLogin()) {
            webUrl += "token=" + UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
        } else {
            webUrl += "platform=android&fromapp=1";
        }
        //        if (!webUrl.contains("?")) {
        //            webUrl += "?token=";
        //        } else {
        //            webUrl += "&token=";
        //        }
        //        webUrl += UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
        return webUrl;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                btnRight.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onShareSuccessEvent(ShareSuccessEvent event) {
        Logger.d("接收 ShareSuccessEvent 事件");
        mWebView.loadUrl(String.format("javascript:h5Control.navBarShareBtnClick(\"%s\")", event.platform));
    }

    /**
     * 线下返利
     * 绑卡后刷新界面
     *
     * @param event
     */
    @Subscribe
    public void onCardBindChangeEvent(CardBindChangeEvent event) {
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                Logger.d("finish webview");
                if (!clickNeedLogin) {
                    finish();
                } else {
                    clickNeedLogin = false;
                }
            } else {
                initData();
            }
        }
    }

    @Override
    protected void onPause() {
        //        mWebView.reload();
        super.onPause();
        mWebView.onPause();
        //        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mWebView = null;
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
        super.onDestroy();
    }

}
