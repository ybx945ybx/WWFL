package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.DealAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.DealModel;

/**
 * 商家详情 - 优惠Tab
 */
public class StoreDiscountFragment extends BaseFragment {
    private MultipleStatusView   msv;
    private XListView            lvList;
    private ArrayList<DealModel> mList;
    private DealAdapter          mAdapter;
    private int page = 1;
    private String mStoreId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        mList = new ArrayList<>();
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            mStoreId = bundle.getString(TransConstant.VALUE);
        }
    }

    private View initView(LayoutInflater inflater) {
        TAG = "商家详情 - 优惠";
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);
        msv = getView(view, R.id.msv);
        lvList = getView(view, R.id.content_view);
        mAdapter = new DealAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(false);

        return view;
    }


    public void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().storeStoreIdStoreDealsListGet(mStoreId, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    Logger.d(response.getData());
                    if (msv == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    msv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == page) {
                            mList.clear();
                        }
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mList.addAll(response.getData().getRows());

                            Logger.d(mList.toString());

                            lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            lvList.setPullLoadEnable(false);
                        }
                        if (mList.isEmpty()) {
                            msv.showEmpty("暂无相关优惠");

                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());

                    }
                },
                error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> {
            msv.showLoading();
            getData();
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                DealModel discountObject = mList.get(index);
                if (discountObject != null) {
                    DiscountDetailActivity.launch(mContext, discountObject.getDealId());
                }
            }
        });

    }

}
