package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.adapter.StoreHotProductAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.HotGoodsModel;

/**
 * 商家详情 - 热品Tab
 * Created by a55 on 2017/12/7.
 */

public class StoreHotProductFragment extends BaseFragment {

    private MultipleStatusView       msv;
    private RecyclerView             lvList;
    private ArrayList<HotGoodsModel> mList;
    private StoreHotProductAdapter   mAdapter;
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
        initData();
        return messageLayout;
    }


    private void initVars() {
        TAG = "商家详情 - 热品";
        mList = new ArrayList<>();
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            mStoreId = bundle.getString(TransConstant.VALUE);

        }
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_rycv, null);
        msv = getView(view, R.id.msv);
        lvList = getView(view, R.id.content_view);
        lvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new StoreHotProductAdapter(mContext, mList);
        mAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, lvList);

        lvList.setAdapter(mAdapter);

        return view;
    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> {
            msv.showLoading();
            getData();
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> ((StoreDetailActivity)mContext).goBuy(mAdapter.getData().get(position).getJumpUrl()));
    }

    private void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().storeStoreIdHotGoodsListGet(mStoreId, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    Logger.d(response);
                    if (msv == null)
                        return;
                    msv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            if (page == 1) {
                                mAdapter.setNewData(response.getData().getRows());
                            } else {
                                mAdapter.addData(response.getData().getRows());
                            }
                            if ("1".equals(response.getData().getHasMore())) {
                                mAdapter.loadMoreComplete();
                            } else {
                                mAdapter.loadMoreEnd(true);
                            }
                        } else {
                            mAdapter.loadMoreEnd(true);
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
                });
    }

}
