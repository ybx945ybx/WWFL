package com.haitao.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.TopicLink;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.SlidePicModel;

/**
 * 两行条目
 *
 * @author 陶声
 * @since 2017-07-24
 */

public class HtAdView extends RelativeLayout {

    @BindView(R.id.img_ad)     CustomImageView mImgAd;      // 广告图
    @BindView(R.id.img_ad_tag) ImageView       mImgAdTag;   // 广告角标

    private Context mContext;

    public HtAdView(Context context, @Nullable AttributeSet attrs) {
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
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_ad_view, this);
        ButterKnife.bind(this);
    }

    /**
     * 设置样式
     */
    public void setView(SlidePicModel slidePicModel) {
        // 广告图
        ImageLoaderUtils.showOnlineImage(slidePicModel.getPic(), mImgAd);
        // 广告角标
        mImgAdTag.setVisibility(TextUtils.equals(slidePicModel.getIsCommercial(), "1") ? VISIBLE : GONE);
        // 点击事件
        mImgAd.setOnClickListener(v -> TopicLink.jump(mContext, slidePicModel, TopicLink.SOURCE_TYPE.CROSS));
    }
}
