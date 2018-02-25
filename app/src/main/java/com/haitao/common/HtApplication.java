package com.haitao.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.haitao.BuildConfig;
import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TalkingDataConstant;
import com.haitao.common.Constant.XNConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.http.okhttp.OkHttpClientManager;
import com.haitao.imp.URILocatorHelper;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.NetUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.utils.universalimageloader.core.ImageLoaderConfiguration;
import com.haitao.utils.universalimageloader.core.assist.QueueProcessingType;
import com.haitao.utils.universalimageloader.utils.StorageUtils;
import com.mob.MobApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.litepal.LitePal;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.Session;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import io.swagger.client.model.SlidePicModel;
import tom.ybxtracelibrary.YbxTrace;


/**
 * Application
 * Created by tqy on 15/11/18.
 */
public class HtApplication extends MobApplication {
    //public static UserObject userObject = null;
    public static  int           serverTime                   = 0;
    private static int           appCount                     = 0;
    private static HtApplication mApplication                 = null;
    public static  String        mActivityRefreshingImg       = ""; // 活动期间，下拉刷新图标
    public static  String        mStoreTransportActivityLabel = ""; // 活动期间，商家 & 转运角标
    public static  String        mActivityFabImg              = ""; // 全局活动入口图
    public static  String        mActivityUrl                 = ""; // 活动url
    private static SlidePicModel mFabData                     = null; // 浮标数据
    public static  boolean       isActivityOn                 = false;
    //    private RefWatcher mRefWatcher;

    public static HtApplication getInstance() {
        return mApplication;
    }

    /**
     * 初始化imageloader
     *
     * @param context mContext
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                Constant.PIC_PATH);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.discCache(new UnlimitedDiskCache(cacheDir));// 自定义缓存路径
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //            config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = HtApplication.this;
        // LeakCanary
        //        if (LeakCanary.isInAnalyzerProcess(this)) {
        //            This process is dedicated to LeakCanary for heap analysis.
        //            You should not init your app in this process.
        //            return;
        //        }
        //        mRefWatcher = LeakCanary.install(this);
        // LitePal
        LitePal.initialize(this);
        // 阿里百川Hotfix
        //        initHotFix();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(mApplication).setDownsampleEnabled(true).build();
        Fresco.initialize(mApplication, config);
        // Bugly初始化
        initBugly();
        // 初始化Stetho
        Stetho.initializeWithDefaults(this);
        //初始化URI
        initURI();
        //初始化推送
        initJPush();
        // 初始化魔窗
        initMW();
        // 初始化图片加载
        initImageLoader(mApplication);
        // 初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        // 初始化crash日志
        //        CrashHandler crashHandler = CrashHandler.getInstance();
        //        crashHandler.init(getApplicationContext());
        //初始化http超时时间
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        OkHttpClientManager.getInstance().getOkHttpClient().setReadTimeout(10000, TimeUnit.MILLISECONDS);
        OkHttpClientManager.getInstance().getOkHttpClient().setWriteTimeout(10000, TimeUnit.MILLISECONDS);
        //对网络数据进行初始化
        NetUtils.isConnected(mApplication);
        //在线客服
        Ntalker.getInstance().initSDK(getApplicationContext(), XNConstant.SITE_ID, XNConstant.SDK_KEY);
        Ntalker.getInstance().enableDebug(true);
        Ntalker.getInstance().setSDKListener(new XNSDKListener() {
            @Override
            public void onChatMsg(boolean b, String s, String s1, String s2, long l, boolean b1) {

            }

            @Override
            public void onUnReadMsg(String s, String s1, String s2, int i) {

            }

            @Override
            public void onClickMatchedStr(String s, String s1) {

            }

            @Override
            public void onClickUrlorEmailorNumber(int i, String s) {

            }

            @Override
            public void onClickShowGoods(int i, int i1, String s, String s1, String s2, String s3, String s4, String s5) {
                return;
            }

            @Override
            public void onError(int i) {

            }
        });
        // 初始化友盟
        initUmeng();
        // 初始化TalkingData
        initTalkingData();

        // 用于判断应用是否在前台
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 初始化 JPush
     */
    private void initJPush() {
        JPushInterface.setDebugMode(Constant.IS_DEBUG);    // 设置开启日志,发布时请关闭日志
        if (Constant.IS_DEBUG) {
            Set<String> tags = new HashSet<>();
            tags.add("DEV");
            JPushInterface.setTags(this, 0, tags);
        }

        /*MultiActionsNotificationBuilder builder = new MultiActionsNotificationBuilder(this);
        //添加按钮，参数(按钮图片、按钮文字、扩展数据)
        builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "查看详情", "my_extra1");
        builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "去购买", "my_extra2");
        JPushInterface.setPushNotificationBuilder(2, builder);*/
        JPushInterface.init(this);
    }

    /**
     * 初始化TalkingData
     */
    private void initTalkingData() {
        //初始化TalkingDATa
        TalkingDataAppCpa.init(this.getApplicationContext(), TalkingDataConstant.APP_ID, CommonUtils.getChannel(mApplication));

        TCAgent.LOG_ON = true;
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this, TalkingDataConstant.APP_APP_ID, CommonUtils.getChannel(mApplication));
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true);
    }

    /**
     * 初始化友盟
     */
    private void initUmeng() {
        //初始化友盟
        MobclickAgent.setDebugMode(Constant.IS_DEBUG);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
        // 该接口默认参数是true，即采集mac地址，但如果开发者需要在googleplay发布，考虑到审核风险，可以调用该接口，参数设置为false就不会采集mac地址。
        MobclickAgent.setCheckDevice(false);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "5658540e67e58ec47c002cc5", CommonUtils.getChannel(this)));
    }

    public static int getAppCount() {
        return appCount;
    }

    /**
     * 初始化阿里百川热修复
     */
    private void initHotFix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setAesKey(null)
                .setEnableDebug(Constant.IS_DEBUG)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    // 补丁加载回调通知
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        // 表明补丁加载成功
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {

                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        if (!CommonUtils.isForeground()) {
                            // 魔窗Session特殊处理
                            Session.onKillProcess();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                        // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                        // SophixManager.getInstance().cleanPatches();
                    } else {
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    /**
     * 初始化Bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = CommonUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(), "73b96bfb76", Constant.BUGLY, strategy);
    }

    /**
     * 初始化魔窗
     */
    private void initMW() {
        /*MWConfiguration config = new MWConfiguration(this);
        config.setLogEnable(Constant.IS_DEBUG) // 开启Debug模式，显示Log，release时注意关闭
                // 带有Fragment的页面。具体查看2.2.2
                .setPageTrackWithFragment(true)
                // 设置分享方式，如果之前有集成sharesdk，可在此开启
                .setSharePlatform(MWConfiguration.SHARE_SDK);
        MagicWindowSDK.initSDK(config);*/
        // 魔窗Session功能
        Session.setAutoSession(this);
    }

    private void initURI() {
        String url = (String) SPUtils.get(mApplication, SPConstant.SETTING_URL, "");
        URILocatorHelper.initUrlBase(Constant.IS_DEBUG && !TextUtils.isEmpty(url) ? url : Constant.URL);
        URILocatorHelper.init();
        String swaggerUrl = (String) SPUtils.get(mApplication, SPConstant.SWAGGER_SETTING_URL, "");
        ForumApi.getInstance().setBaseUrl(Constant.IS_DEBUG && !TextUtils.isEmpty(swaggerUrl) ? swaggerUrl : Constant.SWAGGER_PROD_URL);
    }

    /**
     * 当用户需要定制默认的通知栏样式时，则可调用此方法。
     */
    private void setDefaultPushNotificationBuilder(Context context) {
        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(context,
                R.layout.customer_notitfication_layout,
                R.id.notif_icon,
                R.id.notif_title,
                R.id.notif_text);
        // 指定定制的 Notification Layout
        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
        // 指定最顶层状态栏小图标
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /* public UserObject getUserObject() {
         return userObject;
     }

     public void setUserObject(UserObject object) {

         userObject = object;
     }*/
    public static SlidePicModel getFabData() {
        return mFabData;
    }

    public static void setFabData(SlidePicModel fabData) {
        HtApplication.mFabData = fabData;
    }

    //    public static RefWatcher getRefWatcher() {
    //        return mApplication.mRefWatcher;
    //    }

    /**
     * app进入后台上传统计数据到服务器 成功返回后清除本地数据
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            YbxTrace.getInstance().uploadErrorCache(mApplication);
        }

    }

}
