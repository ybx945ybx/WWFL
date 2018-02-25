package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.adapter.EventAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.PostObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 活动
 * Created by penley on 16/3/1.
 */
public class EventFragment extends BaseFragment {
    Context mContext;
    private XListView             lvList;
    private EventAdapter          mAdapter;
    private ArrayList<PostObject> mList;
    protected IViewContext<PostObject, IEntityService<PostObject>> commandViewContext = VF.<PostObject>getDefault(PostObject.class);
    private String type;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null != getArguments()) {
                Bundle bundle = getArguments();
                if (bundle.containsKey(TransConstant.TYPE)) {
                    type = bundle.getString(TransConstant.TYPE);
                }
            }
            isVisible = true;
            invisible();
        } else {
            isVisible = false;
        }
    }

    private void invisible() {
        if (!TextUtils.isEmpty(type) && null != mList && mList.size() <= 0) {
            ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
            loadData();
        }
    }

    public static EventFragment newInstance(String type) {
        EventFragment f = new EventFragment();
        Bundle        b = new Bundle();
        b.putString(TransConstant.TYPE, type);
        f.setArguments(b);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        return initView(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_post_old, null);
        initError(view);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
        }
        lvList = getView(view, R.id.lvList);
        lvList.setAutoLoadEnable(true);
        initError(view);
        return view;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
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
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                PostObject postObject = mList.get(index);
                if (postObject != null) {
                    TopicDetailActivity.launch(mContext, postObject.tid);
                }
            }
        });
        btnRefresh.setOnClickListener(v -> loadData());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<PostObject>();
        mAdapter = new EventAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setVisibility(View.GONE);
        if (isVisible) {
            ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
            loadData();
        }
    }

    private void loadData() {
        commandViewContext.getPage().page = 1;
        commandViewContext.getEntity().category = type;
        commandViewContext.asynQuery(MethodConstant.SHIPPING_ACTIVITY, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.getEntity().category = type;
        commandViewContext.asynQueryNext(MethodConstant.SHIPPING_ACTIVITY, commandViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<PostObject> {

        @Override
        public void onSuccess(PostObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<PostObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            lvList.setVisibility(View.VISIBLE);
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
        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (mList.isEmpty()) {
                ll_common_error.setVisibility(View.VISIBLE);
                setErrorType(1);
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
        }
    }
}
