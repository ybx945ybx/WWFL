package com.haitao.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.HtItemTextView;
import com.haitao.view.dialog.WithdrawPwdDlg;
import com.haitao.view.dialog.WithdrawTypeSelectBsDlg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.UserWithdrawingIndexModel;
import io.swagger.client.model.UserWithdrawingIndexModelModesAmountLimits;
import io.swagger.client.model.UserWithdrawingModeModel;
import io.swagger.client.model.WithdrawingModesModelData;


/**
 * 申请提现
 */
public class WithdrawApplyActivity extends BaseActivity {

    @BindView(R.id.et_amount)          ClearEditText  mEtAmount;              // 提现金额
    @BindView(R.id.btn_submit)         TextView       mTvSubmit;              // 提交
    @BindView(R.id.tvRate)             TextView       mTvRate;
    @BindView(R.id.tv_info)            TextView       mTvInfo;
    @BindView(R.id.hitv_withdraw_type) HtItemTextView mHitvWithdrawType;      // 提现方式

    private String money = "", password = "";
    private String mAccountId = "", mModelName = "";
    private String                         mAccountName;
    private AlertDialog                    mDlgBindAccount;
    private List<UserWithdrawingModeModel> mUserModes;
    private String                         mMinAmount; // 单次最小金额
    private String                         mSingleLimit; // 单次最大金额
    private String mAvailableAmount = "0"; // 余额
    private List<UserWithdrawingIndexModelModesAmountLimits> mModeWithdrawLimits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_cash);
        ButterKnife.bind(this);
        initVars();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
            FirstBindPhoneActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "申请提现";
        mUserModes = new ArrayList<>();
        mSingleLimit = "0";
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mEtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                money = s.toString().trim();
                if (!TextUtils.isEmpty(money)) {
                    double amount = Double.valueOf(money);
                    if (amount < Double.valueOf(mMinAmount)) {
                        // 小于最低金额
                        mTvRate.setText(String.format(getString(R.string.min_withdraw_amount_placeholder), mMinAmount));
                        mTvSubmit.setEnabled(false);
                    } else if (amount > Double.valueOf(mSingleLimit)) {
                        // 超出上限
                        mTvRate.setText(String.format(getString(R.string.max_withdraw_amount_placeholder),
                                Double.valueOf(mSingleLimit) < Double.valueOf(mAvailableAmount) ? mSingleLimit : mAvailableAmount));
                        mTvSubmit.setEnabled(false);
                    } else {
                        // 金额范围合格
                        mTvRate.setText("");
                        mTvSubmit.setEnabled(true);
                    }
                } else {
                    // 金额为空
                    mTvRate.setText(String.format(getString(R.string.min_withdraw_amount_placeholder), mMinAmount));
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        refreshView();
        getData();
    }

    private void getData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        // 加载首页数据
        loadIndexData();
    }

    /**
     * 加载首页数据
     */
    private void loadIndexData() {
        ForumApi.getInstance().userAccountWithdrawingIndexGet(response -> {
            ProgressDialogUtils.dismiss();
            if (TextUtils.equals(response.getCode(), "0")) {
                UserWithdrawingIndexModel data = response.getData();
                if (data != null) {
                    // 最低金额
                    mMinAmount = data.getMinimumAmount();
                    mTvRate.setText(String.format(getString(R.string.min_withdraw_amount_placeholder), mMinAmount));
                    // 可用金额
                    mAvailableAmount = data.getBalance();
                    mEtAmount.setHint(String.format(getString(R.string.cash_tips), mAvailableAmount));
                    // 单次最高提现金额（不同提现方式不同）
                    //                    mModeWithdrawLimits = data.getModesAmountLimits();
                    // 提示信息
                    mTvInfo.setText(data.getComment());
                    // 加载账户列表
                    loadAccountList();
                }
            } else {
                ToastUtils.show(mContext, response.getMsg());
            }
        }, error -> {
            showErrorToast(error);
            ProgressDialogUtils.dismiss();
        });
    }

    /**
     * 读取账户列表
     */
    private void loadAccountList() {
        ForumApi.getInstance().commonWithdrawingModesGet(response -> {
            if (mTvSubmit == null)
                return;
            if ("0".equals(response.getCode())) {
                WithdrawingModesModelData data = response.getData();
                if (data != null) {
                    if (data.getUserModes() != null && data.getUserModes().size() != 0)
                        mUserModes.clear();
                    mUserModes.addAll(data.getUserModes());
                    // 过滤，只保留审核通过的
                    Iterator<UserWithdrawingModeModel> iter = mUserModes.iterator();
                    while (iter.hasNext()) {
                        UserWithdrawingModeModel item = iter.next();
                        if (!TextUtils.equals(item.getStatus(), "1")) {
                            iter.remove();
                        }
                    }
                    // 默认选择第一个提现方式
                    if (mUserModes.size() > 0) {
                        UserWithdrawingModeModel userModel = mUserModes.get(0);
                        mAccountId = userModel.getAccountId();
                        mModelName = userModel.getModeName();
                        mAccountName = userModel.getAccount();
                        mSingleLimit = userModel.getAmountLimit();
                        refreshView();
                    }
                }
            }
        }, error -> {
            if (mTvSubmit == null)
                return;
            showErrorToast(error);
        });
    }

    private void refreshView() {
        if (TextUtils.isEmpty(mModelName) || TextUtils.isEmpty(mAccountName)) {
            mHitvWithdrawType.clearRightText();
        } else {
            mHitvWithdrawType.setRightText(mModelName + " " + mAccountName);
        }
        //        if (!TextUtils.isEmpty(mModeKey)) {
        //            mSingleLimit = getWithdrawLimit(mModeKey);
        //        }
    }

    /**
     * 验证提现密码
     */
    private void inputPassword() {
        new WithdrawPwdDlg(mContext)
                .setOnDlgClickListener(new WithdrawPwdDlg.OnDlgClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, String pwd) {
                        password = pwd;
                        if (TextUtils.isEmpty(password)) {
                            ToastUtils.show(mContext, "输入您的提现密码");
                            return;
                        }
                        submit();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 提现网络请求
     */
    private void submit() {
        ProgressDialogUtils.show(mContext, "正在提交……");
        ForumApi.getInstance().userWithdrawingAccountAccountIdActingPost(mAccountId, money, password,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            UserObject userObject = UserManager.getInstance().getUser();
                            userObject.current_money = response.getData().getBalance();
                            userObject.waitcashback = response.getData().getFreezedBalance();
                            UserManager.getInstance().setUser(userObject);
                        }
                        SuccessFeedbackActivity.launch(mContext, SuccessFeedbackActivity.SuccessType.WITHDRAW);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }

    /**
     * 确认提现
     */
    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        money = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAccountId)) {
            ToastUtils.show(mContext, R.string.cash_type_none);
            return;
        }
        inputPassword();
    }

    /**
     * 单次提现金额上限
     */
    /*private String getWithdrawLimit(String modeKey) {
        if (mModeWithdrawLimits != null && mModeWithdrawLimits.size() > 0) {
            for (UserWithdrawingIndexModelModesAmountLimits item : mModeWithdrawLimits) {
                if (TextUtils.equals(item.getModeKey(), modeKey)) {
                    return item.getLimit();
                }
            }
        }
        return null;
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
        } else if (requestCode == TransConstant.REFRESH && requestCode == resultCode && null != data) {
            mAccountId = data.getStringExtra(TransConstant.ID);
            mModelName = data.getStringExtra(TransConstant.TITLE);
            mAccountName = data.getStringExtra(TransConstant.VALUE);
            refreshView();
        }
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawApplyActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 选择提现方式
     */
    @OnClick(R.id.hitv_withdraw_type)
    public void onClickWithdrawType() {
        if (mUserModes.size() > 0) {
            new WithdrawTypeSelectBsDlg(mContext, mUserModes, mAccountId)
                    .setOnTypeSelectCallback((dlg, accountId, accountName, modeName, maxAmount) -> {
                        mAccountId = accountId;
                        mModelName = modeName;
                        mAccountName = accountName;
                        mSingleLimit = maxAmount;
                        refreshView();
                        dlg.dismiss();
                    })
                    .show();
        } else {
            showToast(ToastType.WARNING, getString(R.string.no_available_withdraw_account_now));
        }
    }

    /**
     * 提现记录
     */
    @OnClick(R.id.tv_withdraw_record)
    public void onClickWithdrawRecord() {
        WithdrawRecordActivity.launch(mContext);
    }

    /**
     * 管理提现账户
     */
    @OnClick(R.id.tv_withdraw_account_manage)
    public void onClickWithdrawAccountManage() {
        WithdrawAccountActivity.launch(mContext);
    }
}
