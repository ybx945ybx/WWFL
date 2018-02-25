package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haitao.R;
import com.haitao.adapter.VipStoreAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.VipRebateStoreModel;

/**
 * VIP商家页面
 */
public class VipStoreActivity extends BaseActivity {

    @BindView(R.id.content_view) XListView          mLvList;
    @BindView(R.id.msv)          MultipleStatusView mMsv;
    @BindView(R.id.ht_headview)  HtHeadView         mHtHeadview;

    private ArrayList<VipRebateStoreModel> mList;
    private VipStoreAdapter                mAdapter;
    private int                            mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_store);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        initData();
    }

    private void initData() {
        mMsv.showLoading();
        loadData();
    }

    private void initVars() {
        TAG = "Vip返利商家页面";
        mList = new ArrayList<>();
        mPage = 1;
    }

    private void initViews(Bundle savedInstanceState) {
        mAdapter = new VipStoreAdapter(mContext, mList);
        mLvList.setAdapter(mAdapter);
        mLvList.setAutoLoadEnable(true);
        mLvList.setPullLoadEnable(false);
        mLvList.setXListViewListener(new XListView.IXListViewListener() {
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
        mLvList.setOnItemClickListener((parent, view, position, id) -> {
            // 跳转到商家详情
        });
        mMsv.setOnRetryClickListener(v -> loadData());
    }

    private void loadData() {
        ForumApi.getInstance().userVipRebateStoresListPost(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mMsv.showContent();
                            mList.addAll(response.getData().getRows());
                            mLvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            mMsv.showEmpty();
                            mLvList.setPullLoadEnable(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    mMsv.showError();
                    showErrorToast(error);
                });
    }

    /**
     * 跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, VipStoreActivity.class));
    }
}