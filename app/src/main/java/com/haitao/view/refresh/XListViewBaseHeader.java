package com.haitao.view.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * XListViewHeader 基类
 *
 * @author 陶声
 * @since 2017-09-26
 */

public abstract class XListViewBaseHeader extends LinearLayout {

    public final static int STATE_NORMAL     = 0;
    public final static int STATE_READY      = 1;
    public final static int STATE_REFRESHING = 2;

    public XListViewBaseHeader(Context context) {
        super(context);
    }

    public XListViewBaseHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void setState(int state);

    public abstract void setVisiableHeight(int height);

    public abstract int getVisiableHeight();
}