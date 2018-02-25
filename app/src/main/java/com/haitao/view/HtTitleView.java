package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 通用标题
 *
 * @author 陶声
 * @since 2017-07-27
 */

public class HtTitleView extends RelativeLayout {
    @BindView(R.id.tv_sub_title)   TextView  mTvSubTitle;       // 标题
    @BindView(R.id.view_indicator) ImageView mImgIcon;    // 图标

    public HtTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_ht_title, this);
        ButterKnife.bind(this);

        TypedArray ta       = context.obtainStyledAttributes(attrs, R.styleable.HtTitleView);
        String     subTitle = ta.getString(R.styleable.HtTitleView_sub_title);
        Drawable   dwIcon   = ta.getDrawable(R.styleable.HtTitleView_sub_title_icon);

        if (!TextUtils.isEmpty(subTitle)) {
            mTvSubTitle.setText(subTitle);
        }
        if (dwIcon != null) {
            mImgIcon.setImageDrawable(dwIcon);
        } else {
            mImgIcon.setVisibility(GONE);
        }
        ta.recycle();
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    public void setTitle(String title) {
        mTvSubTitle.setText(title);
    }

    /**
     * 设置图标
     *
     * @param imgRes 图标
     */
    public void setIcon(int imgRes) {
        //        Drawable drawable = ContextCompat.getDrawable(mContext, imgRes);
        mImgIcon.setImageResource(imgRes);
    }
}
