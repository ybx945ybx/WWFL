package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.adapter.SectionAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.model.SectionObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * store搜索
 */
public class SectionFragment extends BaseFragment {
    Context mContext;
    private XListView lvList;
    ArrayList<SectionObject> mList;
    SectionAdapter           mAdapter;

    protected IViewContext<SectionObject, IEntityService<SectionObject>> commandViewContext = VF.<SectionObject>getDefault(SectionObject.class);


    private OnFavChangeListener mOnCallbackLitener;

    public void setOnCallbackLitener(OnFavChangeListener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    public boolean isEdit = false;

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        mAdapter.isDelete = this.isEdit;
        mAdapter.notifyDataSetChanged();
    }

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
        View view = inflater.inflate(R.layout.fragment_post_old, null);
        initError(view);
        lvList = getView(view, R.id.lvList);
        return view;
    }


    public void initData() {
        mList = new ArrayList<SectionObject>();
        mAdapter = new SectionAdapter(mContext, mList);
        mAdapter.isSearch = true;
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        loadData();

    }

    public int getCount() {
        if (null == mList || 0 == mList.size())
            return 0;
        else
            return mList.size();
    }

    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0 && mList.size() > index) {
                SectionObject sectionObject = mList.get(index);
                if (sectionObject != null) {
                    BoardDetailActivity.launch(mContext, sectionObject.fid);
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
        commandViewContext.getPage().page = 1;
        if (mList.isEmpty()) {
            ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        }
        commandViewContext.asynQuery(MethodConstant.CELLECT_FORUM, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.asynQueryNext(MethodConstant.CELLECT_FORUM, commandViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<SectionObject> {

        @Override
        public void onSuccess(SectionObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<SectionObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (1 == commandViewContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == commandViewContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= commandViewContext.getPage().page) {
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
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
            if (null != mOnCallbackLitener) {
                mOnCallbackLitener.onRefresh();
            }
        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
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

    public void del() {
        List<SectionObject> deleteList = mAdapter.getList();
        String              id         = "";
        for (SectionObject obj : deleteList) {
            if (obj.isSelected)
                id += "," + obj.fid;
        }
        id = id.replaceFirst(",", "");
        if (TextUtils.isEmpty(id))
            return;
        commandViewContext.getEntity().fid = id;
        commandViewContext.getService().asynFunction(MethodConstant.FORUM_FAV_REMOVE, commandViewContext.getEntity(), new IAsynServiceHandler<SectionObject>() {
            @Override
            public void onSuccess(SectionObject entity) throws Exception {
                if (mAdapter.isDelete) {
                    List<SectionObject> deleteList = mAdapter.getList();
                    int                 size       = deleteList.size();
                    List<SectionObject> tempList   = new ArrayList<SectionObject>();
                    for (int i = 0; i < size; i++) {
                        SectionObject obj = deleteList.get(i);
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
                if (null != mOnCallbackLitener) {
                    mOnCallbackLitener.onRefresh();
                }
            }

            @Override
            public void onSuccessPage(PageResult<SectionObject> entity) throws Exception {

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
