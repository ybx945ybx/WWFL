package com.haitao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BoardActivity;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.TalentActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TopicSendActivity;
import com.haitao.adapter.BoardAdapter;
import com.haitao.adapter.TalentRecommendAdapter;
import com.haitao.adapter.TopicAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.FullListView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumBoardModel;
import io.swagger.client.model.TalentModel;
import io.swagger.client.model.TopicModel;


/**
 * 论坛- 动态
 */
public class ForumMovementFragment extends BaseFragment {
    private XListView             lvList;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    //加载动画
    private ViewGroup layoutProgress;

    //顶部
    private View                       headerView;
    //版块
    private FullListView               lvBoard;
    private ArrayList<ForumBoardModel> boardList;
    private BoardAdapter               boardAdapter;
    //推荐达人
    private TextView                   tvSubTitle;
    private GridView                   gvTalent;
    private ArrayList<TalentModel>     talentList;
    private TalentRecommendAdapter     talentAdapter;

    private ImageView ivPublish;

    //判断是否上滑或下滑
    private int   mTouchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int   direction;

    private int page = 1;

    private ChangeReceiver mChangeReceiver;

    private class ChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_LOGIN:
                        case TransConstant.BROAD_LOGOUT:
                            lvList.autoRefresh();
                            break;
                        case TransConstant.BROAD_SECTION_FAV:
                            lvList.autoRefresh();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "社区 - 动态";
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
        ivPublish = getView(view, R.id.ivPublish);
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        return view;
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.layout_follow_header, null);
        lvList.addHeaderView(headerView);
        lvBoard = getView(headerView, R.id.lvBoard);
        tvSubTitle = getView(headerView, R.id.tvSubTitle);
        gvTalent = getView(headerView, R.id.gvTalent);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getData();
    }

    public void initData() {
        mChangeReceiver = new ChangeReceiver();
        mList = new ArrayList<TopicModel>();
        mAdapter = new TopicAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);

        boardList = new ArrayList<ForumBoardModel>();
        boardAdapter = new BoardAdapter(mContext, boardList);
        boardAdapter.setOnCallbackLitener(() -> BoardActivity.launch(mContext));
        lvBoard.setAdapter(boardAdapter);

        talentList = new ArrayList<TalentModel>();
        talentAdapter = new TalentRecommendAdapter(mContext, talentList);
        gvTalent.setAdapter(talentAdapter);
        ivPublish.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        page = 1;
        ForumApi.getInstance().forumIndexFollowedGet("100", "6", String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            mList.clear();
                            boardList.clear();
                            talentList.clear();
                            if (null != response.getData().getTalents() && response.getData().getTalents().size() > 0) {
                                talentList.addAll(response.getData().getTalents());
                                talentList.add(null);
                                if (null != talentList && talentList.size() > 0) {
                                    int            size          = talentList.size();
                                    int            length        = 60;
                                    DisplayMetrics dm            = mContext.getResources().getDisplayMetrics();
                                    float          density       = dm.density;
                                    int            gridviewWidth = (int) ((size * (length + 16) + 0) * density);
                                    int            itemWidth     = (int) (length * density);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                                    gvTalent.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                                    gvTalent.setColumnWidth(itemWidth); // 设置列表项宽
                                    gvTalent.setHorizontalSpacing((int) (16 * density)); // 设置列表项水平间距
                                    gvTalent.setStretchMode(GridView.NO_STRETCH);

                                    gvTalent.setNumColumns(size); // 设置列数量=列表集合数
                                    gvTalent.setAdapter(talentAdapter);
                                }
                            }
                            if (null != response.getData().getBoards() && response.getData().getBoards().size() > 0) {
                                boardList.addAll(response.getData().getBoards());
                            }
                            boardAdapter.isClose = false;
                            boardAdapter.largeSize = 0;
                            lvBoard.setAdapter(boardAdapter);
                            //关注的帖子
                            if (null != response.getData().getTopics() && response.getData().getTopics().size() > 0) {
                                mList.addAll(response.getData().getTopics());
                                lvList.setPullLoadEnable(response.getData().getTopics().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (mList.isEmpty() && boardList.isEmpty() && talentList.isEmpty()) {
                        headerView.setVisibility(View.GONE);
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                        headerView.setVisibility(View.VISIBLE);
                    }
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if (mList.isEmpty() && boardList.isEmpty() && talentList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(1);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                });
    }

    private void getNext() {
        ForumApi.getInstance().forumTopicsListGet("3", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
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
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    lvList.stopLoadMore();
                }
        );
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

        lvBoard.setOnItemClickListener((parent, view, position, id) -> {
            ForumBoardModel forumBoardModel = boardList.get(position);
            if (null != forumBoardModel)
                BoardDetailActivity.launch(mContext, forumBoardModel.getBoardId());
        });

        gvTalent.setOnItemClickListener((parent, view, position, id) -> {
            TalentModel talentModel = talentList.get(position);
            if (null != talentModel && !TextUtils.isEmpty(talentModel.getUid())) {
                TalentDetailActivity.launch(mContext, talentModel.getUid());
            } else {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransConstant.CHANGE_BROADCAST);
        mContext.registerReceiver(mChangeReceiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mChangeReceiver);
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
