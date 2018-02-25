package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;

/**
 * 提现账户列表 - 删除
 *
 * @author 陶声
 * @since 2018-01-29
 */
public class WithdrawAccountRemoveBsDlg extends BottomSheetDialog {

    private OnRemoveListener mOnRemoveListener;

    public WithdrawAccountRemoveBsDlg(@NonNull Context context) {
        super(context);
        initDlg(context);
    }

    private void initDlg(final Context context) {
        View layout = View.inflate(context, R.layout.dlg_withdraw_account_remove, null);
        layout.setOnClickListener(v -> {
            if (mOnRemoveListener != null)
                mOnRemoveListener.onRemove(this);
        });
        setContentView(layout);
    }

    public interface OnRemoveListener {
        void onRemove(WithdrawAccountRemoveBsDlg dlg);
    }

    public WithdrawAccountRemoveBsDlg setOnRemoveListener(OnRemoveListener onRemoveListener) {
        mOnRemoveListener = onRemoveListener;
        return this;
    }
}
