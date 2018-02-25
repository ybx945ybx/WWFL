package com.haitao.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.common.annotation.ToastType;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 表单 - 图标 + 文字
 *
 * @author 陶声
 * @since 2018-01-24
 */

public class HtItemTextView extends RelativeLayout {

    @BindView(R.id.img_left)     ImageView mImgLeft;
    @BindView(R.id.tv_title)     TextView  mTvTitle;
    @BindView(R.id.tv_right)     TextView  mTvRight;
    @BindView(R.id.img_right)    ImageView mImgRight;
    @BindView(R.id.view_divider) View      mViewUnderline;

    @BindDimen(R.dimen.hitv_left_title_width) int mTitleWidth;
    //    @BindColor(R.color.greyA5A5A8)            int mColorText;
    //    @BindColor(R.color.grey4F4F53)            int mColorTextUnselected;

    private Context mContext;
    private Boolean mTextCenter;

    public HtItemTextView(Context context, AttributeSet attrs) {
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
        LayoutInflater.from(context).inflate(R.layout.view_ht_item_text, this);
        ButterKnife.bind(this);

        TypedArray ta               = context.obtainStyledAttributes(attrs, R.styleable.HtItemTextView);
        int        leftImgRes       = ta.getResourceId(R.styleable.HtItemTextView_hitv_left_img, 0);
        String     title            = ta.getString(R.styleable.HtItemTextView_hitv_title);
        String     rightText        = ta.getString(R.styleable.HtItemTextView_hitv_right_text);
        Boolean    rightImgVisible  = ta.getBoolean(R.styleable.HtItemTextView_hitv_right_img_visible, true);
        Boolean    underlineVisible = ta.getBoolean(R.styleable.HtItemTextView_hitv_underline_visible, true);
        int        maxlines         = ta.getInt(R.styleable.HtItemTextView_android_maxLines, 0);
        // 是否是居中样式
        mTextCenter = ta.getBoolean(R.styleable.HtItemTextView_hitv_center, false);
        if (mTextCenter) {
            mTvTitle.setMinWidth(mTitleWidth);
            mTvRight.setGravity(Gravity.LEFT);
        }
        // 左侧图标
        mImgLeft.setVisibility(leftImgRes != 0 ? VISIBLE : GONE);
        if (leftImgRes != 0) {
            mImgLeft.setImageResource(leftImgRes);
        }
        // 标题
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
        // 右侧文本
        if (!TextUtils.isEmpty(rightText)) {
            mTvRight.setText(rightText);
            mTvRight.setEnabled(true);
        } else if (mTextCenter) {
            mTvRight.setText(R.string.unselected);
            mTvRight.setEnabled(false);
        }
        // 单行设置
        if (maxlines != 0) {
            mTvRight.setMaxLines(maxlines);
        }
        // 右侧图标
        mImgRight.setVisibility(rightImgVisible ? VISIBLE : GONE);
        // 右侧图标
        mViewUnderline.setVisibility(underlineVisible ? VISIBLE : GONE);
        // 长按复制
        setOnLongClickListener(v -> {
            String rText = mTvRight.getText().toString();
            if (!TextUtils.isEmpty(rText)) {
                copyToClipboard(context, rText);
                return true;
            } else {
                return false;
            }
        });
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
     * 左侧图标
     *
     * @param imgLeft 左侧图标
     */
    public void setImgLeft(int imgLeft) {
        mImgLeft.setImageResource(imgLeft);
    }

    /**
     * 设置右侧文本
     *
     * @param text 右侧文本
     */
    public void setRightText(String text) {
        mTvRight.setText(text);
        if (mTextCenter)
            mTvRight.setEnabled(true);
    }

    /**
     * 清除右侧文本
     */
    public void clearRightText() {
        mTvRight.setText(mTextCenter ? "未选择" : "");
        if (mTextCenter)
            mTvRight.setEnabled(false);
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
     * 复制到剪贴板
     *
     * @param context mContext
     * @param content 内容
     */
    private void copyToClipboard(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        ((BaseActivity) context).showToast(ToastType.COMMON_SUCCESS, "复制成功");
    }
}
