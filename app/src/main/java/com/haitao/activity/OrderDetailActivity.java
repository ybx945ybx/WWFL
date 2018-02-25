package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.CommentImageAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.annotation.OrderType;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.KFUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.HtItemTextView;
import com.haitao.view.OrderLostProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.OrderDetailModel;
import io.swagger.client.model.RebateMissedOrderDetailModel;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.hv_title)          HtHeadView            mHvTitle;                   // 标题
    @BindView(R.id.gv_order_pics)     GridView              mGvOrderPics;               // 订单截图
    @BindView(R.id.ll_order_pics)     LinearLayout          mLlOrderPics;               // 订单截图容器
    @BindView(R.id.tv_order_lost_tip) TextView              mTvOrderLostTip;            // 丢单提示文本
    @BindView(R.id.pv_order_lost)     OrderLostProgressView mPvOrderLost;               // 丢单进度

    @BindView(R.id.hitv_date)                  HtItemTextView mHitvDate;
    @BindView(R.id.hitv_order_number)          HtItemTextView mHitvOrderNumber;
    @BindView(R.id.hitv_store)                 HtItemTextView mHitvStore;
    @BindView(R.id.hitv_amount)                HtItemTextView mHitvAmount;
    @BindView(R.id.hitv_status)                HtItemTextView mHitvStatus;
    @BindView(R.id.hitv_rebate_amount)         HtItemTextView mHitvRebateAmount;
    @BindView(R.id.hitv_estimated_rebate_date) HtItemTextView mHitvEstimatedRebateDate;
    @BindView(R.id.hitv_note)                  HtItemTextView mHitvNote;

    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private String mOrderId = "";

    @OrderType private int mOrderType = OrderType.ORDER_NORMAL;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, @OrderType int orderType) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.TYPE, orderType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
        loadData();
    }


    private void initVars() {
        TAG = "订单详情";
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.ID)) {
                mOrderId = intent.getStringExtra(TransConstant.ID);
            }
            if (intent.hasExtra(TransConstant.TYPE)) {
                mOrderType = intent.getIntExtra(TransConstant.TYPE, OrderType.ORDER_NORMAL);
            }
        }
        TAG = mOrderType == OrderType.ORDER_NORMAL ? getString(R.string.order_detail) : getString(R.string.order_lost_detail);
    }

    private void initViews(Bundle savedInstanceState) {
        mGvOrderPics.setOnItemClickListener((parent, view, position, id1) -> openImagePreview(position));
        mHvTitle.setOnRightClickListener(view -> KFUtils.startChat(OrderDetailActivity.this));
        mHvTitle.setCenterText(TAG);
        mHitvDate.setFocusable(true);
        mHitvDate.setFocusableInTouchMode(true);
        mHitvDate.requestFocus();
    }

    private void loadData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);

        if (mOrderType == OrderType.ORDER_NORMAL) { // 正常订单
            ForumApi.getInstance().userOrderOrderIdDetailGet(mOrderId,
                    response -> {
                        if (mHvTitle == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        if (TextUtils.equals(response.getCode(), "0")) {
                            OrderDetailModel orderDetail = response.getData();
                            if (orderDetail != null) {
                                renderView(orderDetail);
                            }
                        }
                    },
                    error -> {
                        if (mHvTitle == null)
                            return;
                        showErrorToast(error);
                        ProgressDialogUtils.dismiss();
                    });
        } else { // 丢单
            ForumApi.getInstance().userMissedOrderOrderIdDetailGet(mOrderId,
                    response -> {
                        if (mHvTitle == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        if (TextUtils.equals(response.getCode(), "0")) {
                            RebateMissedOrderDetailModel lostOrderDetail = response.getData();
                            if (lostOrderDetail != null) {
                                renderView(lostOrderDetail);
                            }
                        }
                    },
                    error -> {
                        if (mHvTitle == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        showErrorToast(error);
                    });
        }
    }

    /**
     * 普通订单显示UI
     *
     * @param orderDetail 普通订单详情
     */
    private void renderView(OrderDetailModel orderDetail) {
        // 日期
        mHitvDate.setRightText(orderDetail.getOrderTime());
        // 订单号
        mHitvOrderNumber.setRightText(orderDetail.getOrderNumber());
        // 消费金额
        mHitvAmount.setRightText(orderDetail.getCostView());
        // 返利金额
        mHitvRebateAmount.setRightText(orderDetail.getRebateView());
        // 商家
        mHitvStore.setRightText(orderDetail.getStoreName());
        // 状态
        if (!TextUtils.isEmpty(orderDetail.getStatusView())) {
            mHitvStatus.setRightText(orderDetail.getStatusView());
        } else {
            mHitvStatus.setVisibility(View.GONE);
        }
        // 预计返利日期
        if (!TextUtils.isEmpty(orderDetail.getRebateTime()) && !TextUtils.equals(orderDetail.getStatus(), "3")) {
            mHitvEstimatedRebateDate.setVisibility(View.VISIBLE);
            mHitvEstimatedRebateDate.setRightText(orderDetail.getRebateTime());
            if (TextUtils.equals(orderDetail.getStatus(), "1")) {
                mHitvEstimatedRebateDate.setTitle(getString(R.string.take_effect_time));
            }
        } else {
            mHitvEstimatedRebateDate.setVisibility(View.GONE);
        }
        // 备注
        if (!TextUtils.isEmpty(orderDetail.getComment())) {
            mHitvNote.setVisibility(View.VISIBLE);
            mHitvNote.setRightText(orderDetail.getComment());
        } else {
            mHitvNote.setVisibility(View.GONE);
        }

        // 订单截图
        List<String> orderPics = orderDetail.getPics();
        if (null != orderPics && orderPics.size() > 0) {
            mLlOrderPics.setVisibility(View.VISIBLE);
            CommentImageAdapter mAdapter = new CommentImageAdapter(mContext, orderPics);
            mGvOrderPics.setAdapter(mAdapter);
            mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.image_list = new ArrayList<>();
            mPhotoPickParameterInfo.image_list.addAll(orderPics);
        } else {
            mLlOrderPics.setVisibility(View.GONE);
        }
        mTvOrderLostTip.setVisibility(mOrderType == OrderType.ORDER_LOST ? View.VISIBLE : View.GONE);
    }

    /**
     * 丢单显示UI
     *
     * @param lostOrderDetail 丢单详情
     */
    private void renderView(RebateMissedOrderDetailModel lostOrderDetail) {
        // 丢单进度
        mPvOrderLost.setVisibility(View.VISIBLE);
        mPvOrderLost.setProgress(lostOrderDetail.getStatus());
        // 日期
        mHitvDate.setRightText(lostOrderDetail.getFeedbackTime());
        // 订单号
        mHitvOrderNumber.setRightText(lostOrderDetail.getOrderNumber());
        // 消费金额
        mHitvAmount.setRightText(lostOrderDetail.getCostView());
        // 返利金额
        mHitvRebateAmount.setRightText(lostOrderDetail.getRebateView());
        // 商家
        mHitvStore.setRightText(lostOrderDetail.getStoreName());
        // 状态
        if (!TextUtils.isEmpty(lostOrderDetail.getStatusView())) {
            mHitvStatus.setRightText(lostOrderDetail.getStatusView());
        } else {
            mHitvStatus.setVisibility(View.GONE);
        }
        // 备注
        mHitvNote.setVisibility(View.GONE);
        // 预计返利日期
        mHitvEstimatedRebateDate.setVisibility(View.GONE);
        // 订单截图
        List<String> orderPics = lostOrderDetail.getOrderPics();
        if (null != orderPics && orderPics.size() > 0) {
            mLlOrderPics.setVisibility(View.VISIBLE);
            CommentImageAdapter mAdapter = new CommentImageAdapter(mContext, orderPics);
            mGvOrderPics.setAdapter(mAdapter);
            mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.image_list = new ArrayList<>();
            mPhotoPickParameterInfo.image_list.addAll(orderPics);
        }
        mTvOrderLostTip.setVisibility(mOrderType == OrderType.ORDER_LOST ? View.VISIBLE : View.GONE);
    }

    /**
     * 图片预览
     */
    public void openImagePreview(int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }
}
