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
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TopicModel;

/**
 * 商家详情 - 晒单
 */
public class StoreTopicFragment extends BaseFragment {
    private MultipleStatusView    msv;
    private XListView             lvList;
    private ArrayList<TopicModel> mList;
    private TopicAdapter          mAdapter;

    private String name = "";
    private String type = "";
    private int    page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        mList = new ArrayList<>();
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.TITLE)) {
                name = bundle.getString(TransConstant.TITLE);
            }
            if (bundle.containsKey(TransConstant.TYPE)) {
                type = bundle.getString(TransConstant.TYPE);
            }
        }
        TAG = "商家详情 - " + (TextUtils.equals(type, "1") ? "帖子" : "晒单");
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);
        msv = getView(view, R.id.msv);
        lvList = getView(view, R.id.content_view);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(false);
        lvList.setPullLoadEnable(false);

        View layoutHeader = View.inflate(mContext, R.layout.layout_board_top_topics, null);
        lvList.addHeaderView(layoutHeader);

        mAdapter = new TopicAdapter(mContext, mList);
        mAdapter.setOnItemClickLitener((position, object) -> mList.set(position, object));
        lvList.setAdapter(mAdapter);
        return view;
    }

    private void initEvent() {
        msv.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msv.showLoading();
                getData();
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
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });
    }

    public void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().searchingKeywordsTopicsListGet(name, type, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (msv == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    msv.showContent();
                    if (1 == page) {
                        mList.clear();
                    }
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            //帖子
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        } else {
                            lvList.setPullLoadEnable(false);
                        }
                        if (mList.isEmpty()) {
                            msv.showEmpty("1".equals(type) ? "暂无相关帖子" : "暂无相关晒单");

                        }
                        mAdapter.notifyDataSetChanged();

                    } else {
                        ToastUtils.show(mContext, response.getMsg());

                    }
                }, error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });

    }

}
