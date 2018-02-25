package com.haitao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页4个icon图标
 *
 * @author 陶声
 * @since 2017-09-26
 */

public class HomeTabView extends LinearLayout {
    @BindView(R.id.img_tab) CustomImageView mImgTab;    // 图标
    @BindView(R.id.tv_tab)  TextView        mTvTab;     // 文本

    public HomeTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context mContext
     * @param attrs   xml属性
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_home_tab_view, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeTabView);
        // 文本
        String tabName = ta.getString(R.styleable.HomeTabView_home_tab_name);
        // 本地图标
        int imgDrId = ta.getResourceId(R.styleable.HomeTabView_home_tab_drawable, 0);

        if (!TextUtils.isEmpty(tabName)) {
            mTvTab.setText(tabName);
        }
        if (imgDrId != 0) {
            mImgTab.setImageResource(imgDrId);
        }

        ta.recycle();
    }

    /**
     * 设置tab显示的文本
     *
     * @param tabName 文本
     */
    public void setTabName(String tabName) {
        mTvTab.setText(tabName);
    }

    /**
     * 设置tab显示的图标
     *
     * @param tabImg 图标
     */
    public void setTabImg(String tabImg) {
        ImageLoaderUtils.showOnlineImage(tabImg, mImgTab);
    }
}
