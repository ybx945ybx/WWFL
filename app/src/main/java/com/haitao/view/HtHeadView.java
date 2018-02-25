package com.haitao.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 通用标题栏
 *
 * @author 陶声
 * @since 2017-07-31
 */

public class HtHeadView extends RelativeLayout {

    @BindView(R.id.ib_left)      ImageButton mIbLeft;       // 左侧按钮
    @BindView(R.id.ib_right)     ImageButton mIbRight;      // 右侧按钮
    @BindView(R.id.tv_title)     TextView    mTvTitle;      // 标题文本
    @BindView(R.id.tv_right)     TextView    mTvRight;      // 右侧文本
    @BindView(R.id.view_divider) View        mViewDivider;  // 底部分割线

    @BindDimen(R.dimen.text_size_mediumx) int mTextSizeMedium;  // 中等字体大小

    private Context              mContext;
    private OnRightClickListener mOnRightClickListener;
    private OnLeftClickListener  mOnLeftClickListener;

    public HtHeadView(Context context, AttributeSet attrs) {
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
        LayoutInflater.from(context).inflate(R.layout.view_ht_head, this);
        ButterKnife.bind(this);

        TypedArray     ta               = context.obtainStyledAttributes(attrs, R.styleable.HtHeadView);
        String         centerText       = ta.getString(R.styleable.HtHeadView_center_text);
        String         rightText        = ta.getString(R.styleable.HtHeadView_right_text);
        ColorStateList rightTextColor   = ta.getColorStateList(R.styleable.HtHeadView_right_text_color);
        boolean        rightTextEnabled = ta.getBoolean(R.styleable.HtHeadView_right_text_enabled, true);
        boolean        dividerVisible   = ta.getBoolean(R.styleable.HtHeadView_divider_visible, true);
        Drawable       rightImg         = ta.getDrawable(R.styleable.HtHeadView_right_img);
        Drawable       leftImg          = ta.getDrawable(R.styleable.HtHeadView_left_img);
        // 是否是等级一的标题
        boolean level1Title = ta.getBoolean(R.styleable.HtHeadView_level_1_title, false);
        // 标题文本
        if (!TextUtils.isEmpty(centerText)) {
            mTvTitle.setText(centerText);
        }
        // 右侧文本
        if (!TextUtils.isEmpty(rightText)) {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(rightText);
        }
        mTvRight.setEnabled(rightTextEnabled);
        // 右侧文本颜色
        if (rightTextColor != null) {
            mTvRight.setTextColor(rightTextColor);
        }
        // 右侧按钮
        if (rightImg != null) {
            mIbRight.setVisibility(View.VISIBLE);
            mIbRight.setImageDrawable(rightImg);
        }
        // 左侧按钮
        if (leftImg != null) {
            mIbLeft.setImageDrawable(leftImg);
        }
        // 底部分割线可见性
        mViewDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        if (level1Title) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        ta.recycle();
    }

    /**
     * 设置标题文本
     *
     * @param centerText 标题文本
     */
    public void setCenterText(String centerText) {
        mTvTitle.setText(centerText);
    }

    /**
     * 设置底部分割线是否可见
     *
     * @param dividerVisible
     */
    public void setDividerVisible(boolean dividerVisible) {
        mViewDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置右侧文本Enabled状态
     *
     * @param enabled enabled状态
     */
    public void setRightTextEnabled(boolean enabled) {
        mTvRight.setEnabled(enabled);
    }

    /**
     * 设置右侧文本可见状态
     *
     * @param visible 可见状态
     */
    public void setRightTextVisible(boolean visible) {
        mTvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右侧按钮图标
     *
     * @param imgRes 图标
     */
    public void setRightImg(int imgRes) {
        mIbRight.setVisibility(View.VISIBLE);
        mIbRight.setImageResource(imgRes);
    }

    /**
     * 设置右侧按钮文本
     *
     * @param rightText 右侧文本
     */
    public void setRightText(String rightText) {
        mTvRight.setText(rightText);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_left)
    public void onIbLeftClicked(View v) {
        if (mOnLeftClickListener != null) {
            mOnLeftClickListener.onLeftClick(v);
        } else {
            // 隐藏输入法键盘
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mIbLeft.getWindowToken(), 0);
            ((Activity) mContext).onBackPressed();
        }
    }

    /**
     * 点击右侧文本
     */
    @OnClick(R.id.tv_right)
    public void onRightTextClicked(View v) {
        if (mOnRightClickListener != null) {
            mOnRightClickListener.onRightClick(v);
        }
    }

    /**
     * 点击右侧图标
     */
    @OnClick(R.id.ib_right)
    public void onRightImgClicked(View v) {
        if (mOnRightClickListener != null) {
            mOnRightClickListener.onRightClick(v);
        }
    }

    public interface OnRightClickListener {
        void onRightClick(View view);
    }

    public interface OnLeftClickListener {
        void onLeftClick(View view);
    }

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        mOnRightClickListener = onRightClickListener;
    }

    public void setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
        mOnLeftClickListener = onLeftClickListener;
    }
}
