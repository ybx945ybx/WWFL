package com.haitao.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.PlatformObject;
import com.haitao.model.UserObject;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.DisplayImageOptions;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.HtVpIndicator;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;

import java.util.HashMap;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import io.swagger.client.model.LoginSuccessModelData;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    @BindDimen(R.dimen.px30) int BOTTOM_PADDING;

    private ViewPager    pager;
    private LinearLayout llytWxLogin;
    private LinearLayout llytRegister;
    //    private TextView     tvAward;
    static  Context      mContext;
    private int currentItem = 0;
    private int             flaggingWidth;// 滑动关闭引导页所需滚动的长度
    private GestureDetector gestureDetector; // 用户滑动
    private int[] IMAGE_URLS = new int[]{R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3, R.mipmap.splash_4, R.mipmap.splash_5};
    //    private int[] IMAGE_URLS = new int[]{R.mipmap.splash_4, R.mipmap.splash_3, R.mipmap.splash_2};

    protected IViewContext<UserObject, IEntityService<UserObject>> commandViewContext = VF.<UserObject>getDefault(UserObject.class);
    PlatformObject platformObject;
    private LinearLayout mLlIndicator; // 指示器容器
    private UserObject   userObject;
    private ImageButton  mIbClose; // 关闭引导页
    private LinearLayout mLlLogin; // 未登录状态下，两个按钮
    private LinearLayout mLlBottom; // 底部容器（包含指示器和登录注册按钮）

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initVars();
        initView();
        initEvent();
        initData();

    }

    private void initVars() {
        TAG = "引导页";
        mContext = GuideActivity.this;
        platformObject = new PlatformObject();
        userObject = new UserObject();
        SPUtils.put(mContext, SPConstant.FIRST_OPEN, false);
    }

    private void initData() {
        /*ForumApi.getInstance().settingsSystemGet(
                response -> {
                    if (response.getData() != null) {
                        if (TextUtils.isEmpty(response.getData().getRegisterRewardAmount())) {
                            tvAward.setVisibility(View.GONE);
                        } else {
                            tvAward.setVisibility(View.VISIBLE);
                            tvAward.setText("送" + response.getData().getRegisterRewardAmount() + "美元");
                        }
                    }
                },
                error -> {
                });*/
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        gestureDetector = new GestureDetector(mContext, new GuideViewTouch());
        flaggingWidth = DensityUtil.dip2px(mContext, 20);

        llytWxLogin = findViewById(R.id.llyt_wx_login);
        llytRegister = findViewById(R.id.llyt_register);
        mLlIndicator = findViewById(R.id.ll_indicator);
        mLlBottom = findViewById(R.id.ll_bottom);
        mLlLogin = findViewById(R.id.ll_login);
        //        tvAward = (TextView) findViewById(R.id.tv_award);
        mIbClose = findViewById(R.id.ib_close);
        // 已经登录，不显示两个按钮
        if (HtApplication.isLogin()) {
            mLlLogin.setVisibility(View.GONE);
            mLlBottom.setPadding(mLlBottom.getPaddingLeft(), BOTTOM_PADDING, mLlBottom.getPaddingRight(), BOTTOM_PADDING);
        }
    }

    private void initEvent() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        pager.setAdapter(new ImageAdapter(mContext));

        pager.addOnPageChangeListener(new HtVpIndicator(mContext, pager, mLlIndicator, IMAGE_URLS.length) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentItem = position;
            }
        });

        /*pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        llytWxLogin.setOnClickListener(this);
        llytRegister.setOnClickListener(this);
        mIbClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llyt_wx_login:
                TCAgent.onEvent(mContext, "引导页-微信登录");
                doWxLogin();
                break;
            case R.id.llyt_register:
                TCAgent.onEvent(mContext, "引导页-快速注册");
                // 注册/快速登录
                QuickLoginActivity.launch(mContext);
                break;
            case R.id.ib_close:
                TCAgent.onEvent(mContext, "引导页-跳过");
                MainActivity.launch(mContext);
                finish();
                break;
        }
    }

    /**
     * 微信登录
     */
    private void doWxLogin() {
        showProgressDialog("正在微信...");
        userObject.type = "3";
        userObject.platformName = "微信";
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.SSOSetting(true);
        plat.setPlatformActionListener(new AuthPlatformActionListener());
        plat.authorize();
    }

    //    Handler mHandler = new Handler() {
    //        public void handleMessage(Message msg) {
    //            if (1 == msg.what) {
    //                thirdLogin();
    //            }
    //        }
    //    };

    class AuthPlatformActionListener implements PlatformActionListener {

        @Override
        public void onCancel(Platform arg0, int arg1) {
            dismissProgressDialog();
            showToast(ToastType.ERROR, "使用" + userObject.platformName + "登录被取消");
        }

        @Override
        public void onComplete(Platform platform, int arg1,
                               HashMap<String, Object> arg2) {
            dismissProgressDialog();
            if (null != platform) {
                platformObject.userid = platform.getDb().getUserId();
                platformObject.token = platform.getDb().getToken();
                platformObject.nickname = platform.getDb().getUserName();
                platformObject.icon = platform.getDb().getUserIcon();
                platformObject.platname = platform.getName();
                if (!platform.getName().equals(Wechat.NAME)) {
                    //                    mHandler.sendEmptyMessage(1);
                    thirdLogin();
                    return;
                }
            }
            if (null == arg2) {
                platform.showUser(null);//执行登录，登录后在回调里面获取用户资料
            } else {
                Logger.d(arg2.toString());
                if (arg2.containsKey("email")) {
                    platformObject.email = (String) arg2.get("email");
                }
                if (platform.getName().equals(Wechat.NAME)) {
                    Logger.d("unionid:" + (String) arg2.get("unionid"));
                    platformObject.unionid = (String) arg2.get("unionid");
                }
                //                mHandler.sendEmptyMessage(1);
                thirdLogin();
            }
        }

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            dismissProgressDialog();
            Logger.d("====error");
            Logger.d(arg2.getMessage());

            String expName = arg2.getClass().getSimpleName();
            Logger.e("error:" + expName);

            if ("WechatClientNotExistException".equals(expName)
                    || "WechatTimelineNotSupportedException".equals(expName)
                    || "WechatFavoriteNotSupportedException".equals(expName)) {
                ToastUtils.show(mContext, "请安装微信客户端");
            }
        }

    }

    private void thirdLogin() {
        runOnUiThread(() -> {
            userObject.username = platformObject.nickname;
            userObject.open_id = platformObject.userid;
            userObject.open_unionid = platformObject.unionid;
            userObject.open_token = platformObject.token;
            userObject.avatar = platformObject.icon;
            Logger.d(userObject.type);
            Logger.d(userObject.open_id);
            Logger.d(userObject.open_token);
            Logger.d(userObject.open_unionid);

            showProgressDialog(R.string.login_authing);
            ForumApi.getInstance().userAccountLoginByTppTppIdPost(userObject.type, userObject.open_id, userObject.open_token, TextUtils.isEmpty(userObject.open_unionid) ? null : userObject.open_unionid,
                    response -> {
                        dismissProgressDialog();
                        Logger.d("LoginSuccessModel----》" + response.toString());
                        if ("0".equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            UserManager.getInstance().setUser(userObject);
                            TalkingDataAppCpa.onLogin(UserManager.getInstance().getUserId());
                            Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                            mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                            mContext.sendBroadcast(mIntent);

                            if (!"0".equals(response.getData().getMobileUnique().trim())) {
                                if (TextUtils.isEmpty(response.getData().getMobile())) {
                                    FirstBindPhoneActivity.launch(mContext);
                                }
                                setResult(TransConstant.IS_LOGIN);
                                MainActivity.launch(mContext);
                                finish();
                            } else {
                                FirstBindPhoneActivity.launch(mContext, 1);
                                MainActivity.launch(mContext);
                                finish();

                            }

                        } else if (CodeConstant.PHONE_BIND_MULTI.equals(response.getCode())) {
                            ToastUtils.show(mContext, R.string.phone_bind_multi);
                        } else if (CodeConstant.THIRD_NOT_BIND_55ACCOUNT.equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            BindActivity.launch(mContext, userObject);
                        } else if (CodeConstant.PHONE_NOT_BIND_55ACCOUNT.equals(response.getCode())) {

                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    },
                    error -> {
                        dismissProgressDialog();
                        showErrorToast(error);
                    });
        });
    }

    private void wrapUserObject(LoginSuccessModelData entity) {
        if (entity != null) {
            userObject.token = entity.getToken();
            userObject.ht_token = entity.getToken();
            userObject.uid = entity.getUid();
            userObject.username = entity.getUsername();
            userObject.mobile = entity.getMobile();
            userObject.mobile_unique = entity.getMobileUnique();
            userObject.refresh_token = entity.getRefreshToken();
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater      inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_image, view, false);
            assert imageLayout != null;
            ImageView imgContent = (ImageView) imageLayout.findViewById(R.id.img_content);
            ImageLoaderUtils.showDrawableImage(IMAGE_URLS[position], imgContent, IMAGE_URLS[position]);
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 当滑动到最后一页时，继续滑动将进入到联赛选择列表页
     */
    private class GuideViewTouch extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            if (currentItem == (IMAGE_URLS.length - 1)) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
                        - e2.getY()) && (e1.getX() - e2.getX() <= (-flaggingWidth)
                        || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        MainActivity.launch(mContext);
                        finish();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            if (HtApplication.isLogin()) {
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                mContext.sendBroadcast(mIntent);
                //                SPUtils.put(mContext, SPConstant.FIRST_OPEN, false);
                MainActivity.launch(mContext);
                finish();
            }
        }
    }

    @Override
    protected String getActivityTAG() {
        return "";
    }
}
