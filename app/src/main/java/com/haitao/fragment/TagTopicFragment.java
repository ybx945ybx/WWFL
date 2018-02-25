package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.adapter.TopicAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TopicModel;

/**
 * 标签-帖子
 */
public class TagTopicFragment extends BaseFragment {
    private XListView             lvList;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    //加载动画
    private ViewGroup layoutProgress;

    public String tagId = "", type = "1", tagName = "";

    private int page = 1;


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
        TAG = "标签详情 - 帖子";
        View view = inflater.inflate(R.layout.fragment_post_old, null);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.VALUE)) {
                tagId = bundle.getString(TransConstant.VALUE);
            }
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
            if (bundle.containsKey(TransConstant.TITLE)) {
                tagName = bundle.getString(TransConstant.TITLE);
            }
        }
        lvList = getView(view, R.id.lvList);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        initError(view);
        return view;
    }


    public void initData() {
        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        lvList.setAdapter(mAdapter);
        page = 1;
        loadData();
    }

    public void loadData() {
        ForumApi.getInstance().tagTagNameTopicsListGet(tagName, type, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    lvList.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    if (1 == page) {
                        mList.clear();
                    }
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            //帖子
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                                lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                        }
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();

                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
    }

    private void initEvent() {
        btnRefresh.setOnClickListener(v -> loadData());
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                TopicModel topicModel = mList.get(index);
                if (null != topicModel) {
                    TopicDetailActivity.launch(mContext, topicModel.getTid());
                }
            }
        });
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


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
