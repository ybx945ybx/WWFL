package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.adapter.TransportNewAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.TransshipperModel;

/**
 * 转运Fragment
 *
 * @author 陶声
 * @since 2017-08-11
 */
public class TransportFragment extends BaseFragment {

    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder                mUnbinder;
    private int                     mPage;
    private List<TransshipperModel> mList;
    private TransportNewAdapter     mAdapter;

    private OnFavChangeListener mOnFavChangeListener;

    public void setOnFavChangeListener(OnFavChangeListener onFavChangeListener) {
        mOnFavChangeListener = onFavChangeListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_common_list, null);
        mUnbinder = ButterKnife.bind(this, layout);
        initVars();
        initView();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        mList = new ArrayList<>();
        TAG = "我的收藏 - 转运";
    }

    private void initView() {
        mAdapter = new TransportNewAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                TransshipperModel transshipperModel = mList.get(index);
                if (transshipperModel != null) {
                    TransportDetailActivity.launch(mContext, transshipperModel.getTransshipperId());
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
        mMsv.setOnRetryClickListener(v -> initData());
    }


    public void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }


    public void loadData() {
        ForumApi.getInstance().userCollectionTransshippersListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
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
                            mMsv.showEmpty("暂时没有转运");
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
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    showErrorToast(error);
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void returnTop() {
        mLvContent.setSelection(0);
    }
}
