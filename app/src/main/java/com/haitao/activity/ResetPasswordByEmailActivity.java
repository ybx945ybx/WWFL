package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;

/**
 * 邮箱发送重置密码成功
 */
public class ResetPasswordByEmailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTips;
    private TextView btnSubmit;
    private static final String EMAIL = "email";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordByEmailActivity.class);
        intent.putExtra(EMAIL, email);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_by_email);
        TAG = "通过邮箱找回密码";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.forget_reset_pwd_by_email_title);
        btnLeft.setImageResource(R.mipmap.ic_close);
        tvTips = getView(R.id.tvTips);
        btnSubmit = getView(R.id.btnSubmit);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnSubmit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String email = getIntent().getStringExtra(EMAIL);
        tvTips.setText(String.format(getResources().getString(R.string.forget_by_email_seccess), email));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                setResult(TransConstant.IS_LOGIN);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(TransConstant.IS_LOGIN);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
