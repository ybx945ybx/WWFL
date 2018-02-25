package com.haitao.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;

public class ConfirmDialogUtils {

    private static Dialog dialog = null;
    Context   mContext;
    ViewGroup layoutContent;
    TextView  tvTitle, tvLeft, tvRight;
    ViewGroup layoutContol;

    public ConfirmDialogUtils(Context mContext) {
        this.mContext = mContext;
        initView(null);
    }

    public ConfirmDialogUtils(Context mContext, View v) {
        this.mContext = mContext;
        initView(v);
    }

    private void initView(View v) {
        dialog = new Dialog(mContext, R.style.confirm_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View           view     = inflater.inflate(R.layout.layout_confirm_dialog, null);// 得到加载view
        layoutContent = view.findViewById(R.id.layoutContent);
        layoutContol = view.findViewById(R.id.layoutContol);
        if (null != v) {
            layoutContent.addView(v);
        }
        tvTitle = view.findViewById(R.id.tvTitle);
        tvLeft = view.findViewById(R.id.btnLeft);
        tvRight = view.findViewById(R.id.btnRight);
        tvTitle.setVisibility(View.GONE);
        tvLeft.setOnClickListener(v1 -> {
            if (null != mOnItemClickLitener) {
                mOnItemClickLitener.onLeftClick();
            } else {
                dialog.dismiss();
            }
        });
        tvRight.setOnClickListener(v12 -> {
            if (null != mOnItemClickLitener) {
                mOnItemClickLitener.onRightClick();
            } else {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
    }

    /**
     * 显示
     *
     * @param txt
     */
    public void show(String txt) {
        TextView tv = new TextView(mContext);
        tv.setTextSize(14);
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.midGrey));
        tv.setText(txt);
        setView(tv);
        show();
    }

    /**
     * 自定义一个view作为内容显示
     *
     * @param v
     */
    public void setView(View v) {
        layoutContent.removeAllViews();
        layoutContent.addView(v);
        setLeftBtnVisibility(true);
        show();
    }

    /**
     * 是否需要显示取消按钮
     *
     * @param isShow
     */
    public void setLeftBtnVisibility(boolean isShow) {
        tvLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否需要显示确定按钮
     *
     * @param isShow
     */
    public void setBtnVisibility(boolean isShow) {
        layoutContol.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (!"".equals(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickLitener {
        void onLeftClick();

        void onRightClick();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 设置左边按钮显示的文字，默认为取消
     *
     * @param resId
     */
    public void setLeftText(int resId) {
        tvLeft.setText(this.mContext.getResources().getString(resId));
    }

    /**
     * 设置左边按钮显示的文字，默认为取消
     *
     * @param str
     */
    public void setLeftText(String str) {
        tvLeft.setText(str);
    }

    /**
     * 设置右边按钮显示的文字，默认为确定
     *
     * @param resId
     */
    public void setRightText(int resId) {
        tvRight.setText(this.mContext.getResources().getString(resId));
    }

    /**
     * 设置右边按钮显示的文字，默认为确定
     *
     * @param str
     */
    public void setRightText(String str) {
        tvRight.setText(str);
    }

    public TextView getLeftView() {
        return tvLeft;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public boolean isShowing() {
        if (null == dialog)
            return false;
        return dialog.isShowing();
    }

    public void setCancel(boolean isCancel) {
        dialog.setCancelable(isCancel);
    }
}
