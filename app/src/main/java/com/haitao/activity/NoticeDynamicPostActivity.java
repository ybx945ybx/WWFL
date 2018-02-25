package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.DynamicPostAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.PostDynamicsMsgsListModelDataRows;

/**
 * 贴子动态
 * Created by penley on 16/3/8.
 */
public class NoticeDynamicPostActivity extends BaseActivity {
    private XListView                                    lvList;
    private ArrayList<PostDynamicsMsgsListModelDataRows> mList;
    private DynamicPostAdapter                           mAdapter;
    private int page = 1;
    private ViewGroup layoutProgress;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, NoticeDynamicPostActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_post);
        TAG = "帖子动态";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        initError();
        tvTitle.setText(R.string.dynamic_post);
        lvList = getView(R.id.lvList);

        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
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
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            PostDynamicsMsgsListModelDataRows object = (PostDynamicsMsgsListModelDataRows) parent.getItemAtPosition(position);
            TopicDetailActivity.launch(mContext, object.getTid(), object.getFloorNum());
        });
        btnRefresh.setOnClickListener(v -> getData());
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<>();
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        mAdapter = new DynamicPostAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        ForumApi.getInstance().userMsgsPostDynamicsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            mList.clear();
                        if (null != response.getData() && null != response.getData().getRows()) {
                            mList.addAll(response.getData().getRows());
                        }
                        lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(1);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                });
    }

}
