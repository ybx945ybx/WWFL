package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haitao.R;
import com.haitao.adapter.TopicTopAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TopicBriefModel;

/**
 * 全部话题
 */
public class TopicTalkActivity extends BaseActivity {

    private XListView                  lvList;
    private TopicTopAdapter            mAdapter;
    private ArrayList<TopicBriefModel> mList;

    private int page = 1;
    private HtHeadView mHtHeadView;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, TopicTalkActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        TAG = "全部话题";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initTop();
        //        tvTitle.setText("全部话题");
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText("全部话题");
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
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
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                TopicBriefModel topicBriefModel = mList.get(index);
                if (topicBriefModel != null) {
                    TopicDetailActivity.launch(mContext, topicBriefModel.getTid());
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<TopicBriefModel>();
        mAdapter = new TopicTopAdapter(mContext, mList);
        mAdapter.isTop = false;
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        page = 1;
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().forumTalkTopicsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopLoadMore();
                    lvList.stopRefresh();
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            mList.clear();
                        if (null != response.getData() && null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mList.addAll(response.getData().getRows());
                            lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            mAdapter.largeSize = mList.size();
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
                    lvList.stopRefresh();
                });
    }


}
