package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.DealAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.DealModel;

/**
 * 收藏的优惠
 *
 * @author 陶声
 * @since 2017-08-18
 */
public class FavDiscountFragment extends BaseFragment {
    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder             unbinder;
    private DealAdapter          mAdapter;
    private ArrayList<DealModel> mList;
    private int                  mPage;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_list, null);
        unbinder = ButterKnife.bind(this, view);
        initVars();
        initViews(savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initVars() {
        TAG = "我的收藏 - 优惠";
        mPage = 1;
        mList = new ArrayList<>();
        Logger.d("收藏的优惠");
    }


    private void initViews(Bundle savedInstanceState) {
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        // Adapter
        mAdapter = new DealAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);
        // Listener
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                DealModel dealModel = mList.get(index);
                if (dealModel != null) {
                    DiscountDetailActivity.launch(mContext, dealModel.getDealId());
                }
            }
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
        mMsv.setOnRetryClickListener(v -> loadData());
    }

    public void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }

    public void loadData() {
        ForumApi.getInstance().userCollectionDealsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    Logger.d(response.toString());
                    //                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有优惠");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    mMsv.showError();
                    //                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    showErrorToast(error);
                });
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        mLvContent.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
