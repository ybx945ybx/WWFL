package com.haitao.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.common.HtApplication;
import com.haitao.utils.TopicLink;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.tendcloud.tenddata.TCAgent;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;


public class BaseFragment extends RxFragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected Context mContext;
    protected String TAG = getClass().getSimpleName();
    //为空或者网络异常
    public ViewGroup ll_common_error;
    public TextView  tvErrorMsg, btnRefresh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 初始化错误信息视图
     */
    public void initError(View view) {
        ll_common_error = getView(view, R.id.ll_common_error);
        tvErrorMsg = getView(view, R.id.tvErrorMsg);
        btnRefresh = getView(view, R.id.btnRefresh);
    }

    /**
     * 设置错误信息 0:为空，1：网络异常
     *
     * @param type 类型
     */
    public void setErrorType(int type) {
        // 图标
       /* Drawable dwImage = getResources().getDrawable(0 == type ? R.mipmap.ic_empty_logo : R.mipmap.ic_error_logo);
        dwImage.setBounds(0, 0, dwImage.getMinimumWidth(), dwImage.getMinimumHeight());
        tvErrorMsg.setCompoundDrawables(null, dwImage, null, null);
        // 错误信息
        tvErrorMsg.setText(0 == type ? R.string.page_empty : R.string.network_load_error);
        // 按钮文字
        btnRefresh.setText(0 == type ? R.string.back : R.string.refresh);
        // 按钮事件
        if (0 == type) {
            btnRefresh.setOnClickListener(v -> ((Activity) mContext).onBackPressed());
        } */

        // 图标
        Drawable dwImage = getResources().getDrawable(1 == type ? R.mipmap.ic_error_logo : R.mipmap.ic_empty_logo);
        dwImage.setBounds(0, 0, dwImage.getMinimumWidth(), dwImage.getMinimumHeight());
        tvErrorMsg.setCompoundDrawables(null, dwImage, null, null);
        // 错误信息
        tvErrorMsg.setText(1 == type ? R.string.network_load_error : R.string.page_empty);
        // 按钮
        btnRefresh.setVisibility(1 == type ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置错误信息
     *
     * @param text
     */
    public void setErrorMessage(String text) {
        tvErrorMsg.setCompoundDrawables(null, null, null, null); //设置左图标
        tvErrorMsg.setText(text);
        btnRefresh.setVisibility(View.GONE);
    }


    /**
     * 设置错误信息
     *
     * @param resId
     * @param content
     * @param confirmMsg
     */
    public void setErrorType(int resId, String content, String confirmMsg) {
        //Drawable dwImage = getResources().getDrawable(resId);
        //dwImage.setBounds(0, 0, dwImage.getMinimumWidth(), dwImage.getMinimumHeight());
        //tvErrorMsg.setCompoundDrawables(null, dwImage, null, null); //设置左图标
        tvErrorMsg.setText(content);
        btnRefresh.setText(confirmMsg);
        btnRefresh.setVisibility(TextUtils.isEmpty(confirmMsg) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        TCAgent.onPageEnd(mContext, TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        TCAgent.onPageStart(mContext, TAG);
    }

    /**
     * 通过泛型来简化findViewById
     */
    protected final <E extends View> E getView(View v, int id) {
        try {
            return (E) v.findViewById(id);
        } catch (ClassCastException ex) {
            Logger.e(ex, "Could not cast View to concrete class.");
            throw ex;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageLoader.getInstance().clearMemoryCache();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
    }

    /**
     * 网络请求 错误信息toast
     *
     * @param error 错误
     */
    protected void showErrorToast(VolleyError error) {
        ((BaseActivity) mContext).showErrorToast(error);
    }

    /**
     * 简单toast
     *
     * @param message
     */
    public void showToast(String message) {
        ((BaseActivity) mContext).showToast(message);
    }

    /**
     * 动画toast
     *
     * @param toastType
     * @param message
     */
    public void showToast(int toastType, String message) {
        ((BaseActivity) mContext).showToast(toastType, message);
    }

    /**
     * 打开活动页
     *
     * @param context mContext
     */
    protected void goEvent(Context context) {
        if (HtApplication.getFabData() != null) {
            TopicLink.jump(mContext, HtApplication.getFabData());
        }
    }
}
