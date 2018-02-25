package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.adapter.TopicAdapter;
import com.haitao.adapter.TopicTopAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.FullListView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TopicBriefModel;
import io.swagger.client.model.TopicModel;


/**
 * 版块-帖子
 */
public class BoardTopicFragment extends BaseFragment {
    private XListView             mLvContent;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;
    private String boardId = "", secId = "";
    private int page = 1;
    //    private ViewGroup layoutProgress;

    //置顶
    private FullListView               lvTopList;
    private TopicTopAdapter            topAdapter;
    private ArrayList<TopicBriefModel> topList;

    private TextView tvError;

    public boolean order = false;
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
        mContext = getActivity();
        TAG = "版块详情 - 帖子";
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.VALUE)) {
                secId = bundle.getString(TransConstant.VALUE);
            }
            if (bundle.containsKey(TransConstant.ID)) {
                boardId = bundle.getString(TransConstant.ID);
            }
        }
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);
        mMsv = getView(view, R.id.msv);
        //        layoutProgress = getView(view, R.id.layoutProgress);
        //        layoutProgress.setVisibility(View.VISIBLE);
        mLvContent = getView(view, R.id.content_view);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullRefreshEnable(false);
        mLvContent.setPullLoadEnable(false);

        View layoutHeader = View.inflate(mContext, R.layout.layout_board_top_topics, null);
        lvTopList = getView(layoutHeader, R.id.lvTopList);
        tvError = getView(layoutHeader, R.id.tvError);
        tvError.setText("暂无相关帖子");
        mLvContent.addHeaderView(layoutHeader);

        mLvContent.setVisibility(View.GONE);
        return view;
    }


    public void initData() {
        mList = new ArrayList<>();
        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        mLvContent.setAdapter(mAdapter);

        //置顶帖
        topList = new ArrayList<>();
        topAdapter = new TopicTopAdapter(mContext, topList);
        topAdapter.subTitle = "置顶帖";
        lvTopList.setAdapter(topAdapter);
        mMsv.showLoading();
        getData();
    }

    TopicTopAdapter.OnCallbackLitener mOnCallbackLitener = () -> {

    };

    private void initEvent() {
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0 && mList.size() > index) {
                TopicModel topicModel = mList.get(index);
                if (null != topicModel) {
                    TopicDetailActivity.launch(mContext, topicModel.getTid());
                }
            }
        });
        lvTopList.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && topList.size() > position) {
                TopicBriefModel topicModel = topList.get(position);
                if (null != topicModel) {
                    TopicDetailActivity.launch(mContext, topicModel.getTid());
                }
            }
        });
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadNext();
            }
        });
    }

    public void refresh() {
        if (mMsv != null) {
            mMsv.showLoading();
        }
        if (null != mLvContent) {
            mLvContent.setVisibility(View.GONE);
        }
        getData();
    }

    private void getData() {
        page = 1;
        ForumApi.getInstance().forumBoardBoardIdIndexGet(secId, "20",
                response -> {
                    if (mMsv == null)
                        return;
                    //                    layoutProgress.setVisibility(View.GONE);
                    mMsv.showContent();
                    mLvContent.setVisibility(View.VISIBLE);
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mList.clear();
                    topList.clear();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            if (null != response.getData().getTopTopics() && response.getData().getTopTopics().size() > 0) {
                                topList.addAll(response.getData().getTopTopics());
                            }
                        }
                    }
                    //topAdapter = new TopicTopAdapter(mContext,topList);
                    lvTopList.setAdapter(topAdapter);
                    loadNext();
                }, error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    //                    layoutProgress.setVisibility(View.GONE);
                    mMsv.showError();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });

    }

    private void loadNext() {
        if (boardId.equals(secId)) {
            ForumApi.getInstance().forumBoardBoardIdTopicsListGet(secId, order ? "dateline-desc" : "lastpost-desc", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                    response -> {
                        if (mLvContent == null)
                            return;
                        mLvContent.stopLoadMore();
                        if (1 == page) {
                            mList.clear();
                        }
                        if ("0".equals(response.getCode())) {
                            if (null != response.getData() && null != response.getData().getRows()) {
                                mList.addAll(response.getData().getRows());
                                mLvContent.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            } else {
                                mLvContent.setPullLoadEnable(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        if (mList.isEmpty() && topList.isEmpty()) {
                            tvError.setVisibility(View.VISIBLE);
                        } else {
                            tvError.setVisibility(View.GONE);
                        }
                    }, error -> {
                        if (mLvContent == null)
                            return;
                        showErrorToast(error);
                        mLvContent.stopLoadMore();
                    });
        } else {
            ForumApi.getInstance().forumBoardBoardIdSubBoardSubBoardIdTopicsListGet(boardId, secId, order ? "dateline-desc" : "lastpost-desc", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                    response -> {
                        if (mLvContent == null)
                            return;
                        mLvContent.stopLoadMore();
                        if (1 == page) {
                            mList.clear();
                        }
                        if ("0".equals(response.getCode())) {
                            if (null != response.getData() && null != response.getData().getRows()) {
                                mList.addAll(response.getData().getRows());
                                mLvContent.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            } else {
                                mLvContent.setPullLoadEnable(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        if (mList.isEmpty() && topList.isEmpty()) {
                            tvError.setVisibility(View.VISIBLE);
                        } else {
                            tvError.setVisibility(View.GONE);
                        }
                    }, error -> {
                        if (mLvContent == null)
                            return;
                        showErrorToast(error);
                        mLvContent.stopLoadMore();
                    });
        }
    }
}
