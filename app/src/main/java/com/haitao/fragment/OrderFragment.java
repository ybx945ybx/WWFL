package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.OrderDetailActivity;
import com.haitao.adapter.OrderAdapter;
import com.haitao.adapter.OrderLostAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.annotation.OrderType;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.OrderModel;
import io.swagger.client.model.RebateMissedOrderModel;

/**
 * 订单Fragment
 */
public class OrderFragment extends BaseFragment {

    @OrderType int mOrderType;

    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder                          unbinder;
    private ArrayList<OrderModel>             mNormalOrderList;
    private ArrayList<RebateMissedOrderModel> mLostOrderList;
    private OrderAdapter                      mNormalOrderAdapter;
    private OrderLostAdapter                  mLostOrderAdapter;

    private String mStatus;     // 订单状态
    private String mStatusName; // 订单状态名
    private int    mPage;       // 当前页数

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_list, null);
        unbinder = ButterKnife.bind(this, view);
        initVars();
        initViews(savedInstanceState);
        initEvent();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initVars() {
        if (null != getArguments()) {
            if (getArguments().containsKey(TransConstant.STATUS)) {
                mStatus = getArguments().getString(TransConstant.STATUS);
            }
            if (getArguments().containsKey(TransConstant.TYPE)) {
                mOrderType = getArguments().getInt(TransConstant.TYPE);
            }
            if (getArguments().containsKey(TransConstant.VALUE)) {
                mStatusName = getArguments().getString(TransConstant.VALUE, "");
            }
        }
        TAG = mOrderType == OrderType.ORDER_NORMAL ? "我的订单" : "丢单追踪" + " - " + mStatusName;
        mPage = 1;
        mNormalOrderList = new ArrayList<>();
        mLostOrderList = new ArrayList<>();
    }


    private void initViews(Bundle savedInstanceState) {
        mLvContent.setAutoLoadEnable(true);
    }


    private void initEvent() {
        // 断网重新加载
        mMsv.setOnRetryClickListener(v -> {
            mMsv.showLoading();
            loadData();
        });
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadData();
            }
        });
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                if (mOrderType == OrderType.ORDER_NORMAL && mNormalOrderList.get(index) != null) {
                    OrderDetailActivity.launch(mContext, mNormalOrderList.get(index).getOrderId());
                } else if (mOrderType == OrderType.ORDER_LOST && mLostOrderList.get(index) != null) {
                    OrderDetailActivity.launch(mContext, mLostOrderList.get(index).getOrderId());
                }
            }
        });
    }

    public void initData() {
        if (mMsv == null)
            return;
        mMsv.showLoading();

        if (mOrderType == OrderType.ORDER_NORMAL) { // 正常订单
            mNormalOrderAdapter = new OrderAdapter(mContext, mNormalOrderList);
            mLvContent.setAdapter(mNormalOrderAdapter);
        } else { // 丢单
            mLostOrderAdapter = new OrderLostAdapter(mContext, mLostOrderList);
            mLvContent.setAdapter(mLostOrderAdapter);
        }
        loadData();
    }

    private void loadData() {
        if (mOrderType == OrderType.ORDER_NORMAL) { // 正常订单
            loadNormalOrderList();
        } else { // 丢单
            loadLostOrderList();
        }
    }

    /**
     * 加载正常订单列表
     */
    private void loadNormalOrderList() {
        ForumApi.getInstance().userOrdersListGet(mStatus, String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mNormalOrderList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mNormalOrderList.addAll(response.getData().getRows());
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mNormalOrderList.isEmpty()) {
                            mMsv.showEmpty("暂时没有订单");
                        }
                        mNormalOrderAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    mMsv.showError();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });
    }

    /**
     * 加载丢单列表
     */
    private void loadLostOrderList() {
        ForumApi.getInstance().userRebateMissedOrdersListGet(mStatus, String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mLostOrderList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mLostOrderList.addAll(response.getData().getRows());
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mLostOrderList.isEmpty()) {
                            mMsv.showEmpty("暂时没有丢单");
                        }
                        mLostOrderAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    mMsv.showError();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
