package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.adapter.PhotoUploadPickAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.OrderObject;
import com.haitao.model.PhotoImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.StoreObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.camera.Bimp;
import com.haitao.view.ClearEditText;
import com.haitao.view.FullGirdView;
import com.haitao.view.HtHeadView;
import com.haitao.view.HtItemView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.VisitedStoreRecordModel;

/**
 * 订单丢单反馈
 */
public class OrderLostFeedbackActivity extends BaseActivity {

    @BindView(R.id.hv_title)               HtHeadView    mHvTitle;         // 标题
    @BindView(R.id.et_order_number)        ClearEditText mEtOrderNumber;   // 订单号
    @BindView(R.id.et_amount)              ClearEditText mEtAmount;        // 消费金额
    @BindView(R.id.et_store_email)         ClearEditText mEtStoreEmail;    // 邮箱输入框
    @BindView(R.id.ll_store_email)         LinearLayout  mLlStoreEmail;    // 邮箱容器
    @BindView(R.id.gv_order_pics)          FullGirdView  mGvOrderPics;     // 图片list
    @BindView(R.id.item_choose_store_time) HtItemView    mItemChooseStoreTime;  // 商家&时间

    private ArrayList<PhotoImageObject> mList;
    private PhotoUploadPickAdapter      mAdapter;
    private PhotoPickParameterObject    mPhotoPickParameterInfo;


    private StoreObject storeObj = null;
    private String      time     = "", mOrderId = "", mAmount = "";

    private String mMail = "";
    //    private List<String> mPicBase64List;
    private String[] mPicsBase64;

    protected IViewContext<OrderObject, IEntityService<OrderObject>> commandViewContext = VF.<OrderObject>getDefault(OrderObject.class);
    private VisitedStoreRecordModel mStoreRecord;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, OrderLostFeedbackActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_feedback);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "丢单反馈";
        mPicsBase64 = new String[]{"", "", ""};
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHvTitle.setOnRightClickListener(view -> clickSubmit());
        mLlStoreEmail.setVisibility(View.GONE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mGvOrderPics.setOnItemClickListener((parent, view, position, id) -> {
            if (mList.size() < mAdapter.maxSize && position == mAdapter.getCount() - 1) {
                //                PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
                photoPickWrapper();
            } else {
                openImagePreview(position);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.max_image = 3;
        mList = new ArrayList<PhotoImageObject>();
        mAdapter = new PhotoUploadPickAdapter(mContext, mList);
        mAdapter.maxSize = mPhotoPickParameterInfo.max_image;
        mGvOrderPics.setAdapter(mAdapter);
    }


    private void getBundle(Bundle bundle) {
        if (bundle != null) {
            mList.clear();
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
            ArrayList<String> list = mPhotoPickParameterInfo.image_list;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    PhotoImageObject img = new PhotoImageObject();
                    Bitmap           bm;
                    try {
                        bm = Bimp.revitionImageSize(list.get(i));
                        img.source_image = FileUtils.saveBitmap(bm);
                        //                        mPicBase64List.add(getPicBase64(img.source_image));
                        //                        mPicBase64List.get(i) = getPicBase64(img.source_image);
                        mPicsBase64[i] = getPicBase64(img.source_image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mList.add(img);
                }
            }
            //            Logger.d(mPicBase64List.toString());
            mAdapter.notifyDataSetChanged();
        }
    }


    private void clickSubmit() {
        mOrderId = mEtOrderNumber.getText().toString().trim();
        mAmount = mEtAmount.getText().toString().trim();
        mMail = mEtStoreEmail.getText().toString().trim();
        if (null == mStoreRecord) {
            ToastUtils.show(mContext, R.string.toast_choose_store_time);
            return;
        }
        if (TextUtils.isEmpty(mOrderId)) {
            ToastUtils.show(mContext, R.string.order_feedback_order_hint);
            return;
        }
        if (TextUtils.isEmpty(mAmount)) {
            ToastUtils.show(mContext, R.string.order_feedback_money_hint);
            return;
        }
        if (mList.size() <= 0) {
            ToastUtils.show(mContext, R.string.order_image_tips);
            return;
        }
        if (TextUtils.isEmpty(mMail) && mLlStoreEmail.getVisibility() == View.VISIBLE) {
            ToastUtils.show(mContext, R.string.order_feedback_email_hint);
            return;
        }
        submit();
    }

    private void submit() {
        ProgressDialogUtils.show(mContext, R.string.operationg);

        ForumApi.getInstance().userRebateMissedOrderPost(mStoreRecord.getStoreId(), mOrderId, mAmount, mStoreRecord.getVisitTime(), mMail,
                mPicsBase64[0], mPicsBase64[1], mPicsBase64[2],
                response -> {
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        SuccessFeedbackActivity.launch(mContext, SuccessFeedbackActivity.SuccessType.ORDER_LOST);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }


    /**
     * 选择商家
     */
    @OnClick(R.id.item_choose_store_time)
    public void onClickChooseStoreTime() {
        StoreTimeChooseActivity.launch(mContext, mStoreRecord == null ? "" : mStoreRecord.getStoreId(), mStoreRecord == null ? "" : mStoreRecord.getVisitTime());
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
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            if (null != data && data.hasExtra(TransConstant.OBJECT)) {
                mStoreRecord = (VisitedStoreRecordModel) data.getSerializableExtra(TransConstant.OBJECT);
                // 商家 + 日期
                mItemChooseStoreTime.setContent(mStoreRecord.getStoreName() + " · " + mStoreRecord.getVisitTime());
                // 是否需要填写邮箱
                mLlStoreEmail.setVisibility(TextUtils.equals(mStoreRecord.getNeedsMail(), "1") ? View.VISIBLE : View.GONE);
            }
        } else if (requestCode == TransConstant.SUCCESS) {
            setResult(TransConstant.REFRESH);
            finish();
        }
    }

    /**
     * 获取图片Base64编码
     *
     * @param pic 图片本地url
     * @return 图片Base64编码
     */
    private String getPicBase64(String pic) {
        Bitmap bmPic = ImageUtil.getBitmap(pic);
        return ImageUtil.compressImageBase64(bmPic);
    }

    //图片预览
    public void openImagePreview(int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        intent.putExtras(b);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }

    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }
}
