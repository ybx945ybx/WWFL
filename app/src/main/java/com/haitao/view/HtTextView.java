package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义TextView
 *
 * @author 陶声
 * @since 2017-11-17
 */

public class HtTextView extends RelativeLayout {

    @BindView(R.id.iv_left_img)   ImageView ivLeftImg;
    @BindView(R.id.textview)      TextView  mTv;
    @BindView(R.id.iv_right_img)  ImageView ivRightImg;
    @BindView(R.id.tv_right_text) TextView  tvRightText;

    private Context                 mContext;
    private onRightTxtClickListener mOnRightTxtClickListener;
    private onRightImgClickListener mOnRightImgClickListener;
    private textChangedListener     mtextChangedListener;

    private boolean hasText;

    public HtTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_ht_textview, this);
        ButterKnife.bind(this);

        TypedArray ta       = context.obtainStyledAttributes(attrs, R.styleable.HtTextView);
        String     text     = ta.getString(R.styleable.HtTextView_tv_text);
        Drawable   rightImg = ta.getDrawable(R.styleable.HtTextView_tv_right_image);
        Drawable   leftImg  = ta.getDrawable(R.styleable.HtTextView_tv_left_image);


        //        mTv.setOnFocusChangeListener((v, hasFocus) -> ivLeftImg.setSelected(hasFocus));

        /*mTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mtextChangedListener != null)
                    mtextChangedListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasText = !TextUtils.isEmpty(s);
                if (mtextChangedListener != null)
                    mtextChangedListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mtextChangedListener != null)
                    mtextChangedListener.afterTextChanged(s);
            }
        });*/

        /*ivRightImg.setOnClickListener(v -> {
            if (mOnRightImgClickListener != null)
                mOnRightImgClickListener.onRightImgClick(v);
        });

        tvRightText.setOnClickListener(v -> {
            if (mOnRightTxtClickListener != null)
                mOnRightTxtClickListener.onRightTxtClick(v);
        });*/


        // 左侧图标
        if (leftImg != null) {
            ivLeftImg.setVisibility(View.VISIBLE);
            ivLeftImg.setImageDrawable(leftImg);
            //            ivLeftImg.setSelected(leftImgSelected);
        }
        // edittext文字
        if (!TextUtils.isEmpty(text)) {
            mTv.setText(text);
        }
        // 右侧文字
        /*if (!TextUtils.isEmpty(rightText)) {
            tvRightText.setVisibility(VISIBLE);
            tvRightText.setText(rightText);
        }*/
        // 右侧文字颜色
        /*if (rightTextColor != null) {
            tvRightText.setTextColor(rightTextColor);
            tvRightText.setEnabled(rightTextEnabled);
        }*/
        // 右侧图标
        if (rightImg != null) {
            ivRightImg.setVisibility(View.VISIBLE);
            ivRightImg.setImageDrawable(rightImg);
            //            ivRightImg.setSelected(rightImgSelected);
        }

        ta.recycle();
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setText(String content) {
        mTv.setText(content);
    }

    public CharSequence getText() {
        return mTv.getText();
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
        mTv.setTransformationMethod(method);
    }

    public void setRightTxt(String content) {
        tvRightText.setText(content);
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
}
