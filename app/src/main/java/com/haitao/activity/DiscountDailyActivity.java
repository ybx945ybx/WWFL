package com.haitao.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haitao.R;
import com.haitao.adapter.DiscountDailyPagerAdapter;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.QRCodeUtil;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.calendar.DateUtil;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.DealDailyModel;
import io.swagger.client.model.DealJumpingPageModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 优惠日报
 *
 * @author 陶声
 * @since 2017-09-20
 */
public class DiscountDailyActivity extends BaseActivity {

    @BindView(R.id.ht_headview)                 HtHeadView         mHtHeadview; // 标题
    @BindView(R.id.content_view)                ViewPager          mVpContent;  // 日报ViewPager
    @BindView(R.id.msv)                         MultipleStatusView mMsv;        // 多状态容器
    @BindView(R.id.rl_save_pic)                 RelativeLayout     mRlSavePic;  // 保存图片视图
    @BindView(R.id.tv_date)                     TextView           mTvDate;
    @BindView(R.id.img_discount)                CustomImageView    mImgDiscount;
    @BindView(R.id.tv_discount_name)            TextView           mTvDiscountName;
    @BindView(R.id.tv_reabte)                   TextView           mTvReabte;
    @BindView(R.id.ll_price)                    LinearLayout       mLlPrice;
    @BindView(R.id.tv_store_rebate_info)        TextView           mTvStoreRebateInfo;
    @BindView(R.id.tv_direct_transport_support) TextView           mTvDirectTransportSupport;
    @BindView(R.id.tv_alipay_support)           TextView           mTvAlipaySupport;
    @BindView(R.id.img_qr_code)                 ImageView          mImgQrCode;
    @BindView(R.id.tv_price)                    TextView           mTvPrice;
    @BindView(R.id.img_logo)                    ImageView          mImgLogo;

    @BindDimen(R.dimen.qr_code_size) int QR_CODE_SIZE;

    private int                       mPage; // 当前页数
    private List<DealDailyModel>      mDailyList;
    private DiscountDailyPagerAdapter mAdapter;
    private boolean                   mHasMoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_daily);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        initData();
    }

    /**
     * 跳转到该页面
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, DiscountDailyActivity.class));
    }

    private void initVars() {
        TAG = "优惠日报";
        mDailyList = new ArrayList<>();
        mPage = 1;
    }

    private void initViews(Bundle savedInstanceState) {
        //        mAdapter = new DiscountDailyPagerAdapter(mContext, mDailyList);
        //        mVpContent.setAdapter(mAdapter);
        mVpContent.setPageMargin(DensityUtil.dip2px(mContext, 8));
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mDailyList.size() - 1 && mHasMoreData) {
                    mPage++;
                    loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mMsv.showLoading();
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().dealDailiesListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize / 2),
                response -> {
                    if (mMsv == null)
                        return;
                    mMsv.showContent();
                    //                    mLvContent.stopRefresh();
                    //                    mLvContent.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mDailyList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mDailyList.addAll(response.getData().getRows());
                                if (mPage == 1) {
                                    mAdapter = new DiscountDailyPagerAdapter(mContext, mDailyList);
                                    mAdapter.setOnDailyClickListener(new DiscountDailyPagerAdapter.OnDailyClickListener() {
                                        @Override
                                        public void onItemClick(String id) {
                                            // 跳转优惠详情
                                            DiscountDetailActivity.launch(mContext, id);
                                        }

                                        @Override
                                        public void onSaveClick(String imgUrl) {
                                            ProgressDialogUtils.show(mContext, "保存中");
                                            showSaveViewWrapper();
                                        }

                                        @Override
                                        public void onShareClick(String shareTitle, String shareContent, String shareContentWeibo, String shareUrl, String sharePic) {

                                            if (!TextUtils.isEmpty(sharePic)) {
                                                ImageLoaderUtils.downloadOnlineImage(mContext, sharePic);
                                            }

                                            String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(sharePic);
                                            if (TextUtils.isEmpty(sharePic)) {
                                                if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                                    picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                                                    bitmap.recycle();
                                                } else {
                                                    picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                                                }
                                            }
                                            Logger.d("share_title = " + shareTitle + "\nshare_content = " + shareContent + "\nshare_url = " + shareUrl + "\nshare_img = " + sharePic);
                                            ShareUtils.showShareDialog(mContext, 1, shareTitle, shareContent, shareContentWeibo, shareUrl, picUrl);
                                        }


                                        @Override
                                        public void onBuyClick(DealDailyModel dailyModel) {
                                            if (!HtApplication.isLogin()) {
                                                QuickLoginActivity.launch(mContext);
                                            } else {
                                                goDiscountWebActivity(dailyModel);
                                            }

                                        }
                                    });
                                    mVpContent.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                            mHasMoreData = TextUtils.equals(response.getData().getHasMore(), "1");
                            //                            mLvContent.setPullLoadEnable(TextUtils.equals(response.loadData().getHasMore(), "1"));
                        }
                        if (mDailyList.isEmpty()) {
                            mMsv.showEmpty();
                        }
                        //                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                });
    }


    private void showSaveViewWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            showSaveView();
        }
    }

    /**
     * 保存图片
     */
    private void showSaveView() {
        int            currentItem = mVpContent.getCurrentItem();
        DealDailyModel data        = mDailyList.get(currentItem);
        if (data != null) {
            mRlSavePic.setVisibility(View.VISIBLE);
            // 日期
            mTvDate.setText(DateUtil.formatPhotoDate(Long.valueOf(data.getPublishTimestamp()) * 1000));
            // 优惠图
            ImageLoaderUtils.showOnlineImage(data.getDealPic(), mImgDiscount);
            // 名称
            mTvDiscountName.setText(data.getTitle());
            // 价格
            mTvPrice.setText(data.getPriceView());
            // 返利信息
            mTvStoreRebateInfo.setText(data.getStoreName() + " · " + data.getRebateInfluenceView());
            // 返利
            if (!TextUtils.isEmpty(data.getRebateView())) {
                mTvReabte.setVisibility(View.VISIBLE);
                mTvReabte.setText(data.getRebateView());
            } else {
                mTvReabte.setVisibility(View.GONE);
            }
            // 支付宝
            mTvAlipaySupport.setVisibility(TextUtils.equals(data.getAlipaySupported(), "1") ? View.VISIBLE : View.GONE);
            // 直邮
            mTvDirectTransportSupport.setVisibility(TextUtils.equals(data.getDirectPostSupported(), "1") ? View.VISIBLE : View.GONE);
            // 二维码
            Bitmap bm = QRCodeUtil.createQRCodeBitmap(data.getShareUrl(), QR_CODE_SIZE, QR_CODE_SIZE);
            mImgQrCode.setImageBitmap(bm);
            Logger.d("logo size = " + mImgLogo.getMeasuredWidth() + " qr code size = " + QR_CODE_SIZE);

            Observable.timer(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(aLong -> {
                        ProgressDialogUtils.dismiss();
                        Bitmap bmSavePic = ImageUtil.getBitmap(mRlSavePic);
                        ImageUtil.saveAsBitmap(mContext, bmSavePic, Constant.DCIM_PATH, null);
                        mRlSavePic.setVisibility(View.INVISIBLE);
                        ToastUtils.show(mContext, "已保存到手机相册");
                    });
        }
    }

    /**
     * 去购买
     *
     * @param dailyModel 跳转数据
     */
    private void goDiscountWebActivity(DealDailyModel dailyModel) {
        Bundle bundle = new Bundle();
        String url    = HtApplication.isLogin() ? dailyModel.getJumpingPage().getJumpUrl() + "&uid=" + UserManager.getInstance().getUserId() : dailyModel.getJumpingPage().getJumpUrl();
        bundle.putString(TransConstant.URL, url);
        DealJumpingPageModel dealJumpingPageModel = new DealJumpingPageModel();
        dealJumpingPageModel.setStoreName(dailyModel.getStoreName());

        //        Logger.d("cashback_view" + obj.cashback_view);

        //        if (！TextUtils.equals(dailyModel.getHasRebate(), "1") {
        //            obj.cashback_view = "";
        //        }
        //        dealJumpingPageModel.setRebateView(obj.cashback_view);
        //        dealJumpingPageModel.setRebateInstruction(obj.cashback_desc);
        //        dealJumpingPageModel.setStoreDescription(obj.description);
        //        dealJumpingPageModel.setAlipaySupported(dailyModel.getAlipaySupported());
        //        dealJumpingPageModel.setCardSupported(dailyModel.is_credit);
        //        dealJumpingPageModel.setDirectPostSupported(obj.is_direct_mail);
        //        dealJumpingPageModel.setPaypalSupported(obj.is_paypal);
        //        dealJumpingPageModel.setTransshippingSupported(obj.is_transports);
        //        dealJumpingPageModel.setMobileHasRebate(obj.is_mobile_buy);
        //        dealJumpingPageModel.setBoundedAccessing(obj.bounded_accessing);
        //bundle.putString(TransConstant.OBJECT, JSON.toJSONString(dealJumpingPageModel));
        bundle.putString("share_title", dailyModel.getShareTitle());
        bundle.putString("share_content", dailyModel.getShareContent());
        bundle.putString("share_content_weibo", dailyModel.getShareContentWeibo());
        bundle.putString("share_url", dailyModel.getShareUrl());
        bundle.putString("share_pic", dailyModel.getSharePic());
        bundle.putString("service_pic", dailyModel.getDealPic());
        bundle.putString("service_title", dailyModel.getStoreName());
        bundle.putString("service_url", "");
        bundle.putString("service_id", "");
        bundle.putString("service_price", dailyModel.getPriceView());
        bundle.putString("store_name", dailyModel.getStoreName());
        bundle.putString("store_rebate", dailyModel.getRebateView());
        bundle.putString("store_id", dailyModel.getStoreId());
        bundle.putString("deal_id", dailyModel.getDealId());
        // 用户token
        //        String htToken = "";
        //        if (HtApplication.isLogin()) {
        //            htToken = UserManager.getInstance().getHtToken();
        //            htToken = "&token=" + htToken;
        //        }
        //        bundle.putString("store_info_url", obj.store_info_url + htToken);
        // 中间页
        String jumpingPageUrl = Constant.SWAGGER_URL + Constant.JUMPING_PAGE_URL;
        // pxy信息
        //        if (mPxyInfo != null) {
        //            Logger.d("发送 pxy信息");
        //            bundle.putSerializable("pxy_info", mPxyInfo);
        //        }
        // 商家信息
        //        JumpingPageUrlModel jumpingPageUrlModel = new JumpingPageUrlModel();
        //        jumpingPageUrlModel.store_id = dealJumpingPageModel.getStoreId();
        //        jumpingPageUrlModel.store_name = dealJumpingPageModel.getStoreName();
        //        jumpingPageUrlModel.store_description = dealJumpingPageModel.getStoreDescription();
        //        jumpingPageUrlModel.store_logo = dealJumpingPageModel.getStoreLogo();
        //        jumpingPageUrlModel.rebate_view = dealJumpingPageModel.getRebateView();
        //        jumpingPageUrlModel.rebate_instruction = dealJumpingPageModel.getRebateInstruction();
        //        jumpingPageUrlModel.card_supported = dealJumpingPageModel.getCardSupported();
        //        jumpingPageUrlModel.alipay_supported = dealJumpingPageModel.getAlipaySupported();
        //        jumpingPageUrlModel.paypal_supported = dealJumpingPageModel.getPaypalSupported();
        //        jumpingPageUrlModel.direct_post_supported = dealJumpingPageModel.getDirectPostSupported();
        //        jumpingPageUrlModel.transshipping_supported = dealJumpingPageModel.getTransshippingSupported();
        //        jumpingPageUrlModel.mobile_has_rebate = dealJumpingPageModel.getMobileHasRebate();
        //        jumpingPageUrlModel.bounded_accessing = dealJumpingPageModel.getBoundedAccessing();
        //        jumpingPageUrlModel.jump_url = dealJumpingPageModel.getJumpUrl();
        //        jumpingPageUrlModel.jump_delay = dealJumpingPageModel.getJumpDelay();

        Gson   gson            = new Gson();
        String jumpingPageJson = gson.toJson(dailyModel.getJumpingPage(), DealJumpingPageModel.class);
        try {
            jumpingPageUrl += URLEncoder.encode(jumpingPageJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        bundle.putString("jumping_page_url", jumpingPageUrl);
        DiscountWebActivity.launch(mContext, bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意授权，则清除缓存
                    showSaveView();
                } else {
                    ProgressDialogUtils.dismiss();
                    ToastUtils.show(mContext, "请授予读写权限，以保存优惠");
                }
        }
    }

}
