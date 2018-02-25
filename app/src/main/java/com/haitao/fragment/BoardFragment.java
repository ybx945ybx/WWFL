package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.adapter.BoardAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumBoardModel;


/**
 * 版块
 */
public class BoardFragment extends BaseFragment {
    private XListView                  lvList;
    private ArrayList<ForumBoardModel> mList;
    private BoardAdapter               mAdapter;

    private String secId = "";

    //加载动画
    private ViewGroup layoutProgress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "全部版块 - 版块列表";
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shaidan, null);
        initError(view);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.VALUE)) {
                secId = bundle.getString(TransConstant.VALUE);
            }
        }
        lvList = getView(view, R.id.lvList);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(view, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        return view;
    }

    public void initData() {
        mList = new ArrayList<ForumBoardModel>();
        mAdapter = new BoardAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        ForumApi.getInstance().forumSectionSectionIdBoardsGet(secId,
                response -> {
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        mList.clear();
                        if (null != response.getData() && response.getData().size() > 0) {
                            mList.addAll(response.getData());
                        }
                        mAdapter.largeSize = mList.size();
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
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
    }


    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                ForumBoardModel forumBoardModel = mList.get(index);
                if (null != forumBoardModel) {
                    BoardDetailActivity.launch(mContext, forumBoardModel.getBoardId());
                }
            }

        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
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
