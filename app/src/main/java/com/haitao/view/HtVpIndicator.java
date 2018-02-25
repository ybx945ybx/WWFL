package com.haitao.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haitao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-11-17
 */
public class HtVpIndicator implements ViewPager.OnPageChangeListener {
    private int mSize;
    private int img2 = R.mipmap.ic_indicator_normal, img1 = R.mipmap.ic_indicator_selected;
    private int             imgSize      = 20;
    private List<ImageView> dotViewLists = new ArrayList<>();

    public HtVpIndicator(Context context, ViewPager viewPager, LinearLayout dotLayout, int size) {
        mSize = size;
        for (int i = 0; i < size; i++) {
            ImageView                 imageView = new ImageView(context);
            LinearLayout.LayoutParams params    = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //为小圆点左右添加间距
            params.leftMargin = 10;
            params.rightMargin = 10;
            //手动给小圆点一个大小
            params.height = imgSize;
            params.width = imgSize;
            if (i == 0) {
                imageView.setBackgroundResource(img1);
            } else {
                imageView.setBackgroundResource(img2);
            }
            //为LinearLayout添加ImageView
            dotLayout.addView(imageView, params);
            dotViewLists.add(imageView);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mSize; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % mSize) == i) {
                ((View) dotViewLists.get(i)).setBackgroundResource(img1);
            } else {
                ((View) dotViewLists.get(i)).setBackgroundResource(img2);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
