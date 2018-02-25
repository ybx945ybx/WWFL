package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 两行条目
 *
 * @author 陶声
 * @since 2017-07-24
 */

public class HtItemView extends LinearLayout {

    @BindView(R.id.tv_title)     TextView  mTvTitle;        // 标题
    @BindView(R.id.tv_content)   TextView  mTvContent;      // 文本
    @BindView(R.id.img_next)     ImageView mImgNext;        // 右箭头
    @BindView(R.id.view_divider) View      mViewDivider;    // 下划线

    public HtItemView(Context context, @Nullable AttributeSet attrs) {
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
        LayoutInflater.from(context).inflate(R.layout.layout_item_view, this);
        ButterKnife.bind(this);

        TypedArray ta             = context.obtainStyledAttributes(attrs, R.styleable.HtItemView);
        String     title          = ta.getString(R.styleable.HtItemView_title);
        String     content        = ta.getString(R.styleable.HtItemView_content);
        boolean    nextEnabled    = ta.getBoolean(R.styleable.HtItemView_next_enabled, false);
        boolean    dividerEnabled = ta.getBoolean(R.styleable.HtItemView_divider_enabled, true);
        // 标题
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
        // 内容
        if (!TextUtils.isEmpty(content)) {
            mTvContent.setText(content);
        }
        // 分割线
        if (!dividerEnabled) {
            mViewDivider.setVisibility(View.GONE);
        }
        mImgNext.setVisibility(nextEnabled ? View.VISIBLE : View.GONE);
        ta.recycle();
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置内容文本
     *
     * @param content 内容文本
     */
    public void setContent(String content) {
        mTvContent.setText(content);
    }

    /**
     * 是否显示下划线
     *
     * @param enabled 是否显示
     */
    public void setDividerEnabled(boolean enabled) {
        mViewDivider.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }
}
