package zhy.com.highlight.position;

import android.graphics.RectF;

import zhy.com.highlight.HighLight;

/**
 * 修改偏移问题
 * Created by caizepeng on 16/8/20.
 */
public class OnTopPosNewCallback extends OnBaseCallback {
    private boolean mUseHalfBorder;

    public OnTopPosNewCallback() {
    }

    public OnTopPosNewCallback(boolean useHalfBorder) {
        mUseHalfBorder = useHalfBorder;
    }

    public OnTopPosNewCallback(float offset) {
        super(offset);
    }

    public OnTopPosNewCallback(float offset, boolean useHalfBorder) {
        super(offset);
        mUseHalfBorder = useHalfBorder;
    }

    public boolean isUseHalfBorder() {
        return mUseHalfBorder;
    }

    @Override
    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
        marginInfo.leftMargin = 0;
        marginInfo.bottomMargin = bottomMargin + rectF.height() + offset;
    }
}
