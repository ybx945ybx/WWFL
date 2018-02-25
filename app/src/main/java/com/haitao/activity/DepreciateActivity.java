package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.DepreciateAdapter;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.dialog.DepreciateEditDialog;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.FollowingDealModel;

/**
 * 降价提醒列表页面
 */
public class DepreciateActivity extends BaseActivity {

    private XListView                     mListView;
    private DepreciateAdapter             mAdapter;
    private ArrayList<FollowingDealModel> mDealList;
    private ViewGroup                     layoutProgress;

    private DepreciateEditDialog depreciateEditDialog;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depreciate);

        initVars();
        initViews();
        initEvents();

        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }

        initData();
    }

    private void initVars() {
        TAG = "降价提醒";
    }

    private void initViews() {
//        initTop();
        initError();
//        tvTitle.setText("降价提醒");
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);

        mListView = getView(R.id.lvList);
        mListView.setVisibility(View.GONE);
        mListView.setPullRefreshEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setPullLoadEnable(false);

        mDealList = new ArrayList<>();
        mAdapter = new DepreciateAdapter(mContext, mDealList);
        mListView.setAdapter(mAdapter);
    }

    private void initEvents() {
        btnRefresh.setOnClickListener(v -> {
            page = 1;
            getData();
        });

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mListView.getHeaderViewsCount();
            if (index >= 0 && mDealList.size() > 0) {
                FollowingDealModel obg = mDealList.get(index);
                if (obg != null) {
                    WebActivity.launch(mContext, obg.getTitle(), obg.getDealUrl(), true);
                }
            }
        });

        mListView.setOnItemLongClickListener((parent, view, position, id) -> {
            int index = position - mListView.getHeaderViewsCount();
            if (index < 0)
                return true;
            FollowingDealModel obg = mDealList.get(index);
            new DepreciateEditDialog(mContext)
                    .setItemClickListener(new DepreciateEditDialog.ItemClickListener() {
                        @Override
                        public void onDelete(DepreciateEditDialog dialog) {
                            deleteDepreciate(obg.getTaskId());
                            dialog.dismiss();
                        }

                        @Override
                        public void onEdit(DepreciateEditDialog dialog) {
                            String uri = Constant.DEPRECIATE_URL + "?task_id=" + obg.getTaskId();

                            Logger.d(uri);
                            WebActivity.launch(mContext, true, "编辑提醒", uri);
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        });

        mListView.setXListViewListener(new XListView.IXListViewListener() {
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
    }

    /**
     * 删除降价提醒
     *
     * @param task_id
     */
    public void deleteDepreciate(String task_id) {
        ProgressDialogUtils.show(mContext, "正在删除...");
        ForumApi.getInstance()
                .userFollowingDealDelete(String.valueOf(1), task_id, response -> {
                    Logger.d(response.toString());
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        page = 1;
                        getData();
                        ToastUtils.show(mContext, "删除成功");
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }

    private void initData() {
        getData();

    }

    /**
     * 获取降价提醒列表
     */
    private void getData() {
        ForumApi.getInstance()
                .userFollowingDealsListGet(String.valueOf(1), String.valueOf(page), String.valueOf(PageConstant.pageSize),
                        response -> {
                            if (mListView == null)
                                return;
                            layoutProgress.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            mListView.stopRefresh();
                            mListView.stopLoadMore();
                            if ("0".equals(response.getCode())) {
                                if (response.getData() != null) {
                                    if (page == 1)
                                        mDealList.clear();

                                    if (response.getData().getRows() != null && response.getData().getRows().size() > 0) {
                                        //                                        response.loadData().getRows().get(6).setDealPicOrigin("http://st-prod.b0.upaiyun.com/prodimage/00380615d415a43b.jpg");
                                        mDealList.addAll(response.getData().getRows());
                                        Logger.d(TAG + response.getData().getRows().toString());
                                        mListView.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                                    } else {
                                        mListView.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                                    }

                                    mAdapter.notifyDataSetChanged();

                                }
                            }

                            if (mDealList.isEmpty()) {
                                ll_common_error.setVisibility(View.VISIBLE);
                                setErrorType(0);
                            } else {
                                ll_common_error.setVisibility(View.GONE);
                            }

                        }, error -> {
                            if (mListView == null)
                                return;
                            showErrorToast(error);
                            layoutProgress.setVisibility(View.GONE);
                            mListView.stopRefresh();
                            mListView.stopLoadMore();

                            if (mDealList.isEmpty()) {
                                ll_common_error.setVisibility(View.VISIBLE);
                                setErrorType(1);
                            } else {
                                ll_common_error.setVisibility(View.GONE);
                            }
                        });
    }

    /**
     * 跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, DepreciateActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
                finish();
            } else {
                initData();
            }
        }
    }
}
