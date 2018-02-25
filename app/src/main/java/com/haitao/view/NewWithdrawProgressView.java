package com.haitao.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

public class NewWithdrawProgressView extends LinearLayout {

    @BindView(R.id.img_progress_a)    ImageView        mImgProgressA;
    @BindView(R.id.img_line_a)        ImageView        mImgLineA;
    @BindView(R.id.img_progress_b)    ImageView        mImgProgressB;
    @BindView(R.id.img_line_b)        ImageView        mImgLineB;
    @BindView(R.id.img_progress_c)    ImageView        mImgProgressC;
    @BindView(R.id.tv_progerss_a)     TextView         mTvProgerssA;
    @BindView(R.id.tv_progerss_b)     TextView         mTvProgerssB;
    @BindView(R.id.tv_progerss_c)     TextView         mTvProgerssC;
    //    @BindView(R.id.tv_progerss_sub_b) TextView     mTvProgerssSubB;
    @BindView(R.id.cl_container)      ConstraintLayout mClContainer;
    @BindView(R.id.tv_progress_b_sub) TextView         mTvProgressBSub;

    private String[] mProgressTexts = {"审核中", "审核通过", "提现成功"};

    protected int mOrange;
    protected int mGrey;
    //    protected int mProgressOk;
    //    protected int mProgressUnTreated;
    //    protected int mProgressWarn;
    //    protected int mProgressFail;
    protected int mLineEmpty;
    protected int mLineHalf;
    protected int mLineFull;

    private int mImgCheck;
    private int mImgCheckPassedGrey;
    private int mImgCheckPassedOrange;
    private int mImgWithdrawSuccessGrey;
    private int mImgWithdrawSuccessOrange;
    private int mImgWithdrawFail;

    public NewWithdrawProgressView(Context context, @Nullable AttributeSet attrs) {
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
        LayoutInflater.from(context).inflate(R.layout.layout_new_withdraw_pgview, this);
        // 颜色
        mOrange = ContextCompat.getColor(context, R.color.orangeFF804D);
        mGrey = ContextCompat.getColor(context, R.color.greyA5A5A8);
        // 节点图
        //        mProgressOk = R.mipmap.ic_progress_ok;
        //        mProgressUnTreated = R.mipmap.ic_progress_untreated_new;
        //        mProgressWarn = R.mipmap.ic_progress_warn;
        //        mProgressFail = R.mipmap.ic_progress_fail;


        mImgCheck = R.mipmap.ic_check_orange;
        mImgCheckPassedGrey = R.mipmap.ic_yes_circle_grey;
        mImgCheckPassedOrange = R.mipmap.ic_yes_circle_orange;
        mImgWithdrawSuccessGrey = R.mipmap.ic_dollar_circle_grey;
        mImgWithdrawSuccessOrange = R.mipmap.ic_dollar_circle_orange;
        mImgWithdrawFail = R.mipmap.ic_warning_circle_orange;


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

    public void setProgress(String progress) {
        switch (progress) {
            case "1": // 审核中
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mGrey, mGrey});
                setImgs(new int[]{mImgCheck, mImgCheckPassedGrey, mImgWithdrawSuccessGrey});
                setLines(new int[]{mLineHalf, mLineEmpty});
                break;
            case "2": // 待付款
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mImgCheck, mImgCheckPassedOrange, mImgWithdrawSuccessGrey});
                setLines(new int[]{mLineFull, mLineHalf});
                mTvProgressBSub.setVisibility(VISIBLE);
                break;
            case "3": // 提现成功
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mImgCheck, mImgCheckPassedOrange, mImgWithdrawSuccessOrange});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            case "4": // 审核未通过
                mProgressTexts[1] = "审核未通过";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mImgCheck, mImgWithdrawFail, mImgWithdrawSuccessGrey});
                setLines(new int[]{mLineFull, mLineEmpty});
                break;
            case "5": // 提现被驳回
                mProgressTexts[1] = "提现驳回";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mImgCheck, mImgWithdrawFail, mImgWithdrawSuccessGrey});
                setLines(new int[]{mLineFull, mLineEmpty});
                break;
            case "6": // 提现失败
                mProgressTexts[2] = "提现失败";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mImgCheck, mImgWithdrawSuccessOrange, mImgWithdrawFail});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            default:
                mClContainer.setVisibility(View.GONE);

        }
    }
}
