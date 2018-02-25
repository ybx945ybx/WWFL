package com.haitao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class FullGirdView extends GridView { 
 
    public FullGirdView(Context context) { 
        super(context); 
    } 
    
    public FullGirdView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
    
    public FullGirdView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
    
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
 
} 