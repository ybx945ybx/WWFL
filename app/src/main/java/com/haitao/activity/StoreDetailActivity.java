package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.CommentChangeEvent;
import com.haitao.event.LoginStateChangedEvent;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.StoreCommentFragment;
import com.haitao.fragment.StoreDetailFragment;
import com.haitao.fragment.StoreDiscountFragment;
import com.haitao.fragment.StoreHotProductFragment;
import com.haitao.fragment.StoreTopicFragment;
import com.haitao.model.JumpingPageUrlModel;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.ClearEditText;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtAdView;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.StickyNavLayout;
import com.haitao.view.dialog.ConfirmDlg;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import io.swagger.client.model.CommentSuccessIfModel;
import io.swagger.client.model.DealJumpingPageModel;
import io.swagger.client.model.StoreDetailModel;
import io.swagger.client.model.StoreProxyInfoIfModelData;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 商家详情页
 */
@MLinkRouter(keys = {"storeKey"})
public class StoreDetailActivity extends BaseActivity implements View.OnClickListener {

    private MultipleStatusView msv;
    private HtHeadView         htHeadView;
    private StickyNavLayout    layoutContent;
    private ViewGroup          layoutComment;
    private CustomImageView    ivAvator;
    private TextView           tvFav, tvBuy;
    private CustomImageView ivImage;
    private CustomImageView ivCountry;
    private TextView        tvStoreName;
    private TextView        tvCategory;
    private TextView        tvOrderNum;
    private TextView        tvRebate;
    private LinearLayout    llytAd;    // 广告布局
    private HtAdView        mHtavAd;       // 广告
    private ClearEditText   etContent;
    private ImageView       ivSend;
    private CustomImageView mImgStoreLabel; // 商家图片活动角标

    private LinearLayout llytTopView;
    private LinearLayout llytIndicator;

    private ViewPager               vpSwitch;
    private TabLayout               tabLayout;
    private BasePagerAdapter        mPagerAdapter;
    private ArrayList<BaseFragment> fragments;
    private String[]                tabs;

    private String mStoreId = "";
    private StoreDetailModel obj;

    private StoreCommentFragment mCommentFragment;
    private String               mCommentId;       // 要评论的评论Id
    int    commentCount = 0;
    //回复的用户名，临时变量
    String username     = "";
    //评论内容
    String content      = "";
    //回复评论的位置
    int commentPosition;

    private StoreProxyInfoIfModelData mPxyInfo;         // pxy信息
    private CustomImageView           mImgActivityFab;  // 全局活动入口

    private ConfirmDlg defeatDialog;

    private int position;      // 线下返利跳转过来的定位到优惠

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, StoreDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    /**
     * 线下返利过来  定位到优惠版块
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, int position) {
        Intent intent = new Intent(context, StoreDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.POSITION, position);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launchForResult(Context context, String id) {
        Intent intent = new Intent(context, StoreDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.DISMISS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.ID)) {
                mStoreId = getIntent().getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {
                // 魔窗字段
                mStoreId = getIntent().getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "storeKey");
                kv.put(TraceUtils.Event_Kv_Value, mStoreId);
                YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

            }
            position = intent.getIntExtra(TransConstant.POSITION, 0);
        }
        TAG = "商家详情";

    }

    /**
     * 初始化视图
     */
    private void initView() {
        msv = getView(R.id.msv);
        htHeadView = getView(R.id.ht_headview);
        ivImage = getView(R.id.ivImage);
        mImgStoreLabel = getView(R.id.img_store_label);
        mImgActivityFab = getView(R.id.img_event);
        ivCountry = getView(R.id.ivCountry);
        tvStoreName = getView(R.id.tvStoreName);
        tvCategory = getView(R.id.tvCategory);
        tvOrderNum = getView(R.id.tvOrderNum);
        tvRebate = getView(R.id.tvRebate);
        mHtavAd = getView(R.id.htav_ad);
        llytAd = getView(R.id.llyt_ad);
        layoutContent = getView(R.id.layoutContent);
        layoutComment = getView(R.id.layoutComment);
        ivAvator = getView(R.id.img_avatar);
        tvFav = getView(R.id.tvFav);
        tvBuy = getView(R.id.tvBuy);
        etContent = getView(R.id.etContent);
        etContent.setShowClear(false);
        ivSend = getView(R.id.iv_send);
        tabLayout = getView(R.id.tab);
        vpSwitch = getView(R.id.id_stickynavlayout_viewpager);
        llytTopView = getView(R.id.id_stickynavlayout_topview);
        llytIndicator = getView(R.id.id_stickynavlayout_indicator);
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            //            ImageLoaderUtils.showOnlineImage(HtApplication.mActivityFabImg, mImgActivityFab);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        msv.setOnRetryClickListener(v -> getData());
        htHeadView.setOnRightClickListener(view -> {
            if (obj != null) {
                String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(obj.getSharePic());
                if (TextUtils.isEmpty(obj.getSharePic())) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                        bitmap.recycle();
                    } else {
                        picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }

                ShareUtils.showShareDialog(mContext, 1, obj.getShareTitle(), obj.getShareContent(), obj.getShareContentWeibo(), obj.getShareUrl(), picUrl);

            }

        });
        ivSend.setOnClickListener(this);
        tvFav.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        etContent.setOnClickListener(v -> {
            if (!HtApplication.isLogin()) {
                etContent.clearFocus();
                QuickLoginActivity.launch(mContext);
                return;
            }

            CommentActivity.launch(mContext, username, etContent.getText().toString());

        });
        vpSwitch.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 在评论页显示底部评论，其他页不显示并隐藏软键盘
                if (position != (tabs.length - 2)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(htHeadView.getWindowToken(), 0);
                    layoutComment.setVisibility(View.GONE);
                } else {
                    layoutComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        layoutContent.setOnTopHiddenListener(status -> {
            htHeadView.setCenterText(null != obj && status ? obj.getStoreName() : "");
            htHeadView.setDividerVisible(status);
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getPxyInfo();
        getData();
    }

    private void getData() {
        msv.showLoading();
        ForumApi.getInstance().storeStoreIdStoredetailGet(mStoreId,
                response -> {
                    Logger.d(response);
                    if (msv == null)
                        return;
                    msv.showContent();

                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            obj = response.getData();
                            layoutContent.setVisibility(View.VISIBLE);
                            renderView();
                        } else {
                            showToast(ToastType.ERROR, getResources().getString(R.string.empty_tips));
                            finish();
                        }
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                });
    }

    private void renderView() {
        llytTopView.setVisibility(View.VISIBLE);
        llytIndicator.setVisibility(View.VISIBLE);
        // 下载分享图片
        ImageLoaderUtils.downloadOnlineImage(mContext, obj.getSharePic());
        // 底部评论自己的头像
        if (UserManager.getInstance().isLogin()) {
            if (TextUtils.isEmpty(UserManager.getInstance().getUser().avatar)) {
                getUserInfo();
            } else {
                ivAvator.setImageURI(UserManager.getInstance().getUser().avatar);
            }
        }
        // 商家图片
        ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), ivImage);
        // 活动角标
        if (HtApplication.isActivityOn && !TextUtils.isEmpty(HtApplication.mStoreTransportActivityLabel) && TextUtils.equals(obj.getInActivity(), "1")) {
            ImageLoaderUtils.showOnlineImage(HtApplication.mStoreTransportActivityLabel, mImgStoreLabel);
            mImgStoreLabel.setVisibility(View.VISIBLE);
        } else {
            mImgStoreLabel.setVisibility(View.GONE);
        }
        // 国旗图标
        ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), ivCountry);
        // 商家名
        tvStoreName.setText(obj.getStoreName());
        // 商家类型
        tvCategory.setText(obj.getCategoryName());
        // 有无返利
        if ("1".equals(obj.getHasRebate())) {   // 有返利
            if (!TextUtils.isEmpty(obj.getRebateView())) {
                tvRebate.setVisibility(View.VISIBLE);
                tvRebate.setText(obj.getRebateView());
            }
            // 收藏和下单人数（获得返利人数）
            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_has_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
            // 去购买按钮
            tvBuy.setText(R.string.store_buy_rebate);

        } else {                                // 无返利
            tvRebate.setVisibility(View.GONE);
            // 收藏和下单人数（获得返利人数）
            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_no_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
            // 去购买按钮
            tvBuy.setText(R.string.store_buy);

        }
        // 收藏按钮
        tvFav.setSelected("1".equals(obj.getIsCollected()));
        tvFav.setText("1".equals(obj.getIsCollected()) ? "取消收藏" : "收藏商家");
        // 广告位
        if (obj.getCrossBarPics() != null && obj.getCrossBarPics().size() > 0) {
            llytAd.setVisibility(View.VISIBLE);
            mHtavAd.setView(obj.getCrossBarPics().get(0));
        } else {
            llytAd.setVisibility(View.GONE);
        }
        // fragment信息
        fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString(TransConstant.VALUE, obj.getStoreId());

        // 详情
        StoreDetailFragment detailFragment = new StoreDetailFragment();
        Bundle              bundleDetail   = new Bundle();
        bundleDetail.putSerializable(TransConstant.OBJECT, obj);
        detailFragment.setArguments(bundleDetail);
        fragments.add(detailFragment);

        // 优惠
        StoreDiscountFragment discountFragment = new StoreDiscountFragment();
        discountFragment.setArguments(bundle);
        fragments.add(discountFragment);

        // 热品
        if ("1".equals(obj.getHotGoodSupported())) {
            tabs = new String[]{"详情", "优惠", "热品", mContext.getResources().getString(R.string.discount_comment), "晒单"};

            StoreHotProductFragment hotProductFragment = new StoreHotProductFragment();
            hotProductFragment.setArguments(bundle);
            fragments.add(hotProductFragment);

        } else {
            tabs = new String[]{"详情", "优惠", mContext.getResources().getString(R.string.discount_comment), "晒单"};

        }

        // 评论
        mCommentFragment = new StoreCommentFragment();
        mCommentFragment.setArguments(bundle);
        mCommentFragment.setOnCallbackLitener(new StoreCommentFragment.OnCallbackLitener() {
            @Override
            public void onLoaded(int commentTotal) {
                commentCount = commentTotal;
            }

            @Override
            public void onReply(String commentId, String replyUsername, int commentosition) {
                commentPosition = commentosition;
                username = replyUsername;
                mCommentId = commentId;
                Logger.d("comment id = " + mCommentId);
                setFastComment();
            }
        });
        fragments.add(mCommentFragment);

        //帖子
        //        StoreTopicFragment topicFragment = new StoreTopicFragment();
        //        Bundle             bundleTopic   = new Bundle();
        //        bundleTopic.putString(TransConstant.TYPE, "1");
        //        bundleTopic.putString(TransConstant.TITLE, obj.getStoreName());
        //        topicFragment.setArguments(bundleTopic);
        //        fragments.add(topicFragment);

        //晒单
        StoreTopicFragment shaidanFragment = new StoreTopicFragment();
        Bundle             bundleShaidan   = new Bundle();
        bundleShaidan.putString(TransConstant.TYPE, "2");
        bundleShaidan.putString(TransConstant.TITLE, obj.getStoreName());
        shaidanFragment.setArguments(bundleShaidan);
        fragments.add(shaidanFragment);

        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        vpSwitch.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(vpSwitch);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ivImage.getWindowToken(), 0);
        layoutComment.setVisibility(View.GONE);
        vpSwitch.setCurrentItem(position);
        vpSwitch.setOffscreenPageLimit(6);

        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }
    }

    /**
     * 设置回复@某人
     */
    private void setFastComment() {
        runOnUiThread(() -> {

            if (!TextUtils.isEmpty(username)) {
                etContent.setHint(String.format("回复 %s ", username));
            }
            etContent.setText(content);
            mCommentFragment.etContent.setText(content);

            //切换后将EditText光标置于末尾
            CharSequence charSequence = etContent.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }

            CommentActivity.launch(mContext, username, etContent.getText().toString());

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFav:
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                } else {
                    tvFav.setEnabled(false);
                    if (tvFav.isSelected()) {
                        delFav();
                    } else {
                        addFav();
                    }
                }
                break;
            case R.id.tvBuy:
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                } else {
                    if (obj == null) {
                        ToastUtils.show(mContext, getString(R.string.loading_now_please_wait));
                        return;
                    }
                    goBuy(obj.getJumpingUrl());
                }
                break;
            case R.id.iv_send:
                submit();
                break;
        }
    }

    /**
     * 提交评论
     */
    private void submit() {
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(mContext, R.string.store_comment_tips);
            return;
        }
        String commentContent = content;
        ForumApi.getInstance().storeStoreIdCommentPost(mStoreId, commentContent, mCommentId,
                new Response.Listener<CommentSuccessIfModel>() {
                    @Override
                    public void onResponse(CommentSuccessIfModel response) {
                        if (TextUtils.equals("0", response.getCode())) {
                            //                        ToastUtils.show(mContext, R.string.post_success);
                            StoreDetailActivity.this.showToast(ToastType.COMMON_SUCCESS, StoreDetailActivity.this.getResources().getString(R.string.post_success));
                            etContent.setText("");
                            etContent.setHint(StoreDetailActivity.this.getResources().getString(R.string.discount_comment_hint));
                            content = "";
                            username = "";
                            InputMethodManager imm = (InputMethodManager) StoreDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(htHeadView.getWindowToken(), 0);
                            mCommentFragment.updateData(response.getData(), commentPosition);
                            mCommentFragment.etContent.setText(content);
                            commentPosition = 0;

                        } else {
                            StoreDetailActivity.this.showToast(ToastType.ERROR, response.getMsg());

                            StoreDetailActivity.this.showSendDefeatDialog();

                        }
                    }
                },
                error -> {
                    showErrorToast(error);
                    Observable.timer(300, TimeUnit.MILLISECONDS)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(isFirstLauch -> {
                                showSendDefeatDialog();

                            });
                });

    }

    private void showSendDefeatDialog() {
        runOnUiThread(() -> {

            // 收起键盘
            InputMethodManager imm = (InputMethodManager) StoreDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);

            if (defeatDialog == null) {
                defeatDialog = new ConfirmDlg(mContext, getResources().getString(R.string.tips), getResources().getString(R.string.comment_send_defeat),
                        dialog -> {
                            dialog.dismiss();
                            submit();
                        },
                        dialog -> {
                            dialog.dismiss();

                        });

            }
            defeatDialog.show();
        });

    }

    /**
     * 立即去买
     */
    public void goBuy(String gourl) {
        if (obj == null) {
            ToastUtils.show(mContext, getString(R.string.loading_now_please_wait));
            return;
        }

        Bundle bundle = new Bundle();
        //        String url    = HtApplication.isLogin() ? obj.getJumpingUrl() + "&uid=" + UserManager.getInstance().getUserId() : obj.getJumpingUrl();
        String url = HtApplication.isLogin() ? gourl + "&uid=" + UserManager.getInstance().getUserId() : gourl;
        bundle.putString(TransConstant.URL, url);
        DealJumpingPageModel dealJumpingPageModel = new DealJumpingPageModel();
        dealJumpingPageModel.setStoreName(obj.getStoreName());

        dealJumpingPageModel.setRebateView(obj.getRebateView());
        dealJumpingPageModel.setRebateInstruction(obj.getRebateInstruction());
        dealJumpingPageModel.setStoreDescription(obj.getStoreDescription());
        dealJumpingPageModel.setAlipaySupported(obj.getAlipaySupported());
        dealJumpingPageModel.setCardSupported(obj.getCardSupported());
        dealJumpingPageModel.setDirectPostSupported(obj.getDirectPostSupported());
        dealJumpingPageModel.setPaypalSupported(obj.getPaypalSupported());
        dealJumpingPageModel.setTransshippingSupported(obj.getTransshippingSupported());
        dealJumpingPageModel.setMobileHasRebate(obj.getMobileHasRebate());
        dealJumpingPageModel.setBoundedAccessing(obj.getBoundedAccessing());
        //bundle.putString(TransConstant.OBJECT, JSON.toJSONString(dealJumpingPageModel));
        //        bundle.putString("share_title", obj.name);
        //        bundle.putString("share_content", obj.description);
        //        bundle.putString("share_url", obj.go_url);
        //        bundle.putString("share_pic", obj.pic);

        bundle.putString("share_title", obj.getShareTitle());
        bundle.putString("share_content", obj.getShareContent());
        bundle.putString("share_content_weibo", obj.getShareContentWeibo());
        bundle.putString("share_url", obj.getShareUrl());
        bundle.putString("share_pic", obj.getSharePic());

        bundle.putString("service_pic", obj.getStoreLogo());
        bundle.putString("service_title", obj.getStoreName());
        bundle.putString("service_url", "");
        bundle.putString("service_id", "");
        bundle.putString("service_price", obj.getRebateView());
        bundle.putString("store_name", obj.getStoreName());
        bundle.putString("store_rebate", obj.getRebateView());
        bundle.putString("store_id", obj.getStoreId());
        // 用户token
        String htToken = "";
        if (HtApplication.isLogin()) {
            htToken = UserManager.getInstance().getHtToken();
            htToken = "&token=" + htToken;
        }
        bundle.putString("store_info_url", obj.getStoreInfoUrl() + htToken);
        // 中间页
        String jumpingPageUrl = Constant.SWAGGER_URL + Constant.JUMPING_PAGE_URL;
        // pxy信息
        if (mPxyInfo != null) {
            Logger.d("发送 pxy信息");
            bundle.putSerializable("pxy_info", mPxyInfo);
        }
        // 商家信息
        JumpingPageUrlModel jumpingPageUrlModel = new JumpingPageUrlModel();
        jumpingPageUrlModel.store_id = dealJumpingPageModel.getStoreId();
        jumpingPageUrlModel.store_name = dealJumpingPageModel.getStoreName();
        jumpingPageUrlModel.store_description = dealJumpingPageModel.getStoreDescription();
        jumpingPageUrlModel.store_logo = dealJumpingPageModel.getStoreLogo();
        jumpingPageUrlModel.rebate_view = dealJumpingPageModel.getRebateView();
        jumpingPageUrlModel.rebate_instruction = dealJumpingPageModel.getRebateInstruction();
        jumpingPageUrlModel.card_supported = dealJumpingPageModel.getCardSupported();
        jumpingPageUrlModel.alipay_supported = dealJumpingPageModel.getAlipaySupported();
        jumpingPageUrlModel.paypal_supported = dealJumpingPageModel.getPaypalSupported();
        jumpingPageUrlModel.direct_post_supported = dealJumpingPageModel.getDirectPostSupported();
        jumpingPageUrlModel.transshipping_supported = dealJumpingPageModel.getTransshippingSupported();
        jumpingPageUrlModel.mobile_has_rebate = dealJumpingPageModel.getMobileHasRebate();
        jumpingPageUrlModel.bounded_accessing = dealJumpingPageModel.getBoundedAccessing();
        jumpingPageUrlModel.jump_url = dealJumpingPageModel.getJumpUrl();
        jumpingPageUrlModel.jump_delay = dealJumpingPageModel.getJumpDelay();

        Gson   gson            = new Gson();
        String jumpingPageJson = gson.toJson(jumpingPageUrlModel, JumpingPageUrlModel.class);
        try {
            jumpingPageUrl += URLEncoder.encode(jumpingPageJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        bundle.putString("jumping_page_url", jumpingPageUrl);
        DiscountWebActivity.launch(mContext, bundle);
    }

    /**
     * 获取pxy信息
     */
    private void getPxyInfo() {
        ForumApi.getInstance().storeStoreIdProxyInfoGet(mStoreId,
                response -> {
                    if ("0".equals(response.getCode())) {
                        mPxyInfo = response.getData();
                        Logger.d("获取 pxy信息\n" + mPxyInfo.toString());
                    }
                }, error -> {

                });
    }

    //收藏
    private void addFav() {
        ForumApi.getInstance().userCollectionPost(TransConstant.favType.STORE, mStoreId, "",
                response -> {
                    tvFav.setEnabled(true);
                    showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.disocunt_fav_success));
                    tvFav.setSelected(true);
                    tvFav.setText("取消收藏");
                    String collect_count = TextUtils.isEmpty(obj.getCollectionsCountView()) ? "0" : obj.getCollectionsCountView();
                    // 过万就不更新数字
                    if (!collect_count.contains("万")) {
                        int collect = Integer.valueOf(collect_count);
                        collect++;
                        obj.setCollectionsCountView(String.valueOf(collect));
                        if ("1".equals(obj.getHasRebate())) {   // 有返利
                            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_has_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
                        } else {                                // 无返利
                            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_no_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
                        }
                    }
                },
                error -> showErrorToast(error));
    }

    // 取消收藏
    private void delFav() {
        ForumApi.getInstance().userCollectionDelete(TransConstant.favType.STORE, mStoreId,
                response -> {
                    showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.disocunt_del_fav_success));
                    tvFav.setEnabled(true);
                    tvFav.setSelected(false);
                    tvFav.setText("收藏商家");
                    String collect_count = TextUtils.isEmpty(obj.getCollectionsCountView()) ? "0" : obj.getCollectionsCountView();
                    // 过万就不更新数字
                    if (!collect_count.contains("万")) {
                        int collect = Integer.valueOf(collect_count);
                        collect = collect - 1 < 0 ? 0 : collect - 1;
                        obj.setCollectionsCountView(String.valueOf(collect));
                        if ("1".equals(obj.getHasRebate())) {   // 有返利
                            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_has_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
                        } else {                                // 无返利
                            tvOrderNum.setText(String.format(getResources().getString(R.string.store_order_fav_no_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
                        }
                    }
                },
                error -> showErrorToast(error));
    }

    @Override
    public void onBackPressed() {
        if (layoutComment.getVisibility() == View.VISIBLE) {

            if (!TextUtils.isEmpty(content)) {
                etContent.setText("");
                content = "";
            } else {
                if (!TextUtils.isEmpty(username)) {
                    etContent.setText("");
                    etContent.setHint(getResources().getString(R.string.discount_comment_hint));
                    content = "";
                    username = "";
                    mCommentId = "";
                } else {
                    super.onBackPressed();
                }
            }
            if (mCommentFragment != null && mCommentFragment.etContent != null) {
                mCommentFragment.etContent.setText(content);
            }
        } else {
            super.onBackPressed();
        }

    }

    /**
     * CommentActivity返回后接收事件
     *
     * @param event
     */
    @Subscribe
    public void onCommentChange(CommentChangeEvent event) {
        content = event.content;
        etContent.setText(content);
        mCommentFragment.etContent.setText(content);
        if (event.isSubmit) {
            submit();
        }
    }

    /**
     * 登录状态改变
     *
     * @param event
     */
    @Subscribe
    public void onLoginChange(LoginStateChangedEvent event) {
        if (event.isLogin) {
            // 更新头像
            getUserInfo();
            // 更新收藏状态
            getStoreInfo();
        }
    }

    private void getStoreInfo() {
        ForumApi.getInstance().storeStoreIdStoredetailGet(mStoreId,
                response -> {
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            obj.setIsCollected(response.getData().getIsCollected());
                            // 收藏按钮
                            tvFav.setSelected("1".equals(response.getData().getIsCollected()));
                            tvFav.setText("1".equals(response.getData().getIsCollected()) ? "取消收藏" : "收藏商家");
                        }
                    }

                },
                error -> {
                });
    }

    private void getUserInfo() {
        ForumApi.getInstance().userAccountInfoGet(
                response -> {
                    if ("0".equals(response.getCode()))
                        ivAvator.setImageURI(response.getData().getAvatar());
                },
                error -> {

                });
    }
}
