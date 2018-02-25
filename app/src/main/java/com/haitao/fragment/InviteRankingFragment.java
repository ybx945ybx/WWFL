package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.InviteRankingAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.InviterModel;
import io.swagger.client.model.InvitersListModelData;

/**
 * 邀请排行榜
 *
 * @author 陶声
 * @since 2017-08-15
 */

public class InviteRankingFragment extends BaseFragment {

    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder             unbinder;
    private int                  mPage;
    private List<InviterModel>   mList;
    private InviteRankingAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_common_list_scroll, null);
        unbinder = ButterKnife.bind(this, layout);
        initVars();
        initViews(savedInstanceState);
        return layout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //        loadData();
        initData();
    }

    private void initVars() {
        TAG = "邀请好友 - 邀请排行榜";
        mList = new ArrayList<>();
        mAdapter = new InviteRankingAdapter(mContext, mList);
    }


    private void initViews(Bundle savedInstanceState) {
        mLvContent.setAdapter(mAdapter);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullRefreshEnable(false);
        mLvContent.setPullLoadEnable(false);
        // listener
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

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

    /**
     * 加载分页数据
     */
    private void loadData() {
        ForumApi.getInstance().userInvitersListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mMsv.showContent();
                    mLvContent.stopLoadMore();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        InvitersListModelData data = response.getData();
                        if (data != null) {
                            if (1 == mPage) {
                                mList.clear();
                            }
                            if (null != response.getData()) {
                                if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                    mList.addAll(response.getData().getRows());
                                }
                                mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                            }
                            if (mList.isEmpty()) {
                                mMsv.showEmpty("暂时没有数据");
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mMsv.showError();
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    mMsv.showError();
                    showErrorToast(error);
                    mLvContent.stopLoadMore();
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
