package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.MobileVerifyObject;
import com.haitao.model.UserObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.ValidateUtils;
import com.haitao.utils.verifycode.OnVerifyCodeCallBackListener;
import com.haitao.utils.verifycode.VerifyCodeUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.ToastPopuWindow;
import com.haitao.view.dialog.CountryCodeChooseDlg;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.AreaModel;

/**
 * 绑定手机号码
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    public static final int BING_NONE   = 0;//未绑定手机
    public static final int BING_MULTI  = 1;//手机绑定多个账户
    public static final int BING_UPDATE = 2;//修改手机绑定
    public static final int BING_VERIFY = 3;//验证原手机
    public static final int BING_NEW    = 4;//未绑定手机

    private TextView tvTips, btnSubmit;
    private ClearEditText etPhone, etVerify;
    private TextView tvGetVerify;

    //修改绑定时的引导
    private ViewGroup layoutBindGuide;
    private TextView  tvGuide1, tvGuide2, tvGuideNum1, tvGuideNum2;

    private String phone       = "";
    private String mVerifyCode = "";
    private String actionToken = "";


    private int        type = 0;
    private UserObject obj  = null;

    //    private Timer mTimer;
    //    private int mTime = TransConstant.TIMEOUT;

    private VerifyCodeUtils verifyCodeUtils;

    protected IViewContext<MobileVerifyObject, IEntityService<MobileVerifyObject>> commandViewContext = VF.<MobileVerifyObject>getDefault(MobileVerifyObject.class);
    private TextView             mTvArea; // 国家区号显示框
    private View                 mDividerArea; // 国家区号下划线
    private String               mRegionId; // 国家区号
    private CountDownTimer       mCountDownTimer; // 发送验证码倒计时
    private ToastPopuWindow      mPwToast;
    private ArrayList<AreaModel> mCountryList; // 国家区号数据
    private CountryCodeChooseDlg mCountryCodeChooseDlg; // 国家区号选择框

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, Bundle bundle) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        if (null != bundle)
            intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    public static void launch(Context context, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TransConstant.TYPE, type);
        launch(context, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        initVars();
        initView();
        initEvent();
        areaListReq();
    }

    private void initVars() {
        TAG = "绑定手机";
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(TransConstant.TYPE))
                type = bundle.getInt(TransConstant.TYPE, 0);
            if (bundle.containsKey(TransConstant.CODE))
                actionToken = bundle.getString(TransConstant.CODE, "");
            if (bundle.containsKey(TransConstant.AREA_CODE)) {
                mRegionId = bundle.getString(TransConstant.AREA_CODE, "+86");
            } else {
                mRegionId = "+86";
            }
            if (bundle.containsKey(TransConstant.OBJECT))
                obj = (UserObject) bundle.getSerializable(TransConstant.OBJECT);

        }
        mCountryList = new ArrayList<>();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.bind_phone_number);
        layoutBindGuide = getView(R.id.layoutBindGuide);
        tvGuide1 = getView(R.id.tvGuide1);
        tvGuide2 = getView(R.id.tvGuide2);
        tvGuideNum1 = getView(R.id.tvGuideNum1);
        tvGuideNum2 = getView(R.id.tvGuideNum2);
        mTvArea = getView(R.id.tv_area);
        mDividerArea = getView(R.id.divider_area);
        etPhone = getView(R.id.etPhone);
        etVerify = getView(R.id.etVerify);
        tvGetVerify = getView(R.id.tvGetVerify);
        btnSubmit = getView(R.id.btnSubmit);
        tvTips = getView(R.id.tvTips);

        if (type == BING_NONE) {
            btnLeft.setVisibility(View.INVISIBLE);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(R.string.bind_pass);
        } else if (type == BING_MULTI) {
            tvTips.setText(R.string.bind_multi_phone);
        } else if (type == BING_VERIFY) {
            layoutBindGuide.setVisibility(View.VISIBLE);
            obj = UserManager.getInstance().getUser();
            tvTitle.setText(R.string.bind_update);
            // tvPhone.setText("原手机号码");
            btnSubmit.setText(R.string.forget_next);
            etPhone.setHint(CommonUtils.transferPhone(obj.mobile));
            etPhone.setFocusable(false);
            etPhone.setClearIconVisible(false);
            phone = obj.mobile;
            tvGetVerify.setEnabled(true);
            tvGuide1.setSelected(true);
            mTvArea.setVisibility(View.GONE);
            mDividerArea.setVisibility(View.GONE);
            tvGuideNum1.setSelected(true);
            tvGuide2.setSelected(false);
            tvGuideNum2.setSelected(false);
            tvTips.setVisibility(View.GONE);
        } else if (type == BING_UPDATE) {
            layoutBindGuide.setVisibility(View.VISIBLE);
            tvTips.setVisibility(View.GONE);
            tvTitle.setText(R.string.bind_update);
            etPhone.setHint("新手机号码");
            tvTitle.setText(R.string.bind_update);
            btnSubmit.setText(R.string.bind_confirm);
            tvGetVerify.setEnabled(true);
            tvGuide1.setSelected(false);
            tvGuideNum1.setSelected(false);
            tvGuide2.setSelected(true);
            tvGuideNum2.setSelected(true);
        }

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvRight.setOnClickListener(this);
        tvGetVerify.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                phone = s.toString().trim();
                //                tvGetVerify.setEnabled(!"".equals(phone) && ValidateUtils.isPhone(phone));
                btnSubmit.setEnabled(!"".equals(phone) && ValidateUtils.isPhone(phone) && !"".equals(mVerifyCode));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etVerify.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                mVerifyCode = s.toString().trim();
                btnSubmit.setEnabled(!"".equals(phone) && ValidateUtils.isPhone(phone) && !"".equals(mVerifyCode));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 选择国家码
        mTvArea.setOnClickListener(v -> {
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
                        mTvArea.setText(String.format("%s %s", areaModel.getAreaName(), areaModel.getAreaCode()));
                        mRegionId = areaModel.getAreaCode();
                        dialog.hide();
                    });
                }
                mCountryCodeChooseDlg.show();
            } else {
                areaListReq();
                ToastUtils.show(mContext, "正在加载，请稍后...");
            }
        });
    }

    /**
     * 加载国家区号数据
     */
    private void areaListReq() {
        ForumApi.getInstance().commonAreasInfoGet(response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        List<AreaModel> data = response.getData();
                        if (data != null && data.size() > 0) {
                            mCountryList.addAll(data);
                        }
                    }
                },
                this::showErrorToast);
    }

    /**
     * 点击发送验证码
     */
    private void sendVerifyCode() {
        if (obj == null) {
            if (TextUtils.isEmpty(etPhone.getText().toString())) {
                ToastUtils.show(mContext, "请先输入手机号码");
                return;
            }
        }
        etVerify.setText("");
        if (type == BING_VERIFY) {
            sendVerifyCodeReq(TransConstant.VerifyType.VERIFY_NOW, null);
        } else {
            verifyCodeUtils = new VerifyCodeUtils(mContext, null, null, TransConstant.VerifyType.BIND, phone);
            verifyCodeUtils.setmOnVerifyCodeCallBackListener(new OnVerifyCodeCallBackListener() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onSuccess(int type, String code) {
                    sendVerifyCodeReq(TransConstant.VerifyType.BIND, code);
                }

                @Override
                public void onSuccess(UserObject userObject) {

                }
            });
            verifyCodeUtils.getSetting();
        }
    }

    /**
     * 发送验证码-网络请求
     *
     * @param code 图形验证码
     */
    private void sendVerifyCodeReq(String type, String code) {
        ForumApi.getInstance().commonVerifyingCodeMobileSmsPost(mRegionId, phone, type, null, null, code,
                response -> {
                    if ("0".equals(response.getCode())) {
                        startCountDown();
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();
                        mPwToast = new ToastPopuWindow((Activity) mContext, String.format("短信验证码已发送至%s", "\n" + mRegionId + "-" + phone));
                        mPwToast.show();
                    } else if (CodeConstant.CODE_NOT_MATCH.equals(response.getCode())) {
                        verifyCodeUtils.refreshImge();
                        ToastUtils.show(mContext, response.getMsg());
                    } else if ("7005".equals(response.getCode())) {
                        ToastUtils.show(mContext, response.getMsg());
                    } else {
                        if (verifyCodeUtils != null)
                            verifyCodeUtils.dismissImageDialog();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, this::showErrorToast);
    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        tvGetVerify.setEnabled(false);
        if (mCountDownTimer == null) {
            mCountDownTimer = new TimerCountDown(60 * 2000, 1000);
            mCountDownTimer.start();
        } else {
            mCountDownTimer.start();

        }
    }

    class TimerCountDown extends CountDownTimer {

        public TimerCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetVerify.setText(String.format("重新获取%ds", millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {
            tvGetVerify.setEnabled(true);
            tvGetVerify.setText("获取验证码");
        }
    }

    private void bindPhoneReq() {
        ForumApi.getInstance().userNewPhoneNumberBindingPost(mRegionId, etPhone.getText().toString(), mVerifyCode, actionToken,
                response -> {
                    dismissProgressDialog();
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        UserObject obj = UserManager.getInstance().getUser();
                        obj.mobile = etPhone.getText().toString();
                        UserManager.getInstance().setUser(obj);
                        ToastUtils.show(mContext, "手机号码更改绑定成功！");
                        setResult(TransConstant.IS_LOGIN);
                        finish();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    showErrorToast(error);
                    dismissProgressDialog();
                });
    }

    /**
     * 验证当前手机号-网络请求
     */
    private void verifyCurrentPhoneReq() {
        ProgressDialogUtils.show(mContext, R.string.operationg);
        ForumApi.getInstance().userCurrentPhoneNumberCheckingPost(mVerifyCode,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        if (response.getData() != null && !TextUtils.isEmpty(response.getData().getActionToken())) {
                            // 成功
                            Bundle bundle = new Bundle();
                            bundle.putInt(TransConstant.TYPE, BindPhoneActivity.BING_UPDATE);
                            bundle.putString(TransConstant.CODE, response.getData().getActionToken());
                            BindPhoneActivity.launch(mContext, bundle);
                            finish();
                        }
                    } else {
                        // 错误
                        //                        tvGetVerify.setEnabled(true);
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    ProgressDialogUtils.dismiss();
                    showErrorToast(error);
                    //                    tvGetVerify.setEnabled(true);
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                finish();
                break;
            case R.id.tvGetVerify:
                //                tvGetVerify.setEnabled(false);
                sendVerifyCode();
                break;
            case R.id.btnSubmit:
                if (type == BING_VERIFY)
                    verifyCurrentPhoneReq();
                else
                    bindPhoneReq();
                break;
            default:
                break;
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
