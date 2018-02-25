package com.haitao.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.CommentChangeEvent;
import com.haitao.event.CommitFaultSuccessEvent;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.PlatformObject;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.model.UserObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.KFUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CustomImageView;
import com.haitao.view.DiscountLoginDialog;
import com.haitao.view.ObservableWebView;
import com.haitao.view.dialog.CommentReplyDialog;
import com.haitao.view.dialog.GenerateImgShareDlg;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.swagger.client.model.DealExtraModel;
import io.swagger.client.model.LoginSuccessModelData;
import io.swagger.client.model.StoreProxyInfoIfModelData;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 优惠详情
 */
@MLinkRouter(keys = {"promotionKey"})
public class DiscountDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup   layoutProgress;
    // 顶部
    private ImageButton btnShare, btnService;
    private ImageView           ivShareGain;
    // webview
    private ObservableWebView   wvBody;
    // 底部
    private LinearLayout        layoutBottom;
    private LinearLayout        llytPraise;
    private LinearLayout        llytCollect;
    private LottieAnimationView lvPraise;
    private LottieAnimationView lvCollect;
    private TextView            tvPraise;
    private TextView            tvCollect;
    private TextView            tvComment;
    private TextView            tvBuy;
    // 优惠id和详情
    private String mDiscountId = "";
    private DealExtraModel mDealExtraModel;
    // 评论相关
    private String content   = "";
    private String mUsername = "";
    private String mReplyId  = "";
    // 第三方登录信息
    PlatformObject platformObject;
    private UserObject userObject;
    // 图片查看
    PhotoPickParameterObject mPhotoPickParameterInfo;
    //搜索关键字，搜索列表传过来的
    public String keyword = "";
    // 广播
    private ChangReceiver mChangReceiver;

    private StoreProxyInfoIfModelData mPxyInfo; // pxy信息
    // 全局活动悬浮入口
    private CustomImageView           mImgActivityFab;
    // 分享相关
    private String                    mSharePicUrl;
    private GenerateImgShareDlg       mImgShareDlg; // 图片分享弹框
    private String                    mDeletePicPath;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, DiscountDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launchForResult(Context context, String id) {
        Intent intent = new Intent(context, DiscountDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.DISMISS);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, String keyword) {
        Intent intent = new Intent(context, DiscountDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.KEY, keyword);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     * 带有来源跟值
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, String sourceType, String sourceVal) {
        Intent intent = new Intent(context, DiscountDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.SOURCE, sourceType);
        intent.putExtra(TransConstant.VALUE, sourceVal);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);
        EventBus.getDefault().register(this);

        initVars();
        initView();
        initData();
        initEvent();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.ID)) {
                mDiscountId = intent.getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {// 魔窗
                mDiscountId = intent.getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "promotionKey");
                kv.put(TraceUtils.Event_Kv_Value, mDiscountId);
                YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

            }
            if (intent.hasExtra(TransConstant.KEY)) {
                keyword = intent.getStringExtra(TransConstant.KEY);
            }
        }
        TAG = "优惠详情";
        platformObject = new PlatformObject();
        userObject = new UserObject();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvTitle = getView(R.id.tvTitle);
        tvTitle.setText("");
        btnLeft = getView(R.id.btnLeft);
        btnService = getView(R.id.btnService);
        btnShare = getView(R.id.btnShare);
        ivShareGain = getView(R.id.iv_share_gain);
        layoutProgress = getView(R.id.layoutProgress);
        mImgActivityFab = getView(R.id.img_event);
        layoutProgress.setVisibility(View.VISIBLE);

        wvBody = getView(R.id.wvBody);
        wvBody.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 允许https http内容混合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvBody.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvBody.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        layoutBottom = getView(R.id.llyt_bottom);
        //赞
        llytPraise = getView(R.id.llyt_praise);
        lvPraise = getView(R.id.lv_praise);
        tvPraise = getView(R.id.tv_praise);
        //收藏
        llytCollect = getView(R.id.llyt_collect);
        lvCollect = getView(R.id.lv_collect);
        tvCollect = getView(R.id.tv_collect);
        //评论
        tvComment = getView(R.id.tv_comment);
        //去购买
        tvBuy = getView(R.id.tv_gobuy);
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        btnLeft.setOnClickListener(this);
        btnService.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        llytPraise.setOnClickListener(this);
        llytCollect.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mChangReceiver = new ChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransConstant.CHANGE_BROADCAST);
        mContext.registerReceiver(mChangReceiver, filter);
        //加载
        layoutBottom.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
        btnService.setVisibility(View.GONE);
        loadData();
    }

    /**
     * 优惠简要信息
     */
    private void loadData() {
        ForumApi.getInstance().dealDealIdBriefGet(mDiscountId, "5",
                response -> {
                    if (wvBody == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            layoutProgress.setVisibility(View.GONE);
                            mDealExtraModel = response.getData();
                            renderView();
                            logging();
                            getPxyInfo();
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                }, error -> {
                    if (wvBody == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                });
    }

    private void logging() {
        ForumApi.getInstance()
                .commonSearchingClickingLoggingPost(TextUtils.isEmpty(keyword) ? TransConstant.LogType.DEAL_CLICK : TransConstant.LogType.DEAL_SEARCH_CLICK,
                        UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserId() : "0",
                        mDealExtraModel.getDealId(), mDealExtraModel.getTitle(),
                        String.valueOf(System.currentTimeMillis()), keyword,
                        response -> {

                        }, error -> {

                        });
    }

    /**
     * 获取pxy信息
     */
    private void getPxyInfo() {
        ForumApi.getInstance().storeStoreIdProxyInfoGet(mDealExtraModel.getStoreId(),
                response -> {
                    if ("0".equals(response.getCode())) {
                        mPxyInfo = response.getData();
                        Logger.d("获取 pxy信息\n" + mPxyInfo.toString());
                    }
                }, error -> {

                });
    }


    private void renderView() {
        if (null != mDealExtraModel) {
            if (null == mPhotoPickParameterInfo)
                mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.image_list = new ArrayList<String>();
            mPhotoPickParameterInfo.link_list = new ArrayList<String>();
            Logger.d("share pic = " + mDealExtraModel.getSharePic());
            ImageLoaderUtils.downloadOnlineImage(mContext, mDealExtraModel.getSharePic());
            btnShare.setVisibility(View.VISIBLE);
            btnService.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);

            ivShareGain.setVisibility("1".equals(mDealExtraModel.getHasRebate()) ? View.VISIBLE : View.GONE);

            tvCollect.setSelected("1".equals(mDealExtraModel.getIsCollected()));
            lvCollect.setAnimation("icon_collect.json", LottieAnimationView.CacheStrategy.Strong);
            lvCollect.setProgress("1".equals(mDealExtraModel.getIsCollected()) ? 1f : 0f);
            tvCollect.setText(TextUtils.equals("1", mDealExtraModel.getIsCollected()) ? mContext.getResources().getString(R.string.disocunt_del_fav) : mContext.getResources().getString(R.string.discount_fav));

            tvPraise.setSelected("1".equals(mDealExtraModel.getIsPraised()));
            lvPraise.setAnimation("1".equals(mDealExtraModel.getIsPraised()) ? "icon_praised.json" : "icon_praise.json", LottieAnimationView.CacheStrategy.Strong);
            tvPraise.setText("0".equals(mDealExtraModel.getPraiseCount()) ? mContext.getResources().getString(R.string.praise) : mContext.getResources().getString(R.string.praise) + " " + mDealExtraModel.getPraiseCount());

            tvComment.setText("0".equals(mDealExtraModel.getCommentCount()) ? mContext.getResources().getString(R.string.my_post_comment) : mContext.getResources().getString(R.string.my_post_comment) + " " + mDealExtraModel.getCommentCount());

            tvBuy.setText(!"1".equals(mDealExtraModel.getHasRebate()) ? R.string.store_buy : R.string.store_buy_rebate);
            tvBuy.setSelected(true);

            wvBody.setHorizontalScrollBarEnabled(false);
            wvBody.setVerticalScrollBarEnabled(false);
            String url = UserManager.getInstance().isLogin() ? mDealExtraModel.getDealUrl() + "&token=" + UserManager.getInstance().getHtToken() : mDealExtraModel.getDealUrl();
            url += "&platform=android";
            wvBody.loadUrl(url);
            wvBody.getSettings().setJavaScriptEnabled(true);
            wvBody.setWebChromeClient(new WebChromeClient());
            wvBody.addJavascriptInterface(new JavascriptInterface(mContext), "android");
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            wvBody.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    if (CommonUtils.isAppUrl(url)) {
                        CommonUtils.parseUrlAndGo(mContext, url);
                    } else {
                        WebActivity.launch(mContext, getResources().getString(R.string.app_name), url);
                    }
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    view.getSettings().setJavaScriptEnabled(true);

                    super.onPageFinished(view, url);
                    Logger.d("===loadOver===" + url);
                }

            });

            showGuide();
        }
    }

    // 显示操作引导
    private void showGuide() {
        boolean discountGuideRead = (boolean) SPUtils.get(mContext, SPConstant.PROMOTION_GUIDE_ICON, false);
        if (!discountGuideRead && "1".equals(mDealExtraModel.getHasRebate())) {
            OperationGuideActivity.launch(mContext);
            SPUtils.put(mContext, SPConstant.PROMOTION_GUIDE_ICON, true);
        }
    }

    // js通信接口
    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void doLogin() {
            QuickLoginActivity.launch(mContext);
            Logger.d("doLogin");
        }

        @android.webkit.JavascriptInterface
        public void goToBoard(String boardId) {
            Logger.d("goToBoard:" + boardId);
            BoardDetailActivity.launch(mContext, boardId);
        }

        @android.webkit.JavascriptInterface
        public void showUserCenter(String uid) {
            Logger.d("showUserCenter:" + uid);
            TalentDetailActivity.launch(mContext, uid);
        }

        @android.webkit.JavascriptInterface
        public void showTag(String tagInfo) {
            Logger.d("showTag:" + tagInfo);
            JSONObject jsonObject = JSON.parseObject(tagInfo);
            if (!jsonObject.containsKey("title") || !jsonObject.containsKey("id"))
                return;
            TagDetailActivity.launch(mContext, jsonObject.getString("title"), jsonObject.getString("id"));
        }


        @android.webkit.JavascriptInterface
        public void showImglinkedByidx(String listInfo) {
            Logger.d("showImgByIdx:" + listInfo);
            JSONObject jsonObject = JSON.parseObject(listInfo);
            if (!jsonObject.containsKey("list") || !jsonObject.containsKey("idx"))
                return;

            int       position  = Integer.parseInt(jsonObject.getString("idx"));
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if (null == jsonArray || jsonArray.size() <= position)
                return;

            openImagePreview(position, jsonArray);
        }

        @android.webkit.JavascriptInterface
        public void goStore(String id) {
            Logger.d("goStore:" + id);
            StoreDetailActivity.launchForResult(mContext, id);
        }

        @android.webkit.JavascriptInterface
        public void buyNow() {
            Logger.d("buyNow:");
            goBuy("");
        }

        @android.webkit.JavascriptInterface
        public void topicDetail(String id) {
            Logger.d("topicDetail:" + id);
            TopicDetailActivity.launch(mContext, id);
        }

        @android.webkit.JavascriptInterface
        public void shareByType(String type) {
            Logger.d("shareByType:" + type);
            mSharePicUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(mDealExtraModel.getSharePic());
            if (TextUtils.isEmpty(mDealExtraModel.getSharePic())) {
                if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    mSharePicUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                    bitmap.recycle();
                } else {
                    mSharePicUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                }
            }

            //qq, weibo, wechat-circle, wechat-friend, more
            if ("qq".equals(type)) {
                ShareUtils.sharePost(context, QQ.NAME, mDealExtraModel.getShareTitle(), mDealExtraModel.getShareContent(), mDealExtraModel.getShareContentWeibo(), mDealExtraModel.getShareUrl(), mSharePicUrl, new ShareAnalyticsObject("分享_优惠", mDiscountId));
            } else if ("weibo".equals(type)) {
                ShareUtils.sharePost(context, SinaWeibo.NAME, mDealExtraModel.getShareTitle(), mDealExtraModel.getShareContent(), mDealExtraModel.getShareContentWeibo(), mDealExtraModel.getShareUrl(), mSharePicUrl, new ShareAnalyticsObject("分享_优惠", mDiscountId));
            } else if ("wechat-circle".equals(type)) {
                ShareUtils.sharePost(context, WechatMoments.NAME, mDealExtraModel.getShareTitle(), mDealExtraModel.getShareContent(), mDealExtraModel.getShareContentWeibo(), mDealExtraModel.getShareUrl(), mSharePicUrl, new ShareAnalyticsObject("分享_优惠", mDiscountId));
            } else if ("wechat-friend".equals(type)) {
                ShareUtils.sharePost(context, Wechat.NAME, mDealExtraModel.getShareTitle(), mDealExtraModel.getShareContent(), mDealExtraModel.getShareContentWeibo(), mDealExtraModel.getShareUrl(), mSharePicUrl, new ShareAnalyticsObject("分享_优惠", mDiscountId));
            } else if ("more".equals(type)) {
                Intent intentMore = new Intent();
                intentMore.setAction(Intent.ACTION_SEND);
                intentMore.putExtra(Intent.EXTRA_TEXT, mDealExtraModel.getShareUrl());
                intentMore.setType("text/plain");
                context.startActivity(Intent.createChooser(intentMore, "分享到"));
            }
        }

        @android.webkit.JavascriptInterface
        public void dealDetail(String id) {
            Logger.d("dealDetail:" + id);
            DiscountDetailActivity.launchForResult(mContext, id);
        }

        @android.webkit.JavascriptInterface
        public void squried(String link) {
            Logger.d("squried:" + link);
            JSONObject jsonObject = JSON.parseObject(link);
            if (!jsonObject.containsKey("link") || !jsonObject.containsKey("code"))
                return;
            goBuy(jsonObject.getString("link"), jsonObject.getString("code"));
        }

        @android.webkit.JavascriptInterface
        public void toast(String text) {
            Logger.d("toast:" + text);
            //praise点赞，collect收藏,collect-cancel取消收藏
            JSONObject jsonObject = JSON.parseObject(text);
            if (!jsonObject.containsKey("type") || !jsonObject.containsKey("code") || !jsonObject.containsKey("msg"))
                return;
            Message msg = new Message();
            msg.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("type", jsonObject.getString("type"));
            bundle.putString("code", jsonObject.getString("code"));
            bundle.putString("msg", jsonObject.getString("msg"));
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }

        @android.webkit.JavascriptInterface
        public void goWeb(String text) {
            Logger.d("goWeb:" + text);
            if (TextUtils.isEmpty(text))
                return;
            String title = "";
            String url   = text;
            //            http://appv6.dev.55haitao.com/template/deal/report.htm?title=报告错误&did=262566
            try {
                text = java.net.URLDecoder.decode(text, "utf-8");

                if (text.contains("title=")) {
                    int start = text.indexOf("title=");
                    text = text.substring(start);
                    if (!TextUtils.isEmpty(text)) {
                        text = text.replace("title=", "");
                        if (text.contains("&"))
                            text = text.substring(0, text.indexOf("&"));
                    }
                    if (!TextUtils.isEmpty(text)) {
                        title = text;
                    } else {
                        title = getResources().getString(R.string.app_name);
                    }
                } else {
                    title = getResources().getString(R.string.app_name);

                }

            } catch (Exception e) {

            }

            WebActivity.launch(mContext, title, url);
        }

        /**
         * 跳转转运详情
         *
         * @param id 转运Id
         */
        @android.webkit.JavascriptInterface
        public void goShipping(String id) {
            Logger.d("goShipping:" + id);
            if (TextUtils.isEmpty(id))
                return;
            TransportDetailActivity.launch(mContext, id);
        }

        /**
         * 跳转帖子
         *
         * @param info json信息
         */
        @android.webkit.JavascriptInterface
        public void goToTopic(String info) {
            JSONObject jsonObject = JSON.parseObject(info);
            String     topicId    = "", floor = "";
            if (jsonObject.containsKey(TransConstant.ID)) {
                topicId = jsonObject.getString(TransConstant.ID);
            }
            if (jsonObject.containsKey(TransConstant.FLOOR)) {
                floor = jsonObject.getString(TransConstant.FLOOR);
            }
            TopicDetailActivity.launch(mContext, topicId, floor);
        }

        /**
         * 跳转降价提醒
         *
         * @param url 降价提醒
         */
        @android.webkit.JavascriptInterface
        public void setDealRemind(String url) {
            if (HtApplication.isLogin()) {
                WebActivity.launch(mContext, true, "添加提醒", Constant.DEPRECIATE_URL + "?url=" + url);
                //                WebActivity.launch(mContext, "添加提醒", Constant.DEPRECIATE_URL + "?url=" + url, true, true);
            } else {
                QuickLoginActivity.launch(mContext);
            }
        }

        /**
         * 报告错误 获得焦点
         */
        @android.webkit.JavascriptInterface
        public void onMistakeTextFocus() {
            runOnUiThread(() -> layoutBottom.setVisibility(View.GONE));
        }

        /**
         * 报告错误 取消焦点
         */
        @android.webkit.JavascriptInterface
        public void onMistakeTextBlur() {
            runOnUiThread(() -> layoutBottom.setVisibility(View.VISIBLE));
        }

        /**
         * 点击评论，弹出评论/复制选择框
         */
        @android.webkit.JavascriptInterface
        public void replyMenu(String json) {
            JSONObject jsonObject = JSON.parseObject(json);
            // 评论Id
            if (jsonObject.containsKey(TransConstant.ID)) {
                mReplyId = jsonObject.getString(TransConstant.ID);
            }
            // 评论用户名
            if (jsonObject.containsKey(TransConstant.AUTHOR_NAME)) {
                mUsername = jsonObject.getString(TransConstant.AUTHOR_NAME);
            }
            runOnUiThread(() -> new CommentReplyDialog(mContext, true)
                    .setCommentClickListener(new CommentReplyDialog.CommentClickListener() {
                        @Override
                        public void onReply(CommentReplyDialog dialog) {
                            if (!HtApplication.isLogin()) {
                                QuickLoginActivity.launch(mContext);
                                dialog.dismiss();
                                return;
                            }

                            dialog.dismiss();
                            CommentActivity.launch(mContext, mUsername, content);

                        }

                        @Override
                        public void onCopyContent(CommentReplyDialog dialog) {
                            // 复制的内容
                            String text = "";
                            if (jsonObject.containsKey(TransConstant.TEXT)) {
                                text = jsonObject.getString(TransConstant.TEXT);
                            }

                            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(text.trim());
                            showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.comment_copy_success));
                            dialog.dismiss();
                        }
                    })
                    .show());

        }


        @android.webkit.JavascriptInterface
        public void actReply(String commentInfo) {
            Logger.d("actReply:" + commentInfo);
            JSONObject jsonObject = JSON.parseObject(commentInfo);
            if (!jsonObject.containsKey("author") || !jsonObject.containsKey("id"))
                return;
            mReplyId = jsonObject.getString("id");
            mUsername = jsonObject.getString("author");

            runOnUiThread(() -> new CommentReplyDialog(mContext, true)
                    .setCommentClickListener(new CommentReplyDialog.CommentClickListener() {
                        @Override
                        public void onReply(CommentReplyDialog dialog) {
                            if (!HtApplication.isLogin()) {
                                QuickLoginActivity.launch(mContext);
                                dialog.dismiss();
                                return;
                            }

                            dialog.dismiss();
                            CommentActivity.launch(mContext, mUsername, content);

                        }

                        @Override
                        public void onCopyContent(CommentReplyDialog dialog) {
                            // 复制的内容
                            String text = "";
                            if (jsonObject.containsKey(TransConstant.TEXT)) {
                                text = jsonObject.getString(TransConstant.TEXT);
                            }

                            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(text.trim());
                            showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.comment_copy_success));
                            dialog.dismiss();
                        }
                    })
                    .show());
        }
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //                    setFastComment();
                    break;
                case 2://toast
                    Bundle bundle = msg.getData();
                    toast(bundle.getString("type"), bundle.getString("code"), bundle.getString("msg"));
                    break;
                case 3:
                    thirdLogin();
            }
        }
    };

    private void toast(String type, String code, String msg) {

        switch (type) {
            case "copy-coupon-success":
                if ("0".equals(code)) {
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(ToastType.COMMON_SUCCESS, msg);
                    }
                } else {
                    showToast(ToastType.ERROR, msg);
                }
                break;
            default:
                if (!TextUtils.isEmpty(msg)) {
                    showToast(msg);

                }
                //            {"type":"copy-coupon-success","code":0,"msg":"折扣码复制成功"}
        }
        //        if ("praise".equals(type)) {
        //            if ("0".equals(code)) {
        //                ivAgree.setSelected(true);
        //                int praiseCount = TextUtils.isEmpty(mDealExtraModel.getPraiseCount()) ? 0 : Integer.parseInt(mDealExtraModel.getPraiseCount());
        //                tvAgree.setText(String.valueOf(praiseCount + 1));
        //                tvAgree.setVisibility(View.VISIBLE);
        //            }
        //
        //        } else if ("collect".equals(type)) {
        //            if ("0".equals(code)) {
        //                btnFav.setSelected(true);
        //                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
        //                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_DEAL_FAV);
        //                mContext.sendBroadcast(mIntent);
        //            }
        //        } else if ("collect-cancel".equals(type)) {
        //            if ("0".equals(code)) {
        //                btnFav.setSelected(false);
        //                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
        //                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_DEAL_FAV);
        //                mContext.sendBroadcast(mIntent);
        //            }
        //
        //        }

    }

    //图片预览
    public void openImagePreview(int position, JSONArray jsonArray) {
        String[] imgList  = new String[jsonArray.size()];
        String[] linkList = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            imgList[i] = object.containsKey("url") ? object.getString("url") : "";
            linkList[i] = object.containsKey("link") ? object.getString("link") : "";
        }
        mPhotoPickParameterInfo.position = position;
        mPhotoPickParameterInfo.image_list.clear();
        mPhotoPickParameterInfo.image_list.addAll(Arrays.asList(imgList));
        mPhotoPickParameterInfo.link_list.clear();
        mPhotoPickParameterInfo.link_list.addAll(Arrays.asList(linkList));
        Intent intent = new Intent();
        intent.setClass(mContext, DealPreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);

        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:     // 关闭
                finish();
                break;
            case R.id.tv_comment:  // 评论
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                wvBody.loadUrl("javascript:h5Control.goToFloor()");
                mUsername = "";
                mReplyId = "";
                CommentActivity.launch(mContext, mUsername, content);
                break;
            case R.id.btnShare:    //分享
                mSharePicUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(mDealExtraModel.getSharePic());
                if (TextUtils.isEmpty(mDealExtraModel.getSharePic())) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        mSharePicUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                        bitmap.recycle();
                    } else {
                        mSharePicUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }
                // 带推广的分享
                ShareUtils.showShareDialog(mContext, "1".equals(mDealExtraModel.getHasRebate()) ? 1 : 2, mDealExtraModel.getShareTitle(), mDealExtraModel.getShareContent(), mDealExtraModel.getShareContentWeibo(), mDealExtraModel.getShareUrl(), mSharePicUrl,
                        new OnPromotionShareClickListener() {
                            // 生成图片分享
                            @Override
                            public void onGenerateImgClick() {
                                mImgShareDlg = new GenerateImgShareDlg(mContext, 1, mDealExtraModel, mDealExtraModel.getShareUrl())
                                        .setOnDlgClickListener(new GenerateImgShareDlg.OnDlgClickListener() {
                                            @Override
                                            public boolean onSaveClicked(GenerateImgShareDlg dialog) {
                                                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(DiscountDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            REQUEST_CODE_READ_STORAGE);
                                                    return false;
                                                } else {
                                                    return true;
                                                }
                                            }

                                            @Override
                                            public void onComplete(GenerateImgShareDlg dialog, boolean finishActivity, String deletePicPath) {
                                                dialog.dismiss();
                                                mDeletePicPath = deletePicPath;
                                            }
                                        });
                                mImgShareDlg.show();
                            }

                            // 推广赚返利
                            @Override
                            public void onPromotionForRebateClick() {
                                if (HtApplication.isLogin()) {
                                    PromotionForRebateActivity.launch(mContext, mDealExtraModel);
                                } else {
                                    QuickLoginActivity.launch(mContext);
                                }
                            }
                        });

                break;
            case R.id.tv_gobuy:     // 去购买
                goBuy("");
                break;
            case R.id.llyt_praise:    // 点赞
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }

                if (tvPraise.isSelected()) {
                    lvPraise.setAnimation("icon_praised.json", LottieAnimationView.CacheStrategy.Strong);
                    lvPraise.playAnimation();
                    return;
                } else {
                    lvPraise.setAnimation("icon_praise.json", LottieAnimationView.CacheStrategy.Strong);
                    lvPraise.playAnimation();
                }
                addAgree();
                break;
            case R.id.llyt_collect:   // 收藏
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }

                if (tvCollect.isSelected()) {
                    lvCollect.setProgress(0f);
                    delFav();
                } else {
                    lvCollect.playAnimation();
                    addFav();
                }
                break;
            case R.id.btnService:   // 客服
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                KFUtils.startChat(DiscountDetailActivity.this, mDealExtraModel);
                break;
        }
    }

    /**
     * 发表/回复评论
     */
    private void submit() {
        String subContent = content;
        ForumApi.getInstance().dealDealIdCommentPost(mDiscountId, subContent, mReplyId,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        hideSoftInput();
                        layoutBottom.postDelayed(() -> showToast(ToastType.COMMON_SUCCESS, mContext.getResources().getString(R.string.post_success)), 200);
                        wvBody.loadUrl("javascript:h5Control.refreshDealComment()");
                        int commentCount = TextUtils.isEmpty(mDealExtraModel.getCommentCount()) ? 0 : Integer.parseInt(mDealExtraModel.getCommentCount());
                        mDealExtraModel.setCommentCount(String.valueOf(++commentCount));
                        tvComment.setText(mContext.getResources().getString(R.string.my_post_comment) + mDealExtraModel.getCommentCount());
                        mReplyId = "";
                        content = "";
                    } else {
                        layoutBottom.postDelayed(() -> showToast(ToastType.ERROR, response.getMsg()), 200);
                        hideSoftInput();
                    }
                }, error -> {
                    hideSoftInput();
                    layoutBottom.postDelayed(() -> showErrorToast(error), 200);
                });
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput() {
        btnLeft.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);

        }, 100);
    }

    /**
     * 添加收藏
     */
    private void addFav() {
        ForumApi.getInstance().userCollectionPost(TransConstant.favType.DEAL, mDiscountId, "",
                response -> {
                    if (wvBody == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.disocunt_fav_success));
                        tvCollect.setSelected(true);
                        tvCollect.setText(mContext.getResources().getString(R.string.disocunt_del_fav));

                        wvBody.loadUrl("javascript:h5Control.setCollect(1)");
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                }, error -> {
                    if (wvBody == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 取消收藏
     */
    private void delFav() {
        ForumApi.getInstance().userCollectionDelete(TransConstant.favType.DEAL, mDiscountId,
                response -> {
                    if (wvBody == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.disocunt_del_fav_success));
                        tvCollect.setSelected(false);
                        tvCollect.setText(mContext.getResources().getString(R.string.discount_fav));

                        wvBody.loadUrl("javascript:h5Control.setCollect(0)");
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());

                    }
                }, error -> {
                    if (wvBody == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 添加赞
     */
    private void addAgree() {
        ForumApi.getInstance().userPraisingPost(TransConstant.praiseType.DEAL, mDiscountId,
                response -> {
                    if (wvBody == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.disocunt_agree_success));
                        tvPraise.setSelected(true);
                        String praise = TextUtils.isEmpty(mDealExtraModel.getPraiseCount()) ? "0" : mDealExtraModel.getPraiseCount();
                        if (!praise.contains("万")) {
                            int praiseCount = Integer.parseInt(praise);
                            tvPraise.setText(mContext.getResources().getString(R.string.praise) + String.valueOf(praiseCount + 1));
                        }
                        wvBody.loadUrl("javascript:h5Control.setPraise()");
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                }, error -> {
                    if (wvBody == null)
                        return;
                    showErrorToast(error);
                });
    }


    /**
     * 立即去买
     */
    private void goBuy(final String goUrl, String code) {
        if ("1".equals(mDealExtraModel.getHasRebate()) && !HtApplication.isLogin()) {
            String              tipContent          = String.format(getResources().getString(R.string.buy_tips_nologin), mDealExtraModel.getStoreName(), mDealExtraModel.getRebateView().replace("返", ""));
            DiscountLoginDialog discountLoginDialog = new DiscountLoginDialog(mContext);
            discountLoginDialog.setOnItemClickLitener(new DiscountLoginDialog.OnInnerClickLitener() {
                @Override
                public void onLogin() {
                    discountLoginDialog.dismiss();
                    QuickLoginActivity.launch(mContext);
                }

                @Override
                public void onUnLogin() {
                    discountLoginDialog.dismiss();
                    goToBuy(goUrl);
                }

                @Override
                public void onThirdLogin(String type) {
                    Platform plat = ShareSDK.getPlatform(type);
                    if (TextUtils.equals(type, Wechat.NAME)) {
                        plat.SSOSetting(true);
                    }

                    if (TextUtils.equals(type, Wechat.NAME)) {
                        showProgressDialog("正在微信...");
                        userObject.platformName = "微信";
                        userObject.type = "3";
                    } else if (TextUtils.equals(type, QQ.NAME)) {
                        showProgressDialog("正在QQ...");
                        userObject.platformName = "QQ";
                        userObject.type = "2";
                    } else {
                        showProgressDialog("正在微博...");
                        userObject.platformName = "微博";
                        userObject.type = "1";
                    }

                    plat.setPlatformActionListener(new AuthPlatformActionListener());
                    plat.authorize();
                    discountLoginDialog.dismiss();
                }
            });
            discountLoginDialog.show(tipContent);
        } else if ("0".equals(mDealExtraModel.getHasRebate()) && !HtApplication.isLogin()) {
            // 无返利 未登录 跳到登录页
            QuickLoginActivity.launch(mContext);
        } else {
            goToBuy(goUrl, code);
        }
    }

    private void goBuy(String url) {
        goBuy(url, "");
    }

    private void goToBuy(String goUrl) {
        goToBuy(goUrl, "");
    }

    private void goToBuy(String goUrl, String code) {
        Bundle bundle = new Bundle();
        String url    = "";
        if (TextUtils.isEmpty(goUrl)) {
            url = HtApplication.isLogin() ? mDealExtraModel.getJumpUrl() + "&uid=" + UserManager.getInstance().getUserId() : mDealExtraModel.getJumpUrl();
        } else {
            bundle.putString(TransConstant.CODE, code);
            url = HtApplication.isLogin() ? goUrl + "&uid=" + UserManager.getInstance().getUserId() : goUrl;
        }
        if (null != mDealExtraModel.getCoupons() && mDealExtraModel.getCoupons().size() > 0) {
            bundle.putString("coupon_list", JSON.toJSONString(mDealExtraModel.getCoupons()));
        }
        bundle.putString(TransConstant.URL, url);
        bundle.putString("share_title", mDealExtraModel.getShareTitle());
        bundle.putString("share_content", mDealExtraModel.getShareContent());
        bundle.putString("share_content_weibo", mDealExtraModel.getShareContentWeibo());
        bundle.putString("share_url", mDealExtraModel.getShareUrl());
        bundle.putString("share_pic", mDealExtraModel.getSharePic());
        bundle.putString("service_pic", mDealExtraModel.getDealPic());
        bundle.putString("service_title", mDealExtraModel.getTitle());
        bundle.putString("service_url", mDealExtraModel.getShareUrl());
        bundle.putString("service_id", mDealExtraModel.getDealId());
        bundle.putString("service_price", mDealExtraModel.getPriceView());
        bundle.putString("store_name", mDealExtraModel.getStoreName());
        bundle.putString("store_rebate", mDealExtraModel.getRebateView());
        bundle.putString("discount_id", mDiscountId);
        // pxy信息
        if (mPxyInfo != null) {
            Logger.d("发送 pxy信息");
            bundle.putSerializable("pxy_info", mPxyInfo);
        }
        // 用户token
        String htToken = "";
        if (HtApplication.isLogin()) {
            htToken = UserManager.getInstance().getHtToken();
            htToken = "&token=" + htToken;
        }
        // 商家信息需要传用户token
        bundle.putString("store_info_url", mDealExtraModel.getStoreInfoUrl() + htToken);
        // 相关优惠推荐
        if (null != mDealExtraModel.getRecommendedDeals() && mDealExtraModel.getRecommendedDeals().size() > 0) {
            bundle.putString("recommend_deal", JSON.toJSONString(mDealExtraModel.getRecommendedDeals()));
        }

        // 页面埋点
        HashMap<String, String> kv = new HashMap<String, String>();
        kv.put(TraceUtils.Event_Kv_Jump_url, url);
        YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Goods_Buy, kv, "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            runOnUiThread(() -> wvBody.evaluateJavascript("javascript:h5Control.getJumpingPageInfo()", value -> {
                bundle.putString("jumping_page_url", value);
                Logger.d("jumping_page_url = " + value);

                //  跳转
                DiscountWebActivity.launch(mContext, bundle);
            }));
        }
    }

    class AuthPlatformActionListener implements PlatformActionListener {

        @Override
        public void onCancel(Platform arg0, int arg1) {

            dismissProgressDialog();
            showToast("使用" + userObject.platformName + "登录被取消");
        }

        @Override
        public void onComplete(Platform platform, int arg1,
                               HashMap<String, Object> arg2) {
            dismissProgressDialog();
            if (null != platform) {
                Logger.t("tg").d("社交平台名称:" + platform.getName());
                Logger.t("tg").d("UID:" + platform.getDb().getUserId());
                Logger.t("tg").d(platform.getDb().getUserName());
                Logger.t("tg").d("TOKEN:" + platform.getDb().getToken());
                Logger.t("tg").d(platform.getDb().getUserIcon());
                platformObject.userid = platform.getDb().getUserId();
                platformObject.token = platform.getDb().getToken();
                platformObject.nickname = platform.getDb().getUserName();
                platformObject.icon = platform.getDb().getUserIcon();
                platformObject.platname = platform.getName();
                if (!platform.getName().equals(Wechat.NAME)) {
                    thirdLogin();
                    return;
                }
            }
            if (null == arg2) {
                platform.showUser(null);//执行登录，登录后在回调里面获取用户资料
            } else {
                Logger.d(arg2.toString());
                if (arg2.containsKey("email")) {
                    platformObject.email = (String) arg2.get("email");
                }
                if (platform.getName().equals(Wechat.NAME)) {
                    Logger.d("unionid:" + (String) arg2.get("unionid"));
                    platformObject.unionid = (String) arg2.get("unionid");
                }
                thirdLogin();
            }
        }

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            Logger.d("====error");
            Logger.d(arg2.getMessage());
            dismissProgressDialog();

            String expName = arg2.getClass().getSimpleName();
            Logger.d("error:" + expName);

            if ("WechatClientNotExistException".equals(expName)
                    || "WechatTimelineNotSupportedException".equals(expName)
                    || "WechatFavoriteNotSupportedException".equals(expName)) {
                ToastUtils.show(mContext, "请安装微信客户端");
            }
        }

    }

    /**
     * 第三方登录
     */
    private void thirdLogin() {
        runOnUiThread(() -> {

            userObject.username = platformObject.nickname;
            userObject.open_id = platformObject.userid;
            userObject.open_unionid = platformObject.unionid;
            userObject.open_token = platformObject.token;
            userObject.avatar = platformObject.icon;
            Logger.d(userObject.type);
            Logger.d(userObject.open_id);
            Logger.d(userObject.open_token);
            Logger.d(userObject.open_unionid);

            showProgressDialog(R.string.login_authing);
            ForumApi.getInstance().userAccountLoginByTppTppIdPost(userObject.type, userObject.open_id, userObject.open_token, TextUtils.isEmpty(userObject.open_unionid) ? null : userObject.open_unionid,
                    response -> {
                        if (wvBody == null)
                            return;
                        dismissProgressDialog();
                        //                        Logger.d("LoginSuccessModel----》" + response.toString());
                        if ("0".equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            UserManager.getInstance().setUser(userObject);
                            TalkingDataAppCpa.onLogin(UserManager.getInstance().getUserId());
                            Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                            mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                            mContext.sendBroadcast(mIntent);

                            if (!"0".equals(response.getData().getMobileUnique().trim())) {
                                if (TextUtils.isEmpty(response.getData().getMobile())) {
                                    FirstBindPhoneActivity.launch(mContext);
                                }
                            } else {
                                FirstBindPhoneActivity.launch(mContext, 1);
                            }

                        } else if (CodeConstant.THIRD_NOT_BIND_55ACCOUNT.equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            BindActivity.launch(mContext, userObject);
                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    },
                    error -> {
                        if (wvBody == null)
                            return;
                        dismissProgressDialog();
                        showErrorToast(error);
                    });
        });
    }

    private void wrapUserObject(LoginSuccessModelData entity) {
        if (entity != null) {
            userObject.token = entity.getToken();
            userObject.ht_token = entity.getToken();
            userObject.uid = entity.getUid();
            userObject.username = entity.getUsername();
            userObject.mobile = entity.getMobile();
            userObject.mobile_unique = entity.getMobileUnique();
            userObject.refresh_token = entity.getRefreshToken();
        }
    }

    /**
     * 广播接收
     */
    private class ChangReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_LOGIN:
                            if (HtApplication.isLogin()) {
                                String htToken = UserManager.getInstance().getHtToken();
                                Logger.d("upload ht token " + htToken);
                                wvBody.loadUrl(String.format("javascript:h5Control.loadToken(\"%s\")", htToken));
                                updataBottom();
                            }
                            break;
                        case 0x1022:
                            Logger.d("get link");
                            String link = intent.getStringExtra("link");
                            goBuy(link);
                            break;
                        case TransConstant.BROAD_DEAL_FAV:
                            String value = intent.getStringExtra(TransConstant.VALUE);
                            if (!TextUtils.isEmpty(value)) {
                                tvCollect.setSelected(TextUtils.equals("1", value));
                                tvCollect.setText(TextUtils.equals("1", value) ? mContext.getResources().getString(R.string.disocunt_del_fav) : mContext.getResources().getString(R.string.discount_fav));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 登录后刷新底部栏信息
     */
    private void updataBottom() {
        ForumApi.getInstance().dealDealIdBriefGet(mDiscountId, "5",
                response -> {
                    if (wvBody == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            mDealExtraModel = response.getData();

                            tvCollect.setSelected("1".equals(mDealExtraModel.getIsCollected()));
                            lvCollect.setAnimation("icon_collect.json", LottieAnimationView.CacheStrategy.Strong);
                            lvCollect.setProgress("1".equals(mDealExtraModel.getIsCollected()) ? 1f : 0f);
                            tvCollect.setText(TextUtils.equals("1", mDealExtraModel.getIsCollected()) ? mContext.getResources().getString(R.string.disocunt_del_fav) : mContext.getResources().getString(R.string.discount_fav));

                            tvPraise.setSelected("1".equals(mDealExtraModel.getIsPraised()));
                            lvPraise.setAnimation("1".equals(mDealExtraModel.getIsPraised()) ? "icon_praised.json" : "icon_praise.json", LottieAnimationView.CacheStrategy.Strong);
                            tvPraise.setText("0".equals(mDealExtraModel.getPraiseCount()) ? mContext.getResources().getString(R.string.praise) : mContext.getResources().getString(R.string.praise) + mDealExtraModel.getPraiseCount());

                            tvComment.setText("0".equals(mDealExtraModel.getCommentCount()) ? mContext.getResources().getString(R.string.my_post_comment) : mContext.getResources().getString(R.string.my_post_comment) + mDealExtraModel.getCommentCount());

                            tvBuy.setText(!"1".equals(mDealExtraModel.getHasRebate()) ? R.string.store_buy : R.string.store_buy_rebate);
                            tvBuy.setSelected(true);
                        }
                    }
                }, error -> {
                    if (wvBody == null)
                        return;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mChangReceiver);
        if (!TextUtils.isEmpty(mDeletePicPath)) {
            FileUtils.deleteFile(mDeletePicPath);
            MediaScannerConnection.scanFile(mContext, new String[]{mDeletePicPath}, null, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.DISMISS && requestCode == resultCode) {
            setResult(TransConstant.DISMISS);
            finish();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAGTrance + "?" + "id=" + mDiscountId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意权限之后，主动保存图片
                    mImgShareDlg.resumeAction();
                    //                    ToastUtils.show(mContext, "已保存到手机相册");
                } else {
                    ToastUtils.show(mContext, "请授予读写权限，以生成图片分享");
                }
        }
    }

    public interface OnPromotionShareClickListener {
        void onGenerateImgClick();

        void onPromotionForRebateClick();
    }

    /**
     * CommentActivity返回后接收事件,提交评论
     *
     * @param event
     */
    @Subscribe
    public void onCommentChange(CommentChangeEvent event) {
        content = event.content;
        if (event.isSubmit) {
            submit();
        }
    }

    /**
     * 成功提交错误报告
     *
     * @param event
     */
    @Subscribe
    public void onCommitFaultSuccess(CommitFaultSuccessEvent event) {
        layoutBottom.postDelayed(() -> showToast(ToastType.COMMON_SUCCESS, event.msg), 300);
    }
}
