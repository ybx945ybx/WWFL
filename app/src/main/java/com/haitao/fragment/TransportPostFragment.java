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
import com.haitao.adapter.PostAdapter;
import com.haitao.adapter.PostTopAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.PostObject;
import com.haitao.model.SectionObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.FullListView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 转运详情 - 帖子
 */
public class TransportPostFragment extends BaseFragment {
    private Context                mContext;
    private XListView              mLvContent;
    private ArrayList<PostObject>  mList;
    private PostAdapter            mAdapter;
    private LogisticsCompanyObject storeObject;

    //置顶
    private FullListView          lvTopList;
    private PostTopAdapter        topAdapter;
    private ArrayList<PostObject> topList;

    ViewGroup contentView = null;

    protected IViewContext<SectionObject, IEntityService<SectionObject>> sectionContext = VF.<SectionObject>getDefault(SectionObject.class);
    protected IViewContext<PostObject, IEntityService<PostObject>>       postContext    = VF.<PostObject>getDefault(PostObject.class);
    private MultipleStatusView mMsv;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initVars();
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = "转运详情 - 帖子";
        mContext = getActivity();
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            storeObject = (LogisticsCompanyObject) bundle.getSerializable(TransConstant.OBJECT);
        }
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);
        mMsv = getView(view, R.id.msv);
        mLvContent = getView(view, R.id.content_view);
        contentView = (ViewGroup) View.inflate(mContext, R.layout.layout_transport_post, null);
        lvTopList = getView(contentView, R.id.lvTopList);
        mLvContent.addHeaderView(contentView);
        return view;
    }

    public void initData() {
        topList = new ArrayList<>();
        topAdapter = new PostTopAdapter(mContext, topList);
        lvTopList.setAdapter(topAdapter);

        mList = new ArrayList<>();
        mAdapter = new PostAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);

        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        mLvContent.setPullRefreshEnable(false);
        getData();
    }

    private void initEvent() {
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                loadNext();
            }
        });
        lvTopList.setOnItemClickListener((parent, view, position, id) -> TopicDetailActivity.launch(mContext, topList.get(position).tid));
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                PostObject postObject = mList.get(index);
                if (postObject != null) {
                    TopicDetailActivity.launch(mContext, postObject.id);
                }
            }
        });
    }

    private void getData() {
        postContext.getPage().page = 1;
        sectionContext.getEntity().order = "1";
        sectionContext.getEntity().fid = storeObject.forumid;
        sectionContext.getService().asynFunction(MethodConstant.FORUM_VIEW, sectionContext.getEntity(), new IAsynServiceHandler<SectionObject>() {
            @Override
            public void onSuccess(SectionObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                mLvContent.setRefreshTime();
                mLvContent.stopRefresh();
                mLvContent.stopLoadMore();
                mMsv.showContent();

                if (null != entity) {
                    topList.clear();
                    if (null != entity.tops && entity.tops.size() > 0) {
                        topList.addAll(entity.tops);
                    }
                    //                    contentView.setVisibility(topList.isEmpty() ? View.GONE : View.VISIBLE);
                    topAdapter.notifyDataSetChanged();
                    if (null != entity.list && null != entity.list._data && entity.list._data.size() > 0) {
                        int pageCount = TextUtils.isEmpty(entity.list._pagecount) ? 0 : Integer.parseInt(entity.list._pagecount);

                        mList.clear();
                        mList.addAll(entity.list._data);
                        mAdapter.notifyDataSetChanged();
                        if (pageCount > 1) {
                            mLvContent.setPullLoadEnable(true);
                        } else {
                            mLvContent.setPullLoadEnable(false);
                        }
                    }
                    if (mList.isEmpty() && topList.isEmpty()) {
                        //                        ll_common_error.setVisibility(View.VISIBLE);
                        //                        setErrorMessage("暂无相关帖子");
                        mMsv.showEmpty("暂无相关帖子");
                    } /*else {
                        ll_common_error.setVisibility(View.GONE);
                    }*/
                } else {
                    mMsv.showError();
                }
            }

            @Override
            public void onSuccessPage(PageResult<SectionObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
                ProgressDialogUtils.dismiss();
                mMsv.showError();
            }
        });
    }

    private void loadNext() {
        postContext.getEntity().fid = storeObject.forumid;
        postContext.getEntity().order = "1";
        postContext.asynQueryNext(MethodConstant.THREAD_LIST, postContext.getEntity(), new IAsynServiceHandler<PostObject>() {
            @Override
            public void onSuccess(PostObject entity) throws Exception {

            }

            @Override
            public void onSuccessPage(PageResult<PostObject> entity) throws Exception {
                mLvContent.stopRefresh();
                mLvContent.stopLoadMore();
                if (null != entity && null != entity.entityList) {
                    if (entity.pageCount <= postContext.getPage().page) {
                        mLvContent.setPullLoadEnable(false);
                    }
                    mList.addAll(entity.entityList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }
}
