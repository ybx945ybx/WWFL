package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 表单 - 输入居左 + 图标？
 *
 * @author 陶声
 * @since 2018-01-26
 */

public class HtItemEditView extends RelativeLayout {

    @BindView(R.id.tv_title)     TextView  mTvTitle;
    @BindView(R.id.et_content)   EditText  mEtContent;
    @BindView(R.id.img_right)    ImageView mImgRight;
    @BindView(R.id.view_divider) View      mViewUnderline;

    private Context mContext;

    public HtItemEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context mContext
     * @param attrs   attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_ht_item_edit, this);
        ButterKnife.bind(this);

        TypedArray ta               = context.obtainStyledAttributes(attrs, R.styleable.HtItemEditView);
        String     title            = ta.getString(R.styleable.HtItemEditView_hiev_title);
        String     text             = ta.getString(R.styleable.HtItemEditView_hiev_text);
        String     hintText         = ta.getString(R.styleable.HtItemEditView_hiev_hint_text);
        int        rightImgRes      = ta.getResourceId(R.styleable.HtItemEditView_hiev_right_img, 0);
        boolean    underlineVisible = ta.getBoolean(R.styleable.HtItemEditView_hiev_underline_visible, true);
        int        maxlines         = ta.getInt(R.styleable.HtItemEditView_android_maxLines, 0);
        int        inputType        = ta.getInt(R.styleable.HtItemEditView_android_inputType, 0);
        boolean    rawInput         = ta.getBoolean(R.styleable.HtItemEditView_hiev_raw_input, false);
        // 标题
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
        // 输入框
        if (!TextUtils.isEmpty(text)) {
            mEtContent.setText(text);
        }
        // 输入框单行设置
        if (maxlines != 0) {
            mEtContent.setMaxLines(maxlines);
        }
        // 输入框inputType
        if (inputType != 0) {
            if (rawInput) {
                mEtContent.setRawInputType(inputType);
            } else {
                mEtContent.setInputType(inputType);
            }
        }
        // 输入框hint
        if (!TextUtils.isEmpty(hintText)) {
            mEtContent.setHint(hintText);
        }
        // 右侧图标
        mImgRight.setVisibility(rightImgRes != 0 ? VISIBLE : GONE);
        if (rightImgRes != 0) {
            mImgRight.setImageResource(rightImgRes);
        }
        // 右侧图标
        mViewUnderline.setVisibility(underlineVisible ? VISIBLE : GONE);
        ta.recycle();
    }

    /**
     * 设置左侧文本
     *
     * @param text 左侧文本
     */
    public void setTitle(String text) {
        mTvTitle.setText(text);
    }

    /**
     * 设置输入框文本
     *
     * @param text 文本
     */
    public void setText(String text) {
        mEtContent.setText(text);
    }

    /**
     * 设置输入框文本hint
     *
     * @param text 文本
     */
    public void setTextHint(String text) {
        mEtContent.setHint(text);
    }

    /**
     * 下划线是否可见
     *
     * @param visible 是否可见
     */
    public void setUnderlineVisible(boolean visible) {
        mViewUnderline.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 输入框监听
     *
     * @param textWatcher textWatcher
     */
    public void addTextChangeListener(TextWatcher textWatcher) {
        mEtContent.addTextChangedListener(textWatcher);
    }

    /**
     * 右侧图标点击事件
     *
     * @param listener 点击事件
     */
    public void setRightClickListener(OnClickListener listener) {
        mImgRight.setOnClickListener(listener);
    }
}
