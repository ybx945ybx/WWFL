package com.haitao.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通用进度
 *
 * @author 陶声
 * @since 2017-08-09
 */

public class BaseProgressView extends LinearLayout {

    @BindView(R.id.img_progress_a)    ImageView    mImgProgressA;
    @BindView(R.id.img_line_a)        ImageView    mImgLineA;
    @BindView(R.id.img_progress_b)    ImageView    mImgProgressB;
    @BindView(R.id.img_line_b)        ImageView    mImgLineB;
    @BindView(R.id.img_progress_c)    ImageView    mImgProgressC;
    @BindView(R.id.tv_progerss_a)     TextView     mTvProgerssA;
    @BindView(R.id.tv_progerss_b)     TextView     mTvProgerssB;
    @BindView(R.id.tv_progerss_c)     TextView     mTvProgerssC;
//    @BindView(R.id.tv_progerss_sub_b) TextView     mTvProgerssSubB;
    @BindView(R.id.ll_container)      LinearLayout mLlContainer;

    protected int mOrange;
    protected int mGrey;
    protected int mProgressOk;
    protected int mProgressUnTreated;
    protected int mProgressWarn;
    protected int mProgressFail;
    protected int mLineEmpty;
    protected int mLineHalf;
    protected int mLineFull;

    public BaseProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context mContext
     * @param attrs   属性
     */
    protected void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_base_progress_view, this);
        // 颜色
        mOrange = ContextCompat.getColor(context, R.color.brightOrange);
        mGrey = ContextCompat.getColor(context, R.color.middle_grey2);
        // 节点图
        mProgressOk = R.mipmap.ic_progress_ok;
        mProgressUnTreated = R.mipmap.ic_progress_untreated_new;
        mProgressWarn = R.mipmap.ic_progress_warn;
        mProgressFail = R.mipmap.ic_progress_fail;
        // 横线
        mLineEmpty = R.mipmap.ic_progress_line_empty;
        mLineHalf = R.mipmap.ic_progress_line_half;
        mLineFull = R.mipmap.ic_progress_line_full;
        ButterKnife.bind(this);
    }

    /**
     * 设置进度文本
     *
     * @param texts 进度文本
     */
    protected void setTexts(String[] texts) {
        if (texts.length < 3)
            return;
        mTvProgerssA.setText(texts[0]);
        mTvProgerssB.setText(texts[1]);
        mTvProgerssC.setText(texts[2]);
    }

    /**
     * 设置进度文本颜色
     *
     * @param colors 进度文本颜色
     */
    protected void setTextColors(int[] colors) {
        if (colors.length < 3)
            return;
        mTvProgerssA.setTextColor(colors[0]);
        mTvProgerssB.setTextColor(colors[1]);
        mTvProgerssC.setTextColor(colors[2]);
    }

    /**
     * 设置进度图标
     *
     * @param imgs 进度图标
     */
    protected void setImgs(int[] imgs) {
        if (imgs.length < 3)
            return;
        mImgProgressA.setImageResource(imgs[0]);
        mImgProgressB.setImageResource(imgs[1]);
        mImgProgressC.setImageResource(imgs[2]);
    }

    /**
     * 设置进度横线
     *
     * @param lines 进度横线
     */
    protected void setLines(int[] lines) {
        if (lines.length < 2)
            return;
        mImgLineA.setImageResource(lines[0]);
        mImgLineB.setImageResource(lines[1]);
    }

    /*protected void setTextBSub(String textBSub) {
        mTvProgerssSubB.setVisibility(View.VISIBLE);
        mTvProgerssSubB.setText(textBSub);
    }*/
}
