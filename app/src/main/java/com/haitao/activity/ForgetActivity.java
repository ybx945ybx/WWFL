package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.MobileVerifyObject;
import com.haitao.model.UserObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.ValidateUtils;
import com.haitao.view.ClearEditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 找回密码
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private TextView  btnSubmit;
    private ViewGroup layoutVerify, layoutPassword;
    private ClearEditText etVerify, etPhone;
    private ClearEditText etPassword;
    private ImageButton   ivEye;
    private TextView      tvGetVerify;
    private String phone = "", verify = "";

    protected IViewContext<MobileVerifyObject, IEntityService<MobileVerifyObject>> commandViewContext = VF.<MobileVerifyObject>getDefault(MobileVerifyObject.class);

    MobileVerifyObject verifyObject;
    private   String                                               password        = "";
    protected IViewContext<UserObject, IEntityService<UserObject>> userViewContext = VF.<UserObject>getDefault(UserObject.class);
    private Timer mTimer;
    private int mTime = TransConstant.TIMEOUT;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, ForgetActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        TAG = "忘记密码";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.forget_title);

        layoutVerify = getView(R.id.layoutVerify);
        btnSubmit = getView(R.id.btnSubmit);
        etPhone = getView(R.id.etPhone);
        etVerify = getView(R.id.etVerify);
        tvGetVerify = getView(R.id.tvGetVerify);

        layoutPassword = getView(R.id.layoutPassword);
        etPassword = getView(R.id.etPassword);
        ivEye = getView(R.id.ivEye);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnSubmit.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                phone = s.toString().trim();
                tvGetVerify.setEnabled(!TextUtils.isEmpty(phone) && ValidateUtils.isPhone(phone));
                btnSubmit.setEnabled(!TextUtils.isEmpty(phone) && ValidateUtils.isPhone(phone) && !TextUtils.isEmpty(verify));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etVerify.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                verify = s.toString().trim();
                btnSubmit.setEnabled(!TextUtils.isEmpty(phone) && ValidateUtils.isPhone(phone) && !TextUtils.isEmpty(verify));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvGetVerify.setOnClickListener(this);

        ivEye.setOnClickListener(this);
        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                password = s.toString().trim();
                btnSubmit.setEnabled(password.length() >= 6);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                submit();
                break;
            case R.id.tvGetVerify:
                tvGetVerify.setEnabled(false);
                sendVerifyCode();
                break;
            case R.id.ivEye:
                ivEye.setSelected(!ivEye.isSelected());
                etPassword.setTransformationMethod(ivEye.isSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                //切换后将EditText光标置于末尾
                CharSequence charSequence = etPassword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置新密码
     */
    public void submit() {
        ProgressDialogUtils.show(mContext, R.string.operationg);
        userViewContext.getEntity().password = password;
        userViewContext.getEntity().mobile = phone;
        userViewContext.getEntity().code = verify;
        userViewContext.getService().asynFunction(MethodConstant.LOGIN_PWD_UPDATE, userViewContext.getEntity(),
                new IAsynServiceHandler<UserObject>() {
                    @Override
                    public void onSuccess(UserObject entity) throws Exception {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.show(mContext, R.string.forget_reset_pwd_success);
                        setResult(TransConstant.IS_LOGIN);
                        finish();
                    }

                    @Override
                    public void onSuccessPage(PageResult<UserObject> entity) throws Exception {

                    }

                    @Override
                    public void onFailed(String error) {
                        btnSubmit.setEnabled(true);
                        ProgressDialogUtils.dismiss();
                        ToastUtils.show(mContext, error);
                    }
                });
    }

    /**
     * 发送验证码
     */
    private void sendVerifyCode() {
        etVerify.setText("");
        sendVerifyCode(null);
    }

    private void sendVerifyCode(String code) {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost("+86", phone, TransConstant.VerifyType.FIND_PWD, null, null, code,
                response -> {
                    if (tvGetVerify == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        tvGetVerify.setEnabled(false);
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(mTime--);
                            }
                        }, 0, 1000);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        tvGetVerify.setEnabled(true);
                    }
                }, error -> {
                    if (tvGetVerify == null)
                        return;
                    showErrorToast(error);
                    tvGetVerify.setEnabled(true);
                });
    }

    /**
     * 验证验证码
     */
    private void checkVerifyCode() {
        commandViewContext.getEntity().mobile = phone;
        commandViewContext.getEntity().code = verify;
        commandViewContext.getEntity().type = TransConstant.VerifyType.FIND_PWD;
        ProgressDialogUtils.show(mContext, R.string.operationg);
        commandViewContext.getService().asynFunction(MethodConstant.CHECK_MOBILE_VERIFY, commandViewContext.getEntity(), new IAsynServiceHandler<MobileVerifyObject>() {
            @Override
            public void onSuccess(MobileVerifyObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                verifyObject = commandViewContext.getEntity();
            }

            @Override
            public void onSuccessPage(PageResult<MobileVerifyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
                ToastUtils.show(mContext, error);
            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                tvGetVerify.setText(String.format(getResources().getString(R.string.forget_verify_reget), "(" + mTime + ")"));
            } else {
                tvGetVerify.setEnabled(true);
                tvGetVerify.setText(R.string.forget_verify_get_again);
                mTime = TransConstant.TIMEOUT;
                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            finish();
        }
    }
}
