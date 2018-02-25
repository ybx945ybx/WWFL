package zhy.com.highlight.position;

import android.graphics.RectF;

import zhy.com.highlight.HighLight;

/**
 * Created by caizepeng on 16/8/20.
 */
public  class OnBottomPosNewCallback extends OnBaseCallback{
    public OnBottomPosNewCallback() {
    }

    public OnBottomPosNewCallback(float offset) {
        super(offset);
    }

    @Override
    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
        marginInfo.rightMargin = 0;
        marginInfo.topMargin = rectF.top + rectF.height()+offset;
    }

}
