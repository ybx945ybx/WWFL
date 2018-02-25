package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haitao.R;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.FileUtils;
import com.haitao.utils.KFUtils;
import com.haitao.utils.PxyUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.TextUtil;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CouponDialog;
import com.haitao.view.DiscountExchangeDlg;
import com.haitao.view.DiscountMoreDialog;
import com.haitao.view.RecommendDealDialog;
import com.haitao.view.dialog.RebateDeclareDlg;
import com.orhanobut.logger.Logger;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.CouponModel;
import io.swagger.client.model.CurrenciesIfModelData;
import io.swagger.client.model.DealExtraModel;
import io.swagger.client.model.DealJumpingPageModel;
import io.swagger.client.model.DealModel;
import io.swagger.client.model.ReachedStoreIfModelData;
import io.swagger.client.model.StoreProxyInfoIfModelData;

/**
 * 去购买的web页面
 */
public class DiscountWebActivity extends BaseActivity {

    @BindView(R.id.pb_web)           ProgressBar mPbWeb;              //  进度条
    @BindView(R.id.tvTitle)          TextView    tvTitle;             //  标题
    @BindView(R.id.tvHeadRebate)     TextView    tvHeadRebate;        //  返利额度
    @BindView(R.id.btnRight)         ImageButton btnRight;            //  分享
    @BindView(R.id.webView_webpage)  WebView     mWebView;            //  官网页面
    @BindView(R.id.web_jumping_page) WebView     mWebJumpingPage;     //  中间跳转页面,至少要显示3秒
    @BindView(R.id.llbottom_bbspage) ViewGroup   layoutBottom;        //  底部菜单

    private String url = "http://www.55haitao.com";
    private String mJumpingPageUrl;                                   // 中间页url信息

    // 落地页信息
    private ReachedStoreIfModelData reachedData;
    // 返利说明
    private RebateDeclareDlg        rebateDeclareDialog;
    private String                  rebateDeclare;
    // 折扣码
    private CouponDialog            couponDialog;
    private ArrayList<CouponModel>  couponList;
    // 相关优惠
    private RecommendDealDialog     recommendDealDialog;
    private ArrayList<DealModel>    recommendDealList;
    // 更多
    private DiscountMoreDialog      discountMoreDialog;
    // 汇率切换
    private DiscountExchangeDlg     mDiscountExchangeDlg;

    private DealJumpingPageModel jumpingPageModel;

    private String share_title = "", share_content = "", share_content_weibo = "", share_url = "", share_pic = "",
            service_pic        = "", service_title = "", service_url = "", service_id = "", service_price = "",
            store_name         = "", store_rebate = "";
    private String storeInfoUrl;       // 商家信息url
    private String mDiscountId;        // 优惠Id
    private String mStoreId;           // 商家Id

    private boolean isTimeout3000 = false;
    private boolean isLoadOver    = false;
    boolean loadingFinished = true;
    boolean redirect        = false;

    private StoreProxyInfoIfModelData mPxyInfo;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DiscountWebActivity.class);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.DISMISS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_web);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "购买跳转&详情页";
        couponList = new ArrayList<>();
        recommendDealList = new ArrayList<>();

        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(TransConstant.URL)) {
                url = intent.getStringExtra(TransConstant.URL);
            }
            if (bundle.containsKey(TransConstant.OBJECT)) {
                jumpingPageModel = JSON.parseObject(bundle.getString(TransConstant.OBJECT), DealJumpingPageModel.class);
            }

            if (bundle.containsKey(TransConstant.CODE) && !TextUtils.isEmpty(bundle.getString(TransConstant.CODE))) {
                CouponModel couponModel = new CouponModel();
                couponModel.setCode(bundle.getString(TransConstant.CODE));
                couponList.add(couponModel);
            }

            if (bundle.containsKey("share_title")) {
                share_title = bundle.getString("share_title");
            }
            if (bundle.containsKey("share_content")) {
                share_content = bundle.getString("share_content");
            }
            if (bundle.containsKey("share_content_weibo")) {
                share_content_weibo = bundle.getString("share_content_weibo");
            }
            if (bundle.containsKey("share_url")) {
                share_url = bundle.getString("share_url");
            }
            if (bundle.containsKey("share_pic")) {
                share_pic = bundle.getString("share_pic");
            }
            if (bundle.containsKey("service_pic")) {
                service_pic = bundle.getString("service_pic");
            }
            if (bundle.containsKey("service_title")) {
                service_title = bundle.getString("service_title");
            }
            if (bundle.containsKey("service_url")) {
                service_url = bundle.getString("service_url");
            }
            if (bundle.containsKey("service_id")) {
                service_id = bundle.getString("service_id");
            }
            if (bundle.containsKey("service_price")) {
                service_price = bundle.getString("service_price");
            }
            if (bundle.containsKey("store_name")) {
                store_name = bundle.getString("store_name");
            }
            if (bundle.containsKey("store_rebate")) {
                store_rebate = bundle.getString("store_rebate");
            }
            if (bundle.containsKey("recommend_deal")) {
                recommendDealList.addAll(JSONArray.parseArray(bundle.getString("recommend_deal"), DealModel.class));
            }
            if (bundle.containsKey("coupon_list")) {
                couponList.addAll(JSONArray.parseArray(bundle.getString("coupon_list"), CouponModel.class));
            }

            if (bundle.containsKey("store_info_url")) { // 商家信息url
                storeInfoUrl = bundle.getString("store_info_url");
                Logger.d("商家信息" + storeInfoUrl);
            } else if (bundle.containsKey("deal_id")) {
                // 优惠日报跳转的情况，不带store_info_url, 需要调用优惠简要信息接口，获取数据
                loadDealBrief(bundle.getString("deal_id"));
            }
            if (bundle.containsKey("jumping_page_url")) { // 跳转中间页url
                mJumpingPageUrl = bundle.getString("jumping_page_url");
            }
            if (bundle.containsKey("discount_id")) { // 优惠Id
                mDiscountId = bundle.getString("discount_id");
                // 优惠详情页跳转过来  获取落地页信息
                Logger.d("discount_id = " + mDiscountId + "  , type = 1");
                loadDiscountData(mDiscountId, 1);
            }
            if (bundle.containsKey("store_id")) { // 商家Id
                mStoreId = bundle.getString("store_id");
                // 商家详情页跳转过来  获取落地页信息
                Logger.d("store_id = " + mStoreId + "  , type = 2");
                loadDiscountData(mStoreId, 2);
            }
            if (bundle.containsKey("pxy_info")) { // pxy信息
                Logger.d("获取 pxy信息");
                mPxyInfo = (StoreProxyInfoIfModelData) bundle.getSerializable("pxy_info");
            }
        }
    }

    /**
     * 获取落地页简要信息
     *
     * @param id
     * @param type
     */
    private void loadDiscountData(String id, int type) {
        ForumApi.getInstance().dealReachedStoreInfoGet(id, String.valueOf(type),
                response -> {
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        reachedData = response.getData();
                        if (reachedData != null) {
                            rebateDeclare = reachedData.getStoreDescription();
                            if (reachedData.getCoupons() != null)
                                couponList = new ArrayList<>(reachedData.getCoupons());
                            if (reachedData.getRecommendedDeals() != null)
                                recommendDealList = new ArrayList<>(reachedData.getRecommendedDeals());
                            mStoreId = reachedData.getStoreId();
                        }
                    }
                },
                error -> {

                });
    }

    /**
     * 请求优惠简要信息  （ 优惠日报跳转过来的 ）
     *
     * @param dealId 优惠Id
     */
    private void loadDealBrief(String dealId) {
        ForumApi.getInstance().dealDealIdBriefGet(dealId, String.valueOf(PageConstant.halfPageSize),
                response -> {
                    if (mWebView == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        DealExtraModel data = response.getData();
                        if (null != data) {
                            Logger.d(data.toString());
                            // 商家信息url
                            storeInfoUrl = data.getStoreInfoUrl();
                            Logger.d("商家信息 = " + storeInfoUrl);
                            // 优惠券
                            couponList = (ArrayList<CouponModel>) data.getCoupons();
                            //                            Logger.d("优惠券 = " + couponList.toString());
                            // 相关优惠
                            recommendDealList = (ArrayList<DealModel>) data.getRecommendedDeals();
                            //                            Logger.d("相关优惠 = " + recommendDealList.toString());
                        }
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                error -> {
                    if (mWebView == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvTitle.setText("正在跳转");
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mPbWeb.setVisibility(View.INVISIBLE);
                } else {
                    if (mPbWeb.getVisibility() == View.INVISIBLE)
                        mPbWeb.setVisibility(View.VISIBLE);
                    mPbWeb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.d("==DiscountWeb==" + url);

                if (!loadingFinished) {
                    redirect = true;
                }
                loadingFinished = false;

                /*if (url.startsWith("http") || url.startsWith("https")){
                    view.loadUrl(url);
                    return true;
                }else {
                    redirect = false;
                    return false;
                }*/

                if (url.startsWith("http") || url.startsWith("https")) {
                    //                    view.loadUrl(url);
                } else {
                    redirect = false;
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingFinished = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!redirect) {
                    loadingFinished = true;
                    Logger.d("加载完成");
                }

                if (loadingFinished && !redirect) {
                    //HIDE LOADING IT HAS FINISHED
                    isLoadOver = true;
                    loadOver();
                } else {
                    redirect = false;
                }
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Logger.d("网页加载失败");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (!TextUtils.isEmpty(mJumpingPageUrl)) {
            mWebJumpingPage.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
            mWebJumpingPage.getSettings().setSupportZoom(false); //可以缩放
            mWebJumpingPage.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//默认缩放模式
            mWebJumpingPage.getSettings().setUseWideViewPort(true);
            mWebJumpingPage.getSettings().setLoadWithOverviewMode(true);
            mWebJumpingPage.getSettings().setJavaScriptEnabled(true);
            mWebJumpingPage.getSettings().setDomStorageEnabled(true);
            // 允许https http内容混合
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebJumpingPage.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mWebJumpingPage.setVerticalScrollBarEnabled(false);
            mWebJumpingPage.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //                    view.loadUrl(url);
                    //                    return true;
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            mWebJumpingPage.loadUrl(mJumpingPageUrl.startsWith("\"") ? mJumpingPageUrl.substring(1, mJumpingPageUrl.length() - 1) : mJumpingPageUrl);
        }

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            isTimeout3000 = true;
            loadOver();
        }, 3000);

        handler.postDelayed(() -> {
            isLoadOver = true;
            loadOver();
        }, 5000);

        if (mPxyInfo != null) {
            Logger.d("设置 pxy信息");
            PxyUtils.setPxy(mWebView, mPxyInfo.getIpAddr(), Integer.valueOf(mPxyInfo.getPort()), "com.haitao.common.HtApplication");
        }
        mWebView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        mWebView.getSettings().setSupportZoom(false); //可以缩放
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//默认缩放模式
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        // 允许https http内容混合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(url);
    }

    private void loadOver() {
        Logger.d("time out = " + isTimeout3000 + " load over  = " + isLoadOver);
        if (isTimeout3000 && isLoadOver) {
            Logger.d("load over");
            mWebJumpingPage.setVisibility(View.GONE);
            tvTitle.setText(store_name);
            tvHeadRebate.setText(store_rebate);
            tvHeadRebate.setVisibility(TextUtils.isEmpty(store_rebate) ? View.GONE : (store_rebate.contains("暂无返利") ? View.GONE : View.VISIBLE));
            btnRight.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.btnLeft, R.id.btnRight, R.id.tv_rebate_declare, R.id.tv_discount_code, R.id.tv_relate_discount, R.id.tv_service, R.id.tv_more})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:              // 关闭
                onBackPressed();
                break;
            case R.id.btnRight:             // 分享
                String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(share_pic);
                if (TextUtils.isEmpty(share_pic)) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                        bitmap.recycle();
                    } else {
                        picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }

                ShareUtils.showShareDialog(mContext, 1, share_title, share_content, share_content_weibo, share_url, picUrl);
                break;
            case R.id.tv_rebate_declare:    // 返利说明
                TCAgent.onEvent(mContext, "优惠购买-返利说明");
                if (rebateDeclareDialog == null) {
                    rebateDeclareDialog = new RebateDeclareDlg(mContext, rebateDeclare);
                }
                rebateDeclareDialog.show();
                break;
            case R.id.tv_discount_code:     // 折扣码
                TCAgent.onEvent(mContext, "优惠购买-折扣码");
                if (null == couponDialog) {
                    couponDialog = new CouponDialog(mContext, couponList);
                    couponDialog.setmOnCouponClickListener(coupon -> {
                        // 复制折扣码
                        TextUtil.Copy(coupon);
                        showToast(ToastType.COMMON_SUCCESS, mContext.getResources().getString(R.string.buy_coupon_copy_success));
                        couponDialog.dismiss();
                    });
                }
                couponDialog.show();
                break;
            case R.id.tv_relate_discount:   // 相关优惠
                TCAgent.onEvent(mContext, "优惠购买-相关优惠");
                if (null == recommendDealDialog) {
                    recommendDealDialog = new RecommendDealDialog(mContext, recommendDealList);
                }
                recommendDealDialog.show();
                break;
            case R.id.tv_service:           //  客服
                TCAgent.onEvent(mContext, "优惠购买-客服");
                if (!UserManager.getInstance().isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                tvHeadRebate.setVisibility(TextUtils.isEmpty(store_rebate) ? View.GONE : (store_rebate.contains("暂无返利") ? View.GONE : View.VISIBLE));

                if (!TextUtils.isEmpty(store_rebate) && store_rebate.contains("暂无返利")) {
                    service_price = "";
                }

                KFUtils.startChat(DiscountWebActivity.this, service_title, service_pic, service_price, service_url, service_id);
                break;
            case R.id.tv_more:              //  更多
                if (null == discountMoreDialog) {
                    discountMoreDialog = new DiscountMoreDialog(mContext, store_name);
                    discountMoreDialog.setOnInnerListener(new DiscountMoreDialog.OnInnerListener() {
                        @Override
                        public void onRefreshClick() {
                            TCAgent.onEvent(mContext, "优惠购买-刷新");
                            mWebView.loadUrl(url);
                        }

                        @Override
                        public void onExchangeClick() {
                            TCAgent.onEvent(mContext, "优惠购买-汇率换算");
                            if (null == mDiscountExchangeDlg) {
                                mDiscountExchangeDlg = new DiscountExchangeDlg(mContext);
                                mDiscountExchangeDlg.setOnDismissListener(dialogInterface -> ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DiscountWebActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS));
                            }
                            mDiscountExchangeDlg.show();
                        }

                        @Override
                        public void onBrowserClick() {
                            TCAgent.onEvent(mContext, "优惠购买-通过浏览器打开");
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            startActivity(intent);
                        }

                        @Override
                        public void onGoStoreClick() {
                            TCAgent.onEvent(mContext, "优惠购买-查看商家详情");
                            StoreDetailActivity.launch(mContext, mStoreId);

                        }
                    });
                }
                discountMoreDialog.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestCode && resultCode == TransConstant.REFRESH) {
            String type = data.getStringExtra(TransConstant.TYPE);
            if ("exchange".equals(type)) {
                String                exchange              = data.getStringExtra(TransConstant.OBJECT);
                int                   position              = data.getIntExtra("position", 0);
                CurrenciesIfModelData currenciesIfModelData = JSON.parseObject(exchange, CurrenciesIfModelData.class);
                if (null != mDiscountExchangeDlg && mDiscountExchangeDlg.isShowing()) {
                    mDiscountExchangeDlg.setChooseData(currenciesIfModelData, position);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPxyInfo != null) {
            PxyUtils.clearPxy(mWebView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
