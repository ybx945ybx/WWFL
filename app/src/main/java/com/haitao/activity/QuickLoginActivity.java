package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.LoginStateChangedEvent;
import com.haitao.model.PlatformObject;
import com.haitao.model.UserObject;
import com.haitao.utils.NetUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.verifycode.OnVerifyCodeCallBackListener;
import com.haitao.utils.verifycode.VerifyCodeUtils;
import com.haitao.view.HtEditTextView;
import com.haitao.view.HtHeadView;
import com.haitao.view.dialog.CountryCodeChooseDlg;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.swagger.client.model.AreaModel;
import io.swagger.client.model.LoginSuccessModelData;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 快速登录（手机验证码登录）
 */
public class QuickLoginActivity extends BaseActivity implements View.OnClickListener, HtEditTextView.textChangedListener {
    private HtHeadView     headView;                // 标题
    private TextView       tvRegisterTips;          // 首次试用直接创建账号提示
    private HtEditTextView etPhone, etCode;         // 手机号 验证码输入框
    private TextView  tvQuickLogin;                 // 快速登录按钮
    private TextView  tvUseAccountLogin;            // 使用账号密码登录
    private ImageView tvSina, tvWx, tvQQ;           // 第三方登录

    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private boolean        isOnCount;               // 是否在倒计时
    private String         mRegionId;               // 国家码

    private CountryCodeChooseDlg mCountryCodeChooseDlg;   // 选择国家弹框

    private List<AreaModel> mCountryList; // 国家区号列表数据
    private PlatformObject  platformObject;
    private VerifyCodeUtils verifyCodeUtils;
    private UserObject      userObject;

    private int type;                   // type=1是注册

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, QuickLoginActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    /**
     * 跳转到当前页(注册)
     *
     * @param context mContext
     */
    public static void launchForRegister(Context context) {
        Intent intent = new Intent(context, QuickLoginActivity.class);
        intent.putExtra(TransConstant.TYPE, 1);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "验证码登录";
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(TransConstant.TYPE, 0);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        headView = getView(R.id.head_view);
        tvRegisterTips = getView(R.id.tv_register_tips);
        etPhone = getView(R.id.et_phone);
        etCode = getView(R.id.et_code);

        tvQuickLogin = getView(R.id.tv_quick_login);

        tvUseAccountLogin = getView(R.id.tv_account_login);

        tvSina = getView(R.id.ivSina);
        tvWx = getView(R.id.ivWx);
        tvQQ = getView(R.id.ivQQ);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        headView.setOnRightClickListener(view -> QuickLoginActivity.launchForRegister(mContext));

        etPhone.setOnLeftClickListener(view -> {
            if (mCountryList != null && mCountryList.size() > 0) {
                if (mCountryCodeChooseDlg == null) {
                    String selectId = "";
                    for (AreaModel areaModel : mCountryList) {
                        if (areaModel.getAreaCode().contains("+86")) {
                            selectId = areaModel.getAreaId();
                        }
                    }
                    mCountryCodeChooseDlg = new CountryCodeChooseDlg(mContext, mCountryList);
                    mCountryCodeChooseDlg.setSelectId(selectId);
                    mCountryCodeChooseDlg.setOnCountryCodeSelectedListener((areaModel, dialog) -> {
                        etPhone.setLeftTxt(areaModel.getAreaCode());
                        mRegionId = areaModel.getAreaCode();
                        dialog.hide();
                    });
                }
                mCountryCodeChooseDlg.show();
            }
        });

        etCode.setOnRightTxtClickListener(view -> {
            if (etCode.getRightTxtEnable()) {
                getCode();
            }
        });

        tvQuickLogin.setOnClickListener(this);
        tvUseAccountLogin.setOnClickListener(this);

        tvSina.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvWx.setOnClickListener(this);

        // 监听快速登录按钮是否可点击
        etPhone.addTextChangedListener(this);
        etCode.addTextChangedListener(this);
    }

    /**
     * 检查快速登录按钮是否可点击
     */
    private void checkLoginEnable() {
        if (etPhone.isHasText() && etCode.isHasText()) {
            tvQuickLogin.setEnabled(true);
        } else {
            tvQuickLogin.setEnabled(false);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRegionId = "+86";
        platformObject = new PlatformObject();
        userObject = new UserObject();
        mCountryList = new ArrayList<>();

        // 获取服务器国家区号
        ForumApi.getInstance().commonAreasInfoGet(response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        List<AreaModel> data = response.getData();
                        if (data != null && data.size() > 0) {
                            mCountryList.addAll(data);
                        }
                    }
                },
                error -> {

                });

        if (type == 1) {
            headView.setRightTextVisible(false);
            tvRegisterTips.setVisibility(View.GONE);
            tvUseAccountLogin.setVisibility(View.GONE);
            tvQuickLogin.setText(mContext.getResources().getString(R.string.forget_next));
        } else {
            headView.setRightTextVisible(true);

        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (TextUtils.isEmpty(etPhone.getText())) {
            ToastUtils.show(mContext, "请先输入手机号码");
            return;
        }
        // 获取图形验证码
        verifyCodeUtils = new VerifyCodeUtils(mContext, null, null, TransConstant.VerifyType.BIND, etPhone.getText().toString());
        verifyCodeUtils.setmOnVerifyCodeCallBackListener(new OnVerifyCodeCallBackListener() {
            @Override
            public void onError() {
                if (!NetUtils.isConnected(mContext)) {
                    showToast(ToastType.ERROR, getResources().getString(R.string.net_error));
                }
            }

            @Override
            public void onSuccess() {
            }

            @Override
            public void onSuccess(int type, String code) {
                sendVerifyCode(code);
            }

            @Override
            public void onSuccess(UserObject userObject) {

            }

        });
        verifyCodeUtils.getSetting();
    }

    private void sendVerifyCode(String code) {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost(mRegionId, etPhone.getText().toString(), TransConstant.VerifyType.BIND, null, null, code,
                response -> {
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        startCountDown();
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();
                        showToast(String.format(getResources().getString(R.string.send_verify_code), "\n" + mRegionId + "-" + etPhone.getText().toString()));
                        etCode.requestFocus();
                    } else if (CodeConstant.CODE_NOT_MATCH.equals(response.getCode())) {
                        verifyCodeUtils.refreshImge();
                        showToast(ToastType.ERROR, response.getMsg());
                    } else if ("7005".equals(response.getCode())) {
                        showToast(ToastType.ERROR, response.getMsg());
                    } else {
                        verifyCodeUtils.dismissImageDialog();
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                }, this::showErrorToast);
    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new TimerCountDown(60 * 2000, 1000);
        }
        mCountDownTimer.start();
        isOnCount = true;
    }

    class TimerCountDown extends CountDownTimer {

        public TimerCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            etCode.setRightTxtEnable(false);
            etCode.setRightTxt("重新获取" + millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            isOnCount = false;
            etCode.setRightTxtEnable(true);
            etCode.setRightTxt("获取验证码");
        }
    }


    @Override
    public void onClick(View v) {
        Platform plat = null;
        switch (v.getId()) {
            case R.id.ivSina:
                showProgressDialog("正在微博...");
                userObject.type = "1";
                userObject.platformName = "微博";
                plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                //			plat.SSOSetting(true);
                plat.setPlatformActionListener(new AuthPlatformActionListener());
                plat.authorize();
                break;
            case R.id.ivWx:
                showProgressDialog("正在微信...");
                userObject.type = "3";
                userObject.platformName = "微信";
                plat = ShareSDK.getPlatform(Wechat.NAME);
                plat.SSOSetting(true);
                plat.setPlatformActionListener(new AuthPlatformActionListener());
                plat.authorize();
                break;
            case R.id.ivQQ:
                showProgressDialog("正在QQ...");
                userObject.type = "2";
                userObject.platformName = "QQ";
                plat = ShareSDK.getPlatform(QQ.NAME);
                //			plat.SSOSetting(true);
                plat.setPlatformActionListener(new AuthPlatformActionListener());
                plat.authorize();
                break;
            case R.id.tv_quick_login:
                doQuickLogin();
                break;
            case R.id.tv_account_login:
                LoginActivity.launch(mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 快速登录(短信验证码登录)
     */
    private void doQuickLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPhone.getWindowToken(), 0);

        showProgressDialog(R.string.operationg);

        ForumApi.getInstance().userAccountLoginWithSmsCaptchaPost(mRegionId,
                etPhone.getText().toString(),
                etCode.getText().toString(),
                null,
                response -> {
                    dismissProgressDialog();
                    Logger.d("LoginSuccessModel----》" + response.toString());
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.login_success));
                        wrapUserObject(response.getData());
                        UserManager.getInstance().setUser(userObject);
                        TalkingDataAppCpa.onLogin(UserManager.getInstance().getUserId());
                        EventBus.getDefault().post(new LoginStateChangedEvent(true));
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                        mContext.sendBroadcast(mIntent);
                        setResult(TransConstant.IS_LOGIN);
                        finishActivity();
                    } else if (CodeConstant.PHONE_BIND_MULTI.equals(response.getCode())) {
                        showToast(ToastType.ERROR, getResources().getString(R.string.phone_bind_multi));
                    } else if (CodeConstant.PHONE_NOT_REGISTER.equals(response.getCode())) {
                        wrapUserObject(response.getData());
                        userObject.mobile = etPhone.getText().toString();//   接口返回的mobile为空，去edittext的值
                        userObject.actionToken = etCode.getText().toString();
                        userObject.area = mRegionId;
                        CompleteUserInfoActivity.launch(mContext, userObject, 2);
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());

                    }
                },
                error -> {
                    showErrorToast(error);
                    dismissProgressDialog();
                });
    }

    private void finishActivity(){
        headView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkLoginEnable();
        if (isOnCount)
            return;
        etCode.setRightTxtEnable(etPhone.getText().toString().trim().length() > 6);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class AuthPlatformActionListener implements PlatformActionListener {

        @Override
        public void onCancel(Platform arg0, int arg1) {
            dismissProgressDialog();
            showToast("使用" + userObject.platformName + "登录被取消");
        }

        @Override
        public void onComplete(Platform platform, int arg1,
                               HashMap<String, Object> arg2) {
            dismissProgressDialog();
            if (null != platform) {
                Logger.t("tg").d("社交平台名称:" + platform.getName());
                Logger.t("tg").d("UID:" + platform.getDb().getUserId());
                Logger.t("tg").d(platform.getDb().getUserName());
                Logger.t("tg").d("TOKEN:" + platform.getDb().getToken());
                Logger.t("tg").d(platform.getDb().getUserIcon());
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
                            showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.login_success));
                            wrapUserObject(response.getData());
                            UserManager.getInstance().setUser(userObject);
                            TalkingDataAppCpa.onLogin(UserManager.getInstance().getUserId());
                            EventBus.getDefault().post(new LoginStateChangedEvent(true));
                            Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                            mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                            mContext.sendBroadcast(mIntent);

                            if (!"0".equals(response.getData().getMobileUnique().trim())) {
                                if (TextUtils.isEmpty(response.getData().getMobile())) {
                                    FirstBindPhoneActivity.launch(mContext);
                                }
                                setResult(TransConstant.IS_LOGIN);
                                finishActivity();
                            } else {
                                FirstBindPhoneActivity.launch(mContext, 1);
                                finishActivity();

                            }
                        } else if (CodeConstant.THIRD_NOT_BIND_55ACCOUNT.equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            BindActivity.launch(mContext, userObject);
                        } else if (CodeConstant.NOTE_BIND_PHONE.equals(response.getCode())) {
                            wrapUserObject(response.getData());
                            UserManager.getInstance().setUser(userObject);
                            FirstBindPhoneActivity.launch(mContext);

                        } else {
                            showToast(ToastType.ERROR, response.getMsg());
                        }
                    }, error -> {
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

            YbxTrace.getInstance().getTraceCommonBean().mid = entity.getUid() + "";

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
                ((Activity) mContext).setResult(TransConstant.IS_LOGIN);
                ((Activity) mContext).finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountryCodeChooseDlg != null) {
            mCountryCodeChooseDlg.dismiss();
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (verifyCodeUtils != null)
            verifyCodeUtils.dismissImageDialog();
    }
}
