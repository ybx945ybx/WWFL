package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.SubBoardSelectAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumSubBoardModel;

/**
 * 全部话题
 */
public class SubBoardSelectActivity extends BaseActivity {

    private XListView                     lvList;
    private SubBoardSelectAdapter         mAdapter;
    private ArrayList<ForumSubBoardModel> mList;

    private int mPage = 1;

    private String selectBoardId = "", selectSubBoardId = "";

    private ViewGroup  layoutProgress;
    private HtHeadView mHtHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "发帖选择分类";
        if (null != getIntent()) {
            selectBoardId = getIntent().getStringExtra(TransConstant.ID);
            selectSubBoardId = getIntent().getStringExtra(TransConstant.VALUE);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initTop();
        initError();
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
        mHtHeadView.setCenterText(getString(R.string.select_category));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
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
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                ForumSubBoardModel forumSubBoardModel = mList.get(index);
                if (null != forumSubBoardModel) {
                    Intent intent = new Intent();
                    intent.putExtra(TransConstant.TYPE, "category");
                    intent.putExtra(TransConstant.ID, forumSubBoardModel.getSubBoardId());
                    intent.putExtra(TransConstant.TITLE, forumSubBoardModel.getSubBoardName());
                    setResult(TransConstant.REFRESH, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<ForumSubBoardModel>();
        mAdapter = new SubBoardSelectAdapter(mContext, mList);
        mAdapter.selectBoardId = selectSubBoardId;
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        mPage = 1;
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().forumBoardBoardIdIndexGet(selectBoardId, "0",
                response -> {
                    if (lvList == null)
                        return;
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        mList.clear();
                        if (null != response.getData() && null != response.getData().getSubBoards() && response.getData().getSubBoards().size() > 0) {
                            for (ForumSubBoardModel forumBoardModel : response.getData().getSubBoards()) {
                                if ("1".equals(forumBoardModel.getAllowedToPost())) {
                                    mList.add(forumBoardModel);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mList.isEmpty()) {
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
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, SubBoardSelectActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String boardId) {
        Intent intent = new Intent(context, SubBoardSelectActivity.class);
        intent.putExtra(TransConstant.ID, boardId);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String boardId, String subBoardId) {
        Intent intent = new Intent(context, SubBoardSelectActivity.class);
        intent.putExtra(TransConstant.ID, boardId);
        intent.putExtra(TransConstant.VALUE, subBoardId);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }
}
