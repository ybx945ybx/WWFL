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
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TopicModel;


/**
 * 版块-帖子
 */
public class TalentTopicFragment extends BaseFragment {
    private XListView             mLvContent;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    private String id   = "";
    private String type = "";
    private int    page = 1;


    private TextView tvError;

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
        TAG = "达人详情 - 帖子";
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.ID)) {
                id = bundle.getString(TransConstant.ID);
            }
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
        }
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);

        mLvContent = getView(view, R.id.content_view);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullRefreshEnable(false);
        mLvContent.setPullLoadEnable(false);

        View layoutHeader = View.inflate(mContext, R.layout.layout_board_top_topics, null);
        tvError = getView(layoutHeader, R.id.tvError);
        tvError.setText("1".equals(type) ? "还没有发布过帖子" : "还没有发布过晒单");
        mLvContent.addHeaderView(layoutHeader);
        return view;
    }


    public void initData() {
        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        mLvContent.setAdapter(mAdapter);

        getData();
    }

    private void getData() {
        ForumApi.getInstance().forumUserUserIdTopicsListGet(id, type, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mLvContent == null)
                        return;
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    if (1 == page) {
                        mList.clear();
                    }
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            //帖子
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                                mLvContent.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            } else {
                                mLvContent.setPullLoadEnable(false);
                            }
                        }
                    }
                    if (mList.isEmpty()) {
                        tvError.setVisibility(View.VISIBLE);
                    } else {
                        tvError.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    if (mLvContent == null)
                        return;
                    showErrorToast(error);
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });

    }


    private void initEvent() {
        mLvContent.setOnItemClickListener((parent, view, position, id1) -> {
            if (mList.size() > 0) {
                int index = position - mLvContent.getHeaderViewsCount();
                if (index >= 0) {
                    TopicModel topicModel = mList.get(index);
                    if (null != topicModel) {
                        TopicDetailActivity.launch(mContext, topicModel.getTid());
                    }
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
                getData();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
