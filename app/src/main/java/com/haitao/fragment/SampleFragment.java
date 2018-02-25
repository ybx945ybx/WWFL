package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.SampleDetailActivity;
import com.haitao.adapter.SampleAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.SampleObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 海淘试用
 */
public class SampleFragment extends BaseFragment {
    Context mContext;
    private XListView               lvList;
    private ArrayList<SampleObject> mList;
    private SampleAdapter           mAdapter;
    private int     type   = 0;
    private boolean isMine = true;//是否是我的试用中的
    private String  url    = "";
    private int     page   = 1;

    protected IViewContext<SampleObject, IEntityService<SampleObject>> commandViewContext = VF.<SampleObject>getDefault(SampleObject.class);


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
        View view = inflater.inflate(R.layout.fragment_sample, null);
        if (null != getArguments() && getArguments().containsKey(TransConstant.TYPE)) {
            Bundle bundle = getArguments();
            type = bundle.getInt(TransConstant.TYPE);
            if (bundle.containsKey(TransConstant.KEY)) {
                isMine = false;
            }
        }
        TAG = isMine ? "我的试用" : "免费试用" + "试用列表";
        lvList = getView(view, R.id.content_view);
        initError(view);
        return view;
    }


    public void initData() {
        mList = new ArrayList<SampleObject>();
        mAdapter = new SampleAdapter(mContext, mList);
        mAdapter.isOver = true;
        mAdapter.overFirstPosition = -1;
        mAdapter.isSuccessApply = (1 == type);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (isMine) {
            url = 0 == type ? MethodConstant.MY_SAMPLE_APPLICATION_IN : 1 == type ? MethodConstant.MY_SAMPLE_APPLICATION_SUCCESS : MethodConstant.MY_SAMPLE_APPLICATION_FAIL;
        } else {
            url = MethodConstant.SAMPLE_LIST;
        }
        if (!isMine) {
            page = 1;
            getData();
        } else {
            loadData();
        }
    }

    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if (!isMine) {
                    page = 1;
                    getData();
                } else {
                    loadData();
                }
            }

            @Override
            public void onLoadMore() {
                if (!isMine) {
                    page++;
                    getData();
                } else {
                    loadNext();
                }
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                SampleObject obj = mList.get(index);
                if (obj != null) {
                    SampleDetailActivity.launch(mContext, obj);
                }
            }
        });
    }

    private void getData() {
        commandViewContext.getEntity().page = String.valueOf(page);
        commandViewContext.getEntity().lpp = String.valueOf(20);
        commandViewContext.getService().asynFunction(url, commandViewContext.getEntity(), new IAsynServiceHandler<SampleObject>() {
            @Override
            public void onSuccess(SampleObject entity) throws Exception {
                lvList.stopRefresh();
                lvList.stopLoadMore();
                if (null != entity) {
                    if (1 == page) {
                        mAdapter.isOver = false;
                        mList.clear();
                        lvList.setRefreshTime();
                    }
                    if (entity._pagecount <= page) {
                        lvList.setPullLoadEnable(false);
                    } else {
                        lvList.setPullLoadEnable(true);
                    }
                    if (null != entity.ongoing && entity.ongoing.size() > 0) {
                        mList.addAll(0, entity.ongoing);
                    }
                    if (null != entity.end && entity.end.size() > 0) {
                        mList.addAll(entity.end);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSuccessPage(PageResult<SampleObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                lvList.stopRefresh();
                lvList.stopLoadMore();
            }
        });
    }

    private void loadData() {
        commandViewContext.getPage().page = 1;
        commandViewContext.asynQuery(url, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.asynQueryNext(url, commandViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<SampleObject> {

        @Override
        public void onSuccess(SampleObject entity) throws Exception {
        }

        @Override
        public void onSuccessPage(PageResult<SampleObject> entity) throws Exception {
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
                if (mList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(0);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
