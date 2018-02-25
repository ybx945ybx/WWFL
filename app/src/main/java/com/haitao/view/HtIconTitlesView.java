package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 1图标 1标题 1文本描述布局
 *
 * @author 陶声
 * @since 2017-12-05
 */

public class HtIconTitlesView extends LinearLayout {

    @BindView(R.id.img_icon) ImageView mImgIcon;        // 图标
    @BindView(R.id.tv_title) TextView  mTvIconTitle;    // 标题
    @BindView(R.id.tv_text)  TextView  mTvIconText;     // 文本

    public HtIconTitlesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context mContext
     * @param attrs   属性
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_ht_icon_title_view, this);
        ButterKnife.bind(this);

        TypedArray ta    = context.obtainStyledAttributes(attrs, R.styleable.HtIconTitlesView);
        String     title = ta.getString(R.styleable.HtIconTitlesView_icon_title);
        String     text  = ta.getString(R.styleable.HtIconTitlesView_icon_text);
        Drawable   icon  = ta.getDrawable(R.styleable.HtIconTitlesView_icon);
        // 标题
        if (!TextUtils.isEmpty(title)) {
            mTvIconTitle.setText(title);
        }
        // 副标题
        if (!TextUtils.isEmpty(text)) {
            mTvIconText.setText(text);
        }
        // 图标
        if (icon != null) {
            mImgIcon.setImageDrawable(icon);
        }
        ta.recycle();
    }

    /**
     * 设置标题文字
     *
     * @param title 文本
     */
    public void setIconTitle(String title) {
        mTvIconTitle.setText(title);
    }

    /**
     * 设置文本文字
     *
     * @param text 文本
     */
    public void setIconText(String text) {
        mTvIconText.setText(text);
    }
}
