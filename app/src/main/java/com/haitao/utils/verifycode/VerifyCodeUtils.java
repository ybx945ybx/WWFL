package com.haitao.utils.verifycode;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.sdk.GT3GeetestUrl;
import com.example.sdk.GT3GeetestUtils;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.UserObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.dialog.VerifyCodeDlg;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 图形验证码工具类
 */
public class VerifyCodeUtils {
    public static final String TAG           = "VerifyCodeUtils";
    public static       String captchaURL    = ForumApi.getInstance().getBasePath() + "/common/geetestcaptchaparams";
    public static final String smsValidate   = "/common/verifying_code/mobile/sms";
    public static final String loginValidate = "/user/mAccount/login";
    public static       String validateURL   = "";

    private Context mContext;
    private String  mAccount, mPwd, mType, mPhone;
    private VerifyCodeDlg     verifyCodeDlg;
    private GT3GeetestUtils   gt3GeetestUtils;
    private DialogResultModel dialogResult;
    private String gtResult = "";

    public OnVerifyCodeCallBackListener mOnVerifyCodeCallBackListener;

    public VerifyCodeUtils(Context context, String account, String password, String type, String phone) {
        mContext = context;
        mAccount = account;
        mPwd = password;
        mType = type;
        mPhone = phone;
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            validateURL = ForumApi.getInstance().getBasePath() + loginValidate;
        } else if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(phone)) {
            validateURL = ForumApi.getInstance().getBasePath() + smsValidate;
        }
    }

    public void setmOnVerifyCodeCallBackListener(OnVerifyCodeCallBackListener onVerifyCodeCallBackListener) {
        mOnVerifyCodeCallBackListener = onVerifyCodeCallBackListener;
    }

    public void getSetting() {
        ProgressDialogUtils.show(mContext, "数据处理中……");
        ForumApi.getInstance().settingsSystemGet(response -> {
            ProgressDialogUtils.dismiss();
            if ("0".equals(response.getCode())) {
                if (null != response.getData()) {
                    if ("0".equals(response.getData().getVerifyType())) {
                        // 无需验证
                        if (null != mOnVerifyCodeCallBackListener)
                            mOnVerifyCodeCallBackListener.onSuccess(0, null);
                    } else if ("1".equals(response.getData().getVerifyType())) {
                        // 图形验证码
                        mHandler.sendEmptyMessage(1);
                    } else if ("2".equals(response.getData().getVerifyType())) {
                        // 极验验证
                        mHandler.sendEmptyMessage(2);
                    }
                }

            } else {

            }
        }, error -> {
            if (error.getCause() instanceof java.net.UnknownHostException) {
                ToastUtils.show(mContext, "网络异常");
            } else {
                ToastUtils.show(mContext, error.getMessage());
            }
            ProgressDialogUtils.dismiss();
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    loadImageVerify();
                    break;
                case 2:
                    loadGTVerify();
                    break;
            }
        }
    };

    /**
     * 加载图形验证码
     */
    public void loadImageVerify() {
        verifyCodeDlg = new VerifyCodeDlg(mContext);
        verifyCodeDlg.setOnClickListener(new VerifyCodeDlg.OnClickListener() {
            @Override
            public void onCloseClick() {
                if (null != mOnVerifyCodeCallBackListener) {
                    mOnVerifyCodeCallBackListener.onError();
                }
                verifyCodeDlg.dismiss();
            }

            @Override
            public void onConfirmClick(String code) {
                if (null != mOnVerifyCodeCallBackListener) {
                    mOnVerifyCodeCallBackListener.onSuccess(1, code);
                }
            }
        });
        verifyCodeDlg.show();
    }

    // 刷新验证码图片
    public void refreshImge() {
        if (verifyCodeDlg != null)
            verifyCodeDlg.refresh();
    }

    public void dismissImageDialog() {
        if (null != verifyCodeDlg) {
            verifyCodeDlg.dismiss();
        }
    }

    /**
     * 极验
     */
    public void loadGTVerify() {
        // 设置二次验证的URL，需替换成自己的服务器URL
        new GT3GeetestUrl().setCaptchaURL(captchaURL);
        new GT3GeetestUrl().setValidateURL(validateURL);
        Logger.d("captchaURL:" + captchaURL);
        Logger.d("validateURL:" + validateURL);
        gt3GeetestUtils = new GT3GeetestUtils(mContext);
        gt3GeetestUtils.gtDologo();//加载验证码之前判断有没有logo
        gt3GeetestUtils.setGtListener(new GT3GeetestUtils.GT3Listener() {
            //点击验证码的关闭按钮来关闭验证码
            @Override
            public void gt3CloseDialog() {
                ToastUtils.show(mContext, "验证已被取消");
                if (null != mOnVerifyCodeCallBackListener) {
                    mOnVerifyCodeCallBackListener.onError();
                }
            }

            //点击屏幕关闭验证码
            @Override
            public void gt3CancelDialog() {
                ToastUtils.show(mContext, "验证已被取消");
                if (null != mOnVerifyCodeCallBackListener) {
                    mOnVerifyCodeCallBackListener.onError();
                }
            }

            //验证码加载准备完成
            @Override
            public void gt3DialogReady() {

            }

            //往第一个请求中添加头部数据
            @Override
            public Map<String, String> captchaHeaders() {
                Map<String, String> map = new HashMap<String, String>();
                return map;
                //return null;
            }

            //拿到第一个url返回的数据
            @Override
            public void gt3FirstResult(JSONObject jsonObject) {
                Logger.d("gt3FirstResult:" + (null != jsonObject ? jsonObject.toString() : ""));
            }

            //拿到验证返回的结果,此时还未进行二次验证
            @Override
            public void gt3GetDialogResult(String result) {
                Logger.d("gt3GetDialogResult:" + result);
                if (!TextUtils.isEmpty(result)) {
                    dialogResult = JSON.parseObject(result, DialogResultModel.class);
                }

            }

            //往二次验证里面put数据，是map类型,注意map的键名不能是以下三个：geetest_challenge，geetest_validate，geetest_seccode
            @Override
            public Map<String, String> gt3SecondResult() {
                Map<String, String> map = new HashMap<String, String>();
                if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPwd)) {
                    map.put("mAccount", mAccount);
                    map.put("mPwd", mPwd);
                } else if (!TextUtils.isEmpty(mType) && !TextUtils.isEmpty(mPhone)) {
                    map.put("mType", mType);
                    map.put("phone_number", mPhone);
                }

                if (null != dialogResult) {
                    map.put("geetest_challenge", dialogResult.geetest_challenge);
                    map.put("geetest_seccode", dialogResult.geetest_seccode);
                    map.put("geetest_validate", dialogResult.geetest_validate);
                }
                Logger.d("map:" + map.toString());
                return map;
            }

            //二次验证请求之后的结果
            @Override
            public void gt3DialogSuccessResult(String result) {
                Logger.d("gt3DialogSuccessResult:" + result);
                gtResult = result;
            }

            //验证码验证成功
            @Override
            public void gt3DialogSuccess() {
                if (!TextUtils.isEmpty(gtResult)) {
                    ResponseModel<String> responseModel = JSON.parseObject(gtResult, new TypeReference<ResponseModel<String>>() {
                    });
                    if ("0".equals(responseModel.code)) {
                        if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPwd)) {
                            ResponseModel<UserObject> userObjectResponseModel = JSON.parseObject(gtResult, new TypeReference<ResponseModel<UserObject>>() {
                            });
                            userObjectResponseModel.data.ht_token = userObjectResponseModel.data.token;
                            if (null != mOnVerifyCodeCallBackListener) {
                                mOnVerifyCodeCallBackListener.onSuccess(userObjectResponseModel.data);
                            }
                        } else if (!TextUtils.isEmpty(mType) && !TextUtils.isEmpty(mPhone)) {

                            if (null != mOnVerifyCodeCallBackListener) {
                                mOnVerifyCodeCallBackListener.onSuccess();
                            }
                        }
                    } else {
                        ToastUtils.show(mContext, responseModel.msg);
                        if (null != mOnVerifyCodeCallBackListener) {
                            mOnVerifyCodeCallBackListener.onError();
                        }
                    }

                }
            }

            @Override
            public void gt3DialogOnError() {
                Logger.d("gt3DialogOnError:");
                ToastUtils.show(mContext, "验证未通过 请重试");
                if (null != mOnVerifyCodeCallBackListener) {
                    mOnVerifyCodeCallBackListener.onError();
                }
            }


        });
        //验证码正式开始
        gt3GeetestUtils.getGeetest();
    }
}
