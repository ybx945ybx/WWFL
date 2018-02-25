package com.haitao.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.haitao.BuildConfig;
import com.haitao.R;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.db.ThreadCategoryDB;
import com.haitao.event.LoginStateChangedEvent;
import com.haitao.model.UserObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.FileSizeUtils;
import com.haitao.utils.FileUtils;
import com.haitao.utils.NetUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.dialog.ConfirmDlg;
import com.haitao.view.dialog.UpdateDlg;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.UpdateModelData;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_phone)       TextView mTvPhone;      // 绑定的手机号
    @BindView(R.id.tv_clear_cache) TextView mTvClearCache; // 绑定的手机号
    @BindView(R.id.btn_logout)     TextView mTvLogout;     // 退出登录

    private ChangReceiver  mChangReceiver;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProDialog;
    private int mTotalSize = 0;

    private static final int REFRESHPROGRESS = 0x0000;
    private static final int ERROR           = 0x0001;
    private HttpURLConnection mConn;
    private InputStream       mIn;
    private FileOutputStream  mOut;
    private boolean           mIsDownloading; // 下载中标记


    private class ChangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_LOGIN:
                            mTvLogout.setVisibility(HtApplication.isLogin() ? View.VISIBLE : View.GONE);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initVars();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initVars() {
        TAG = "设置";
        mChangReceiver = new ChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransConstant.CHANGE_BROADCAST);
        mContext.registerReceiver(mChangReceiver, filter);

    }

    /**
     * 初始化视图
     */
    private void initView() {
        mTvLogout.setVisibility(HtApplication.isLogin() ? View.VISIBLE : View.GONE);
        String catchStr = FileSizeUtils.getAutoFileOrFilesSize(FileUtils.CATCH_PATH);
        mTvClearCache.setText(String.format("清除缓存（%s）", catchStr));//缓存大小
        if (HtApplication.isLogin()) {
            String mobile = UserManager.getInstance().getUser().mobile;
            if (!TextUtils.isEmpty(mobile)) {
                mTvPhone.setText(mobile);
            }
        } else {
            mTvPhone.setText("");
        }
    }

    /**
     * 退出登录 清除缓存
     */
    @OnClick({R.id.btn_logout, R.id.tv_clear_cache})
    public void clickLogoutClearCache(View v) {

        switch (v.getId()) {
            case R.id.btn_logout: // 退出登录
                ConfirmDlg confirmDlg = new ConfirmDlg(mContext, "", getResources().getString(R.string.logout_confirm),
                        dialog -> {
                            UserManager.getInstance().clearUser();
                            SPUtils.put(mContext, SPConstant.PASSWORD, "");
                            SPUtils.put(mContext, SPConstant.TYPE, "");
                            SPUtils.remove(mContext, SPConstant.OPEN_ID);
                            SPUtils.remove(mContext, SPConstant.OPEN_UNION_ID);
                            SPUtils.remove(mContext, SPConstant.OPEN_TOKEN);
                            Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                            mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGOUT);
                            mContext.sendBroadcast(mIntent);
                            //清除板块分类相关数据
                            ThreadCategoryDB.clear(CategoryConstant.THREAD_CATEGORY);
                            ThreadCategoryDB.clear(CategoryConstant.THREAD_POST_CATEGORY);
                            ToastUtils.show(mContext, R.string.setting_logout_success);
                            EventBus.getDefault().post(new LoginStateChangedEvent(false));

                            dialog.dismiss();
                            finish();
                        },
                        dialog -> {
                            dialog.dismiss();
                        });
                confirmDlg.show();
                break;
            case R.id.tv_clear_cache: // 清除缓存
                clearAppCacheWrapper();
                break;
        }
    }

    /**
     * 清除缓存Wrapper
     */
    protected void clearAppCacheWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            clearAppCache();
        }
    }

    /**
     * 清除缓存
     */
    private void clearAppCache() {
        ConfirmDlg confirmDlg = new ConfirmDlg(mContext, "", getResources().getString(R.string.clear_cache_confirm),
                dialog -> {
                    ImageLoader.getInstance().getDiskCache().clear();//图片缓存
                    FileUtils.deleteDir(FileUtils.CATCH_PATH);//删除文件夹下的所有文件，不包括文件夹
                    mTvClearCache.setText(String.format("清除缓存（%s）", "0B"));//缓存大小
                    dialog.dismiss();
                },
                dialog -> {
                    dialog.dismiss();
                });
        confirmDlg.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意授权，则清除缓存
                    clearAppCache();
                } else {
                    ToastUtils.show(mContext, "请授予读写权限，以清除应用缓存");
                }
        }
    }

    /**
     * 手机号码绑定
     */
    @OnClick(R.id.rl_phone)
    public void onClickBindPhone() {
        toBindPhone();
    }


    /**
     * 社交账号绑定
     */
    @OnClick(R.id.tv_bind_third)
    public void clickBindThird() {
        BindThirdActivity.launch(mContext);
    }

    /**
     * 修改登录密码
     */
    @OnClick(R.id.tv_change_login_pwd)
    public void clickChangeLoginPwd() {
        LoginPwdUpdateActivity.launch(mContext);
    }

    /**
     * 修改提现密码
     */
    @OnClick(R.id.tv_change_withdraw_pwd)
    public void clickChangeWithdrawPwd() {
        WithdrawPwdUpdateActivity.launch(mContext);
    }

    /**
     * 收货地址管理
     */
    /*@OnClick(R.id.tv_address_management)
    public void clickAddressManagement() {
        AddressManagementActivity.launch(mContext);
    }*/

    /**
     * 自动填充表单
     */
    /*@OnClick(R.id.tv_autofill_form)
    public void clickAutoFill() {
        AutoFillFormActivity.launch(mContext);
    }*/

    /**
     * 提现账户
     */
    @OnClick(R.id.tv_withdraw_account)
    public void clickWithdrawAccount() {
        WithdrawAccountActivity.launch(mContext);
    }

    /**
     * 给予好评
     */
    @OnClick(R.id.tv_rate_us)
    public void clickRateUs() {
        try {
            Uri    uri           = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(commentIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务条款
     */
    @OnClick(R.id.tv_service_rules)
    public void clickServiceRules(View v) {
        WebActivity.launch(mContext, getString(R.string.about_service), Constant.TERM_URL);
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.tv_feedback)
    public void clickFeedBack() {
        FeedbackActivity.launch(mContext);
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.tv_check_update)
    public void clickCheckUpdate() {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.update_warnning));
        mProgressDialog.show();
        loadVersion();
    }

    /**
     * 关于55
     */
    @OnClick(R.id.tv_about)
    public void clickAbout() {
        AboutActivity.launch(mContext);
    }

    /**
     * 检查服务端最新版本
     */
    private void loadVersion() {
        ForumApi.getInstance().commonNewVersionCheckingGet(updateModel -> {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != updateModel) {
                checkVersion(updateModel.getData());
            }
        }, error -> {
            showErrorToast(error);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 服务端版本对比本地版本，判断是否需要更新
     *
     * @param updateModelData 版本信息
     */
    private void checkVersion(final UpdateModelData updateModelData) {
        try {
            int curVersion = BuildConfig.VERSION_CODE;
            Logger.d(updateModelData.getNowVerNum());
            if (curVersion < Integer.parseInt(updateModelData.getNowVerNum())) {
                // 让用户选择是否更新
                showUpdateDlg(updateModelData);
            } else {
                // 不用更新
                ToastUtils.show(mContext, "亲~此版本已是最新版本了。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 升级弹窗
     */
    private void showUpdateDlg(final UpdateModelData updateModelData) {
        SPUtils.put(mContext, SPConstant.LAST_UPDATE_POP_TIME, System.currentTimeMillis());
        final UpdateDlg updateDlg = new UpdateDlg(mContext, updateModelData.getNewChange(), updateModelData.getLowVerNum());
        updateDlg.setOnUpdateClickListener(updateDialog -> {
            if (!NetUtils.isConnected(mContext)) {
                ToastUtils.show(mContext, "请检查您的网络连接");
                SPUtils.remove(mContext, SPConstant.LAST_UPDATE_POP_TIME);
                updateDialog.dismiss();
                return;
            }
            if (NetUtils.isWifi(mContext)) {
                updateApk(updateModelData);
                updateDialog.dismiss();
            } else {
                new Thread(() -> {
                    // 安装包的大小
                    String downloadSize = CommonUtils.getDownloadSize(updateModelData.getDownloadUrl());
                    if (TextUtils.isEmpty(downloadSize)) return;
                    runOnUiThread(() -> {
                        // 是否使用流量下载提示
                        new AlertDialog.Builder(mContext)
                                .setMessage(String.format(mContext.getResources().getString(R.string.traffic_tips), downloadSize))
                                .setPositiveButton(R.string.continue_update, (dialog, which) -> {
                                    updateApk(updateModelData);
                                    updateDialog.dismiss();
                                })
                                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
                    });
                }).start();
            }
        });
        updateDlg.show();
    }

    public void updateApk(final UpdateModelData updateModelData) {
        mIsDownloading = true;
        mProDialog = new ProgressDialog(mContext);
        mProDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProDialog.setTitle("提示");
        mProDialog.setMessage("正在下载...");
        mProDialog.setMax(100);
        mProDialog.setIndeterminate(false);
        mProDialog.setCancelable(false);
        // 非强制更新情况，可以取消下载
        if (!CommonUtils.isForceUpdate(updateModelData.getLowVerNum())) {
            mProDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.exit), (dialog, which) -> {
                mIsDownloading = false;
                dialog.cancel();
            });
        }
        new Thread() {
            @Override
            public void run() {
                downloadApk(updateModelData.getDownloadUrl());
            }
        }.start();
        mProDialog.show();
    }

    /**
     * 下载新apk
     *
     * @param updateURL 更新地址
     */
    public void downloadApk(String updateURL) {
        File apkFile   = null;
        File updateDir = null;
        try {
            URL url = new URL(updateURL);
            mConn = (HttpURLConnection) url.openConnection();
            mIn = mConn.getInputStream();
            mConn.setConnectTimeout(15000);
            mTotalSize = mConn.getContentLength();
            int curSize  = 0;
            int progress = 0;

            updateDir = new File(getExternalCacheDir() + "apk/");
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            apkFile = new File(updateDir.getPath(), "haitao-" + System.currentTimeMillis() + ".apk");
            if (apkFile.exists()) {
                apkFile.delete();
            }
            apkFile.createNewFile();

            // 修改文件夹及安装包的权限,供第三方应用访问
            try {
                Runtime.getRuntime().exec("chmod 705 " + updateDir.getPath());
                Runtime.getRuntime().exec("chmod 604 " + apkFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mOut = new FileOutputStream(apkFile);
            byte[] bytes = new byte[1024];
            int    c;
            while ((c = mIn.read(bytes)) != -1) {
                // 取消下载标记
                if (!mIsDownloading) {
                    mProDialog.cancel();
                    mIn.close();
                    mOut.close();
                    return;
                }
                mOut.write(bytes, 0, c);
                curSize += c;
                progress = curSize * 100 / mTotalSize;
                Message msg    = new Message();
                Bundle  bundle = new Bundle();
                bundle.putInt("progress", progress);
                msg.what = REFRESHPROGRESS;
                msg.setData(bundle);
                this.hdl.sendMessage(msg);
            }
            mProDialog.cancel();
            mIn.close();
            mOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            this.hdl.sendEmptyMessage(ERROR);
        }

        if (apkFile != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(mContext, getApplicationContext().getPackageName() + ".fileprovider", new File(apkFile.getPath()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.parse("file://" + apkFile.getPath()), "application/vnd.android.package-archive");
            }

            startActivity(intent);
        }
    }

    Handler hdl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESHPROGRESS:
                    int p = msg.getData().getInt("progress");
                    mProDialog.setProgress(p);
                    break;
                case ERROR:
                    Toast.makeText(mContext, "更新失败,请重试", Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mChangReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN && requestCode == resultCode) {
            if (HtApplication.isLogin()) {
                toBindPhone();
                String mobile = UserManager.getInstance().getUser().mobile;
                if (!TextUtils.isEmpty(mobile)) {
                    mTvPhone.setText(mobile);
                }
            } else {
                mTvPhone.setText("");
            }
        }
    }

    private void toBindPhone() {
        if (HtApplication.isLogin()) {
            UserObject user = UserManager.getInstance().getUser();
            if (!TextUtils.isEmpty(user.mobile)) {
                // 修改绑定
                Bundle bundle = new Bundle();
                bundle.putInt(TransConstant.TYPE, BindPhoneActivity.BING_VERIFY);
                bundle.putString(TransConstant.CODE, user.mobile);
                bundle.putString(TransConstant.AREA_CODE, user.area);
                BindPhoneActivity.launch(mContext, bundle);
            } else {
                // 新增绑定
                //                BindPhoneActivity.launch(mContext, BING_NEW);
                FirstBindPhoneActivity.launch(mContext);
            }
        } else {
            QuickLoginActivity.launch(mContext);
        }
    }
}
