package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.RebateAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.FilterBsDlg;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.IfFilterModel;
import io.swagger.client.model.RebateModel;


/**
 * 我的返利
 */
public class MyRebateActivity extends BaseActivity {
    @BindView(R.id.hv_title) HtHeadView mHvTitle;         // 标题
    @BindView(R.id.lvList)   XListView  mLvList;          // 列表

    private ArrayList<RebateModel> mList;       // 列表数据
    private List<IfFilterModel>    mFilters;    // 筛选数据
    private RebateAdapter          mAdapter;
    public String type   = "";
    public String status = "";

    private View     layoutTop;
    private TextView tvAvailable;
    private TextView tvPending;
    private TextView tvAvailableTips;
    private TextView tvPendingTips;

    private int      mPage;  // 当前页数
    private String[] mSelectedIds;
    private String   mRebateIntro;

    /**
     * 跳转到当前页
     *
     * @param context mContext;
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, MyRebateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rebate);
        ButterKnife.bind(this);
        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
            //            BindPhoneActivity.launch(mContext, BindPhoneActivity.BING_NEW);
            FirstBindPhoneActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "我的返利";
        mPage = 1;
        mSelectedIds = new String[]{"", ""};
    }

    /**
     * 初始化视图
     */
    private void initView() {
        layoutTop = View.inflate(mContext, R.layout.layout_gold, null);
        tvAvailable = getView(layoutTop, R.id.tvGold);
        tvPending = getView(layoutTop, R.id.tvScore);
        tvAvailableTips = getView(layoutTop, R.id.tvGoldTips);
        tvPendingTips = getView(layoutTop, R.id.tvScoreTips);
        //        mTvRebateDesc = getView(layoutTop, R.id.tv_rebate_desc);
        tvAvailable.setTextColor(ContextCompat.getColor(mContext, R.color.orangeFF804D));
        tvAvailableTips.setText(R.string.my_available);
        tvPendingTips.setText(R.string.my_pending);
        layoutTop.setOnClickListener(null);
        mLvList = getView(R.id.lvList);
        mLvList.addHeaderView(layoutTop);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
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
        // 详情跳转
        mLvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvList.getHeaderViewsCount();
            if (index >= 0) {
                RebateModel obj = mList.get(index);
                if (null != obj) {
                    String type = obj.getType();
                    if (TextUtils.equals(type, "3")) {
                        OrderDetailActivity.launch(mContext, obj.getDataId());
                    } else if (TextUtils.equals(type, "2")) {
                        WithdrawDetailActivity.launch(mContext, obj.getDataId());
                    } else if (TextUtils.equals(type, "12")) {
                        RebateDetailActivity.launch(mContext, obj.getDataId());
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvAvailable.setText(String.format("$%s", UserManager.getInstance().getUser().current_money));
        tvPending.setText(String.format("$%s", UserManager.getInstance().getUser().waitcashback));
        mList = new ArrayList<RebateModel>();
        mAdapter = new RebateAdapter(mContext, mList);
        mLvList.setAdapter(mAdapter);
        mLvList.setAutoLoadEnable(true);
        mLvList.setPullLoadEnable(false);
        loadData();
    }

    public void loadData() {
        ForumApi.getInstance().userRebatesListGet(mSelectedIds[1], mSelectedIds[0], String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mLvList == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();

                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            // 返利说明
                            mRebateIntro = response.getData().getIntroTopicId();
                            // 列表数据
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            // 筛选信息
                            List<IfFilterModel> filters = response.getData().getFilters();
                            if (filters != null) {
                                mFilters = filters;
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            //                            ll_common_error.setVisibility(View.VISIBLE);
                            //                            setErrorType(0);
                            ToastUtils.show(mContext, "暂时没有数据");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                }, error -> {
                    if (mLvList == null)
                        return;
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                });
    }

    /**
     * 显示筛选
     *
     * @param v view
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
        }
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
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            tvAvailable.setText(String.format("$%s", UserManager.getInstance().getUser().current_money));
            tvPending.setText(String.format("$%s", UserManager.getInstance().getUser().waitcashback));
        }
    }

    /**
     * 提现
     */
    @OnClick(R.id.tv_withdraw)
    public void onClickWithdraw() {
        /*if ("0".equals(UserManager.getInstance().getUser().hasSetWithdrawPwd)) {
            new AlertDialog.Builder(mContext)
                    .setMessage(R.string.withdraw_pwd_not_set_hint)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        WithdrawPwdUpdateActivity.launch(mContext);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }*/
        UserObject user = UserManager.getInstance().getUser();
        if (HtApplication.isLogin()) {
            if (TextUtils.equals(user.hasSetWithdrawPwd, "1")
                    && TextUtils.equals(user.hasWithdrawAccount, "1")
                    && TextUtils.equals(user.hasBindedPhone, "1")) {
                WithdrawApplyActivity.launch(mContext);
            } else {
                WithdrawUnavailableActivity.launch(mContext);
            }
        } else {
            QuickLoginActivity.launch(mContext);
        }
    }

    /**
     * 筛选
     */
    @OnClick(R.id.tv_filter)
    public void onClickFilter(View v) {
        showFilter(v);
    }

    /**
     * 返利说明
     */
    @OnClick(R.id.tv_rebate_desc)
    public void onClickRebateDesc(View v) {
        if (!TextUtils.isEmpty(mRebateIntro)) {
            TopicDetailActivity.launch(mContext, mRebateIntro);
        }
    }
}
