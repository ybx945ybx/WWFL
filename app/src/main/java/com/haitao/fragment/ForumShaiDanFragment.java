package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.TagDetailActivity;
import com.haitao.activity.TagListActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TopicSendActivity;
import com.haitao.adapter.TagRecommendAdapter;
import com.haitao.adapter.TopicAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.TagObject;
import com.haitao.model.forum.BoardObject;
import com.haitao.view.HtTitleView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumSubBoardModel;
import io.swagger.client.model.TopicModel;
import io.swagger.client.model.TopicModelTags;


/**
 * 首页-晒单
 */
public class ForumShaiDanFragment extends BaseFragment {
    private XListView             lvList;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    //加载动画
    private ViewGroup layoutProgress;

    //顶部标签
    private View                      headerView;
    private HtTitleView               mTvTitle;
    private GridView                  gvTag;
    private ArrayList<TopicModelTags> tagList;
    private TagRecommendAdapter       tagAdapter;

    private ImageView ivPublish;

    private BoardObject boardObject;


    private int page = 1;

    //判断是否上滑或下滑
    private int   mTouchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int   direction;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "社区 - 晒单";
        View messageLayout = initView(inflater);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shaidan, null);
        initError(view);
        lvList = getView(view, R.id.lvList);
        initHeaderView();
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        ivPublish = getView(view, R.id.ivPublish);
        return view;
    }

    /**
     * 初始化顶部
     */
    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.layout_h_scroll_tag, null);
        lvList.addHeaderView(headerView);
        //        tvSubTitle = getView(headerView, R.id.tvSubTitle);
        mTvTitle = getView(headerView, R.id.tv_title);
        gvTag = getView(headerView, R.id.gvTag);

    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getData();
    }

    public void initData() {
        mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        //mAdapter.subTitle = "精选晒单";
        lvList.setAdapter(mAdapter);
        //标签
        tagList = new ArrayList<TopicModelTags>();
        tagAdapter = new TagRecommendAdapter(mContext, tagList);
        gvTag.setAdapter(tagAdapter);
        //        tvSubTitle.setText(R.string.forum_shaidan_tag);
        mTvTitle.setTitle(getString(R.string.forum_shaidan_tag));
        ivPublish.setVisibility(View.VISIBLE);
        getData();

    }

    private void getData() {
        page = 1;
        ForumApi.getInstance().forumIndexShowOrdersGet("6", String.valueOf(PageConstant.pageSize),
                response -> {
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        mList.clear();
                        if (null != response.getData()) {
                            if (null != response.getData().getBoardInfo()) {
                                boardObject = new BoardObject();
                                boardObject.id = response.getData().getBoardInfo().getBoardId();
                                ArrayList<TagObject> tagObjects = new ArrayList<TagObject>();
                                for (ForumSubBoardModel forumSubBoardModel : response.getData().getSubBoards()) {
                                    TagObject tagObject = new TagObject();
                                    tagObject.id = forumSubBoardModel.getSubBoardId();
                                    tagObject.text = forumSubBoardModel.getSubBoardName();
                                    tagObjects.add(tagObject);
                                }
                                boardObject.subBoardModels = tagObjects;
                                ivPublish.setVisibility(View.VISIBLE);
                            }
                            //帖子
                            if (null != response.getData().getTopics() && response.getData().getTopics().size() > 0) {
                                mList.addAll(response.getData().getTopics());
                                lvList.setPullLoadEnable(response.getData().getTopics().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                            //标签
                            if (null != response.getData().getTags() && response.getData().getTags().size() > 0) {
                                headerView.setVisibility(View.VISIBLE);
                                tagList.clear();
                                tagList.addAll(response.getData().getTags());
                                tagList.add(null);
                                if (null != tagList && tagList.size() > 0) {
                                    int            size          = tagList.size();
                                    int            length        = 80;
                                    DisplayMetrics dm            = mContext.getResources().getDisplayMetrics();
                                    float          density       = dm.density;
                                    int            gridviewWidth = (int) ((size * (length + 10) + 0) * density);
                                    int            itemWidth     = (int) (length * density);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                                    gvTag.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                                    gvTag.setColumnWidth(itemWidth); // 设置列表项宽
                                    gvTag.setHorizontalSpacing((int) (10 * density)); // 设置列表项水平间距
                                    gvTag.setStretchMode(GridView.NO_STRETCH);

                                    gvTag.setNumColumns(size); // 设置列数量=列表集合数
                                    gvTag.setAdapter(tagAdapter);
                                }
                            } else {
                                headerView.setVisibility(View.GONE);
                            }

                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mList.isEmpty() && tagList.isEmpty()) {
                        headerView.setVisibility(View.GONE);
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        headerView.setVisibility(View.VISIBLE);
                        ll_common_error.setVisibility(View.GONE);
                    }
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                }, error -> {
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if (mList.isEmpty() && tagList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(1);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }

                });
    }

    /**
     * 分页加载
     */
    private void loadNext() {
        ForumApi.getInstance().forumTopicsListGet("1", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    lvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData() && null != response.getData().getRows()) {
                            mList.addAll(response.getData().getRows());
                            lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            lvList.setPullLoadEnable(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                }, error -> lvList.stopLoadMore());
    }


    private void initEvent() {

        lvList.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = event.getY();
                    if (mCurrentY - mFirstY > mTouchSlop) {
                        direction = 0;// down
                    } else if (mFirstY - mCurrentY > mTouchSlop) {
                        direction = 1;// up
                    }
                    if (direction == 1) {
                        //上滑todo
                        ivPublish.setVisibility(View.GONE);

                    } else if (direction == 0) {
                        //下滑todo
                        ivPublish.setVisibility(View.VISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return false;
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadNext();
            }
        });
        gvTag.setOnItemClickListener((parent, view, position, id) -> {
            TopicModelTags tags = tagList.get(position);
            if (null != tags && !TextUtils.isEmpty(tags.getTagId())) {
                TagDetailActivity.launch(mContext, tags.getTagName(), tags.getTagId());
            } else {
                //加载更多
                TagListActivity.launch(mContext);
            }
        });
        ivPublish.setOnClickListener(v -> {
            if (!UserManager.getInstance().isLogin()) {
                QuickLoginActivity.launch(mContext);
                return;
            }
            TopicSendActivity.launch(mContext);
        });

        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                TopicModel topicModel = mList.get(index);
                if (null != topicModel) {
                    TopicDetailActivity.launch(mContext, topicModel.getTid());
                }
            }
        });
        btnRefresh.setOnClickListener(v -> {
            layoutProgress.setVisibility(View.VISIBLE);
            ll_common_error.setVisibility(View.GONE);
            getData();
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

    /**
     * 回到顶部
     */
    public void returnTop() {
        int firstVisiblePosition = lvList.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            lvList.autoRefresh();
        } else {
            lvList.smoothScrollToPosition(0);
        }
    }
}
