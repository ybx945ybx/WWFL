package com.haitao.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.HtHeadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改提现密码
 */
public class WithdrawPwdUpdateActivity extends BaseActivity {

    @BindView(R.id.tv_phone)           TextView      mTvPhone;          // 手机号
    @BindView(R.id.et_veirfy_code)     ClearEditText mEtVeirfyCode;     // 验证码
    @BindView(R.id.et_new_pwd)         ClearEditText mEtNewPwd;         // 新密码
    @BindView(R.id.et_new_pwd_confirm) ClearEditText mEtNewPwdConfirm;  // 新密码确认
    @BindView(R.id.tv_get_verify_code) TextView      mTvGetVerifyCode;  // 发送验证码
    @BindView(R.id.btn_submit)         TextView      mBtnSubmit;        // 提交按钮
    @BindView(R.id.ht_headview)        HtHeadView    mHtHeadview;       // 标题

    private String newPassword = "", newPasswordConfirm = "", verifyCode = "";

    private UserObject obj;

    private CountDownTimer mCountDownTimer; // 发送验证码倒计时
    private boolean        mNewPwd;         // 是否是新设置提现密码方式

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawPwdUpdateActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    /**
     * 跳转到当前页 设置提现密码
     *
     * @param context mContext
     * @param newPwd  设置新密码
     */
    public static void launch(Context context, boolean newPwd) {
        Intent intent = new Intent(context, WithdrawPwdUpdateActivity.class);
        intent.putExtra("new_pwd", newPwd);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_pwd_update);
        ButterKnife.bind(this);

        initVars();
        initView(savedInstanceState);
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
        TAG = "修改提现密码";
        Intent intent = getIntent();
        if (intent != null) {
            mNewPwd = intent.getBooleanExtra("new_pwd", false);
            if (mNewPwd) {
                // 新增提现密码方式，标题改为设置提现密码
                TAG = getString(R.string.set_withdraw_pwd);
                mHtHeadview.setCenterText(TAG);
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView(Bundle savedInstanceState) {
        mEtNewPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                newPassword = s.toString();
                refreshSubmitState();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtNewPwdConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordConfirm = s.toString();
                refreshSubmitState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtVeirfyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyCode = s.toString();
                refreshSubmitState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (HtApplication.isLogin()) {
            obj = UserManager.getInstance().getUser();
            if (!TextUtils.isEmpty(obj.mobile)) {
                mTvPhone.setText(String.format("%s %s", obj.area, CommonUtils.transferPhone(obj.mobile)));
                mTvGetVerifyCode.setEnabled(true);
            }
        }
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_get_verify_code)
    public void onClickGetVerifyCode() {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost(obj.area, obj.mobile, TransConstant.VerifyType.WITHDRAW_PWD, null, null, null,
                response -> {
                    if (mBtnSubmit == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, String.format("短信验证码已发送至%s", "\n" + obj.area + "-" + obj.mobile));
                        startCountDown();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (mBtnSubmit == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        mTvGetVerifyCode.setEnabled(false);
        mCountDownTimer = new CountDownTimer(60 * 2000, 1000) { // 开始倒计时
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                mTvGetVerifyCode.setText(String.format("重新获取%ds", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mTvGetVerifyCode.setEnabled(true);
                mTvGetVerifyCode.setText("获取验证码");
            }
        }.start();
    }

    /**
     * 提交验证
     *
     * @return 是否通过验证
     */
    private boolean submitCheck() {
        if (!TextUtils.equals(newPassword, newPasswordConfirm)) { // 两次密码校验
            ToastUtils.show(mContext, R.string.setting_password_again_error);
        } else {
            return true;
        }
        return false;
    }

    /**
     * 提交修改密码-网络请求
     */
    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        if (!submitCheck())
            return;
        ForumApi.getInstance().userAccountWithdrawingPasswordResettingPost(verifyCode, newPassword, newPasswordConfirm,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        UserObject userObject = UserManager.getInstance().getUser();
                        userObject.hasSetWithdrawPwd = "1";
                        UserManager.getInstance().setUser(userObject);
                        ToastUtils.show(mContext, mNewPwd ? R.string.set_password_success : R.string.update_password_success);
                        setResult(TransConstant.REFRESH);
                        finish();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    ProgressDialogUtils.dismiss();
                    showErrorToast(error);
                });
    }

    /**
     * 更新提交按钮状态
     */
    private void refreshSubmitState() {
        mBtnSubmit.setEnabled(!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(newPasswordConfirm) && !TextUtils.isEmpty(verifyCode));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }
}
