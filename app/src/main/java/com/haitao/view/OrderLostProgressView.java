package com.haitao.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 丢单详情 - ProgressView
 *
 * @author 陶声
 * @since 2017-08-09
 */

public class OrderLostProgressView extends BaseProgressView {

    private String[] mProgressTexts = {"丢单处理中", "待商家确认", "确认丢单"};

    public OrderLostProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(String progress) {
        switch (progress) {
            case "0": // 已处理
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressOk});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            case "1": // 处理中
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mGrey, mGrey});
                setImgs(new int[]{mProgressOk, mProgressUnTreated, mProgressUnTreated});
                setLines(new int[]{mLineHalf, mLineEmpty});
                break;
            case "2": // 待商家确认
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mGrey});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressUnTreated});
                setLines(new int[]{mLineFull, mLineHalf});
//                setTextBSub("(待付款)");
                break;
            case "3": // 无效丢单
                mProgressTexts[2] = "无效丢单";
                setTexts(mProgressTexts);
                setTextColors(new int[]{mOrange, mOrange, mOrange});
                setImgs(new int[]{mProgressOk, mProgressOk, mProgressWarn});
                setLines(new int[]{mLineFull, mLineFull});
                break;
            default:
                mLlContainer.setVisibility(View.GONE);

        }
    }
}
