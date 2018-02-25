package com.haitao.fragment;

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
import com.haitao.common.annotation.PostFragmentType;
import com.haitao.connection.api.ForumApi;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.ApiException;
import io.swagger.client.model.TopicModel;

/**
 * 帖子
 */
public class TopicFragment extends BaseFragment {

    @BindView(R.id.content_view) XListView          mLvList;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder              unbinder;
    private TopicAdapter          mAdapter;
    private ArrayList<TopicModel> mList;

    @PostFragmentType
    private int mType = PostFragmentType.MY_POST;

    public String keyword = "";
    private int mPage;

    private OnFavChangeListener mOnFavChangeListener;

    public void setOnFavChangeListener(OnFavChangeListener onFavChangeListener) {
        mOnFavChangeListener = onFavChangeListener;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.layout_common_list, null);
        unbinder = ButterKnife.bind(this, messageLayout);
        initVars();
        initView();
        return messageLayout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initVars() {
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.TYPE)) {
                mType = bundle.getInt(TransConstant.TYPE);
            }
        }
        mPage = 1;
        mList = new ArrayList<>();
        TAG = "帖子列表";
    }


    private void initView() {
        mAdapter = new TopicAdapter(mContext, mList);
        mLvList.setAdapter(mAdapter);
        mLvList.setAutoLoadEnable(true);
        mLvList.setPullLoadEnable(false);
        mLvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvList.getHeaderViewsCount();
            if (index >= 0) {
                TopicModel topicModel = mList.get(index);
                if (topicModel != null) {
                    TopicDetailActivity.launch(mContext, topicModel.getTid());
                }
            }
        });
        mLvList.setXListViewListener(new XListView.IXListViewListener() {
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

    private void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }


    public void loadData() {
        switch (mType) {
            case PostFragmentType.MY_POST:
                loadMyPost();
                break;
            case PostFragmentType.MY_FAV:
                loadFavPost();
                break;
            default:
                loadSearchPostUnboxing();
                break;
        }
    }


    /**
     * 我的帖子
     */
    public void loadMyPost() {
        ForumApi.getInstance().userTopicsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有帖子");
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
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    showErrorToast(error);
                });
    }


    /**
     * 收藏的帖子
     */
    public void loadFavPost() {
        ForumApi.getInstance().userCollectionForumTopicsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有帖子");
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
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    showErrorToast(error);
                });
    }


    /**
     * 搜索帖子
     */
    public void loadSearchPostUnboxing() {
        ForumApi.getInstance().searchingKeywordsTopicsListGet(keyword,
                mType == PostFragmentType.SEARCH_POST ? "1" : "2",
                String.valueOf(mPage),
                String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("没有相关搜索结果");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (mMsv == null)
                        return;
                    mMsv.showError();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    showErrorToast(error);
                });
        logging();
    }


    private void logging() {
        try {
            ForumApi.getInstance().commonSearchingLoggingPost(TransConstant.LogType.BBS_SEARCH,
                    UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserId() : "0",
                    keyword,
                    String.valueOf(System.currentTimeMillis()),
                    "0",
                    "0",
                    String.valueOf(mPage),
                    String.valueOf(PageConstant.pageSize));
        } catch (TimeoutException | ExecutionException | ApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    public void returnTop() {
        mLvList.setSelection(0);
    }
}
