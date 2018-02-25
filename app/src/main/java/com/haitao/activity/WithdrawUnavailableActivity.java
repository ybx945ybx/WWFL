package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.model.UserObject;
import com.haitao.view.HtItemTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现 - 不可提现
 */
public class WithdrawUnavailableActivity extends BaseActivity {

    @BindView(R.id.hitv_bind_phone_number) HtItemTextView mHitvBindPhoneNumber; // 绑定手机号
    @BindView(R.id.hitv_withdraw_pwd)      HtItemTextView mHitvWithdrawPwd;     // 提现密码绑定
    @BindView(R.id.hitv_withdraw_account)  HtItemTextView mHitvWithdrawAccount; // 提现账户
    @BindView(R.id.tv_step_info)           TextView       mTvStepInfo;          // 步骤信息

    private boolean mHasSetWithdrawPwd;     // 已经设置提现密码
    private boolean mHasWithdrawAccount;    // 已经绑定提现账户
    private boolean mHasBindedPhone;        // 已经绑定手机

    private UserObject mUserObj;
    private int[] imgRes = {R.mipmap.ic_complete, R.mipmap.ic_uncomplete};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_unavailable);
        ButterKnife.bind(this);
        initVars();
        initViews();
    }

    private void initVars() {
        mUserObj = UserManager.getInstance().getUser();
        if (mUserObj != null) {
            mHasBindedPhone = TextUtils.equals(mUserObj.hasBindedPhone, "1");
            mHasSetWithdrawPwd = TextUtils.equals(mUserObj.hasSetWithdrawPwd, "1");
            mHasWithdrawAccount = TextUtils.equals(mUserObj.hasWithdrawAccount, "1");
        }
    }

    private void initViews() {
        setBindPhoneView();
        setWithdrawPwdView();
        setWithdrawAccountView();
        setStepView();
    }

    /**
     * 绑定手机号码View
     */
    private void setBindPhoneView() {
        if (mHasBindedPhone) {
            mHitvBindPhoneNumber.setImgLeft(imgRes[0]);
            mHitvBindPhoneNumber.setRightText(mUserObj.area + " " + mUserObj.mobile);
        } else {
            mHitvBindPhoneNumber.setImgLeft(imgRes[1]);
            mHitvBindPhoneNumber.setRightText("未设置");
        }
    }

    /**
     * 提现账户View
     */
    private void setWithdrawAccountView() {
        if (mHasWithdrawAccount) {
            mHitvWithdrawAccount.setImgLeft(imgRes[0]);
            mHitvWithdrawAccount.setRightText("已设置");
        } else {
            mHitvWithdrawAccount.setImgLeft(imgRes[1]);
            mHitvWithdrawAccount.setRightText("未设置");
        }
    }

    /**
     * 提现密码View
     */
    private void setWithdrawPwdView() {
        if (mHasSetWithdrawPwd) {
            mHitvWithdrawPwd.setImgLeft(imgRes[0]);
            mHitvWithdrawPwd.setRightText("已设置");
        } else {
            mHitvWithdrawPwd.setImgLeft(imgRes[1]);
            mHitvWithdrawPwd.setRightText("未设置");
        }
    }

    /**
     * 更新还需完成的步骤数
     */
    private void setStepView() {
        int step = 0;
        if (!mHasBindedPhone)
            step++;
        if (!mHasWithdrawAccount)
            step++;
        if (!mHasSetWithdrawPwd)
            step++;
        mTvStepInfo.setText(String.format(getString(R.string.withdraw_unavailable_hint), step));
    }

    /**
     * 绑定手机号
     */
    @OnClick(R.id.hitv_bind_phone_number)
    public void onMHitvBindPhoneNumberClicked() {
        if (mHasBindedPhone) {
            // 修改绑定
            Bundle bundle = new Bundle();
            bundle.putInt(TransConstant.TYPE, BindPhoneActivity.BING_VERIFY);
            bundle.putString(TransConstant.CODE, mUserObj.mobile);
            bundle.putString(TransConstant.AREA_CODE, mUserObj.area);
            BindPhoneActivity.launch(mContext, bundle);
        } else {
            FirstBindPhoneActivity.launch(mContext);
        }
    }

    /**
     * 提现密码
     */
    @OnClick(R.id.hitv_withdraw_pwd)
    public void onMHitvWithdrawPwdClicked() {
        WithdrawPwdUpdateActivity.launch(mContext, mHasSetWithdrawPwd);
    }

    /**
     * 提现账户
     */
    @OnClick(R.id.hitv_withdraw_account)
    public void onMHitvWithdrawAccountClicked() {
        if (mHasWithdrawAccount) {
            WithdrawAccountActivity.launch(mContext);
        } else {
            WithdrawAccountAddActivity.launch(mContext);
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawUnavailableActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.REFRESH && requestCode == resultCode) {
            // 提现密码设置
            //            WithdrawPwdUpdateActivity.launch(mContext, true);
            UserObject obj = UserManager.getInstance().getUser();
            mUserObj.hasSetWithdrawPwd = obj.hasSetWithdrawPwd;
            setWithdrawPwdView();
            setStepView();
            // 检测提现条件是否满足
            verifyCheck();
        } else if (requestCode == TransConstant.IS_LOGIN && requestCode == resultCode) {
            // 绑定手机号
            UserObject obj = UserManager.getInstance().getUser();
            if (!TextUtils.isEmpty(obj.mobile)) {
                // 已经设置手机号
                mUserObj.mobile = obj.mobile;
                setBindPhoneView();
                // 检测提现条件是否满足
                verifyCheck();
            }
        } else if (requestCode == TransConstant.ADD && resultCode == RESULT_OK) {
            // 添加提现账户
            UserObject obj = UserManager.getInstance().getUser();
            if (TextUtils.equals(obj.hasWithdrawAccount, "1")) {
                // 已经绑定
                mHasWithdrawAccount = true;
                mUserObj.hasWithdrawAccount = "1";
                setWithdrawAccountView();
                // 检测提现条件是否满足
                verifyCheck();
            }
        }
    }

    /**
     * 检测提现条件是否满足 - 满足3个条件，跳转到提现页面
     */
    public void verifyCheck() {
        if (mHasWithdrawAccount && mHasSetWithdrawPwd && mHasBindedPhone) {
            WithdrawApplyActivity.launch(mContext);
            finish();
        }
    }

    /*public static void launch(Context mContext, boolean hasSetWithdrawPwd, boolean hasWithdrawAccount, boolean hasBindedPhone) {
        Intent intent = new Intent(mContext, WithdrawUnavailableActivity.class);
        intent.putExtra("hasSetWithdrawPwd", hasSetWithdrawPwd);
        intent.putExtra("hasWithdrawAccount", hasWithdrawAccount);
        intent.putExtra("hasBindedPhone", hasBindedPhone);
        mContext.startActivity(intent);
    }*/
}
