package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.verifycode.OnVerifyCodeCallBackListener;
import com.haitao.utils.verifycode.VerifyCodeUtils;
import com.haitao.view.HtEditTextView;
import com.haitao.view.dialog.CountryCodeChooseDlg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.AreaModel;

/**
 * 重置密码 (未登录状态 点击忘记密码)
 *
 * @author 陶声
 * @since 2017-11-16
 */
public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.et_phone)     HtEditTextView mEtPhone;        // 手机号
    @BindView(R.id.et_code)      HtEditTextView mEtCode;         // 验证码
    @BindView(R.id.et_password)  HtEditTextView mEtPassword;     // 密码
    @BindView(R.id.tv_reset_pwd) TextView       mTvResetPwd;     // 重置密码

    private List<AreaModel>      mCountryList; // 国家区号列表数据
    private CountryCodeChooseDlg mCountryCodeChooseDlg;
    private String               mCountryCode;
    private VerifyCodeUtils      verifyCodeUtils;
    private CountDownTimer       mCountDownTimer;         // 发送验证码倒计时
    private boolean              isOnCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVars() {
        TAG = "重置密码";
        mCountryList = new ArrayList<>();
        mCountryCode = "+86";
    }

    private void initViews(Bundle savedInstanceState) {
        mEtPhone.setOnLeftClickListener(view -> {
            if (mCountryList != null && mCountryList.size() > 0) {
                if (mCountryCodeChooseDlg == null) {
                    String selectId = "";
                    for (AreaModel areaModel : mCountryList) {
                        if (areaModel.getAreaCode().contains("+86")) {
                            selectId = areaModel.getAreaId();
                        }
                    }
                    mCountryCodeChooseDlg = new CountryCodeChooseDlg(mContext, mCountryList);
                    mCountryCodeChooseDlg.setSelectId(selectId);
                    mCountryCodeChooseDlg.setOnCountryCodeSelectedListener((areaModel, dialog) -> {
                        mEtPhone.setLeftTxt(areaModel.getAreaCode());
                        mCountryCode = areaModel.getAreaCode();
                        dialog.hide();
                    });
                }
                mCountryCodeChooseDlg.show();
            } else {
                ToastUtils.show(mContext, "正在加载，请稍后...");
            }
        });
        mEtCode.setOnRightTxtClickListener(view -> getCode());
        mEtPassword.setOnRightImgClickListener(view -> {
            mEtPassword.setRightImgSelected(!mEtPassword.getRightImgSelected());

            mEtPassword.setTransformationMethod(mEtPassword.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = mEtPassword.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        });
        HtEditTextView.textChangedListener textChangedListener = new HtEditTextView.textChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetPwdEnabledCheck();
                if (isOnCount)
                    return;
                mEtCode.setRightTxtEnable(mEtPhone.getText().toString().trim().length() > 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEtPassword.addTextChangedListener(textChangedListener);
        mEtPhone.addTextChangedListener(textChangedListener);
        mEtCode.addTextChangedListener(textChangedListener);
    }

    private void loadData() {
        // 加载国家区号数据
        ForumApi.getInstance().commonAreasInfoGet(response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        List<AreaModel> data = response.getData();
                        if (data != null && data.size() > 0) {
                            mCountryList.addAll(data);
                        }
                    }
                },
                error -> {

                });
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        // 发送验证码验证
        if (!sendVerifyCodeCheck()) return;
        // 获取图形验证码
        verifyCodeUtils = new VerifyCodeUtils(mContext, null, null, TransConstant.VerifyType.BIND, mEtPhone.getText().toString().trim());
        verifyCodeUtils.setmOnVerifyCodeCallBackListener(new OnVerifyCodeCallBackListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess() {
            }

            @Override
            public void onSuccess(int type, String code) {
                sendVerifyCodeReq(code);
            }

            @Override
            public void onSuccess(UserObject userObject) {

            }

        });
        verifyCodeUtils.getSetting();
    }

    /**
     * 发送验证码网络请求
     *
     * @param code 图形验证码
     */
    private void sendVerifyCodeReq(String code) {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost(mCountryCode, mEtPhone.getText().toString().trim(), TransConstant.VerifyType.FIND_PWD, null, null, code,
                response -> {
                    if ("0".equals(response.getCode())) {
                        startCountDown();
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();
                        showToast(String.format(getResources().getString(R.string.send_verify_code), "\n" + mCountryCode + "-" + mEtPhone.getText().toString()));
                    } else if (CodeConstant.CODE_NOT_MATCH.equals(response.getCode())) {
                        verifyCodeUtils.refreshImge();
                        showToast(ToastType.ERROR, response.getMsg());
                    } else if ("7005".equals(response.getCode())) {
                        showToast(ToastType.ERROR, response.getMsg());
                    } else {
                        verifyCodeUtils.dismissImageDialog();
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                }, error -> showErrorToast(error));
    }

    /**
     * 重设密码网络请求
     */
    private void resetPwdReq() {
        ForumApi.getInstance().userAccountPasswordResettingWithSmsCaptchaPost(mCountryCode,
                mEtPhone.getText().toString().trim(),
                mEtCode.getText().toString().trim(),
                mEtPassword.getText().toString(),
                response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, mContext.getResources().getString(R.string.reset_pwd_success));
                        mTvResetPwd.postDelayed(() -> finish(), 200);
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                this::showErrorToast);

    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new TimerCountDown(60 * 2000, 1000);
        }
        mCountDownTimer.start();
        isOnCount = true;
    }

    class TimerCountDown extends CountDownTimer {

        public TimerCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            mEtCode.setRightTxtEnable(false);
            mEtCode.setRightTxt("重新获取" + millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            isOnCount = false;
            mEtCode.setRightTxtEnable(true);
            mEtCode.setRightTxt("获取验证码");
        }
    }

    /**
     * 重置密码
     */
    @OnClick(R.id.tv_reset_pwd)
    public void onClickResetPwd() {
        if (!resetPwdCheck()) return;
        resetPwdReq();
    }

    /**
     * 发送验证码验证
     *
     * @return 是否通过验证
     */
    private boolean sendVerifyCodeCheck() {
        if (TextUtils.isEmpty(mEtPhone.getText())) {
            ToastUtils.show(mContext, "请先输入手机号码");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 重置密码验证
     *
     * @return 是否通过验证
     */
    private boolean resetPwdCheck() {
        if (TextUtils.isEmpty(mEtPhone.getText())) {
            ToastUtils.show(mContext, "请先输入手机号码");
        } else if (mEtPhone.getText().length() < 11) {
            ToastUtils.show(mContext, "请输入正确的手机号码");
        } else if (TextUtils.isEmpty(mEtCode.getText().toString())) {
            ToastUtils.show(mContext, "请输入验证码");
        } else if (TextUtils.isEmpty(mEtPassword.getText().toString())) {
            ToastUtils.show(mContext, "请输入新密码");
        } else {
            return true;
        }
        return false;
    }

    /**
     * 重置密码按钮状态更新
     */
    private void resetPwdEnabledCheck() {
        if (TextUtils.isEmpty(mEtPhone.getText())
                || TextUtils.isEmpty(mEtCode.getText().toString())
                || TextUtils.isEmpty(mEtPassword.getText().toString())) {
            mTvResetPwd.setEnabled(false);
        } else {
            mTvResetPwd.setEnabled(true);
        }
    }

    /**
     * 跳转到本页
     *
     * @param context cotext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        (context).startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountryCodeChooseDlg != null) {
            mCountryCodeChooseDlg.dismiss();
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (verifyCodeUtils != null)
            verifyCodeUtils.dismissImageDialog();
    }
}
