package com.haitao.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.WithdrawAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.TagObject;
import com.haitao.model.WithdrawObject;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.FilterBsDlg;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.PopListView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.IfFilterModel;
import io.swagger.client.model.WithdrawingRecordModel;

/**
 * 提现列表
 */
public class WithdrawRecordActivity extends BaseActivity {

    @BindView(R.id.ht_headview)  HtHeadView         mHtHeadview;
    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    private ArrayList<WithdrawingRecordModel> mList;
    private WithdrawAdapter                   mAdapter;
    private List<IfFilterModel>               mFilters;    // 筛选数据
    private String[]                          mSelectedIds;


    private int mPage;

    //    private TextView    tvStatus;
    private PopListView statusPopListView;
    private ArrayList<TagObject> statusList            = null;
    private int                  currentStatusPosition = 0;
    private String               status                = "";

    protected IViewContext<WithdrawObject, IEntityService<WithdrawObject>> commandViewContext = VF.<WithdrawObject>getDefault(WithdrawObject.class);
    private AlertDialog.Builder mDlgSetWithdrawPwd;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);
        ButterKnife.bind(this);
        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
        } else if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
            // 没有绑定手机的，需要绑定手机
            FirstBindPhoneActivity.launch(mContext);
        } else {
            initData();
        }
    }

    private void initVars() {
        TAG = "提现记录";
        mPage = 1;
        mSelectedIds = new String[]{"", ""};
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initError();
        mHtHeadview.setOnRightClickListener(this::showFilter);
        mLvContent = getView(R.id.content_view);
        //        tvStatus = getView(R.id.tvStatus);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                WithdrawingRecordModel withdrawingRecordModel = mList.get(index);
                if (withdrawingRecordModel != null) {
                    WithdrawDetailActivity.launch(mContext, withdrawingRecordModel.getRecordId());
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
        mMsv.setOnRetryClickListener(v -> {
            mMsv.showLoading();
            loadData();
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<WithdrawingRecordModel>();
        mAdapter = new WithdrawAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);
        mLvContent.setPullLoadEnable(false);
        mLvContent.setAutoLoadEnable(true);
        mMsv.showLoading();
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().userWithdrawingRecordsListGet(mSelectedIds[0], mSelectedIds[1], String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    mMsv.showContent();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    if (response != null && "0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            // 筛选信息
                            List<IfFilterModel> filters = response.getData().getFilters();
                            if (filters != null) {
                                mFilters = filters;
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有提现记录");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }

                }, error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    mMsv.showError();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });
        //        mLvContent.setSelection(0);
    }

    /**
     * 弹出筛选框
     */
    private void showFilter(View v) {
        if (mFilters != null) {
            // 底部弹框
            FilterBsDlg filterBsDlg = new FilterBsDlg(mContext, mFilters, mSelectedIds)
                    .setOnFilterClickListener(positions -> {
                        mSelectedIds = positions;
                        Logger.d(mSelectedIds);
                        PopWindowUtils.dismiss();
                        mPage = 1;
                        loadData();
                    });
            filterBsDlg.show();

            /*FilterPopView filterPopView = new FilterPopView(mContext, mFilters, mSelectedIds)
                    .setOnFilterClickListener(new FilterPopView.OnFilterClickListener() {
                        @Override
                        public void onConfirm(String[] positions) {
                            mSelectedIds = positions;
                            Logger.d(mSelectedIds);
                            PopWindowUtils.dismiss();
                            mPage = 1;
                            loadData();
                        }

                        @Override
                        public void onReset(String[] positions) {
                            mSelectedIds = positions;
                            Logger.d(mSelectedIds);
                            PopWindowUtils.dismiss();
                            mPage = 1;
                            loadData();
                        }
                    });
            PopWindowUtils.show(mContext, v, filterPopView);*/
        }
    }

    /**
     * 申请提现
     */
    /*@OnClick(R.id.tv_withdraw)
    public void onClickWithdrawApply() {
        if ("0".equals(UserManager.getInstance().getUser().funds_pwd)) {
            if (mDlgSetWithdrawPwd == null) {
                mDlgSetWithdrawPwd = new AlertDialog.Builder(mContext)
                        .setMessage("您还未设置提现密码，设置成功后可以进行提现操作。")
                        .setPositiveButton("设置密码", (dialog, which) -> {
                            dialog.dismiss();
                            WithdrawPwdUpdateActivity.launch(mContext);
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            dialog.dismiss();
                        });
            }
            mDlgSetWithdrawPwd.show();
            return;
        }
        WithdrawApplyActivity.launch(mContext);
    }*/
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

    /**
     * 筛选
     */
    @OnClick(R.id.tv_filter)
    public void onClickFilter(View view) {
        showFilter(mHtHeadview);
    }
}
