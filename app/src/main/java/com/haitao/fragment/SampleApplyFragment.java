package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.adapter.SampleApplyAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.SampleApplyObject;
import com.haitao.model.SampleObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 申请名单
 */
public class SampleApplyFragment extends BaseFragment {
    Context mContext;
    private XListView lvList;
    private ArrayList<SampleApplyObject> mList;
    private SampleApplyAdapter mAdapter;
    private int page = 1;
    private String type = "";

    private SampleObject sampleObject;

    protected IViewContext<SampleApplyObject, IEntityService<SampleApplyObject>> commandViewContext = VF.<SampleApplyObject>getDefault(SampleApplyObject.class);


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
        TAG = "试用详情 - 申请名单";
        View view = inflater.inflate(R.layout.fragment_sample, null);
        if(null != getArguments()){
            Bundle bundle = getArguments();
            if(getArguments().containsKey(TransConstant.OBJECT))
                sampleObject = (SampleObject) bundle.getSerializable(TransConstant.OBJECT);
            if(getArguments().containsKey(TransConstant.TYPE))
                type = bundle.getString(TransConstant.TYPE);
        }
        lvList = getView(view, R.id.content_view);
        if(type.equals("smapleDetail")){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvList.getLayoutParams();
            params.setMargins(0,0,0,(int)mContext.getResources().getDimension(R.dimen.px150));
            lvList.setLayoutParams(params);
        }
        initError(view);
        return view;
    }


    public void initData() {
        mList = new ArrayList<SampleApplyObject>();
        mAdapter = new SampleApplyAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setPullRefreshEnable(false);
        //ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        loadData();
    }

    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
    }



    private void loadData(){
        commandViewContext.getEntity().page = String.valueOf(page);
        commandViewContext.getEntity().lpp = String.valueOf(20);
        commandViewContext.getEntity().id = sampleObject.id;
        commandViewContext.getService().asynFunction(MethodConstant.SAMPLE_MEMBER, commandViewContext.getEntity(), new IAsynServiceHandler<SampleApplyObject>() {
            @Override
            public void onSuccess(SampleApplyObject entity) throws Exception {
                lvList.stopRefresh();
                lvList.stopLoadMore();
                if(null != entity){
                    if(1 == page){
                        mAdapter.isSuccess = false;
                        mAdapter.isFail = false;
                        mList.clear();
                        lvList.setRefreshTime();
                    }
                    if(entity._pagecount <= page){
                        lvList.setPullLoadEnable(false);
                    }else{
                        lvList.setPullLoadEnable(true);
                    }
                    if(null != entity.success && entity.success.size() > 0){
                        for (SampleApplyObject sampleApplyObject : entity.success){
                            sampleApplyObject.status = "1";
                            mList.add(sampleApplyObject);
                        }
                    }
                    if(null != entity.all && entity.all.size() > 0){
                        for (SampleApplyObject sampleApplyObject : entity.all){
                            sampleApplyObject.status = "0";
                            mList.add(sampleApplyObject);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if(mList.isEmpty()){
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorMessage("暂无收到任何申请，赶紧来试试你的运气吧！");
                }else{
                    ll_common_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSuccessPage(PageResult<SampleApplyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                lvList.stopRefresh();
                lvList.stopLoadMore();
                if(mList.isEmpty()){
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorMessage("暂无收到任何申请，赶紧来试试你的运气吧！");
                }else{
                    ll_common_error.setVisibility(View.GONE);
                }
            }
        });
    }


    class responseHandler implements IAsynServiceHandler<SampleApplyObject> {

        @Override
        public void onSuccess(SampleApplyObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<SampleApplyObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if(1 == commandViewContext.getPage().page)
                mList.clear();
            if(null != entity && null != entity.entityList){
                if(1 == commandViewContext.getPage().page)
                    lvList.setRefreshTime();
                if(entity.pageCount <= commandViewContext.getPage().page){
                    lvList.setPullLoadEnable(false);
                }else{
                    lvList.setPullLoadEnable(true);
                }
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }
            if(mList.isEmpty()){
                ll_common_error.setVisibility(View.VISIBLE);
                setErrorType(0);
            }else{
                ll_common_error.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if(error.equals(TransConstant.NET_ERROR)) {
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
