package com.haitao.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.haitao.BuildConfig;
import com.haitao.R;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.db.StoreDB;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.ForumFragment;
import com.haitao.fragment.HomeFragment;
import com.haitao.fragment.MeFragment;
import com.haitao.fragment.MessageFragment;
import com.haitao.fragment.StoreFragment;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.http.okhttp.callback.ResultCallback;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.framework.utils.HTTPUtil;
import com.haitao.imp.URILocatorHelper;
import com.haitao.imp.VF;
import com.haitao.model.NoticeObject;
import com.haitao.model.PlatformObject;
import com.haitao.model.RefreshSwitchObject;
import com.haitao.model.ShareObject;
import com.haitao.model.SignListObject;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.StoreResponseObject;
import com.haitao.model.UserObject;
import com.haitao.utils.AdDialogUtils;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.NetUtils;
import com.haitao.utils.PressExit;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TopicLink;
import com.haitao.utils.calendar.CustomDate;
import com.haitao.view.MainTabView;
import com.haitao.view.dialog.SignBottomSheetDlg;
import com.haitao.view.dialog.UpdateDlg;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.mlink.annotation.MLinkDefaultRouter;
import io.swagger.client.model.AppWidgetModel;
import io.swagger.client.model.AppWidgetStyleModel;
import io.swagger.client.model.AppWidgetStyleModelImageStyle;
import io.swagger.client.model.AppWidgetStyleModelTextStyle;
import io.swagger.client.model.LinkWidgetModel;
import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.StaticsIfModelData;
import io.swagger.client.model.SystemSettingsModelData;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import tom.ybxtracelibrary.YbxTrace;
import zhy.com.highlight.HighLight;

/**
 * 主页
 */
@MLinkDefaultRouter
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFRESHPROGRESS = 0x0000;
    private static final int ERROR           = 0x0001;

    // UI
    //    private TextView btnDiscount, tabMsgs, tabDiscover, tabForum, tabMine;
    private MainTabView tabDeal, tabMsgs, tabDiscover, tabForum, tabMine;
    private ImageView       tvNotice;
    private FragmentManager fragmentManager;
    private BaseFragment    mPreFragment;
    private HomeFragment    mHomeFragment;
    private MessageFragment mMsgFragment;
    //    private DiscoveryFragment mDiscoveryFragment; // V5.8取消
    private StoreFragment   mStoreFragment; // 商家Fragment since V5.8
    private ForumFragment   mForumFragment;
    private MeFragment      mMeFragment;

    private AdDialogUtils             adDialogUtils;    // 弹窗广告
    private ChangReceiver             mChangReceiver;
    private ProgressDialog            mProgressDialog; // 更新进度条
    private int                       mTotalSize;
    private boolean                   mNeedSign;
    private HighLight                 mHighLight;
    private SignBottomSheetDlg        mSignDialog;
    private boolean                   mNeedShowUpdate;
    private List<StaticsIfModelData>  mStaticResData;   // 静态资源
    private List<AppWidgetStyleModel> mCommonStyle;     // 样式资源
    private List<AppWidgetModel>      mTabData;
    private boolean                   mIsDownloading; // 下载中标记

    protected IViewContext<NoticeObject, IEntityService<NoticeObject>>     noticeViewContext  = VF.<NoticeObject>getDefault(NoticeObject.class);
    protected IViewContext<ShareObject, IEntityService<ShareObject>>       shareViewContext   = VF.<ShareObject>getDefault(ShareObject.class);
    protected IViewContext<SignListObject, IEntityService<SignListObject>> commandViewContext = VF.<SignListObject>getDefault(SignListObject.class);

    private String mActivityUrl;
    private int    mCurrentTabIndex;

    private int[][] mColorStateList = {new int[]{android.R.attr.state_selected},
            new int[]{android.R.attr.state_enabled}};
    private List<LinkWidgetModel> mTemplateWidgets;

    private class ChangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_LOGIN:
                        case TransConstant.BROAD_LOGOUT:
                            if (null != mMeFragment) {
                                Logger.d("登录状态变化后去initdata个人中心");
                                mMeFragment.initData();
                            }
                            if (null != mMsgFragment) {
                                Logger.d(TAG + TransConstant.BROAD_LOGOUT);

                                mMsgFragment.initData();
                            }
                            if (null != mForumFragment) {
                                mForumFragment.refreshData();
                            }
                            //                            tvNotice.setVisibility(View.GONE);
                            break;
                        case TransConstant.BROAD_NOTICE:
                            if (HtApplication.isLogin()) {
                                if (null != mMsgFragment) {
                                    mMsgFragment.initData();
                                }
                                int noticeNum = TextUtils.isEmpty(UserManager.getInstance().getUser().newpm) ? 0 : Integer.parseInt(UserManager.getInstance().getUser().newpm);
                                //                                tvNotice.setVisibility(noticeNum > 0 ? View.VISIBLE : View.GONE);
                            } else {
                                //                                tvNotice.setVisibility(View.GONE);
                            }
                            break;
                        case TransConstant.BROAD_NOTICE_UPDATE:
                            getNotice();
                            break;
                        case TransConstant.BROAD_SHARE:
                            shareLog();
                            break;
                        case TransConstant.BROAD_DEAL_CATEGORY_UPDATE:
                            if (null != mHomeFragment) {
                                mHomeFragment.initData();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MLinkAPIFactory.createAPI(this).deferredRouter();

        initVars(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    private void initVars(Bundle savedInstanceState) {
        TAG = "首页";
        EventBus.getDefault().register(this);
        mCurrentTabIndex = 1;

        /*if (savedInstanceState == null) {
            mCurrentTabIndex = 1;
        } else {
            mCurrentTabIndex = savedInstanceState.getInt("TAB_INDEX", 1);
        }*/
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tabDeal = getView(R.id.tab_deal);
        tabMsgs = getView(R.id.tab_msgs);
        tabDiscover = getView(R.id.tab_discover);
        tabForum = getView(R.id.tab_forum);
        tabMine = getView(R.id.tab_mine);
        //        tvNotice = getView(R.id.tvNotice);
        //        layoutMessage = getView(R.id.tab_msgs);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tabDeal.setOnClickListener(this);
        tabMsgs.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
        tabForum.setOnClickListener(this);
        tabMine.setOnClickListener(this);
        //        layoutMessage.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mChangReceiver = new ChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransConstant.CHANGE_BROADCAST);
        mContext.registerReceiver(mChangReceiver, filter);
        fragmentManager = getSupportFragmentManager();
        setTabSelection();

        loadStyleData();
        initStore();
    }

    private void loadStyleData() {
        long startTime = System.currentTimeMillis();
        // 接口配置
        ForumApi.getInstance().settingsSystemGet(response -> {
            long duration = System.currentTimeMillis() - startTime;
            Logger.d("duration = " + duration);
            if (TextUtils.equals(response.getCode(), "0")) {
                SystemSettingsModelData data = response.getData();
                if (data != null) {
                    // 活动状态
                    if (data.getActivity() != null && TextUtils.equals(data.getActivity().getIsEnabled(), "1")) {
                        // 活动开启的情况
                        HtApplication.isActivityOn = true;
                        // 活动H5 url
                        mActivityUrl = data.getActivity().getUrl();
                        HtApplication.mActivityUrl = mActivityUrl;
                    }
                    // 全局浮标
                    if (data.getTemplateInfo() != null) {
                        if (data.getTemplateInfo().getWidgets() != null) {
                            //// 根据版本判断是否需要更新
                            // 新版本
                            String newVersion = data.getTemplateInfo().getStyleVersion();
                            // 本地版本
                            String localStyleVersion = (String) SPUtils.get(mContext, SPConstant.STYLE_VERSION, "");
                            if (!TextUtils.equals(newVersion, localStyleVersion)) {
                                // 需要更新
                                loadCommonStyle();
                                loadStaticStyle();
                                SPUtils.put(mContext, SPConstant.STYLE_VERSION, newVersion);
                            } else {
                                // 读取本地样式，如果样式为空，则需要请求样式
                                String staticRes = (String) SPUtils.get(mContext, SPConstant.STATIC_RES, "");
                                String styleRes  = (String) SPUtils.get(mContext, SPConstant.STYLE_RES, "");

                                if (!TextUtils.isEmpty(staticRes) && !TextUtils.isEmpty(styleRes)) {
                                    // 加载活动样式
                                    mCommonStyle = JSON.parseArray(styleRes, AppWidgetStyleModel.class);
                                    //                                    Logger.d("common style:\n" + mCommonStyle.toString());
                                    applyCommonStyle();
                                    // 加载静态样式
                                    mStaticResData = JSON.parseArray(staticRes, StaticsIfModelData.class);
                                    //                                    Logger.d("statiwc style:\n" + mStaticResData.toString());
                                    applyStaticStyle();
                                } else {
                                    // 需要更新
                                    loadCommonStyle();
                                    loadStaticStyle();
                                }
                            }

                            //// widgets
                            mTemplateWidgets = data.getTemplateInfo().getWidgets();
                            for (LinkWidgetModel widget : mTemplateWidgets) {
                                // 浮标数据
                                if (TextUtils.equals(widget.getWidgetId(), "rb_float_icon")) {
                                    SlidePicModel model = new SlidePicModel();
                                    model.setType(widget.getLinkType());
                                    model.setLinkData(widget.getLinkData());
                                    HtApplication.setFabData(model);
                                }
                            }
                        }
                    }
                }
            }
        }, error -> {

        });
    }

    /**
     * 请求服务端最新样式
     */
    private void loadCommonStyle() {
        ForumApi.getInstance().commonStyleGet(response -> {
            Logger.d("resp = " + response.toString());
            if (TextUtils.equals(response.getCode(), "0")) {
                if (response.getData() != null) {
                    mCommonStyle = response.getData();
                    // 缓存到本地
                    String jsonStyleRes = JSON.toJSONString(mCommonStyle);
                    SPUtils.put(mContext, SPConstant.STYLE_RES, jsonStyleRes);
                    // 加载活动样式
                    applyCommonStyle();
                }
            }
        }, error -> {
            Logger.d("error = " + error.toString());
        });
    }

    /**
     * 静态资源文件
     */
    private void loadStaticStyle() {
        ForumApi.getInstance().commonStaticsGet(response -> {
            if (TextUtils.equals(response.getCode(), "0")) {
                if (response.getData() != null) {
                    mStaticResData = response.getData();
                    // 缓存到本地
                    String jsonStaticRes = JSON.toJSONString(mStaticResData);
                    SPUtils.put(mContext, SPConstant.STATIC_RES, jsonStaticRes);
                    // 加载静态资源样式
                    applyStaticStyle();
                }
            }
        }, error -> {

        });
    }

    /**
     * 加载静态资源样式
     */
    private void applyStaticStyle() {
        if (!HtApplication.isActivityOn)
            return;
        for (StaticsIfModelData data : mStaticResData) {
            // 刷新图标
            if (TextUtils.equals(data.getId(), "refreshing_animate")) {
                List<String> files = data.getFiles();
                if (files != null && files.size() > 0) {
                    HtApplication.mActivityRefreshingImg = files.get(0);
                    EventBus.getDefault().post(new RefreshSwitchObject());
                }
            }
            // 商家 & 转运角标
            if (TextUtils.equals(data.getId(), "store_activity_label_pic")) {
                List<String> files = data.getFiles();
                if (files != null && files.size() > 0) {
                    HtApplication.mStoreTransportActivityLabel = files.get(0);
                }
            }
        }
    }

    /**
     * 开启活动样式
     */
    private void applyCommonStyle() {
        applyFabStyle();
        applyNavStyle();
        applyMainTabStyle();
    }

    /**
     * 全局活动入口样式
     */
    private void applyFabStyle() {
        AppWidgetStyleModel fabStyle = getStyle("rb_float_icon");
        if (fabStyle != null && fabStyle.getImageStyle() != null) {
            HtApplication.mActivityFabImg = fabStyle.getImageStyle().getNormalImage();
            EventBus.getDefault().post(new ActivityFabImgSetEvent(HtApplication.mActivityFabImg));
        }
    }

    /**
     * 导航栏样式
     */
    private void applyNavStyle() {
        if (!HtApplication.isActivityOn)
            return;
        AppWidgetStyleModel dealNavStyle = getStyle("deal_nav_activity");
        if (dealNavStyle != null && dealNavStyle.getImageStyle() != null) {
            if (mHomeFragment != null)
                mHomeFragment.setTabActivity(dealNavStyle.getImageStyle().getNormalImage(), mActivityUrl);
        }
    }

    /**
     * 底部tab样式
     */
    private void applyMainTabStyle() {
        ForumApi.getInstance().commonAppTabGet(response -> {
            if (TextUtils.equals(response.getCode(), "0")) {
                if (response.getData() != null) {
                    mTabData = response.getData();
                    //                    ArrayList<String>       tabIds = new ArrayList<>(mTabData.size());
                    HashMap<String, String> tabMap = new HashMap<>(mTabData.size());
                    for (AppWidgetModel tab : mTabData) {
                        //                        tabIds.add(tab.getStyleId());
                        tabMap.put(tab.getStyleId(), tab.getTitle());
                    }

                    // 优惠
                    if (!tabMap.containsKey("tab_deal")) {
                        tabDeal.setVisibility(View.GONE);
                    } else {
                        setMainTabView("tab_deal");
                        tabDeal.setText(tabMap.get("tab_deal"));
                    }
                    // 发现
                    if (!tabMap.containsKey("tab_discover")) {
                        tabDiscover.setVisibility(View.GONE);
                    } else {
                        setMainTabView("tab_discover");
                        tabDiscover.setText(tabMap.get("tab_discover"));
                    }
                    // 论坛
                    if (!tabMap.containsKey("tab_forum")) {
                        tabForum.setVisibility(View.GONE);
                    } else {
                        setMainTabView("tab_forum");
                        tabForum.setText(tabMap.get("tab_forum"));
                    }
                    // 消息
                    if (!tabMap.containsKey("tab_msgs")) {
                        tabMsgs.setVisibility(View.GONE);
                    } else {
                        setMainTabView("tab_msgs");
                        tabMsgs.setText(tabMap.get("tab_msgs"));
                    }
                    // 我的
                    if (!tabMap.containsKey("tab_mine")) {
                        tabMine.setVisibility(View.GONE);
                    } else {
                        setMainTabView("tab_mine");
                        tabMine.setText(tabMap.get("tab_mine"));
                    }
                }
            }
        }, error -> {

        });
    }

    /**
     * 根据id名获取底部对应tab
     *
     * @param tabName tab名
     * @return tab
     */
    private MainTabView getTabByName(String tabName) {
        switch (tabName) {
            case "tab_deal":
                return tabDeal;
            case "tab_discover":
                return tabDiscover;
            case "tab_forum":
                return tabForum;
            case "tab_msgs":
                return tabMsgs;
            case "tab_mine":
                return tabMine;
            default:
                return null;
        }
    }

    /**
     * 设置主页底部tab的图标
     *
     * @param tabId 底部tab的id
     */
    private void setMainTabView(String tabId) {
        AppWidgetStyleModel styleDeal = getStyle(tabId);
        MainTabView         mainTab   = getTabByName(tabId);
        if (styleDeal != null) {
            // 字体颜色
            AppWidgetStyleModelTextStyle textStyle = styleDeal.getTextStyle();
            if (textStyle != null) {
                ColorStateList colorStateList = new ColorStateList(mColorStateList, new int[]{Color.parseColor(textStyle.getActiveColor()), Color.parseColor(textStyle.getNormalColor())});
                mainTab.setTextColor(colorStateList);
            }
            // 图标
            AppWidgetStyleModelImageStyle imageStyle = styleDeal.getImageStyle();
            if (imageStyle != null) {
                mainTab.setImgUrls(new String[]{imageStyle.getNormalImage(), imageStyle.getActiveImage()});
                if (TextUtils.equals(tabId, "tab_deal")) {
                    ImageLoaderUtils.downloadOnlineImage(mContext, imageStyle.getNormalImage());
                } else {
                    ImageLoaderUtils.downloadOnlineImage(mContext, imageStyle.getActiveImage());
                }
            }
        }
    }


    /**
     * 获取指定样式
     *
     * @param styleId 样式Id
     * @return 样式model
     */
    public AppWidgetStyleModel getStyle(String styleId) {
        if (mCommonStyle != null) {
            for (AppWidgetStyleModel styleModel : mCommonStyle) {
                if (styleModel != null && TextUtils.equals(styleModel.getStyleId(), styleId)) {
                    return styleModel;
                }
            }
        }
        return null;
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection() {
        switch (mCurrentTabIndex) {
            case 1:
                clearSelection();
                tabDeal.setSelected(true);
                mHomeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
                switchContent(mPreFragment, mHomeFragment);
                break;
            case 2:
                clearSelection();
                tabDiscover.setSelected(true);
                mStoreFragment = (StoreFragment) fragmentManager.findFragmentByTag(StoreFragment.class.getSimpleName());
                if (mStoreFragment == null) {
                    mStoreFragment = new StoreFragment();
                }
                switchContent(mPreFragment, mStoreFragment);
                break;
            case 3:
                clearSelection();
                tabForum.setSelected(true);
                mForumFragment = (ForumFragment) fragmentManager.findFragmentByTag(ForumFragment.class.getSimpleName());
                if (mForumFragment == null) {
                    mForumFragment = new ForumFragment();
                }
                switchContent(mPreFragment, mForumFragment);
                break;
            case 4:
                getNotice();
                clearSelection();
                tabMsgs.setSelected(true);
                mMsgFragment = (MessageFragment) fragmentManager.findFragmentByTag(MessageFragment.class.getSimpleName());
                if (mMsgFragment == null) {
                    mMsgFragment = new MessageFragment();
                }
                switchContent(mPreFragment, mMsgFragment);
                break;
            case 5:
                clearSelection();
                tabMine.setSelected(true);
                mMeFragment = (MeFragment) fragmentManager.findFragmentByTag(MeFragment.class.getSimpleName());
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
                switchContent(mPreFragment, mMeFragment);
                break;
            default:
                break;
        }
    }

    public void switchContent(BaseFragment from, BaseFragment to) {
        // 切换底部tab清除chid.
        YbxTrace.getInstance().clearChid();
        purl = to.getClass().getSimpleName();
        purlh = to.toString().substring(to.toString().indexOf("{") + 1, to.toString().indexOf("}"));

        if (mPreFragment != to) {
            mPreFragment = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (null != from && from.isAdded()) {
                transaction.hide(from);
                from.onPause();
            }
            if (!to.isAdded()) { // 先判断是否被add过
                //                transaction.add(R.id.flContent_framepage, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.flContent_framepage, to, to.getClass().getSimpleName()).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                //                transaction.show(to).commit(); // 隐藏当前的fragment，显示下一个
                transaction.show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
                //                to.onResume();
            }
        } else {
            mPreFragment.returnTop();
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tabDeal.setSelected(false);
        tabMsgs.setSelected(false);
        tabDiscover.setSelected(false);
        tabForum.setSelected(false);
        tabMine.setSelected(false);
    }

    /**
     * 升级弹窗
     */
    private void showUpdateDlg(final PlatformObject platformObject) {
        SPUtils.put(mContext, SPConstant.LAST_UPDATE_POP_TIME, System.currentTimeMillis());
        final UpdateDlg updateDlg = new UpdateDlg(mContext, platformObject.new_change, platformObject.low_ver_num);
        updateDlg.setOnUpdateClickListener(updateDialog -> {
            if (!NetUtils.isConnected(mContext)) {
                ToastUtils.show(mContext, "请检查您的网络连接");
                SPUtils.remove(mContext, SPConstant.LAST_UPDATE_POP_TIME);
                updateDialog.dismiss();
                return;
            }
            if (NetUtils.isWifi(mContext)) {
                updateApk(platformObject);
                updateDialog.dismiss();
            } else {
                new Thread(() -> {
                    // 安装包的大小
                    String downloadSize = CommonUtils.getDownloadSize(platformObject.downloadurl);
                    if (TextUtils.isEmpty(downloadSize)) return;
                    // 是否使用流量下载提示
                    runOnUiThread(() -> new AlertDialog.Builder(mContext)
                            .setMessage(String.format(mContext.getResources().getString(R.string.traffic_tips), downloadSize))
                            .setPositiveButton(R.string.continue_update, (dialog, which) -> {
                                updateApk(platformObject);
                                updateDialog.dismiss();
                            })
                            .setNegativeButton(R.string.cancel, (dialog, which) -> {
                                dialog.dismiss();
                                mIsDownloading = false;
                            }).show());
                }).start();

            }
        });
        updateDlg.show();
    }

    /**
     * 开始更新
     *
     * @param platformObject 平台版本信息
     */
    private void updateApk(final PlatformObject platformObject) {
        mIsDownloading = true;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在下载...");
        mProgressDialog.setMax(mTotalSize);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        // 非强制更新情况，可以取消下载
        if (!CommonUtils.isForceUpdate(platformObject)) {
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.exit), (dialog, which) -> {
                mIsDownloading = false;
                dialog.cancel();
            });
        }
        new Thread() {
            @Override
            public void run() {
                doUpdate(platformObject.downloadurl);
            }
        }.start();
        mProgressDialog.show();
    }

    /**
     * 强制更新
     *
     * @param updateURL url
     */
    private void doUpdate(String updateURL) {
        Logger.d("updateURL = " + updateURL);
        File apkFile   = null;
        File updateDir = null;
        try {
            URL               url = new URL(updateURL);
            HttpURLConnection hc  = (HttpURLConnection) url.openConnection();
            hc.setRequestProperty("Accept-Encoding", "identity");
            hc.connect();
            InputStream in = hc.getInputStream();
            hc.setConnectTimeout(15000);
            mTotalSize = hc.getContentLength();
            int curSize  = 0;
            int progress = 0;
            updateDir = new File(getExternalCacheDir() + "apk/");
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }

            Logger.d("updateDir path = " + updateDir.getPath());
            apkFile = new File(updateDir.getPath(), "Haitao-" + System.currentTimeMillis() + ".apk");
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
            Logger.d(" total size = " + mTotalSize);
            FileOutputStream out   = new FileOutputStream(apkFile);
            byte[]           bytes = new byte[1024];
            int              c;
            while ((c = in.read(bytes)) != -1) {
                // 取消下载标记
                if (!mIsDownloading) {
                    hc.disconnect();
                    mProgressDialog.cancel();
                    in.close();
                    out.close();
                    return;
                }
                out.write(bytes, 0, c);
                curSize += c;
                progress = curSize * 100 / mTotalSize;
                Message msg    = new Message();
                Bundle  bundle = new Bundle();
                bundle.putInt("progress", progress);
                msg.what = REFRESHPROGRESS;
                msg.setData(bundle);
                myMessageHandler.sendMessage(msg);
            }
            mProgressDialog.cancel();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            myMessageHandler.sendEmptyMessage(ERROR);
        }

        if (apkFile != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(mContext, getApplicationContext().getPackageName() + ".fileprovider", new File(apkFile.getPath()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.parse("file://" + apkFile.getPath()), "application/vnd.android.package-archive");
            }
            startActivity(intent);
            finish();
        }
    }


    Handler myMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESHPROGRESS:
                    int p = msg.getData().getInt("progress");
                    mProgressDialog.setProgress(p);
                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(), "更新失败,请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PressExit.pressAgainExit(getApplicationContext());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_deal:
                mCurrentTabIndex = 1;
                break;
            case R.id.tab_discover:
                mCurrentTabIndex = 2;
                break;
            case R.id.tab_forum:
                mCurrentTabIndex = 3;
                break;
            case R.id.tab_msgs:
                mCurrentTabIndex = 4;
                break;
            case R.id.tab_mine:
                mCurrentTabIndex = 5;
                break;
            default:
                break;
        }
        setTabSelection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotice();
    }

    /**
     * 加载商家数据
     */
    private void initStore() {
        HTTPUtil.postAsyn(URILocatorHelper.getUrlBase().getBaseURI() + "/" + MethodConstant.STORE_LIST, MethodConstant.STORE_LIST, null, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    new Thread(() -> {
                        try {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }


    private void getNotice() {
        if (!HtApplication.isLogin())
            return;
        noticeViewContext.getService().asynFunction(MethodConstant.MY_UNREAD, noticeViewContext.getEntity(), new IAsynServiceHandler<NoticeObject>() {
            @Override
            public void onSuccess(NoticeObject entity) throws Exception {
                if (null != entity) {
                    UserObject userObject = UserManager.getInstance().getUser();
                    if (!userObject.newpm.equals(entity.newpm)) {
                        userObject.newpm = entity.newpm;
                        userObject.new_pm = entity.new_pm;
                        userObject.new_notice = entity.new_notice;
                        UserManager.getInstance().setUser(userObject);
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_NOTICE);
                        mContext.sendBroadcast(mIntent);
                    }
                    if (HtApplication.isLogin()) {
                        int noticeNum = TextUtils.isEmpty(UserManager.getInstance().getUser().newpm) ? 0 : Integer.parseInt(UserManager.getInstance().getUser().newpm);
                        //                        tvNotice.setVisibility(noticeNum > 0 ? View.VISIBLE : View.GONE);
                    } else {
                        //                        tvNotice.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onSuccessPage(PageResult<NoticeObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    private void shareLog() {
        String share_type    = (String) SPUtils.get(mContext, SPConstant.SHARE_TYPE, "");
        String share_content = (String) SPUtils.get(mContext, SPConstant.SHARE_CONTENT, "");
        shareViewContext.getEntity().share_type = share_type;
        shareViewContext.getEntity().share_content = share_content;
        shareViewContext.getService().asynFunction(MethodConstant.SHARE_LOG, shareViewContext.getEntity(), new IAsynServiceHandler<ShareObject>() {
            @Override
            public void onSuccess(ShareObject entity) throws Exception {
                SPUtils.put(mContext, SPConstant.SHARE_TYPE, "");
                SPUtils.put(mContext, SPConstant.SHARE_CONTENT, "");
            }

            @Override
            public void onSuccessPage(PageResult<ShareObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    /**
     * 广告弹窗
     */
    private void showAdPop() {
        ForumApi.getInstance()
                .commonPopupAdGet(response -> {
                    Logger.d("ad entity = " + response.toString());
                    SlidePicModel entity = response.getData();
                    // 弹窗广告
                    String strPopHis = (String) SPUtils.get(mContext, SPConstant.POP_AD, "");
                    if (entity != null && !TextUtils.isEmpty(entity.getPic())) {
                        String popDate = new CustomDate().toString();
                        if (!TextUtils.equals(strPopHis, popDate)) {
                            SPUtils.put(mContext, SPConstant.POP_AD, popDate);
                            adDialogUtils = new AdDialogUtils(mContext);
                            adDialogUtils.setOnItemClickLitener(() -> TopicLink.jump(mContext, entity, TopicLink.SOURCE_TYPE.POPUP));
                            adDialogUtils.show(entity.getPic());
                        } else {
                            showSign();
                        }
                    } else {
                        showSign();
                    }
                }, error -> showSign());
    }

    /**
     * 判断是否显示签到逻辑
     */
    private void showSign() {
        // 显示签到弹窗
        if (HtApplication.isLogin()) {
            ForumApi.getInstance().userAccountSignedInInfoGet(response -> {
                boolean needShowSignFlag = true;
                if (null != response && "0".equals(response.getCode())) {
                    if (TextUtils.equals(response.getData().getIsSignedIn(), "1")) {
                        needShowSignFlag = false;
                    }
                }
                showSignPop(needShowSignFlag);
            }, error -> showSignPop(true));
        } else {
            showSignPop(true);
        }
    }

    /**
     * 显示签到弹窗
     *
     * @param needShowSign 是否需要显示签到弹框
     */
    private void showSignPop(boolean needShowSign) {
        // 延迟1.5秒再弹窗(签到 / 广告)
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(aLong -> {
                    // 没有显示更新提示的情况
                    if (!mNeedShowUpdate) {
                        // 上次签到提示的时间
                        long lastSignTime;
                        if (HtApplication.isLogin()) {
                            lastSignTime = (long) SPUtils.get(mContext, SPConstant.LAST_SIGN_POP_TIME + "_" + UserManager.getInstance().getUserId(), 0L);
                        } else {
                            lastSignTime = (long) SPUtils.get(mContext, SPConstant.LAST_SIGN_POP_TIME, 0L);
                        }
                        // 超过24小时 且 该用户当前未签到 再提示签到
                        if (System.currentTimeMillis() - lastSignTime > 24 * 60 * 60 * 1000 && needShowSign) {
                            // 显示签到弹窗
                            showSignDlg();
                        }
                    }
                });
    }

    /**
     * 签到弹窗
     */
    private void showSignDlg() {
        if (HtApplication.isLogin()) {
            SPUtils.put(mContext, SPConstant.LAST_SIGN_POP_TIME + "_" + UserManager.getInstance().getUserId(), System.currentTimeMillis());
        } else {
            SPUtils.put(mContext, SPConstant.LAST_SIGN_POP_TIME, System.currentTimeMillis());
        }
        mSignDialog = new SignBottomSheetDlg(mContext)
                .setSignListener(new SignBottomSheetDlg.SignListener() {
                    @Override
                    public void sign(SignBottomSheetDlg dialog) {
                        if (HtApplication.isLogin()) {
                            doSign();
                        } else {
                            mNeedSign = true;
                            QuickLoginActivity.launch(mContext);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void checkGold(SignBottomSheetDlg dialog) {
                        GoldActivity.launchForResult(mContext);
                        dialog.dismiss();
                    }
                });
        mSignDialog.show();
    }

    /**
     * dismiss签到框
     */
    private void dismisSignDlg() {
        if (mSignDialog != null && mSignDialog.isShowing()) {
            mSignDialog.dismiss();
        }
    }

    /**
     * 签到
     */
    private void doSign() {
        ForumApi.getInstance().userAccountSigningInPost(
                response -> {
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        String msg = String.format(getResources().getString(R.string.sign_success), "{+" + response.getData() + "}");
                        CharSequence formatted = ColorPhrase.from(msg)
                                .withSeparator("{}")
                                .innerColor(mContext.getResources().getColor(R.color.yellowFEEB36))
                                .outerColor(mContext.getResources().getColor(R.color.white))
                                .format();
                        tabDeal.postDelayed(() -> showToast(ToastType.SIGN_SUCCESS, formatted), 500);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                    dismisSignDlg();
                },
                error -> {
                    showErrorToast(error);
                    dismisSignDlg();
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mNeedSign && resultCode == requestCode && requestCode == TransConstant.IS_LOGIN) {
            SPUtils.put(mContext, SPConstant.LAST_SIGN_POP_TIME + "_" + UserManager.getInstance().getUserId(), System.currentTimeMillis());
            doSign();
        } else if (requestCode == TransConstant.CHECK_GOLD && resultCode == RESULT_OK) {
            SPUtils.put(mContext, SPConstant.LAST_SIGN_POP_TIME + "_" + UserManager.getInstance().getUserId(), System.currentTimeMillis());
        }
        mNeedSign = false;
        if (requestCode == TransConstant.IS_LOGIN && HtApplication.isLogin() && mStoreFragment != null && mStoreFragment.isAdded()) {
            mStoreFragment.refreshData();
        }
    }

    /**
     * 响应所有R.id.iv_known的控件的点击事件
     * <p>
     * 移除高亮布局
     * </p>
     *
     * @param view
     */
    public void clickKnown(View view) {
        //如果开启next模式
        if (mHighLight.isShowing() && mHighLight.isNext()) {
            mHighLight.next();
        } else {
            remove(null);
        }
    }

    public void remove(View view) {
        mHighLight.remove();
    }

    /**
     * 如果用户已经登录，先获取用户签到信息
     */
    /*private boolean hasSigned() {
        ForumApi.getInstance().userAccountSignedInInfoGet(response -> {
            if (null != response && "0".equals(response.getCode())) {
                return (TextUtils.equals(response.loadData().getIsSignedIn(), "1"));
            } else {
                return false;
            }
        }, error -> false);
    }*/

    /**
     * @param platformObject 版本信息
     * @return 是否需要显示更新弹窗
     */
    public boolean needShowUpdateDlg(PlatformObject platformObject) {
        if (BuildConfig.VERSION_CODE < Integer.parseInt(platformObject.now_ver_num)) {
            // 上次更新提示的时间
            long lastTime = (long) SPUtils.get(mContext, SPConstant.LAST_UPDATE_POP_TIME, 0L);
            // 强制更新 或 超过24小时再提示更新
            if (CommonUtils.isForceUpdate(platformObject) || System.currentTimeMillis() - lastTime > 24 * 60 * 60 * 1000) {
                return true;
            }
        }
        return false;
    }

    @Subscribe(sticky = true)
    public void onUpdateEvent(PlatformObject platformObject) {
        if (needShowUpdateDlg(platformObject)) {
            mNeedShowUpdate = true;
            // 显示更新弹窗
            showUpdateDlg(platformObject);
            return;
        }
        showAdPop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //        super.onSaveInstanceState(outState);
        //        outState.putInt("TAB_INDEX", mCurrentTabIndex);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mChangReceiver);
        //        if (mIsUpdateReceiverRegistered) {
        //            mContext.unregisterReceiver(mUpdateReceiver);
        //    }
    }

    @Override
    protected String getActivityTAG() {
        return "";
    }
}
