package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haitao.R;
import com.haitao.adapter.UnionpayShopListAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import io.swagger.client.model.EnteredStoreModel;

/**
 * 全部线下返利商家列表
 */
public class UnionPayShopListActivity extends BaseActivity {

    @BindView(R.id.msv)          MultipleStatusView msv;
    @BindView(R.id.content_view) XListView          listView;

    private UnionpayShopListAdapter      mAdapter;
    private ArrayList<EnteredStoreModel> mList;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay_shop_list);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        mList = new ArrayList<>();
    }

    private void initView() {
        msv = getView(R.id.msv);
        listView = getView(R.id.content_view);
        mAdapter = new UnionpayShopListAdapter(mContext, mList);
        listView.setAdapter(mAdapter);
        listView.setAutoLoadEnable(true);
        listView.setPullRefreshEnable(true);
    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> {
            msv.showLoading();
            getData();
        });
        listView.setXListViewListener(new XListView.IXListViewListener() {
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
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - listView.getHeaderViewsCount();
            if (index >= 0) {
                EnteredStoreModel storeModel = mList.get(index);
                if (storeModel != null) {
                    UnionPayShopDetailActivity.launch(mContext, storeModel.getStoreId());
                }
            }
        });
    }

    private void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().storeOfflineStoresListGet(null, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    Logger.d(response.getData());
                    if (msv == null)
                        return;
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    msv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == page) {
                            mList.clear();
                        }
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mList.addAll(response.getData().getRows());

                            Logger.d(mList.toString());

                            listView.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            listView.setPullLoadEnable(false);
                        }
                        if (mList.isEmpty()) {
                            msv.showEmpty("暂无商家");

                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());

                    }
                },
                error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                    listView.stopRefresh();
                    listView.stopLoadMore();
                });
    }

    /**
     * 跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, UnionPayShopListActivity.class));
    }

}
