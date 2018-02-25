package com.haitao.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.activity.BoardActivity;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.ExchangeActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.SampleActivity;
import com.haitao.activity.TalentActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TopicSendActivity;
import com.haitao.activity.TopicTalkActivity;
import com.haitao.adapter.SectionIndexAdapter;
import com.haitao.adapter.TopicAdapter;
import com.haitao.adapter.TopicTopAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.TopicLink;
import com.haitao.view.FullGirdView;
import com.haitao.view.FullListView;
import com.haitao.view.SlideCycleView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumIndexBoardModel;
import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.TopicBriefModel;
import io.swagger.client.model.TopicModel;


/**
 * 论坛-热帖
 */
public class ForumHotPostFragment extends BaseFragment {
    private XListView             lvList;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    // 加载动画
    private ViewGroup layoutProgress;

    // 焦点图
    private SlideCycleView           layoutCircle;
    private ArrayList<SlidePicModel> adList;

    //快速导航
    private FullGirdView                    gvSection;
    private SectionIndexAdapter             sectionAdapter;
    private ArrayList<ForumIndexBoardModel> sectionList;

    // 置顶帖
    private FullListView               lvTopList;
    private TopicTopAdapter            topAdapter;
    private ArrayList<TopicBriefModel> topList;

    // 今日话题
    private FullListView               lvTopicList;
    private TopicTopAdapter            talkAdapter;
    private ArrayList<TopicBriefModel> talkList;

    private ImageView ivPublish;

    // 判断是否上滑或下滑
    private int   mTouchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int   direction;


    private int page = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "社区 - 热帖";
        return initView(inflater);
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
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setVisibility(View.GONE);
        ivPublish = getView(view, R.id.ivPublish);
        initHeaderView();
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        return view;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getData();
    }

    public void initData() {
        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        lvList.setAdapter(mAdapter);
        //焦点图
        adList = new ArrayList<SlidePicModel>();
        //版块
        sectionList = new ArrayList<ForumIndexBoardModel>();
        sectionAdapter = new SectionIndexAdapter(mContext, sectionList);
        gvSection.setAdapter(sectionAdapter);
        //置顶帖
        topList = new ArrayList<TopicBriefModel>();
        topAdapter = new TopicTopAdapter(mContext, topList);
        topAdapter.subTitle = "置顶帖";
        lvTopList.setAdapter(topAdapter);
        //话题
        talkList = new ArrayList<TopicBriefModel>();
        talkAdapter = new TopicTopAdapter(mContext, talkList);
        talkAdapter.subTitle = "今日话题";
        talkAdapter.isTop = false;
        talkAdapter.setOnCallbackLitener(() -> TopicTalkActivity.launch(mContext));
        lvTopicList.setAdapter(talkAdapter);
        ivPublish.setVisibility(View.VISIBLE);
        getData();

    }


    private void initHeaderView() {
        View layoutHeader = View.inflate(mContext, R.layout.layout_forum_hot_post, null);
        layoutCircle = getView(layoutHeader, R.id.layoutCircle);
        int                       screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams lp          = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth / (16f / 8f)));
        layoutCircle.setLayoutParams(lp);
        gvSection = getView(layoutHeader, R.id.gvSection);
        lvTopList = getView(layoutHeader, R.id.lvTopList);
        lvTopicList = getView(layoutHeader, R.id.lvTopicList);
        lvList.addHeaderView(layoutHeader);
    }

    private void getData() {
        page = 1;
        ForumApi.getInstance().forumIndexHotTopicsGet("20", "1", String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        mList.clear();
                        sectionList.clear();
                        topList.clear();
                        talkList.clear();
                        if (null != response.getData()) {
                            if (null != response.getData().getSlidePics() && response.getData().getSlidePics().size() > 0) {
                                layoutCircle.setVisibility(View.VISIBLE);
                                layoutCircle.setImageResources(response.getData().getSlidePics(), mAdCycleViewListener);
                            } else {
                                layoutCircle.setVisibility(View.GONE);
                            }
                            //版块
                            if (null != response.getData().getBoards() && response.getData().getBoards().size() > 0) {
                                sectionList.addAll(response.getData().getBoards());
                                ForumIndexBoardModel more = new ForumIndexBoardModel();
                                more.setIconTitle("全部");
                                more.setIcon("res://com.haitao/" + R.mipmap.ic_board_more);
                                sectionList.add(more);
                            } else {
                                ForumIndexBoardModel more = new ForumIndexBoardModel();
                                more.setIconTitle("全部");
                                more.setIcon("res://com.haitao/" + R.mipmap.ic_board_more);
                                sectionList.add(more);
                            }
                            //置顶帖
                            if (null != response.getData().getTopTopics() && response.getData().getTopTopics().size() > 0) {
                                topList.addAll(response.getData().getTopTopics());
                            }
                            //话题
                            if (null != response.getData().getTalkTopics() && response.getData().getTalkTopics().size() > 0) {
                                talkList.addAll(response.getData().getTalkTopics());
                            }
                            //帖子
                            if (null != response.getData().getTopics() && response.getData().getTopics().size() > 0) {
                                mList.addAll(response.getData().getTopics());
                                lvList.setPullLoadEnable(response.getData().getTopics().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                        }
                        sectionAdapter.notifyDataSetChanged();
                        topAdapter.notifyDataSetChanged();
                        talkAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                        // 新手引导
                        /*if (isResumed()) {
                            ((MainActivity) mContext).showForumGuide();
                        }*/
                    }
                    if (mList.isEmpty() && sectionList.isEmpty() && topList.isEmpty() && talkList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);


                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    lvList.stopLoadMore();
                    lvList.stopRefresh();
                    layoutProgress.setVisibility(View.GONE);
                    if (mList.isEmpty() && sectionList.isEmpty() && topList.isEmpty() && talkList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(1);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                });
    }

    //加载图片资源
    private SlideCycleView.ImageCycleViewListener mAdCycleViewListener = (position, v) -> TopicLink.jump(mContext, (SlidePicModel) v.getTag(), TopicLink.SOURCE_TYPE.BANNER);

    private void getNext() {
        ForumApi.getInstance().forumTopicsListGet("2", String.valueOf(page), String.valueOf(PageConstant.pageSize),
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
                getNext();
            }
        });
        gvSection.setOnItemClickListener((parent, view, position, id) -> {
            ForumIndexBoardModel forumBoardBriefModel = sectionList.get(position);
            //1：论坛版块 2：试用 3：兑换 4：达人"
            if (TextUtils.isEmpty(forumBoardBriefModel.getType())) {
                BoardActivity.launch(mContext);
            } else if ("1".equals(forumBoardBriefModel.getType())) {
                BoardDetailActivity.launch(mContext, forumBoardBriefModel.getId());
            } else if ("2".equals(forumBoardBriefModel.getType())) {
                SampleActivity.launch(mContext);
            } else if ("3".equals(forumBoardBriefModel.getType())) {
                ExchangeActivity.launch(mContext);
            } else if ("4".equals(forumBoardBriefModel.getType())) {
                TalentActivity.launch(mContext);
            }
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
        lvTopList.setOnItemClickListener((parent, view, position, id) -> {
            TopicBriefModel topicModel = topList.get(position);
            if (null != topicModel) {
                TopicDetailActivity.launch(mContext, topicModel.getTid());
            }
        });
        lvTopicList.setOnItemClickListener((parent, view, position, id) -> {
            TopicBriefModel topicModel = talkList.get(position);
            if (null != topicModel) {
                TopicDetailActivity.launch(mContext, topicModel.getTid());
            }
        });
        ivPublish.setOnClickListener(v -> {
            if (!UserManager.getInstance().isLogin()) {
                QuickLoginActivity.launch(mContext);
                return;
            }
            TopicSendActivity.launch(mContext);
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
        if (null != adList && adList.size() > 0) {
            layoutCircle.pushImageCycle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != adList && adList.size() > 0) {
            layoutCircle.startImageCycle();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        if (lvList == null)
            return;
        int firstVisiblePosition = lvList.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            lvList.autoRefresh();
        } else {
            lvList.smoothScrollToPosition(0);
        }
    }
}
