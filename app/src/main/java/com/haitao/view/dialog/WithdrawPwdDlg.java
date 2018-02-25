package com.haitao.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.haitao.R;
import com.haitao.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入提现密码Dialog
 *
 * @author 陶声
 * @since 2018-01-30
 */

public class WithdrawPwdDlg extends Dialog {
    @BindView(R.id.et_pwd) ClearEditText mEtPwd;

    private OnDlgClickListener mOnDlgClickListener;

    public WithdrawPwdDlg(@NonNull Context context) {
        super(context);
        initDlg(context);
    }

    /**
     * 初始化
     *
     * @param context mContext
     */
    private void initDlg(final Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.dlg_withdraw_pwd, null);
        setContentView(layout);
        ButterKnife.bind(this);
    }

    /**
     * 取消
     */
    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        if (mOnDlgClickListener != null) {
            mOnDlgClickListener.onCancel(this);
        }
    }

    /**
     * 确定
     */
    @OnClick(R.id.tv_confirm)
    public void onMTvConfirmClicked() {
        if (mOnDlgClickListener != null) {
            mOnDlgClickListener.onConfirm(this, mEtPwd.getText().toString().trim());
        }
    }

    public interface OnDlgClickListener {
        void onConfirm(Dialog dialog, String pwd);

        void onCancel(Dialog dialog);
    }

    public WithdrawPwdDlg setOnDlgClickListener(OnDlgClickListener onDlgClickListener) {
        mOnDlgClickListener = onDlgClickListener;
        return this;
    }
}
