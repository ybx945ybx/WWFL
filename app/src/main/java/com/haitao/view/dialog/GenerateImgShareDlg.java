package com.haitao.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.common.Constant.Constant;
import com.haitao.common.annotation.ToastType;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.QRCodeUtil;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;
import com.tendcloud.tenddata.TCAgent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.swagger.client.model.DealExtraModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 生成图片分享-Dialog
 *
 * @author 陶声
 * @since 2018-01-08
 */

public class GenerateImgShareDlg extends Dialog {

    @BindView(R.id.img_cover)          CustomImageView  mImgCover;
    @BindView(R.id.tv_deal_title)      TextView         mTvDealTitle;
    @BindView(R.id.tv_deal_price)      TextView         mTvDealPrice;
    @BindView(R.id.img_qr_code)        ImageView        mImgQrCode;
    @BindView(R.id.tv_tag_rebate)      TextView         mTvTagRebate;
    @BindView(R.id.tv_tag_direct_post) TextView         mTvTagDirectPost;
    @BindView(R.id.tv_tag_alipay)      TextView         mTvTagAlipay;
    @BindView(R.id.tv_tag_country)     TextView         mTvTagCountry;
    @BindView(R.id.tv_store_name)      TextView         mTvStoreName;
    @BindView(R.id.cl_dlg_content)     ConstraintLayout mClDlgContent;
    @BindView(R.id.img_logo)           ImageView        mImgLogo;
    @BindView(R.id.img_qr_logo)        ImageView        mImgQrLogo;
    @BindView(R.id.ic_save_pic)        TextView         mIcSavePic;

    @BindDimen(R.dimen.qr_code_size_70) int QR_CODE_SIZE;

    private OnDlgClickListener mOnDlgClickListener;
    private Context            mContext;
    private DealExtraModel     mDealExtraModel;
    private int                source;             //  用于统计 1是优惠生成图片2是优惠推广赚返利

    private int mLastClickedId;

    public GenerateImgShareDlg(@NonNull Context context, int source, DealExtraModel dealExtraModel, String link) {
        super(context);
        initDlg(context, source, dealExtraModel, link, null);
    }

    public GenerateImgShareDlg(@NonNull Context context, int source, DealExtraModel dealExtraModel, String link, String localImg) {
        super(context);
        initDlg(context, source, dealExtraModel, link, localImg);
    }

    /**
     * 初始化
     *
     * @param context        mContext
     * @param dealExtraModel 优惠简要信息
     * @param link           链接
     * @param localImg       本地图片
     */
    private void initDlg(final Context context, int source, DealExtraModel dealExtraModel, String link, String localImg) {
        mContext = context;
        mDealExtraModel = dealExtraModel;
        this.source = source;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            // 顶部透明
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View layout   = LayoutInflater.from(context).inflate(R.layout.dlg_generate_img_share, null);
        int  dlgWidth = ScreenUtils.getScreenWidth(context) - DensityUtil.dip2px(context, 48);
        setContentView(layout, new ConstraintLayout.LayoutParams(dlgWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this);

        // 封面图
        if (!TextUtils.isEmpty(localImg)) {
            ImageLoaderUtils.showLocationImage(localImg, mImgCover, R.mipmap.ic_default_240);
        } else {
            ImageLoaderUtils.showOnlineImage(mDealExtraModel.getDealPic(), mImgCover);
        }
        // 商家
        mTvStoreName.setText(mDealExtraModel.getStoreName());
        // 标题
        mTvDealTitle.setText(mDealExtraModel.getTitle());
        // 价格信息
        mTvDealPrice.setText(mDealExtraModel.getPriceView());
        // 返利信息
        boolean hasRebate = TextUtils.equals(mDealExtraModel.getHasRebate(), "1");
        mTvTagRebate.setVisibility(hasRebate ? View.VISIBLE : View.GONE);
        if (hasRebate) {
            mTvTagRebate.setText(mDealExtraModel.getRebateView());
        }
        // 直邮
        mTvTagDirectPost.setVisibility(TextUtils.equals(mDealExtraModel.getDirectPostSupported(), "1") ? View.VISIBLE : View.GONE);
        // 支付宝
        mTvTagAlipay.setVisibility(TextUtils.equals(mDealExtraModel.getAlipaySupported(), "1") ? View.VISIBLE : View.GONE);
        // 国家
        mTvTagCountry.setText(mDealExtraModel.getCountryName());
        // 二维码
        Bitmap bm = QRCodeUtil.createQRCodeBitmap(link, QR_CODE_SIZE, QR_CODE_SIZE);
        mImgQrCode.setImageBitmap(bm);
        Logger.d("logo size = " + mImgQrCode.getMeasuredWidth() + " qr code size = " + QR_CODE_SIZE);
    }

    /**
     * 设置是否是推广赚翻返利
     *
     * @param isPromotion 是否是推广赚翻返利
     * @return this
     */
    public GenerateImgShareDlg setIsPromotion(boolean isPromotion) {
        // 隐藏3个元素
        mImgLogo.setVisibility(View.GONE);
        mImgQrLogo.setVisibility(View.GONE);
        mTvTagRebate.setVisibility(View.GONE);
        return this;
    }

    /**
     * 关闭Dialog
     */
    @OnClick(R.id.img_close)
    void clickClose() {
        dismiss();
    }

    /**
     * 保存图片方法
     *
     * @return 图片url
     */
    private String savePic() {
        Bitmap bmSavePic = ImageUtil.getBitmap(mClDlgContent);
        String picUrl    = ImageUtil.saveAsBitmap(mContext, bmSavePic, Constant.DCIM_PATH, null);
        bmSavePic.recycle();
        return picUrl;
    }

    /**
     * 继续上一次的操作
     */
    public void resumeAction() {
        //        mIcSavePic.performClick();
        if (mLastClickedId != 0) {
            findViewById(mLastClickedId).performClick();
            mLastClickedId = 0;
        }
    }

    /**
     * 保存 & 分享
     */
    @OnClick({R.id.ic_save_pic, R.id.ic_qq, R.id.ic_weibo, R.id.ic_wechat, R.id.ic_moments})
    public void onShareClicked(View view) {
        if (mOnDlgClickListener != null) {
            if (mOnDlgClickListener.onSaveClicked(this)) {
                String shareImg = savePic();
                String platform = Wechat.NAME;
                switch (view.getId()) {
                    case R.id.ic_save_pic:
                        if (source == 1) {
                            TCAgent.onEvent(mContext, "优惠生成图片-保存");
                        } else {
                            TCAgent.onEvent(mContext, "优惠推广赚返利-生成图片—保存");
                        }
                        ((BaseActivity) mContext).showToast(ToastType.COMMON_SUCCESS, "已保存到手机相册");
                        mOnDlgClickListener.onComplete(this, true, null);
                        return;
                    case R.id.ic_qq:
                        if (source == 1) {
                            TCAgent.onEvent(mContext, "优惠生成图片-QQ");
                        } else {
                            TCAgent.onEvent(mContext, "优惠推广赚返利-生成图片—QQ");
                        }
                        platform = QQ.NAME;
                        break;
                    case R.id.ic_weibo:
                        if (source == 1) {
                            TCAgent.onEvent(mContext, "优惠生成图片-微博");
                        } else {
                            TCAgent.onEvent(mContext, "优惠推广赚返利-生成图片—微博");
                        }
                        platform = SinaWeibo.NAME;
                        break;
                    case R.id.ic_wechat:
                        if (source == 1) {
                            TCAgent.onEvent(mContext, "优惠生成图片-微信");
                        } else {
                            TCAgent.onEvent(mContext, "优惠推广赚返利-生成图片—微信");
                        }
                        platform = Wechat.NAME;
                        break;
                    case R.id.ic_moments:
                        if (source == 1) {
                            TCAgent.onEvent(mContext, "优惠生成图片-朋友圈");
                        } else {
                            TCAgent.onEvent(mContext, "优惠推广赚返利-生成图片—朋友圈");
                        }
                        platform = WechatMoments.NAME;
                        break;
                }
                String finalPlatform = platform;
                Observable.timer(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycleAndroid.bindActivity(((RxAppCompatActivity) mContext).lifecycle()))
                        .subscribe(aLong -> {
                            // 图片分享
                            ShareUtils.sharePost(mContext, finalPlatform, "", "", "", "", shareImg, new ShareAnalyticsObject("分享_图片分享"), new PlatformActionListener() {
                                @Override
                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                    ((BaseActivity) mContext).showToast(ToastType.COMMON_SUCCESS, "分享成功");
                                    mOnDlgClickListener.onComplete(GenerateImgShareDlg.this, true, shareImg);
                                }

                                @Override
                                public void onError(Platform platform, int i, Throwable throwable) {
                                    ToastUtils.show(mContext, "分享失败");
                                    mOnDlgClickListener.onComplete(GenerateImgShareDlg.this, false, shareImg);
//                                    MediaScannerConnection.scanFile(mContext, new String[]{shareImg}, null, null);
                                }

                                @Override
                                public void onCancel(Platform platform, int i) {
                                    ToastUtils.show(mContext, "分享取消");
                                    mOnDlgClickListener.onComplete(GenerateImgShareDlg.this, false, shareImg);
//                                    MediaScannerConnection.scanFile(mContext, new String[]{shareImg}, null, null);
                                }
                            });
                        });
            } else {
                mLastClickedId = view.getId();
            }
        }
    }

    public interface OnDlgClickListener {
        boolean onSaveClicked(GenerateImgShareDlg dialog);

        void onComplete(GenerateImgShareDlg dialog, boolean finishActivity, String deleteFilePath);
    }

    public GenerateImgShareDlg setOnDlgClickListener(OnDlgClickListener listener) {
        mOnDlgClickListener = listener;
        return this;
    }
}
