package com.haitao.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用确认对话框
 *
 * @author 陶声
 * @since 2017-08-07
 */

public class ConfirmDlg extends Dialog {
    @BindView(R.id.tv_title)    TextView mTvTitle;       //  标题
    @BindView(R.id.tv_content)  TextView mTvContent;     //  内容
    @BindView(R.id.view_center) View     viewCenter;     //  分割线
    @BindView(R.id.tv_cancel)   TextView mTvCancle;      //  取消
    @BindView(R.id.tv_confirm)  TextView mTvConfirm;     //  确定

    private OnConfirmListener mOnConfirmListener;
    private OnCancelListener  mOnCancelListener;

    public ConfirmDlg(@NonNull Context context, String title, String content, OnConfirmListener onConfirmListener, OnCancelListener onCancelListener) {
        super(context);
        mOnConfirmListener = onConfirmListener;
        mOnCancelListener = onCancelListener;
        init(context, title, content);
    }

    private void init(Context context, String title, String content) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            // 顶部透明
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View layout   = View.inflate(context, R.layout.dlg_confirm, null);
        int  margin   = DensityUtil.dip2px(context, 32); // 32dp
        int  dlgWidth = ScreenUtils.getScreenWidth(context) - margin * 2;
        setContentView(layout, new LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this);

        mTvTitle.setText(title);
        mTvContent.setText(content);

        // 是否有标题
        mTvTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        // 是否有取消按钮
        mTvCancle.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);
        viewCenter.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);

    }

    /**
     * 取消
     */
    @OnClick(R.id.tv_cancel)
    public void onCancelClicked() {
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(this);
        } else {
            dismiss();
        }
    }

    /**
     * 确认
     */
    @OnClick(R.id.tv_confirm)
    public void onConfirmClicked() {
        if (mOnConfirmListener != null) {
            mOnConfirmListener.onConfirm(this);
        } else {
            dismiss();
        }
    }

    /**
     * 取消回调
     */
    public interface OnConfirmListener {
        void onConfirm(ConfirmDlg confirmDlg);
    }

    /**
     * 确认回调
     */
    public interface OnCancelListener {
        void onCancel(ConfirmDlg confirmDlg);
    }

}
