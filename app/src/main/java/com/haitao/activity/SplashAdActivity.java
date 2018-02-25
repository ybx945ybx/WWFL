package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.TopicLink;
import com.haitao.view.CustomImageView;
import com.haitao.view.RoundProgressBar;
import com.tendcloud.tenddata.TCAgent;

import java.util.Timer;
import java.util.TimerTask;

import io.swagger.client.model.SlidePicModel;

/**
 * 启动页
 */
public class SplashAdActivity extends BaseActivity {
    private RoundProgressBar roundProgress;
    private ViewGroup        layoutProgress;
    private TextView         tvProgress;
    //    private AdObject         mSlidePicModel;
    private SlidePicModel    mSlidePicModel;
    private CustomImageView  ivImage, ivAd;
    private int mTime  = 300;
    private int mTime1 = 4;
    private Timer mTimer;
    private Timer mTimer1;

    /**
     * 跳转到当前页
     *
     * @param mContext mContext
     */
    /*public static void launch(Context mContext, AdObject object) {
        Intent intent = new Intent(mContext, SplashAdActivity.class);
        intent.putExtra(TransConstant.OBJECT, object);
        mContext.startActivity(intent);
    }*/

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, SlidePicModel object) {
        Intent intent = new Intent(context, SplashAdActivity.class);
        intent.putExtra(TransConstant.OBJECT, object);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏 状态栏 & 底部导航栏
       /* View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        setContentView(R.layout.activity_splash);
        TAG = "闪屏广告";
        if (null != getIntent()) {
            //            mSlidePicModel = (AdObject) getIntent().getSerializableExtra(TransConstant.OBJECT);
            mSlidePicModel = (SlidePicModel) getIntent().getSerializableExtra(TransConstant.OBJECT);
        }
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        RelativeLayout imgSplash = getView(R.id.rl_logo);
        ImageView imgSplash = getView(R.id.img_splash);
        ivAd = getView(R.id.ivAd);
        imgSplash.setVisibility(View.GONE);
        ivAd.setVisibility(View.VISIBLE);

        //        int                         screenWidth  = ScreenUtils.getScreenWidth(mContext);
        //        int                         screenHeight = ScreenUtils.getScreenHeight(mContext);
        //        RelativeLayout.LayoutParams lp           = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        //        ivAd.setLayoutParams(lp);
        layoutProgress = getView(R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        tvProgress = getView(R.id.tvProgress);
        roundProgress = getView(R.id.roundProgress);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ivAd.setOnClickListener(v -> {
            if (mTimer != null) {
                mTimer.cancel();
            }
            TCAgent.onEvent(mContext, "广告页-查看详情");
            TopicLink.jump(mContext, mSlidePicModel);
        });
        layoutProgress.setOnClickListener(v -> {
            if (mTimer != null) {
                mTimer.cancel();
            }
            TCAgent.onEvent(mContext, "广告页-跳过");
            MainActivity.launch(mContext);
            finish();
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
               /* if((mTime % 100) == 0){
                    tvProgress.setText(String.format("跳过\n%ds",(int)(mTime/100)));
                }*/
                roundProgress.setProgress(mTime / 3 < 0 ? 0 : mTime / 3);
            } else {
                if (mTimer != null) {
                    mTimer.cancel();
                }
                MainActivity.launch(mContext);
                finish();
            }
        }
    };

    Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                tvProgress.setText(String.format("跳过\n%ds", mTime1));
            } else {
                if (mTimer1 != null) {
                    mTimer1.cancel();
                }
            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        if (null != mSlidePicModel) {
            ImageLoaderUtils.showLocationImage(mSlidePicModel.getPic(), ivAd);
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(mTime--);
            }
        }, 0, 10);
        mTimer1 = new Timer();
        mTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler1.sendEmptyMessage(mTime1--);
            }
        }, 0, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTime < 250) {
            MainActivity.launch(mContext);
            finish();
        }
    }

    @Override
    protected String getActivityTAG() {
        return "";
    }
}
