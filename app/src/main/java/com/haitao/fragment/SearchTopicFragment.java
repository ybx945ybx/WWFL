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
import com.haitao.adapter.TopicAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.TopicModel;

/**
 * 帖子搜索
 */
public class SearchTopicFragment extends BaseFragment {
    private Context               mContext;
    private XListView             lvList;
    private TopicAdapter          mAdapter;
    private ArrayList<TopicModel> mList;

    public String keyword = "";

    public String type = "";

    private int page = 1;
    private ViewGroup layoutProgress;


    public void setKeyword(String key) {
        this.keyword = key;
        Logger.d(key);
        if (null != mList && mList.isEmpty()) {
            loadData();
        }
    }

    public void clearData() {
        if (null != mList) {
            mList.clear();
            lvList.setPullLoadEnable(false);
            mAdapter.notifyDataSetChanged();
        }
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
        TAG = "搜索 - 帖子";
        View view = inflater.inflate(R.layout.fragment_post_old, null);
        initError(view);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
        }
        lvList = getView(view, R.id.lvList);
        layoutProgress = getView(view, R.id.layoutProgress);
        return view;
    }


    public void initData() {
        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        if (!TextUtils.isEmpty(keyword) && 0 == mList.size()) {
            ll_common_error.setVisibility(View.GONE);
            layoutProgress.setVisibility(View.VISIBLE);
            page = 1;
            loadData();
        }

    }


    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                TopicModel topicModel = mList.get(index);
                if (topicModel != null) {
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
        btnRefresh.setOnClickListener(v -> loadData());

    }

    public void loadData() {
        ForumApi.getInstance().searchingKeywordsTopicsListGet(keyword, type, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    lvList.setVisibility(View.VISIBLE);
                    if (1 == page) {
                        mList.clear();
                    }
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            //帖子
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        }
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                        setErrorMessage("没有相关搜索结果");
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
        logging();
    }

    private void logging() {
        ForumApi.getInstance().commonSearchingLoggingPost(TransConstant.LogType.BBS_SEARCH, UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserId() : "0", keyword, String.valueOf(System.currentTimeMillis()), "0", "0", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {

                }, error -> {

                });
    }
}
