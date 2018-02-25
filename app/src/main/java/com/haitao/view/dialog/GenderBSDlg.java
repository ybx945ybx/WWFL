package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 性别选择Dialog
 *
 * @author 陶声
 * @since 2017-08-07
 */

public class GenderBSDlg extends BottomSheetDialog {
    private OnGenderSelectListener mOnGenderSelectListener;

    public GenderBSDlg(@NonNull Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context mContext
     */
    private void init(Context context) {
        View layout = View.inflate(context, R.layout.dlg_gender, null);
        setContentView(layout);
        ButterKnife.bind(this);
    }

    /**
     * 男
     */
    @OnClick(R.id.tv_male)
    public void onMaleClicked() {
        if (mOnGenderSelectListener != null) {
            mOnGenderSelectListener.onGenderSelect(0);
            dismiss();
        }
    }

    /**
     * 女
     */
    @OnClick(R.id.tv_female)
    public void onFemaleClicked() {
        if (mOnGenderSelectListener != null) {
            mOnGenderSelectListener.onGenderSelect(1);
            dismiss();
        }
    }

    /**
     * 保密
     */
    /*@OnClick(R.id.tv_keep_secret)
    public void onKeepSecretClicked() {
        if (mOnGenderSelectListener != null) {
            mOnGenderSelectListener.onGenderSelect(2);
            dismiss();
        }
    }*/

    /**
     * 性别选择回调
     */
    public interface OnGenderSelectListener {
        void onGenderSelect(int pos);
    }

    /**
     * 设置接口
     *
     * @param onGenderSelectListener 性别选择回调
     */
    public GenderBSDlg setOnGenderSelectListener(OnGenderSelectListener onGenderSelectListener) {
        mOnGenderSelectListener = onGenderSelectListener;
        return this;
    }
}
