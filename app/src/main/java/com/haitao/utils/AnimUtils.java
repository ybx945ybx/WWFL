package com.haitao.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.haitao.R;

import io.swagger.client.model.DealExtraModel;

/**
 * Created by Administrator on 2016/6/17.
 * 动画类
 */
public class AnimUtils {
    /**
     * 点赞的动画
     * @param mContext
     * @param v
     */
    public static void animAgree(Context mContext, final View v, final TextView subV, final DealExtraModel obj){
        Animation scale = AnimationUtils.loadAnimation(mContext, R.anim.scale_anim);
        scale.setRepeatMode(Animation.REVERSE);
        scale.setRepeatCount(1);
        v.startAnimation(scale);
        scale.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setSelected("1".equals(obj.getIsPraised()));
                subV.setText(String.format("%s", obj.getPraiseCount()));
            }
        });
    }
}
