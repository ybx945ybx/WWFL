package com.haitao.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更新提示Dialog
 *
 * @author 陶声
 * @since 2017-06-09
 */

public class UpdateDlg extends Dialog {
    @BindView(R.id.tv_update_content) TextView     mTvUpdateContent; // 更新内容
    @BindView(R.id.ib_close)          ImageButton  mIbClose;         // 关闭按钮
    @BindView(R.id.ll_dlg_container)  LinearLayout mLlDlgContainer;

    private OnUpdateClickListener mOnUpdateClickListener;


    public UpdateDlg(@NonNull Context context, String newChange, String lowVerNum) {
        super(context);
        initDlg(context, newChange, lowVerNum);
    }

    /**
     * 初始化
     *
     * @param context   mContext
     * @param newChange 更新文案
     * @param lowVerNum 最低版本
     */
    private void initDlg(final Context context, String newChange, String lowVerNum) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            // 顶部透明
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.dlg_update, null);
        setContentView(layout);
        ButterKnife.bind(this);
        // 更新文案
        mTvUpdateContent.setText(newChange);
        // 是否强制更新
        final boolean forceUpdate = CommonUtils.isForceUpdate(lowVerNum);
        mIbClose.setVisibility(forceUpdate ? View.GONE : View.VISIBLE);
        setCanceledOnTouchOutside(!forceUpdate);
        setOnCancelListener(dialog -> {
            if (forceUpdate) {
                // 强制更新下，点击返回键，关闭页面
                if (context instanceof Activity) {
                    dismiss();
                    ((Activity) context).finish();
                }

            } else {
                dismiss();
            }
        });
    }

    /**
     * 关闭Dialog
     */
    @OnClick(R.id.ib_close)
    void clickClose() {
        dismiss();
    }

    /**
     * 立即更新
     */
    @OnClick(R.id.tv_update_now)
    void clickUpdateNow() {
        if (mOnUpdateClickListener != null)
            mOnUpdateClickListener.clickUpdate(this);
    }

    /**
     * 设置点击更新监听
     *
     * @param onUpdateClickListener 点击更新监听
     */
    public void setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
        mOnUpdateClickListener = onUpdateClickListener;
    }


    /**
     * 点击更新
     */
    public interface OnUpdateClickListener {
        void clickUpdate(Dialog updateDialog);
    }
}
