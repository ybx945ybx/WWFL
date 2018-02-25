package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改登录密码
 */
public class LoginPwdUpdateActivity extends BaseActivity {
    @BindView(R.id.et_current_pwd)     EditText mEtCurrentPwd;     // 当前密码
    @BindView(R.id.et_new_pwd)         EditText mEtNewPwd;         // 新密码
    @BindView(R.id.et_new_pwd_confirm) EditText mEtNewPwdConfirm;  // 新密码确认
    @BindView(R.id.btn_submit)         TextView mBtnSubmit;        // 提交按钮

    private String currentPwd = "", newPassword = "", newPasswordConfirm = "";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginPwdUpdateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
    }

    private void initVars() {
        TAG = "修改登录密码";
    }

    /**
     * 初始化事件
     */
    private void initViews(Bundle savedInstanceState) {
        mEtCurrentPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                currentPwd = s.toString().trim();
                refreshSubmitState();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtNewPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                newPassword = s.toString();
                refreshSubmitState();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
    }

    /**
     * 更新提交按钮状态
     */
    private void refreshSubmitState() {
        mBtnSubmit.setEnabled(!TextUtils.isEmpty(currentPwd) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(newPasswordConfirm));
    }

    /**
     * 点击确定
     */
    @OnClick(R.id.btn_submit)
    public void onClickSubmit(View v) {
        if (!submitCheck())
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        ProgressDialogUtils.show(mContext, R.string.operationg);
        ForumApi.getInstance().userAccountPasswordResettingPost(currentPwd, newPassword, newPasswordConfirm,
                response -> {
                    if (mBtnSubmit == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        ToastUtils.show(mContext, R.string.update_password_success);
                        SPUtils.put(mContext, SPConstant.PASSWORD, newPassword);
                        finish();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mBtnSubmit == null)
                        return;
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                    mBtnSubmit.setEnabled(true);
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
                finish();
            }
        }
    }
}
