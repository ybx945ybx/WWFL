package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * v5.9推广赚返利操作引导
 * <p>
 * Created by a55 on 2018/2/5.
 */

public class OperationGuideActivity extends AppCompatActivity {

    @BindView(R.id.llyt_content) LinearLayout llytContent;
    @BindView(R.id.wave1)        ImageView    mWave1;
    @BindView(R.id.iv_guide_top) ImageView    ivGuideTop;
    @BindView(R.id.wave2)        ImageView    mWave2;
    @BindView(R.id.wave3)        ImageView    mWave3;
    @BindView(R.id.tv_know)      TextView     tvKnow;

    private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3;

    private static final int OFFSET              = 600;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mWave2.startAnimation(mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mWave3.startAnimation(mAnimationSet3);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);
        ButterKnife.bind(this);

        renderUi();

    }

    private void renderUi() {

        int statusBarHeight = getstatusBarHeigh();
        Logger.d("statusBarHeight----->" + statusBarHeight);
        if (statusBarHeight > 0){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mWave1.getLayoutParams();
            lp.topMargin = statusBarHeight;

            ivGuideTop.setLayoutParams(lp);
            mWave1.setLayoutParams(lp);
        }

        mAnimationSet1 = initAnimationSet();
        mAnimationSet2 = initAnimationSet();
        mAnimationSet3 = initAnimationSet();

        showWaveAnimation();

        tvKnow.setOnClickListener(v -> onBackPressed());
        llytContent.setOnClickListener(v -> onBackPressed());
    }

    private int getstatusBarHeigh() {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 3);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(OFFSET * 3);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private void showWaveAnimation() {
        mWave1.startAnimation(mAnimationSet1);
        //        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
        //        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET * 2);
    }

    private void clearWaveAnimation() {
        mWave1.clearAnimation();
        mWave2.clearAnimation();
        mWave3.clearAnimation();
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, OperationGuideActivity.class);
        context.startActivity(intent);
        ((BaseActivity) context).overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
    }
}
