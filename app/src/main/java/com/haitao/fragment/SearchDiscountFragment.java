package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.DiscountAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.model.DiscountObject;
import com.haitao.utils.ToastUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.List;


/**
 * store搜索
 */
public class SearchDiscountFragment extends BaseFragment {

    public static final int MY_FAV = 1;//我收藏的
    public static final int SEARCH = 2;//搜索

    private XListView                 lvList;
    public  DiscountAdapter           mAdapter;
    private ArrayList<DiscountObject> mList;
    public String keyword = "";
    public int    type    = 2;

    protected IViewContext<DiscountObject, IEntityService<DiscountObject>> discountContext = VF.<DiscountObject>getDefault(DiscountObject.class);

    private OnFavChangeListener mOnFavChangeListener;

    private ViewGroup layoutProgress;
    private int page = 1;

    public void setOnCallbackLitener(OnFavChangeListener onFavChangeListener) {
        mOnFavChangeListener = onFavChangeListener;
    }

    public boolean isEdit = false;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        TAG = "搜索 - 优惠";
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
        mList = new ArrayList<DiscountObject>();
        mAdapter = new DiscountAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (!TextUtils.isEmpty(keyword) && 0 == mList.size()) {
            loadData();
        } else if (type != SEARCH) {
            loadData();
        }
    }


    private void logging() {
        ForumApi.getInstance().commonSearchingLoggingPost(TransConstant.LogType.DEAL_SEARCH, UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserId() : "0", keyword, String.valueOf(System.currentTimeMillis()), "0", "0", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {

                }, error -> {

                });
    }

    public void loadData() {
        page = 1;
        discountContext.getPage().page = 1;
        discountContext.getEntity().keywords = keyword;
        if (mList.isEmpty()) {
            ll_common_error.setVisibility(View.GONE);
            layoutProgress.setVisibility(View.VISIBLE);
        }
        discountContext.asynQuery(type == SEARCH ? MethodConstant.SEARCH_DEAL : MethodConstant.CELLECT_LIST, discountContext.getEntity(), new responseHandler());
        if (type == SEARCH) {
            logging();
        }
    }

    private void loadNext() {
        discountContext.asynQueryNext(type == SEARCH ? MethodConstant.SEARCH_DEAL : MethodConstant.CELLECT_LIST, discountContext.getEntity(), new responseHandler());
        if (type == SEARCH) {
            logging();
        }
    }

    class responseHandler implements IAsynServiceHandler<DiscountObject> {

        @Override
        public void onSuccess(DiscountObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<DiscountObject> entity) throws Exception {
            layoutProgress.setVisibility(View.GONE);
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (1 == discountContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == discountContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= discountContext.getPage().page) {
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
                if (SEARCH == type) {
                    setErrorMessage("没有相关搜索结果");
                }
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
            if (null != mOnFavChangeListener) {
                mOnFavChangeListener.onRefresh();
            }
        }

        @Override
        public void onFailed(String error) {
            if (lvList == null)
                return;
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

    private void initEvent() {
        btnRefresh.setOnClickListener(v -> loadData());
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                DiscountObject discountObject = mList.get(index);
                if (discountObject != null) {
                    DiscountDetailActivity.launch(mContext, discountObject.id, keyword);
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
                page++;
                loadNext();
            }
        });

    }


    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        mAdapter.isDelete = this.isEdit;
        mAdapter.notifyDataSetChanged();
    }

    public void del() {
        List<DiscountObject> deleteList = mAdapter.getList();
        String               id         = "";
        for (DiscountObject obj : deleteList) {
            if (obj.isSelected)
                id += "," + obj.id;
        }
        id = id.replaceFirst(",", "");
        if (TextUtils.isEmpty(id))
            return;
        discountContext.getEntity().id = id;
        discountContext.getService().asynFunction(MethodConstant.CELLECT_DEL, discountContext.getEntity(), new IAsynServiceHandler<DiscountObject>() {
            @Override
            public void onSuccess(DiscountObject entity) throws Exception {
                if (mAdapter.isDelete) {
                    List<DiscountObject> deleteList = mAdapter.getList();
                    int                  size       = deleteList.size();
                    List<DiscountObject> tempList   = new ArrayList<DiscountObject>();
                    for (int i = 0; i < size; i++) {
                        DiscountObject obj = deleteList.get(i);
                        if (!obj.isSelected)
                            tempList.add(obj);
                    }
                    mList.clear();
                    mList.addAll(tempList);
                    mAdapter.notifyDataSetChanged();
                }
                if (mList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(0);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
                if (null != mOnFavChangeListener) {
                    mOnFavChangeListener.onRefresh();
                }
            }

            @Override
            public void onSuccessPage(PageResult<DiscountObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
            }
        });
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
