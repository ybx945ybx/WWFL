package com.haitao.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.adapter.StoreFavAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.CollectionStoreModel;

/**
 * 收藏的商家
 *
 * @author 陶声
 */
public class FavStoreFragment extends BaseFragment {
    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private Unbinder                        unbinder;
    private StoreFavAdapter                 mAdapter;
    private ArrayList<CollectionStoreModel> mList;
    private int                             mPage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_list, null);
        unbinder = ButterKnife.bind(this, view);
        initVars();
        initViews(savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initVars() {
        TAG = "我的收藏 - 商家";
        mPage = 1;
        mList = new ArrayList<>();
    }


    private void initViews(Bundle savedInstanceState) {
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        // Adapter
        mAdapter = new StoreFavAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);
        // Listener
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                CollectionStoreModel collectionStoreModel = mList.get(index);
                if (collectionStoreModel != null) {
                    StoreDetailActivity.launchForResult(mContext, collectionStoreModel.getStoreId());
                }
            }
        });
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
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
        //        btnRefresh.setOnClickListener(v -> loadData());
        mMsv.setOnRetryClickListener(v -> initData());
    }

    public void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }

    public void loadData() {
        ForumApi.getInstance().userCollectionStoresListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    //                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
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
                            mMsv.showEmpty("暂时没有商家");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    //                    ProgressDialogUtils.dismiss();
                    mMsv.showError();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    showErrorToast(error);
                });
    }


    /**
     * 回到顶部
     */
    public void returnTop() {
        mLvContent.smoothScrollToPosition(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.DISMISS && requestCode == resultCode) {
            ((Activity) mContext).finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
