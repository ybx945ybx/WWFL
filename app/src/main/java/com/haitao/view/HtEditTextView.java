package com.haitao.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by a55 on 2017/11/14.
 */

public class HtEditTextView extends RelativeLayout {

    @BindView(R.id.llyt_left)         LinearLayout  llytLeft;
    @BindView(R.id.tv_left_text)      TextView      tvLeftText;
    @BindView(R.id.iv_left_img)       ImageView     ivLeftImg;
    @BindView(R.id.iv_left_right_img) ImageView     ivLeftRightImg;
    @BindView(R.id.iv_right_img)      ImageView     ivRightImg;
    @BindView(R.id.tv_right_text)     TextView      tvRightText;
    @BindView(R.id.line_unfocus)      View          lineUnfocus;
    @BindView(R.id.line_focus)        View          lineFocus;
    public                            ClearEditText clearEditText;

    private Context                 mContext;
    private onLeftClickListener     mOnLeftClickListener;
    private onRightTxtClickListener mOnRightTxtClickListener;
    private onRightImgClickListener mOnRightImgClickListener;
    private textChangedListener     mtextChangedListener;

    private boolean hasText;

    public HtEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_ht_edittext, this);
        ButterKnife.bind(this);

        TypedArray     ta               = context.obtainStyledAttributes(attrs, R.styleable.HtEditTextView);
        String         editText         = ta.getString(R.styleable.HtEditTextView_edit_text);
        String         editHintText     = ta.getString(R.styleable.HtEditTextView_edit_hit_text);
        boolean        editClearShow    = ta.getBoolean(R.styleable.HtEditTextView_edit_clear_show, true);
        boolean        editdigitShow    = ta.getBoolean(R.styleable.HtEditTextView_edit_digits_show, false);
        String         rightText        = ta.getString(R.styleable.HtEditTextView_right_txt);
        ColorStateList rightTextColor   = ta.getColorStateList(R.styleable.HtEditTextView_right_txt_color);
        boolean        rightTextEnabled = ta.getBoolean(R.styleable.HtEditTextView_right_txt_enabled, true);
        boolean        leftImgSelected  = ta.getBoolean(R.styleable.HtEditTextView_left_image_selected, false);
        boolean        rightImgSelected = ta.getBoolean(R.styleable.HtEditTextView_right_image_selected, false);
        Drawable       rightImg         = ta.getDrawable(R.styleable.HtEditTextView_right_image);
        String         leftText         = ta.getString(R.styleable.HtEditTextView_left_txt);
        Drawable       leftImg          = ta.getDrawable(R.styleable.HtEditTextView_left_image);
        Drawable       leftRightImg     = ta.getDrawable(R.styleable.HtEditTextView_left_right_image);
        boolean        focusable        = ta.getBoolean(R.styleable.HtEditTextView_android_focusable, true);
        int            inputType        = ta.getInt(R.styleable.HtEditTextView_android_inputType, -1);

        if (editdigitShow) {
            clearEditText = findViewById(R.id.edietText_show_digits);
            clearEditText.setVisibility(VISIBLE);
            findViewById(R.id.edietText).setVisibility(GONE);
        } else {
            clearEditText = findViewById(R.id.edietText);
            clearEditText.setVisibility(VISIBLE);
            findViewById(R.id.edietText_show_digits).setVisibility(GONE);
        }
        // inputType
        if (inputType >= 0) {
            clearEditText.setInputType(inputType);
        }
        // 是否显示清楚按钮
        clearEditText.setShowClear(editClearShow);
        // 是否获取焦点
        clearEditText.setFocusable(focusable);

        clearEditText.setOnFocusChangeListener((v, hasFocus) -> {
            ivLeftImg.setSelected(hasFocus);
            clearEditText.setClearIconVisible(hasFocus && getText().length() > 0);

            lineFocus.setVisibility(hasFocus ? VISIBLE : GONE);
            lineUnfocus.setVisibility(hasFocus ? GONE : VISIBLE);
        });

        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mtextChangedListener != null)
                    mtextChangedListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasText = !TextUtils.isEmpty(s.toString().trim());
                if (mtextChangedListener != null)
                    mtextChangedListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mtextChangedListener != null)
                    mtextChangedListener.afterTextChanged(s);
            }
        });

        llytLeft.setOnClickListener(v -> {
            if (mOnLeftClickListener != null)
                mOnLeftClickListener.onLeftClick(v);
        });

        ivRightImg.setOnClickListener(v -> {
            if (mOnRightImgClickListener != null)
                mOnRightImgClickListener.onRightImgClick(v);
        });

        tvRightText.setOnClickListener(v -> {
            if (mOnRightTxtClickListener != null)
                mOnRightTxtClickListener.onRightTxtClick(v);
        });

        // 左侧图标
        if (leftImg != null) {
            ivLeftImg.setVisibility(View.VISIBLE);
            ivLeftImg.setImageDrawable(leftImg);
            ivLeftImg.setSelected(leftImgSelected);
        } else {
            ivLeftImg.setVisibility(View.GONE);

        }
        // 左侧文字
        if (!TextUtils.isEmpty(leftText)) {
            tvLeftText.setVisibility(VISIBLE);
            tvLeftText.setText(leftText);
        } else {
            tvLeftText.setVisibility(GONE);
        }
        // 左侧文字右边的图标
        if (leftRightImg != null) {
            ivLeftRightImg.setVisibility(View.VISIBLE);
            ivLeftRightImg.setImageDrawable(leftRightImg);
        } else {
            ivLeftRightImg.setVisibility(View.GONE);

        }
        // 左侧部分是否显示
        if (leftImg == null && TextUtils.isEmpty(leftText) && leftRightImg == null) {
            llytLeft.setVisibility(GONE);
        } else {
            llytLeft.setVisibility(VISIBLE);
        }
        // edittext提示文字
        if (!TextUtils.isEmpty(editHintText)) {
            clearEditText.setHint(editHintText);
        }
        // edittext文字
        if (!TextUtils.isEmpty(editText)) {
            clearEditText.setText(editText);
        }
        // 右侧文字
        if (!TextUtils.isEmpty(rightText)) {
            tvRightText.setVisibility(VISIBLE);
            tvRightText.setText(rightText);
        } else {
            tvRightText.setVisibility(GONE);

        }
        // 右侧文字颜色
        if (rightTextColor != null) {
            tvRightText.setTextColor(rightTextColor);
            tvRightText.setEnabled(rightTextEnabled);
        }
        // 右侧图标
        if (rightImg != null) {
            ivRightImg.setVisibility(View.VISIBLE);
            ivRightImg.setImageDrawable(rightImg);
            ivRightImg.setSelected(rightImgSelected);
        } else {
            ivRightImg.setVisibility(View.GONE);

        }

        ta.recycle();
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setText(String content) {
        clearEditText.setText(content);
    }

    public CharSequence getText() {
        return clearEditText.getText();
    }

    public void setRightTxtEnable(boolean enable) {
        tvRightText.setEnabled(enable);
    }

    public boolean getRightTxtEnable() {
        return tvRightText.isEnabled();
    }

    public void setRightImgSelected(boolean selected) {
        ivRightImg.setSelected(selected);
    }

    public boolean getRightImgSelected() {
        return ivRightImg.isSelected();
    }

    public void setTransformationMethod(TransformationMethod method) {
        clearEditText.setTransformationMethod(method);
    }

    public void setRightTxt(String content) {
        tvRightText.setText(content);
    }

    public void setLeftTxt(String content) {
        tvLeftText.setText(content);
    }

    public String getRightTxt() {
        return tvRightText.getText().toString();
    }

    public interface onRightTxtClickListener {
        void onRightTxtClick(View view);
    }

    public void setOnRightTxtClickListener(onRightTxtClickListener onRightTxtClickListener) {
        mOnRightTxtClickListener = onRightTxtClickListener;
    }

    public interface textChangedListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    public void addTextChangedListener(textChangedListener textChangedListener) {
        mtextChangedListener = textChangedListener;
    }

    public interface onRightImgClickListener {
        void onRightImgClick(View view);
    }

    public void setOnRightImgClickListener(onRightImgClickListener onRightImgClickListener) {
        mOnRightImgClickListener = onRightImgClickListener;
    }

    public interface onLeftClickListener {
        void onLeftClick(View view);
    }

    public void setOnLeftClickListener(onLeftClickListener onLeftClickListener) {
        mOnLeftClickListener = onLeftClickListener;
    }

    public void setInputFilter(InputFilter[] inputFilters) {
        clearEditText.setInPutFilter(inputFilters);
    }

    public void removeTextChangedListener() {
        mOnRightImgClickListener = null;

    }

    public void setSelection(int selection) {
        clearEditText.setSelection(selection);

    }
}
