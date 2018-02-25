package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.NoticeFriendAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.FriendsRequestionsListModelDataRows;

/**
 * 好友申请
 * Created by apple on 16/3/1.
 */
public class NoticeFriendActivity extends BaseActivity {

    private XListView                                      lvList;
    private ArrayList<FriendsRequestionsListModelDataRows> mList;
    private NoticeFriendAdapter                            mAdapter;

    private ViewGroup layoutProgress;

    private int page = 1;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, NoticeFriendActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        TAG = "好友申请";
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
        tvTitle.setText("好友申请");
        lvList = getView(R.id.lvList);
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setPullRefreshEnable(true);
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

        });
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<FriendsRequestionsListModelDataRows>();
        mAdapter = new NoticeFriendAdapter(mContext, mList);
        mAdapter.setOnInnerClickLitener(this::operateRequest);
        lvList.setAdapter(mAdapter);
        getData();

    }

    private void operateRequest(int position) {
        FriendsRequestionsListModelDataRows obj = mList.get(position);
        ForumApi.getInstance().userFriendFriendUidRequestingPut(obj.getFriendUid(), "1",
                response -> {
                    if ("0".equals(response.getCode())) {
                        obj.setStatus("1");
                        mList.set(position, obj);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {

                });
    }

    private void getData() {
        ForumApi.getInstance().userMsgsRequestionsBeFriendsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
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
