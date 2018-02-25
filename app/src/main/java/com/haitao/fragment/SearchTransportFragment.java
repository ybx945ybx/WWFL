package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.adapter.TransportAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 转运搜索
 */
public class SearchTransportFragment extends BaseFragment {
    Context mContext;
    private XListView                         lvList;
    private TransportAdapter                  mAdapter;
    private ArrayList<LogisticsCompanyObject> mList;
    public String keyword = "";

    private ViewGroup layoutProgress;


    private OnFavChangeListener mOnCallbackLitener;

    public void setOnCallbackLitener(OnFavChangeListener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    public boolean isEdit = false;

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        //mAdapter.isDelete = this.isEdit;
        mAdapter.notifyDataSetChanged();
    }

    public void setKeyword(String key) {
        this.keyword = key;
        if (null != mList && 0 == mList.size())
            loadData();
    }

    public void clearData() {
        if (null != mList) {
            mList.clear();
            lvList.setPullLoadEnable(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    public int getCount() {
        if (null == mList || 0 == mList.size())
            return 0;
        else
            return mList.size();
    }

    protected IViewContext<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>> storeContext = VF.<LogisticsCompanyObject>getDefault(LogisticsCompanyObject.class);

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView(LayoutInflater inflater) {
        TAG = "搜索 - 转运";
        View view = inflater.inflate(R.layout.fragment_search_transport, null);
        lvList = getView(view, R.id.lvList);
        layoutProgress = getView(view, R.id.layoutProgress);
        initError(view);
        return view;
    }


    public void initData() {
        mList = new ArrayList<LogisticsCompanyObject>();
        mAdapter = new TransportAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (!TextUtils.isEmpty(keyword) && 0 == mList.size()) {
            loadData();
        }
    }

    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                LogisticsCompanyObject logisticsCompanyObject = mList.get(index);
                if (logisticsCompanyObject != null) {
                    TransportDetailActivity.launch(mContext, logisticsCompanyObject.id);
                }
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadNext();
            }
        });
        btnRefresh.setOnClickListener(v -> loadData());
    }

    public void loadData() {
        storeContext.getPage().page = 1;
        storeContext.getEntity().keywords = keyword;
        if (mList.isEmpty()) {
            ll_common_error.setVisibility(View.GONE);
            layoutProgress.setVisibility(View.VISIBLE);
        }
        storeContext.asynQuery(MethodConstant.SEARCH_TRANSPORT, storeContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        storeContext.asynQueryNext(MethodConstant.SEARCH_TRANSPORT, storeContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<LogisticsCompanyObject> {

        @Override
        public void onSuccess(LogisticsCompanyObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {
            layoutProgress.setVisibility(View.GONE);
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (1 == storeContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == storeContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= storeContext.getPage().page) {
                    lvList.setPullLoadEnable(false);
                } else {
                    lvList.setPullLoadEnable(true);
                }
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }
            if (mList.isEmpty()) {
                ll_common_error.setVisibility(View.VISIBLE);
                setErrorType(0);
                setErrorMessage("没有相关搜索结果");
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
            if (null != mOnCallbackLitener) {
                mOnCallbackLitener.onRefresh();
            }
        }

        @Override
        public void onFailed(String error) {
            layoutProgress.setVisibility(View.GONE);
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (error.equals(TransConstant.NET_ERROR)) {
                if (mList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(1);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * 回到顶部
     */
    public void returnTop() {
        lvList.setSelection(0);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
