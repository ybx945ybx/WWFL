package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.adapter.SampleReportAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.SampleObject;
import com.haitao.model.SampleReportObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 试用报告
 */
public class SampleReportFragment extends BaseFragment {
    Context mContext;
    private XListView                     lvList;
    private ArrayList<SampleReportObject> mList;
    private SampleReportAdapter           mAdapter;
    private String type = "";
    private SampleObject sampleObject;
    private boolean isMine = true;
    private String  url    = "";
    private ViewGroup layoutProgress;

    protected IViewContext<SampleReportObject, IEntityService<SampleReportObject>> commandViewContext = VF.<SampleReportObject>getDefault(SampleReportObject.class);


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View messageLayout = initView(inflater);
        initEvent();
        initError(messageLayout);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_sample, null);
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (getArguments().containsKey(TransConstant.OBJECT)) {
                sampleObject = (SampleObject) bundle.getSerializable(TransConstant.OBJECT);
            }
            if (getArguments().containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
            if (bundle.containsKey(TransConstant.KEY)) {
                isMine = false;
            }
        }
        TAG = isMine ? "我的试用" : "免费试用" + "试用报告";
        lvList = getView(view, R.id.content_view);
        if (type.equals("smapleDetail")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvList.getLayoutParams();
            params.setMargins(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.px150));
            lvList.setLayoutParams(params);
        }
        return view;
    }


    public void initData() {
        mList = new ArrayList<SampleReportObject>();
        mAdapter = new SampleReportAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (isMine) {
            url = null != sampleObject ? MethodConstant.SAMPLE_REPORT : MethodConstant.MY_SAMPLE_REPORT;
        } else {
            url = MethodConstant.SAMPLE_SELECTED_REPORT;
        }
        loadData();
    }

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
                SampleReportObject sampleReportObject = mList.get(index);
                if (sampleReportObject != null) {
                    TopicDetailActivity.launch(mContext, sampleReportObject.tid);
                }
            }
        });
    }


    private void loadData() {
        commandViewContext.getPage().page = 1;
        commandViewContext.getEntity().id = null != sampleObject ? sampleObject.id : "";

        commandViewContext.asynQuery(url, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.getEntity().id = null != sampleObject ? sampleObject.id : "";
        commandViewContext.asynQueryNext(url, commandViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<SampleReportObject> {

        @Override
        public void onSuccess(SampleReportObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<SampleReportObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();
            layoutProgress.setVisibility(View.GONE);
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
                setErrorMessage(!isMine ? "暂无试用报告" : null != sampleObject ? "暂无收到任何申请试用报告！" : "暂未提交任何申请试用报告！");
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailed(String error) {
            layoutProgress.setVisibility(View.GONE);
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
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
