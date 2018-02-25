package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.adapter.DealAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.DealModel;
import io.swagger.client.model.DealsListModelData;

/**
 * 精选推荐消息列表
 */
public class NoticeRecommendActivity extends BaseActivity {

    private XListView            mListview;
    private ArrayList<DealModel> latestdiscountList;
    private DealAdapter          latestDiscountAdapter;

    private LinearLayout llytprogress;

    private int page = 1;
    private HtHeadView mHtHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_recommend);

        initVars();
        initViews();
        initEvents();
        initData();
    }

    private void initVars() {
        latestdiscountList = new ArrayList<>();

    }

    private void initViews() {
        //        initTop();
        //        tvTitle.setText("精选推荐");
        initError();

        mListview = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText("精选推荐");

        mListview.setAutoLoadEnable(true);
        mListview.setPullLoadEnable(false);
        mListview.setPullRefreshEnable(true);
        mListview.setVisibility(View.GONE);
        latestDiscountAdapter = new DealAdapter(mContext, latestdiscountList);
        mListview.setAdapter(latestDiscountAdapter);

        llytprogress = getView(R.id.llProgress_common_progress);
        llytprogress.setVisibility(View.VISIBLE);

    }

    private void initEvents() {
        btnRefresh.setOnClickListener(v -> {
            page = 1;
            loadData();
        });

        mListview.setXListViewListener(new XListView.IXListViewListener() {
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

        mListview.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mListview.getHeaderViewsCount();
            if (index >= 0) {
                DealModel dealModel = latestdiscountList.get(index);
                if (dealModel != null) {
                    DiscountDetailActivity.launch(mContext, dealModel.getDealId());
                }
            }
        });
    }

    private void initData() {
        loadData();
    }

    /**
     * 首页数据
     */
    private void loadData() {
        ForumApi.getInstance()
                .userMsgsRecommendedDealsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                        response -> {
                            mListview.stopLoadMore();
                            mListview.stopRefresh();
                            llytprogress.setVisibility(View.GONE);
                            mListview.setVisibility(View.VISIBLE);
                            if ("0".equals(response.getCode())) {
                                DealsListModelData data = response.getData();
                                if (null != data) {
                                    if (1 == page) {
                                        latestdiscountList.clear();
                                    }
                                    if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                        Logger.d(TAG + response.getData().getRows().toString());
                                        latestdiscountList.addAll(response.getData().getRows());
                                        mListview.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                                    } else {
                                        mListview.setPullLoadEnable(false);
                                    }
                                    latestDiscountAdapter.notifyDataSetChanged();
                                }
                            }

                            if (latestdiscountList.isEmpty()) {
                                ll_common_error.setVisibility(View.VISIBLE);
                                setErrorType(0);
                            } else {
                                ll_common_error.setVisibility(View.GONE);
                            }

                        }, error -> {
                            llytprogress.setVisibility(View.GONE);
                            mListview.stopLoadMore();
                            mListview.stopRefresh();

                            if (latestdiscountList.isEmpty()) {
                                ll_common_error.setVisibility(View.VISIBLE);
                                setErrorType(1);
                            } else {
                                ll_common_error.setVisibility(View.GONE);
                            }
                        });
        return;
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String type) {
        Intent intent = new Intent(context, NoticeRecommendActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }
}
