package com.haitao.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtImgGrid;
import com.haitao.view.dialog.GenerateImgShareDlg;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.DealExtraModel;
import io.swagger.client.model.DealPublicityIfModelData;

/**
 * 推广赚返利
 *
 * @version v5.8.1
 * @since 2018-01-08
 */
public class PromotionForRebateActivity extends BaseActivity {

    @BindView(R.id.tv_commission_rate)                  TextView         mTvCommissionRate;
    @BindView(R.id.tv_estimate_commission_amount)       TextView         mTvEstimateCommissionAmount;
    @BindView(R.id.et_share_price)                      EditText         mEtSharePrice;
    @BindView(R.id.et_share_title)                      EditText         mEtShareTitle;
    @BindView(R.id.img_cover_1)                         CustomImageView  mImgCover1;
    @BindView(R.id.ig_cover_2)                          HtImgGrid        mIgCover2;
    @BindView(R.id.tv_generate_share_img)               TextView         mTvGenerateShareImg;
    @BindView(R.id.tv_estimate_commission_amount_label) TextView         mTvEstimateCommissionAmountLabel;
    @BindView(R.id.cl_commission)                       ConstraintLayout mClCommission;
    @BindView(R.id.cl_cover_img)                        ConstraintLayout mClCoverImg;

    @BindDrawable(R.drawable.border_rect_orange_2dp) Drawable mBgImgSelected;

    private PhotoPickParameterObject mPhotoPickParameterInfo;

    private DealExtraModel           mDealModel;
    private DealPublicityIfModelData mData;
    private String                   mLocalImg;
    private boolean                  mIsFirstImgSelected;
    private Activity                 mActivity;
    private String                   mSharePicUrl; // 分享的图片
    private GenerateImgShareDlg      mImgShareDlg;


    private String mshareTitle;       //   记录厨师标题和价格，统计是否使用自定义文案用
    private String msharePrice;
    private String mDeletePicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_for_rebate);
        ButterKnife.bind(this);

        initVars();
        initViews();
        loadData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TransConstant.OBJECT)) {
            mDealModel = (DealExtraModel) intent.getSerializableExtra(TransConstant.OBJECT);
        }
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.single_mode = true;
        mIsFirstImgSelected = true;
        mActivity = this;
    }

    private void initViews() {
        mImgCover1.setOnClickListener(v -> setFirstImgSelected(true));
        mIgCover2.setCallbackListener(new HtImgGrid.CallbackListener() {
            @Override
            public void onClose() {
                setFirstImgSelected(true);
                mLocalImg = null;
            }

            @Override
            public void onAdd() {
                photoPickWrapper();
            }

            @Override
            public void onSelected() {
                setFirstImgSelected(false);
            }
        });
        mEtShareTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDealModel.setTitle(s.toString());
                inputCheck();
            }
        });
        mEtSharePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDealModel.setPriceView(s.toString());
                inputCheck();
            }
        });
    }

    /**
     * 输入检查
     */
    private void inputCheck() {
        if (TextUtils.isEmpty(mEtSharePrice.getText().toString()) || TextUtils.isEmpty(mEtShareTitle.getText().toString())) {
            mTvGenerateShareImg.setEnabled(false);
        } else {
            mTvGenerateShareImg.setEnabled(true);
        }
    }

    private void loadData() {
        if (mDealModel == null)
            return;
        ForumApi.getInstance().dealDealIdPublicityGet(mDealModel.getDealId(),
                response -> {
                    if (mClCoverImg == null)
                        return;
                    if (response != null && TextUtils.equals(response.getCode(), "0")) {
                        mData = response.getData();
                        if (mData != null) {
                            if (!TextUtils.isEmpty(mData.getCommissionRateView())) {
                                // 返利佣金比例
                                mTvCommissionRate.setText(String.format("商家返利佣金为：%s", mData.getCommissionRateView()));
                                // 返利金额
                                mTvEstimateCommissionAmount.setText(mData.getCommissionView());
                                if (!TextUtils.isEmpty(mData.getCommissionView())) {
                                    mTvEstimateCommissionAmountLabel.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mClCommission.setVisibility(View.GONE);
                                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mClCoverImg.getLayoutParams();
                                lp.topMargin = DensityUtil.dip2px(mContext, 8);
                                mClCoverImg.setLayoutParams(lp);
                            }
                            // 标题
                            mshareTitle = mData.getTitle();
                            mEtShareTitle.setText(mData.getTitle());
                            // 价格
                            msharePrice = mData.getPriceView();
                            mEtSharePrice.setText(mData.getPriceView());
                            // 封面图
                            ImageLoaderUtils.showOnlineImage(mDealModel.getDealPic(), mImgCover1);
                            //                            setFirstImgSelected(true);
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mClCoverImg == null)
                        return;
                    showErrorToast(error);
                });
    }

    private void getBundle(Bundle bundle) {
        if (bundle != null) {
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
            ArrayList<String> list = mPhotoPickParameterInfo.image_list;
            if (list != null) {
                mLocalImg = list.get(0);
                mIgCover2.setImg(mLocalImg);
                setFirstImgSelected(false);
            }
        }
    }

    /**
     * 设置选中的图片
     *
     * @param isFirst 是否是第一个
     */
    public void setFirstImgSelected(boolean isFirst) {
        mImgCover1.setBackground(isFirst ? mBgImgSelected : null);
        mIgCover2.setImgSelected(!isFirst);
        mIsFirstImgSelected = isFirst;
    }

    /**
     * 跳转本页面
     *
     * @param context  mContext
     * @param dealData 优惠简要信息
     */

    public static void launch(Context context, DealExtraModel dealData) {
        Intent intent = new Intent(context, PromotionForRebateActivity.class);
        intent.putExtra(TransConstant.OBJECT, dealData);
        context.startActivity(intent);
    }

    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }

    /**
     * 生成分享图片
     */
    @OnClick(R.id.tv_generate_share_img)
    public void clickGenerateShareImg() {
        // 处理分享图片
        mSharePicUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(mDealModel.getSharePic());
        if (TextUtils.isEmpty(mDealModel.getSharePic())) {
            if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                mSharePicUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                bitmap.recycle();
            } else {
                mSharePicUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
            }
        }
        if (!mIsFirstImgSelected)
            TCAgent.onEvent(mContext, "优惠推广赚返利-使用自定义封面");
        if (!mshareTitle.equals(mDealModel.getTitle()) || !msharePrice.equals(mDealModel.getPriceView()))
            TCAgent.onEvent(mContext, "优惠推广赚返利-使用自定义分享文案");

        // 生成分享图片弹框
        mImgShareDlg = new GenerateImgShareDlg(mContext, 2, mDealModel, mData.getTrackLink(), mIsFirstImgSelected ? null : mLocalImg)
                .setIsPromotion(true)
                .setOnDlgClickListener(new GenerateImgShareDlg.OnDlgClickListener() {
                    @Override
                    public boolean onSaveClicked(GenerateImgShareDlg dialog) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                        if (finishActivity) {
                            mTvGenerateShareImg.postDelayed(() -> finish(), 1500);
                        }
                    }
                });
        mImgShareDlg.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(PhotoPickParameterObject.EXTRA_PARAMETER)) {
                    getBundle(data.getExtras());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(mDeletePicPath)) {
            FileUtils.deleteFile(mDeletePicPath);
            MediaScannerConnection.scanFile(mContext, new String[]{mDeletePicPath}, null, null);
        }
    }
}
