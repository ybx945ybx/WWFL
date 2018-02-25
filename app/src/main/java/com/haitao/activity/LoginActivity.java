package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.haitao.utils.ToastUtils;
import com.haitao.utils.verifycode.OnVerifyCodeCallBackListener;
import com.haitao.utils.verifycode.VerifyCodeUtils;
import com.haitao.view.HtEditTextView;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.swagger.client.model.LoginSuccessModelData;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 账号登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, HtEditTextView.textChangedListener {
    private HtEditTextView etAccount, etPassWord;
    private TextView  tvUseQuickLogin;
    private TextView  tvForgetPassword;
    private TextView  tvLogin;
    private ImageView tvSina, tvWx, tvQQ;

    private UserObject userObject;

    PlatformObject platformObject;
    private VerifyCodeUtils verifyCodeUtils;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "登录";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        etAccount = getView(R.id.et_account);
        etPassWord = getView(R.id.et_password);
        etPassWord.setRightImgSelected(false);
        etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());

        tvLogin = getView(R.id.tv_login);

        tvUseQuickLogin = getView(R.id.tv_use_quick_login);
        tvForgetPassword = getView(R.id.tv_forget_password);

        tvSina = getView(R.id.ivSina);
        tvWx = getView(R.id.ivWx);
        tvQQ = getView(R.id.ivQQ);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        etPassWord.setOnRightImgClickListener(view -> {
            etPassWord.setRightImgSelected(!etPassWord.getRightImgSelected());

            etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = etPassWord.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        });

        tvLogin.setOnClickListener(this);
        tvUseQuickLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

        tvSina.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvWx.setOnClickListener(this);

        // 监听快速登录按钮是否可点击
        etAccount.addTextChangedListener(this);
        etPassWord.addTextChangedListener(this);
    }

    /**
     * 检查快速登录按钮是否可点击
     */
    private void checkLoginEnable() {
        if (etAccount.isHasText() && etPassWord.isHasText()) {
            tvLogin.setEnabled(true);
        } else {
            tvLogin.setEnabled(false);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        platformObject = new PlatformObject();
        userObject = new UserObject();
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
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_use_quick_login:
                finish();
                break;
            case R.id.tv_forget_password:
                // 重置密码
                ResetPwdActivity.launch(mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void doLogin() {
        // 先获取验证码
        verifyCodeUtils = new VerifyCodeUtils(mContext, etAccount.getText().toString(), etPassWord.getText().toString(), null, null);
        verifyCodeUtils.setmOnVerifyCodeCallBackListener(new OnVerifyCodeCallBackListener() {
            @Override
            public void onError() {
            }

            @Override
            public void onSuccess() {
            }

            @Override
            public void onSuccess(int type, String code) {
                login(code);
            }

            @Override
            public void onSuccess(UserObject entity) {
            }

        });
        verifyCodeUtils.getSetting();
    }

    /**
     * 登录-网络请求
     *
     * @param code code
     */
    private void login(String code) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAccount.getWindowToken(), 0);

        showProgressDialog(R.string.operationg);
        ForumApi.getInstance().userAccountLoginPost(etAccount.getText().toString(),
                etPassWord.getText().toString(),
                null,
                null,
                null,
                code,
                null,
                null,
                null,
                response -> {
                    dismissProgressDialog();
                    Logger.d("LoginSuccessModel----》" + response.toString());
                    if ("0".equals(response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.login_success));

                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();

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
                    } else if (CodeConstant.NOTE_BIND_PHONE.equals(response.getCode())) {
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();

                        wrapUserObject(response.getData());
                        UserManager.getInstance().setUser(userObject);
                        FirstBindPhoneActivity.launch(mContext);
                    } else if (CodeConstant.CODE_NOT_MATCH.equals(response.getCode())) {
                        verifyCodeUtils.refreshImge();
                        //                        showToast(response.getMsg());
                        ToastUtils.show(mContext, response.getMsg());
                    } else {
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();

                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                error -> {
                    if (verifyCodeUtils != null)
                        verifyCodeUtils.dismissImageDialog();
                    showErrorToast(error);
                    dismissProgressDialog();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkLoginEnable();
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
                        //                        Logger.d("LoginSuccessModel----》" + response.toString());
                        if ("0".equals(response.getCode())) {
                            showToast(ToastType.COMMON_SUCCESS, "登录成功");
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
                    },
                    error -> {
                        dismissProgressDialog();
                        showErrorToast(error);
                    });
        });
    }

    private void finishActivity() {
        tvLogin.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
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
        if (verifyCodeUtils != null)
            verifyCodeUtils.dismissImageDialog();
    }
}
