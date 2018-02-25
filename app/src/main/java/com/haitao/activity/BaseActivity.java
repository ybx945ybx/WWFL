package com.haitao.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.haitao.R;
import com.haitao.common.ActivityCollector;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.utils.ExitApp;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.TopicLink;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.ToastPopuWindow;
import com.orhanobut.logger.Logger;
import com.tendcloud.tenddata.TCAgent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import tom.ybxtracelibrary.YbxTrace;

/**
 * activity基类
 */
public class BaseActivity extends RxAppCompatActivity {
    protected static final int REQUEST_CODE_READ_STORAGE = 1001; // SD卡权限
    protected static final int REQUEST_CODE_CAMERA       = 1002; // Camera权限

    protected String TAG = getClass().getSimpleName();
    protected Context         mContext;
    private   ProgressDialog  mDialog;
    private   ToastPopuWindow mPwToast;

    // 用于页面跟踪的参数
    protected String TAGTrance = getClass().getSimpleName();
    protected boolean hasCreated;                                // 第一次进页面为false统计pageview事件后置为true，返回进入页面时不统计pageview事件
    public    String  pref;                                      // 前一页面
    public    String  prefh;                                     // 前一页面的hash
    public    String  purl;                                      // 当前页面
    public    String  purlh;                                     // 当前页面的hash


    public ImageButton btnLeft, btnRight;
    public TextView tvRight, tvTitle;
    //为空或者网络异常
    public ViewGroup ll_common_error;
    public TextView  tvErrorMsg, btnRefresh;

    /**
     * 初始化顶部
     */
    public void initTop() {
        btnLeft = getView(R.id.btnLeft);
        btnRight = getView(R.id.btnRight);
        tvRight = getView(R.id.tvRight);
        tvTitle = getView(R.id.tvTitle);
        btnLeft.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            finish();
        });
    }

    /**
     * 初始化错误信息视图
     */
    public void initError() {
        ll_common_error = getView(R.id.ll_common_error);
        tvErrorMsg = getView(R.id.tvErrorMsg);
        btnRefresh = getView(R.id.btnRefresh);
    }

    /**
     * 设置错误信息 0:为空，1：网络异常
     *
     * @param type 类型
     */
    public void setErrorType(int type) {
        // 图标
        Drawable dwImage = getResources().getDrawable(0 == type ? R.mipmap.ic_empty_logo : R.mipmap.ic_error_logo);
        dwImage.setBounds(0, 0, dwImage.getMinimumWidth(), dwImage.getMinimumHeight());
        tvErrorMsg.setCompoundDrawables(null, dwImage, null, null);
        // 错误信息
        tvErrorMsg.setText(0 == type ? R.string.page_empty : R.string.network_load_error);
        // 按钮文字
        btnRefresh.setText(0 == type ? R.string.back : R.string.refresh);
        if (0 == type) {
            btnRefresh.setVisibility(View.GONE);
        } else {
            btnRefresh.setText(R.string.refresh);

        }
        // 按钮事件
        if (0 == type) {
            btnRefresh.setOnClickListener(v -> ((Activity) mContext).onBackPressed());
        }
    }

    public void setErrorMessage(String msg) {
        tvErrorMsg.setText(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ExitApp.getInstance().addActivity(this);
        if (TextUtils.isEmpty(TransConstant.WIDTH)) {
            TransConstant.WIDTH = String.valueOf(ScreenUtils.getScreenWidth(BaseActivity.this));
            TransConstant.HEIGHT = String.valueOf(ScreenUtils.getScreenHeight(BaseActivity.this));
            TransConstant.DEVICE = android.os.Build.BRAND;
        }
        mContext = this;

        ActivityCollector.add(this);
        purlh = mContext.toString().substring(mContext.toString().indexOf("@") + 1);
        Logger.d("purlh------->" + purlh);

    }

    /**
     * 跳转图片选择页之前进行权限检查
     */
    protected void photoPickWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            photoPick();
        }
    }

    /**
     * 跳转图片选择页 - 子类实现
     */
    protected void photoPick() {
    }

    /**
     * 请求权限回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoPickWrapper();
                } else {
                    showToast(ToastType.ERROR, "请先打开访问相册权限");
                }
                break;
            }
        }
    }

    public void showProgressDialog(String text) {
        showProgressDialog(text, false);
    }

    public void showProgressDialog(String text, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(mContext);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }
            mDialog.setMessage(text);
            if (!((Activity) mContext).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public void showProgressDialog(int resId) {
        showProgressDialog(resId, false);
    }

    public void showProgressDialog(int resId, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(mContext);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }

            mDialog.setMessage(mContext.getResources().getString(resId));
            if (!((Activity) mContext).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public boolean isShowing() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    public void dismissProgressDialog() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 查看渠道信息
     */
    /*public String getChannel() {
        String channel = null;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            channel = ai.metaData.getString("UMENG_CHANNEL");
            if (channel == null) {
                channel = String.valueOf(ai.metaData.getInt("UMENG_CHANNEL"));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  channel;
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExitApp.getInstance().getActivityList().remove(this);
        ImageLoader.getInstance().clearMemoryCache();
        ActivityCollector.remove(this);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        if (mPwToast != null && mPwToast.isShowing())
            mPwToast.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        // TalkingData统计
        TCAgent.onPageStart(mContext, TAG);
        JPushInterface.onResume(BaseActivity.this);
        JPushInterface.resumePush(BaseActivity.this);
        if (HtApplication.isLogin()) {
            JPushInterface.setAlias(getApplicationContext(), 10000, (Constant.IS_DEBUG ? "DEV_" : "") + UserManager.getInstance().getUserId());
        }
        if ((Boolean) SPUtils.get(mContext, "isBackground", true)) {
            //CoreService.actionStart(getApplicationContext());
            SPUtils.put(mContext, "isBackground", false);
        }

        // 埋点 pageview事件  还没有全部埋点getActivityTAG为空说明还没埋
        if (!hasCreated && !TextUtils.isEmpty(getActivityTAG())) {
            hasCreated = true;
            BaseActivity frontActivity = ActivityCollector.getSecondActivity();
            if (frontActivity != null) {
                purl = getActivityTAG();
                pref = frontActivity.purl;
                prefh = frontActivity.purlh;
                YbxTrace.getInstance().pageView(this, frontActivity.purl, frontActivity.purlh, getActivityTAG(), purlh, "");
            }
        }

    }

    protected String getActivityTAG() {
        return TAGTrance;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(mContext, TAG);
        JPushInterface.onPause(BaseActivity.this);
    }

    /**
     * 通过泛型来简化findViewById
     */
    protected final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Logger.e(ex, "Could not cast View to concrete class.");
            throw ex;
        }
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
    protected void onStop() {
        super.onStop();
        if (!ScreenUtils.isAppOnForeground(this)) {
            SPUtils.put(mContext, "isBackground", true);
        }
    }

    /**
     * 网络请求 错误信息toast
     *
     * @param error 错误
     */
    public void showErrorToast(VolleyError error) {
        if (error.getCause() instanceof java.net.UnknownHostException) {
            showToast(getResources().getString(R.string.net_error));
        } else {
            showToast(error.getMessage());
        }
    }

    /**
     * 简单toast
     *
     * @param message
     */
    public void showToast(CharSequence message) {
        runOnUiThread(() -> {
            mPwToast = new ToastPopuWindow(BaseActivity.this, message);
            mPwToast.show();
        });

    }

    /**
     * 动画toast
     *
     * @param toastType
     * @param message
     */
    public void showToast(int toastType, CharSequence message) {
        runOnUiThread(() -> {
            mPwToast = new ToastPopuWindow(BaseActivity.this, toastType, message);
            mPwToast.show();
        });
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

    /**
     * 复制到剪贴板
     *
     * @param context mContext
     * @param content 内容
     */
    protected void copyToClipboard(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        ((BaseActivity) context).showToast(ToastType.COMMON_SUCCESS, "复制成功");
    }
}
