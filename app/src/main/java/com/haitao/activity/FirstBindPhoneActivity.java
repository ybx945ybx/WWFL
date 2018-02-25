package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.verifycode.OnVerifyCodeCallBackListener;
import com.haitao.utils.verifycode.VerifyCodeUtils;
import com.haitao.view.HtEditTextView;
import com.haitao.view.dialog.CountryCodeChooseDlg;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.AreaModel;

import static com.haitao.activity.BindPhoneActivity.BING_MULTI;

/**
 * 绑定手机号码
 */
public class FirstBindPhoneActivity extends BaseActivity implements View.OnClickListener, HtEditTextView.textChangedListener {

    private TextView       tvTips;
    //    private HtTextView     tvCountryCode;            // 国家区号
    private HtEditTextView etPhone, etCode;
    private TextView tvSubmit;

    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private boolean        isOnCount;
    private String         mRegionId;               // 国家码

    private VerifyCodeUtils verifyCodeUtils;

    private CountryCodeChooseDlg mCountryCodeChooseDlg;

    private List<AreaModel> mCountryList; // 国家区号列表数据
    private int type = 0;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, FirstBindPhoneActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    /**
     * 跳转到当前页(一个手机绑定了多个账号)
     *
     * @param context mContext
     */
    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, FirstBindPhoneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TransConstant.TYPE, type);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_bind_phone);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "绑定手机号码";
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(TransConstant.TYPE, 0);
        }
        mRegionId = "+86";
        mCountryList = new ArrayList<>();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvTips = getView(R.id.tvTips);

        etPhone = getView(R.id.et_phone);
        etCode = getView(R.id.et_code);

        tvSubmit = getView(R.id.tv_commit);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        etPhone.setOnLeftClickListener(view -> {
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
                        etPhone.setLeftTxt(areaModel.getAreaCode());
                        mRegionId = areaModel.getAreaCode();
                        dialog.hide();
                    });
                }
                mCountryCodeChooseDlg.show();
            }
        });

        etCode.setOnRightTxtClickListener(view -> {
            if (etCode.getRightTxtEnable()) {
                getCode();
            }
        });

        tvSubmit.setOnClickListener(this);

        // 监听确定按钮是否可点击
        etPhone.addTextChangedListener(this);
        etCode.addTextChangedListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (type == BING_MULTI) {
            tvTips.setText(R.string.bind_multi_phone);
        }

        // 获取服务器国家区号
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
        if (TextUtils.isEmpty(etPhone.getText())) {
            ToastUtils.show(mContext, "请先输入手机号码");
            return;
        }
        // 获取图形验证码
        verifyCodeUtils = new VerifyCodeUtils(mContext, null, null, TransConstant.VerifyType.BIND, etPhone.getText().toString());
        verifyCodeUtils.setmOnVerifyCodeCallBackListener(new OnVerifyCodeCallBackListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess() {
            }

            @Override
            public void onSuccess(int type, String code) {
                sendVerifyCode(code);
            }

            @Override
            public void onSuccess(UserObject userObject) {

            }

        });
        verifyCodeUtils.getSetting();
    }

    private void sendVerifyCode(String code) {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost(mRegionId, etPhone.getText().toString(), TransConstant.VerifyType.BIND, null, null, code,
                response -> {
                    if ("0".equals(response.getCode())) {
                        startCountDown();
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();
                        showToast(String.format(getResources().getString(R.string.send_verify_code), "\n" + mRegionId + "-" + etPhone.getText().toString()));
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
            etCode.setRightTxtEnable(false);
            etCode.setRightTxt("重新获取" + millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            isOnCount = false;
            etCode.setRightTxtEnable(true);
            etCode.setRightTxt("获取验证码");
        }
    }

    private void bindPhone() {
        showProgressDialog(R.string.operationg);
        ForumApi.getInstance().userNewPhoneNumberBindingPost(mRegionId, etPhone.getText().toString(), etCode.getText().toString(), null,
                response -> {
                    dismissProgressDialog();
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, "绑定成功");
                        UserObject obj = UserManager.getInstance().getUser();
                        obj.mobile = etPhone.getText().toString();
                        UserManager.getInstance().setUser(obj);
                        setResult(TransConstant.IS_LOGIN);
                        finishActivity();
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                error -> {
                    showErrorToast(error);
                    dismissProgressDialog();
                });
    }

    private void finishActivity(){
        tvSubmit.postDelayed(() -> finish(), 1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                bindPhone();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkCommitEnable();
        if (isOnCount)
            return;
        etCode.setRightTxtEnable(etPhone.getText().toString().trim().length() > 6);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 检查快速登录按钮是否可点击
     */
    private void checkCommitEnable() {
        if (etPhone.isHasText() && etCode.isHasText()) {
            tvSubmit.setEnabled(true);
        } else {
            tvSubmit.setEnabled(false);
        }
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
