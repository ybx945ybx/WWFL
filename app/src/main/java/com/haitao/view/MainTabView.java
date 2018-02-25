package com.haitao.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页 底部tab
 *
 * @author 陶声
 * @since 2017-09-26
 */

public class MainTabView extends LinearLayout {
    @BindView(R.id.img_tab) CustomImageView mImgTab;    // 图标
    @BindView(R.id.tv_tab)  TextView        mTvTab;     // 文本

    private boolean mSelected; // 是否选中
    private String  mNormalImgUrl, mActiveImgUrl;

    public MainTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_main_tab_view, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainTabView);
        // 文本
        String tabName = ta.getString(R.styleable.MainTabView_tab_name);
        // 本地图标
        int imgDrId = ta.getResourceId(R.styleable.MainTabView_tab_drawable, 0);

        if (!TextUtils.isEmpty(tabName)) {
            mTvTab.setText(tabName);
        }
        if (imgDrId != 0) {
            mImgTab.setImageResource(imgDrId);
        }

        // 是否选中
        boolean selectd = ta.getBoolean(R.styleable.MainTabView_tab_selected, false);
        setSelected(selectd);


        ta.recycle();

        setOnClickListener(v -> setSelected(true));
    }

    /**
     * 设置选中状态
     *
     * @param selected 选中状态
     */
    public void setSelected(boolean selected) {
        mSelected = selected;
        mTvTab.setSelected(mSelected);
        mImgTab.setSelected(mSelected);
        loadNetImg();
    }

    /**
     * 设置文本
     *
     * @param text 文本
     */
    public void setText(String text) {
        mTvTab.setText(text);
    }

    /**
     * 设置字体颜色
     *
     * @param colorStateList 字体颜色
     */
    public void setTextColor(ColorStateList colorStateList) {
        mTvTab.setTextColor(colorStateList);
    }

    /**
     * 设置网路图标url
     *
     * @param imgUrls 图标urls
     */
    public void setImgUrls(String[] imgUrls) {
        if (imgUrls != null && imgUrls.length >= 2) {
            mNormalImgUrl = imgUrls[0];
            mActiveImgUrl = imgUrls[1];
            loadNetImg();
        }
    }

    /**
     * 加载网络图
     */
    private void loadNetImg() {
        if (!TextUtils.isEmpty(mActiveImgUrl)) {
            //            ImageLoaderUtils.showOnlineImage(mSelected ? mActiveImgUrl : mNormalImgUrl, mImgTab);
            mImgTab.setImageURI(mSelected ? mActiveImgUrl : mNormalImgUrl);
        }
    }
}
