package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.adapter.PostCommentAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.MyReplyModel;

/**
 * 我的帖子 - 评论Tab
 */
public class MyCommentFragment extends BaseFragment {
    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder                unbinder;
    private PostCommentAdapter      mAdapter;
    private ArrayList<MyReplyModel> mList;

    private int mPage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.layout_common_list, null);
        unbinder = ButterKnife.bind(this, messageLayout);
        initVars();
        initViews(savedInstanceState);
        return messageLayout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initVars() {
        TAG = "我的帖子 - 评论";
        mList = new ArrayList<MyReplyModel>();
        mAdapter = new PostCommentAdapter(mContext, mList);
    }


    private void initViews(Bundle savedInstanceState) {
        mLvContent.setAdapter(mAdapter);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        // listener
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                MyReplyModel myReplyModel = mList.get(index);
                if (myReplyModel != null) {
                    TopicDetailActivity.launch(mContext, myReplyModel.getTid());
                }
            }
        });
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadData();
            }
        });
        mMsv.setOnRetryClickListener(v -> initData());
    }


    public void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }


    private void loadData() {
        ForumApi.getInstance().userTopicsRepliesListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时评论哦");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    mMsv.showError();
                    showErrorToast(error);
                    mLvContent.stopLoadMore();
                });
    }

    public void returnTop() {
        mLvContent.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
