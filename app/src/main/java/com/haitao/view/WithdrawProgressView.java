package com.haitao.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 提现详情 - ProgressView
 *
 * @author 陶声
 * @since 2017-08-09
 */

public class WithdrawProgressView extends BaseProgressView {

    private String[] mProgressTexts = {"审核中", "审核通过", "提现成功"};

    public WithdrawProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(String progress) {
        switch (progress) {
            case "1": // 审核中
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mGrey, mGrey});
                setImgs(new int[]{mProgressOk, mProgressUnTreated, mProgressUnTreated});
                setLines(new int[]{mLineHalf, mLineEmpty});
                break;
            case "2": // 待付款
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressUnTreated});
                setLines(new int[]{mLineFull, mLineHalf});
//                setTextBSub("(待付款)");
                break;
            case "3": // 提现成功
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressOk});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            case "4": // 审核未通过
                mProgressTexts[1] = "审核未通过";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mProgressOk, mProgressWarn, mProgressUnTreated});
                setLines(new int[]{mLineFull, mLineEmpty});
                break;
            case "5": // 提现被驳回
                mProgressTexts[1] = "提现驳回";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mProgressOk, mProgressWarn, mProgressUnTreated});
                setLines(new int[]{mLineFull, mLineEmpty});
                break;
            case "6": // 提现失败
                mProgressTexts[2] = "提现失败";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressFail});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            default:
                mLlContainer.setVisibility(View.GONE);

        }
    }
}
