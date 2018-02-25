package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtEditTextView;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import io.swagger.client.model.LoginSuccessModelData;

/**
 * 第三方登录账号绑定到55海淘账号
 * Created by a55 on 2017/11/14.
 */

public class BindAccountActivity extends BaseActivity implements View.OnClickListener, HtEditTextView.textChangedListener {

    private HtEditTextView etAccount, etPassWord;
    private TextView tvForgetPassword;
    private TextView tvCommit;

    private UserObject obj;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, UserObject obj) {
        Intent intent = new Intent(context, BindAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.OBJECT, obj);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        initVars();
        initViews();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "社交登录 - 绑定账号";
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            obj = (UserObject) bundle.getSerializable(TransConstant.OBJECT);
        }
    }

    private void initViews() {

        etAccount = getView(R.id.et_account);
        etPassWord = getView(R.id.et_password);
        etPassWord.setRightImgSelected(false);
        etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());

        tvCommit = getView(R.id.tv_commit);

        tvForgetPassword = getView(R.id.tv_forget_password);
    }

    private void initEvent() {
        etPassWord.setOnRightImgClickListener(view -> {
            etPassWord.setRightImgSelected(!etPassWord.getRightImgSelected());

            etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = etPassWord.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        });

        tvCommit.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

        // 监听快速登录按钮是否可点击
        etAccount.addTextChangedListener(this);
        etPassWord.addTextChangedListener(this);
    }

    private void initData() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkCommitEnable();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 检查快速登录按钮是否可点击
     */
    private void checkCommitEnable() {
        if (etAccount.isHasText() && etPassWord.isHasText()) {
            tvCommit.setEnabled(true);
        } else {
            tvCommit.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                doBind();
                break;
            case R.id.tv_forget_password:
                ResetPwdActivity.launch(mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 第三方账号绑定55海淘账号userAccountBindingTppTppIdPost
     */
    private void doBind() {
        showProgressDialog(R.string.operationg);

        ForumApi.getInstance().userAccountBindingTppTppIdPost(obj.type, obj.open_id, obj.open_token, obj.open_unionid, etAccount.getText().toString(), etPassWord.getText().toString(),
                response -> {
                    dismissProgressDialog();
                    Logger.d("LoginSuccessModel----》" + response.toString());
                    if ("0".equals(response.getCode())) {
                        wrapUserObject(response.getData());
                        UserManager.getInstance().setUser(obj);
                        TalkingDataAppCpa.onLogin(UserManager.getInstance().getUserId());
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                        mContext.sendBroadcast(mIntent);
                        setResult(TransConstant.IS_LOGIN);
                        finish();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());

                    }
                },
                error -> {
                    dismissProgressDialog();
                    ToastUtils.show(mContext, error.toString());
                    showErrorToast(error);
                });
    }

    private void wrapUserObject(LoginSuccessModelData entity) {
        if (entity != null) {
            obj.token = entity.getToken();
            obj.ht_token = entity.getToken();
            obj.uid = entity.getUid();
            obj.username = entity.getUsername();
            obj.mobile = entity.getMobile();
            obj.mobile_unique = entity.getMobileUnique();
            obj.refresh_token = entity.getRefreshToken();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            if (HtApplication.isLogin()) {
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                mContext.sendBroadcast(mIntent);
                ((Activity) mContext).setResult(TransConstant.IS_LOGIN);
                ((Activity) mContext).finish();
            }
        }
    }
}
