package com.haitao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.haitao.BuildConfig;
import com.haitao.R;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.db.CategoryDB;
import com.haitao.db.StoreDB;
import com.haitao.db.ThreadCategoryDB;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.http.okhttp.callback.ResultCallback;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.framework.utils.HTTPUtil;
import com.haitao.imp.URILocatorHelper;
import com.haitao.imp.VF;
import com.haitao.model.AdObject;
import com.haitao.model.CategoryAllObject;
import com.haitao.model.CategoryListObject;
import com.haitao.model.PlatformObject;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.StoreResponseObject;
import com.haitao.model.UserObject;
import com.haitao.model.VersionObject;
import com.haitao.receiver.UpdateApkReceiver;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.TopicLink;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.YYBCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import tom.ybxtracelibrary.Entity.TraceCommonBean;
import tom.ybxtracelibrary.Utils.DeviceUtils;
import tom.ybxtracelibrary.YbxTrace;


/**
 * 闪屏页
 */
public class SplashActivity extends BaseActivity {
    private IViewContext<UserObject, IEntityService<UserObject>>               commandViewContext = VF.<UserObject>getDefault(UserObject.class);
    private IViewContext<CategoryAllObject, IEntityService<CategoryAllObject>> categoryContext    = VF.<CategoryAllObject>getDefault(CategoryAllObject.class);
    private IViewContext<VersionObject, IEntityService<VersionObject>>         versionContext     = VF.<VersionObject>getDefault(VersionObject.class);

    private ProgressDialog mProgressDialog;
    private AlertDialog    mDlgSetUrl; // 设置Url Dialog

    private int mTotalSize = 0;

    private static final int REFRESHPROGRESS = 0x0000;
    private static final int ERROR           = 0x0001;
    private static final int CHECVERSION     = 0x0002;

    private boolean isLoadOver = false;

    //    private ConfirmDialogUtils confirmDialogUtils;
    private PlatformObject platformObject;

    private boolean isLoadStoreFirst = false;
    private UpdateApkReceiver mReceiver;
    private boolean           mIsRegister;// 是否注册了广播

    private long mStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏 状态栏 & 底部导航栏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_splash);

        TAG = "闪屏";
        // 页面跟踪初始化
        initYbxTrace();

        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action     = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        // 页面跟踪lanuch埋点
        YbxTrace.getInstance().launch(this);

        // 初始化魔窗
        initMW();
        MLinkAPIFactory.createAPI(this).registerWithAnnotation(this);
        String s           = getIntent().toURI();
        Uri    parse       = Uri.parse(s);
        String type        = parse.getQueryParameter("type");
        String title       = parse.getQueryParameter("title");
        String value       = parse.getQueryParameter("value");
        String sourceType  = parse.getQueryParameter("sourceType");
        String sourceValue = parse.getQueryParameter("sourceValue");

        if (!TextUtils.isEmpty(type)) {
            AdObject adObject = new AdObject();
            adObject.type = type;
            adObject.value = value;
            adObject.id = sourceValue;
            adObject.title = title;
            TopicLink.jump(mContext, adObject, sourceType);
            finish();
            //            return;
        } else if (getIntent().getData() != null) {
            MLinkAPIFactory.createAPI(this).router(this, getIntent().getData());
            //跳转后结束当前activity
            finish();
        } else {
            MLinkAPIFactory.createAPI(this).checkYYB(SplashActivity.this, new YYBCallback() {
                @Override
                public void onFailed(Context context) {
                    if (Constant.IS_DEBUG) {
                        mDlgSetUrl = new AlertDialog.Builder(mContext)
                                .setMessage("设置url环境")
                                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                                    SettingUrlActivity.launch(mContext);
                                    dialog.dismiss();
                                })
                                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                                    initData();
                                    dialog.dismiss();
                                })
                                .setCancelable(false)
                                .show();
                        return;
                    }
                    initData();
                }

                @Override
                public void onSuccess() {
                    finish();
                }
            });
        }

//        ToastUtils.show(this, ScreenUtils.getDisplayMetrics(this).toString());
//        Logger.d( ScreenUtils.getDisplayMetrics(this).toString());
    }

    @Override
    protected String getActivityTAG() {
        return "";
    }

    /**
     * 页面跟踪数据统计
     */
    private void initYbxTrace() {
        TraceCommonBean traceCommonBean = new TraceCommonBean();

        int uid = 0;
        try {
            if (UserManager.getInstance().isLogin() && !TextUtils.isEmpty(UserManager.getInstance().getUserId())) {
                uid = Integer.valueOf(UserManager.getInstance().getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        traceCommonBean.mid = uid + "";
        traceCommonBean.ver = "0.02";
        traceCommonBean.v = BuildConfig.VERSION_NAME;
        traceCommonBean.bid = "wwfl";
        traceCommonBean.rst = DeviceUtils.getDensityWidth(this) + "*" + DeviceUtils.getDensityHeight(this);
        traceCommonBean.gid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        traceCommonBean.l = DeviceUtils.getDeviceLanguage(this);

        traceCommonBean.iev = Build.BRAND + ","
                + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK_INT + ","
                + android.os.Build.VERSION.RELEASE + ","
                + Build.USER;
        YbxTrace.initTrace(mContext, traceCommonBean, 1);
        YbxTrace.setUploadSwitch(Constant.ACTIVITYANALY);  // 是否上传的开关

    }

    /**
     * 初始化魔窗
     */
    private void initMW() {
        MWConfiguration config = new MWConfiguration(this);
        config.setLogEnable(Constant.IS_DEBUG);//打开魔窗Log信息
        config.setChannel(CommonUtils.getChannel(mContext));
        MagicWindowSDK.initSDK(config);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mStartTime = System.currentTimeMillis();
//        BadgeUtil.resetBadgeCount(getApplicationContext(), R.mipmap.ic_launcher);
        new checkVersionTask().execute();
    }

    private static final int SHOW_TIME_MIN = 800;

    /**
     * 更新字典
     */
    public class UpdateTask extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {
            loadHtCategory();
            return "itcast";
        }

        protected void onPostExecute(String result) {
        }

        protected void onProgressUpdate(Integer... values) {
        }
    }

    /**
     * 检测版本
     */
    public class checkVersionTask extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {
            //CustomService.actionStart(mContext);
            loadVersion();
            return "itcast";
        }

        protected void onPostExecute(String result) {


        }

        protected void onProgressUpdate(Integer... values) {
        }
    }

    /**
     * 检测是否保存有登录信息
     */
    private void checkLogin() {
        if (UserManager.getInstance().isLogin() && !TextUtils.isEmpty(UserManager.getInstance().getUser().refresh_token)) {
            commandViewContext.getEntity().refresh_token = UserManager.getInstance().getUser().refresh_token;
            login();
        } else {
            UserManager.getInstance().clearUser();
            goNext();
        }
    }

    /**
     * 登录
     */
    private void login() {
        commandViewContext.getService().asynFunction(MethodConstant.REFRESH_TOKEN, commandViewContext.getEntity(), new IAsynServiceHandler<UserObject>() {
            @Override
            public void onSuccess(UserObject entity) throws Exception {
                goNext();
            }

            @Override
            public void onSuccessPage(PageResult<UserObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                UserManager.getInstance().clearUser();
                SPUtils.put(mContext, SPConstant.ACCOUNT, "");
                SPUtils.put(mContext, SPConstant.PASSWORD, "");
                SPUtils.put(mContext, SPConstant.TYPE, "");
                SPUtils.put(mContext, SPConstant.OPEN_ID, "");
                goNext();
            }
        });
    }


    /**
     * 跳转
     */
    private void goNext() {
        isLoadOver = true;
        doGoNext();
    }


    private void doGoNext() {
        if (isLoadOver) {
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(aLong -> {
                        boolean firstOpen = (boolean) SPUtils.get(mContext, SPConstant.FIRST_OPEN, true);
                        if (firstOpen) {
                            /* if (!isLoadStoreFirst) {
                                 initStore();
                                 return;
                             }*/
                            if (mDlgSetUrl == null || (mDlgSetUrl != null && !mDlgSetUrl.isShowing())) {
                                GuideActivity.launch(mContext);
                                finish();
                            }
                        } else {
                            //                            CustomService.actionStart(mContext);
                            loadAd();
                        }
                    });

        }
    }


    /**
     * 获取版本信息
     */
    private void loadVersion() {
        versionContext.getService().asynFunction(MethodConstant.VERSION, versionContext.getEntity(), new IAsynServiceHandler<VersionObject>() {
            @Override
            public void onSuccess(VersionObject entity) throws Exception {
                if (null != entity) {
                    SPUtils.put(mContext, SPConstant.SMS_SEND_PHONE, entity.sms_tel_number);
                    String threadVersion = (String) SPUtils.get(mContext, SPConstant.THREAD_VERSION, "0");
                    if (!threadVersion.equals(entity.thread_class_new)) {
                        SPUtils.put(mContext, SPConstant.THREAD_VERSION, entity.thread_class_new);
                        //清除板块分类相关数据
                        ThreadCategoryDB.clear(CategoryConstant.THREAD_CATEGORY);
                        ThreadCategoryDB.clear(CategoryConstant.THREAD_POST_CATEGORY);
                    }
                    /*String storeVersion = (String) SPUtils.get(mContext, SPConstant.STORE_VERSION, "0");
                    int currentStoreVersion = null != entity.stores && !TextUtils.isEmpty(entity.stores) ? Integer.parseInt(entity.stores) : 0;
                    if (Integer.parseInt(storeVersion) < currentStoreVersion) {
                        StoreDB.clear();
                    }*/
                    String catVersion = (String) SPUtils.get(mContext, SPConstant.CATEGORY_VERSION, "0");
                    //int currentCatVerion = null != entity.data_dict && !TextUtils.isEmpty(entity.data_dict) ? Integer.parseInt(entity.data_dict) : 0;
                    if ("0".equals(catVersion)) {
                        new UpdateTask().execute();
                    }
                    SPUtils.put(mContext, SPConstant.INVITE_MONEY, entity.invite_money);
                    // 向首页发送更新提示
                    EventBus.getDefault().postSticky(entity.platform == null ? new PlatformObject() : entity.platform);
                    checkLogin();
                } else {
                    checkLogin();
                }
            }

            @Override
            public void onSuccessPage(PageResult<VersionObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                checkLogin();
            }
        });
    }

    /**
     * 加载商家数据
     */
    private void initStore() {
        new Thread(() -> HTTPUtil.postAsyn(URILocatorHelper.getUrlBase().getBaseURI() + "/" + MethodConstant.STORE_LIST, MethodConstant.STORE_LIST, null, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    StoreResponseObject storeResponseObject = JSON.parseObject(response, StoreResponseObject.class);
                    if ("0".equals(storeResponseObject.code)) {
                        StoreFilterObject entity = storeResponseObject.data;
                        if (null != entity) {
                            if (null != entity.list && entity.list.size() > 0) {
                                StoreDB.add(entity.list);
                            }
                            if (null != entity.char_list) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_CHAR, JSON.toJSONString(entity.char_list));
                            }
                            if (null != entity.version) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_VERSION, entity.version);
                            }

                            if (null != entity.super_rebate) {
                                SPUtils.put(getApplicationContext(), SPConstant.STORE_SUPER_REBATE, JSON.toJSONString(entity.super_rebate));
                            }
                        }
                    }
                    isLoadStoreFirst = true;
                    doGoNext();
                }
            }
        })).start();
    }

    /**
     * 获取闪屏广告
     */
    private void loadAd() {
        ForumApi.getInstance().commonSplashScreenAdGet(TransConstant.WIDTH, TransConstant.HEIGHT,
                popupAdModel -> {
                    if (TextUtils.equals(popupAdModel.getCode(), "0")
                            && popupAdModel.getData() != null
                            && !TextUtils.isEmpty(popupAdModel.getData().getPic())) {
                        SplashAdActivity.launch(mContext, popupAdModel.getData());
                    } else {
                        MainActivity.launch(mContext);
                    }
                    finish();
                },
                volleyError -> {
                    MainActivity.launch(mContext);
                    finish();
                });
    }


    /**
     * 更新海淘类别
     */
    private void loadHtCategory() {
        categoryContext.getService().asynFunction(MethodConstant.CATEGORY_LIST, categoryContext.getEntity(), new IAsynServiceHandler<CategoryAllObject>() {
            @Override
            public void onSuccess(CategoryAllObject result) throws Exception {

                if (null != result) {
                    CategoryDB.clear();
                    SPUtils.put(mContext, SPConstant.CATEGORY_VERSION, result._version);
                    CategoryListObject entity = result._data;
                    if (null != entity.money_type && entity.money_type.size() > 0) {
                        CategoryDB.add(entity.money_type, String.valueOf(CategoryConstant.MONEY_TYPE));
                    }
                    if (null != entity.cashback_status && entity.cashback_status.size() > 0) {
                        CategoryDB.add(entity.cashback_status, String.valueOf(CategoryConstant.CASHBACK_STATUS));
                    }
                    if (null != entity.withdrawal_type && entity.withdrawal_type.size() > 0) {
                        CategoryDB.add(entity.withdrawal_type, String.valueOf(CategoryConstant.WITHDRAWAL_TYPE));
                    }
                    if (null != entity.withdrawal_status && entity.withdrawal_status.size() > 0) {
                        CategoryDB.add(entity.withdrawal_status, String.valueOf(CategoryConstant.WITHDRAWAL_STATUS));
                    }
                    if (null != entity.withdrawal_bank && entity.withdrawal_bank.size() > 0) {
                        CategoryDB.add(entity.withdrawal_bank, String.valueOf(CategoryConstant.WITHDRAWAL_BANK));
                    }
                    if (null != entity.order_status && entity.order_status.size() > 0) {
                        CategoryDB.add(entity.order_status, String.valueOf(CategoryConstant.ORDER_STATUS));
                    }
                    if (null != entity.talent_category && entity.talent_category.size() > 0) {
                        CategoryDB.add(entity.talent_category, String.valueOf(CategoryConstant.TALENT_CATEGORY));
                    }
                    if (null != entity.deal_category && entity.deal_category.size() > 0) {
                        CategoryDB.add(entity.deal_category, String.valueOf(CategoryConstant.DEAL_CATEGORY));
                    }
                    if (null != entity.store_category && entity.store_category.size() > 0) {
                        CategoryDB.add(entity.store_category, String.valueOf(CategoryConstant.STORE_CATEGORY));
                    }
                    if (null != entity.store_country && entity.store_country.size() > 0) {
                        CategoryDB.add(entity.store_country, String.valueOf(CategoryConstant.STORE_COUNTRY));
                    }
                    if (null != entity.store_dict && entity.store_dict.size() > 0) {
                        CategoryDB.add(entity.store_dict, String.valueOf(CategoryConstant.STORE_RECOMMEND));
                    }
                }
            }

            @Override
            public void onSuccessPage(PageResult<CategoryAllObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                //ToastUtils.show(mContext, error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.REFRESH) {
            initData();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(getIntent().toURI());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsRegister) {
            unregisterReceiver(mReceiver);
        }
    }
}
