package com.haitao.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.haitao.R;
import com.haitao.utils.ScreenUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.haitao.common.annotation.ToastType.COMMON_SUCCESS;
import static com.haitao.common.annotation.ToastType.COPPY_SUCCESS;
import static com.haitao.common.annotation.ToastType.ERROR;
import static com.haitao.common.annotation.ToastType.SIGN_SUCCESS;
import static com.haitao.common.annotation.ToastType.WARNING;

/**
 * 通用toast
 * Created by a55 on 2017/11/21.
 */

public class ToastPopuWindow extends PopupWindow {

    @BindView(R.id.toastImage)   LottieAnimationView toastImage;
    @BindView(R.id.toastMessage) TextView            toastMessage;
    //    @BindView(R.id.toastPoint)   HaiTextView  toastPoint;
    @BindView(R.id.centerView)   LinearLayout        centerView;

    private Activity       mActivity;
    private LayoutInflater mInflater;
    private View           mContentView;
    private View           mParentView;

    public static ToastPopuWindow makeText(Activity activity, String message) {
        return new ToastPopuWindow(activity, -1, message);
    }

    public static ToastPopuWindow makeText(Activity activity, int type, String message) {
        return new ToastPopuWindow(activity, type, message);
    }

    public ToastPopuWindow(Activity activity, CharSequence message) {
        this(activity, -1, message);
    }

    public ToastPopuWindow(Activity activity, int type, CharSequence message) {
        if (activity.isFinishing())
            return;
        init(activity);
        renderUI_Common(type, message);
    }

    /**
     * 初始化
     *
     * @param activity Activity
     */
    private void init(Activity activity) {
        mActivity = activity;
        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.alert_toast_view, null);
        ButterKnife.bind(this, mContentView);
        setContentView(mContentView);
        setWidth(ScreenUtils.getScreenWidth(mActivity));
        setHeight(ScreenUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        update();
    }

    public ToastPopuWindow parentView(View parentView) {
        mParentView = parentView;
        return this;
    }

    public void show() {
        show(null);
    }

    private void renderUI_Common(int type, CharSequence message) {
        toastMessage.setText(message);

        String animation = "";
        switch (type) {
            case COMMON_SUCCESS:                        //  通用成功
                animation = "toast_common_success.json";
                break;
            case WARNING:                               //  警告
                animation = "toast_warning.json";
                break;
            case ERROR:                                 //  错误
                animation = "toast_error.json";
                break;
            case COPPY_SUCCESS:                         //  复制
                animation = "toast_copy_success.json";
                break;
            case SIGN_SUCCESS:                          //  签到
                animation = "toast_sign_success.json";
                break;
        }

        if (TextUtils.isEmpty(animation)) return;

        Logger.d(animation);

        toastImage.setVisibility(View.VISIBLE);
        toastImage.setAnimation(animation);

        toastImage.playAnimation();
    }

    private void show(View parent) {
        // activity已销毁
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }
        if (parent == null) {
            if (mParentView != null) {
                parent = mParentView;
            } else {
                parent = mActivity.getWindow().getDecorView();
            }
        }

        if (parent == null) return;

        if (!isShowing()) {
            // Appear
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            dismissMySelf(1500);
        }
    }

    private void dismissMySelf(long delayMillis) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(centerView, "scaleX", 1, 0.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(centerView, "scaleY", 1, 0.8f);
        ObjectAnimator alpha  = ObjectAnimator.ofFloat(centerView, "alpha", 1, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delayMillis);
        animatorSet.setDuration(300);
        animatorSet.play(scaleX).with(scaleY).before(alpha);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShowing()) {
                    dismiss();
                }
            }
        });

    }
}

