package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.adapter.StoreSearchAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.model.StoreObject;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 搜索商家
 */
public class SearchStoreFragment extends BaseFragment {
    private XListView              lvList;
    private StoreSearchAdapter     mSearchStoreAdapter;
    private ArrayList<StoreObject> mSearchList;

    public String keyword = "";

    public static final int MY_FAV = 1;//我收藏的
    public static final int SEARCH = 2;//搜索

    private int type = 2;

    private ViewGroup layoutProgress;

    private OnFavChangeListener mOnCallbackLitener;
    protected IViewContext<StoreObject, IEntityService<StoreObject>> storeContext = VF.<StoreObject>getDefault(StoreObject.class);

    private int mPage;

    public void setOnCallbackLitener(OnFavChangeListener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    /*public boolean isEdit = false;

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        mSearchStoreAdapter.isDelete = this.isEdit;
        mSearchStoreAdapter.notifyDataSetChanged();
    }*/


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

    private View initView(LayoutInflater inflater) {
        TAG = "搜索 - 商家";
        View view = inflater.inflate(R.layout.fragment_post_old, null);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getInt(TransConstant.TYPE);
            }
        }
        lvList = getView(view, R.id.lvList);
        layoutProgress = getView(view, R.id.layoutProgress);
        initError(view);
        return view;
    }


    public void initData() {
        mSearchList = new ArrayList<>();
        mSearchStoreAdapter = new StoreSearchAdapter(mContext, mSearchList);
        lvList.setAdapter(mSearchStoreAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (!TextUtils.isEmpty(keyword) && 0 == mSearchList.size()) {
            loadSearchStoreList();
        } else if (type != SEARCH) {
            loadSearchStoreList();
        }
    }


    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                StoreObject storeObject = mSearchList.get(index);
                if (storeObject != null) {
                    StoreDetailActivity.launch(mContext, storeObject.id);
                }
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadSearchStoreList();
            }

            @Override
            public void onLoadMore() {
                loadSearchDataNext();
            }
        });
        btnRefresh.setOnClickListener(v -> loadSearchStoreList());
    }


    public void loadSearchStoreList() {
        storeContext.getPage().page = 1;
        storeContext.getEntity().keywords = keyword;
        if (mSearchList.isEmpty()) {
            ll_common_error.setVisibility(View.GONE);
            layoutProgress.setVisibility(View.VISIBLE);
        }
        storeContext.asynQuery(MethodConstant.SEARCH_STORE, storeContext.getEntity(), new responseHandler());
    }


    private void loadSearchDataNext() {
        storeContext.asynQueryNext(MethodConstant.SEARCH_STORE, storeContext.getEntity(), new responseHandler());
    }


    class responseHandler implements IAsynServiceHandler<StoreObject> {

        @Override
        public void onSuccess(StoreObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<StoreObject> entity) throws Exception {
            layoutProgress.setVisibility(View.GONE);
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (1 == storeContext.getPage().page)
                mSearchList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == storeContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= storeContext.getPage().page) {
                    lvList.setPullLoadEnable(false);
                } else {
                    lvList.setPullLoadEnable(true);
                }
                mSearchList.addAll(entity.entityList);
                mSearchStoreAdapter.notifyDataSetChanged();
            }
            if (mSearchList.isEmpty()) {
                ll_common_error.setVisibility(View.VISIBLE);
                setErrorType(0);
                if (SEARCH == type) {
                    setErrorMessage("没有相关搜索结果");
                }
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
                if (mSearchList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(1);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
            }
        }
    }


    public void setKeyword(String key) {
        this.keyword = key;
        if (null != mSearchList && 0 == mSearchList.size())
            loadSearchStoreList();
    }


    public void clearData() {
        if (null != mSearchList) {
            mSearchList.clear();
            lvList.setPullLoadEnable(false);
            mSearchStoreAdapter.notifyDataSetChanged();
        }
    }


    public int getCount() {
        if (null == mSearchList || 0 == mSearchList.size())
            return 0;
        else
            return mSearchList.size();
    }


    /**
     * 回到顶部
     */
    public void returnTop() {
        //        lvList.setSelection(0);
        lvList.smoothScrollToPosition(0);
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
