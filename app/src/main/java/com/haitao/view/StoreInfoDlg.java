package com.haitao.view;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haitao.R;
import com.haitao.activity.LoginActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ToastUtils;
import com.orhanobut.logger.Logger;

/**
 * 去购买商家详情
 */

public class StoreInfoDlg extends BottomSheetDialog {
    private Context mContext;
    private WebView wbView;

    public StoreInfoDlg(@NonNull Context context) {
        super(context);
        this.mContext = context;
        initDlg(context);
    }

    private void initDlg(final Context context) {
        View layout = View.inflate(context, R.layout.dialog_store, null);
        wbView = (WebView) layout.findViewById(R.id.wbView);
        wbView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wbView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenHeight(mContext) * 0.9)));
        setContentView(layout);
    }


    public void setData(String url) {
        wbView.getSettings().setJavaScriptEnabled(true);
        wbView.setWebChromeClient(new WebChromeClient());
        wbView.getSettings().setDomStorageEnabled(true);
        wbView.addJavascriptInterface(new JavascriptInterface(mContext), "android");
        wbView.setOnTouchListener((v, ev) -> {

            if (!v.canScrollVertically(-1)) {      //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                ((WebView) v).requestDisallowInterceptTouchEvent(false);
            } else {
                ((WebView) v).requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        wbView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);

                super.onPageFinished(view, url);
                Logger.d("====loadOver===" + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("确定", (dialog, which) -> handler.proceed());
                builder.setNegativeButton("取消", (dialog, which) -> handler.cancel());
                final AlertDialog dialog = builder.create();
                dialog.show();*/
            }
        });
        //        String htToken = UserManager.getInstance().getHtToken();
        //        Logger.d("upload ht token " + htToken);
        //        wbView.loadUrl(String.format("javascript:h5Control.loadToken(\"%s\")", htToken));
        wbView.loadUrl(url);
    }

    /**
     * 登录之后传用户Token
     *
     * @param token 用户token
     */
    public void uploadToken(String token) {
        wbView.loadUrl(String.format("javascript:h5Control.loadToken(\"%s\")", token));
    }

    // js通信接口
    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void doLogin() {
            if (!HtApplication.isLogin()) {
                QuickLoginActivity.launch(mContext);
                Logger.d("doLogin");
            }
        }

        @android.webkit.JavascriptInterface
        public void toast(String text) {
            Logger.d("toast:" + text);
            //praise点赞，collect收藏,collect-cancel取消收藏
            JSONObject jsonObject = JSON.parseObject(text);
            if (!jsonObject.containsKey("type") || !jsonObject.containsKey("code") || !jsonObject.containsKey("msg"))
                return;

            doToast(jsonObject.getString("type"), jsonObject.getString("code"), jsonObject.getString("msg"));
        }
    }

    private void doToast(String type, String code, String msg) {
        /*if ("praise".equals(type)) {
            if ("0".equals(code)) {
                int praiseCount = TextUtils.isEmpty(dealExtraModel.getPraiseCount()) ? 0 : Integer.parseInt(dealExtraModel.getPraiseCount());
                tvAgree.setText(String.valueOf(praiseCount + 1));
                tvAgree.setVisibility(View.VISIBLE);
            }

        } else */
        if ("collect".equals(type)) {
            if ("0".equals(code)) {
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_DEAL_FAV);
                mIntent.putExtra(TransConstant.VALUE, "1");
                mContext.sendBroadcast(mIntent);
            }
        } else if ("collect-cancel".equals(type)) {
            if ("0".equals(code)) {
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_DEAL_FAV);
                mIntent.putExtra(TransConstant.VALUE, "0");
                mContext.sendBroadcast(mIntent);
            }
        }
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.show(mContext, msg);
        }
    }
}