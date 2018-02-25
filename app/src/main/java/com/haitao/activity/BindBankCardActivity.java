package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.event.CardBindChangeEvent;
import com.haitao.utils.LengthFilter;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtEditTextView;
import com.haitao.view.ToastPopuWindow;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定银联卡
 */
public class BindBankCardActivity extends BaseActivity implements HtEditTextView.textChangedListener {

    @BindView(R.id.et_phone)        HtEditTextView etPhone;            // 手机号
    @BindView(R.id.et_card_number)  HtEditTextView etCardNumber;       // 银行卡号
    @BindView(R.id.et_name)         HtEditTextView etAccount;          // 持卡人姓名
    @BindView(R.id.tv_commit)       TextView       tvCommit;           // 提交绑定
    @BindView(R.id.tv_bind_declare) TextView       tvBindDeclare;      // 绑定说明

    private ToastPopuWindow toastPopuWindow;

    private final        int MY_SCAN_REQUEST_CODE          = 1000;
    private static final int REQUEST_CODE_READ_PHONE_STATE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initView();
        initEvent();

    }

    private void initVars() {
        if (UserManager.getInstance().isLogin()) {
            if (!TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
                etPhone.setText(UserManager.getInstance().getUser().mobile);
            } else {
                FirstBindPhoneActivity.launch(this);
            }
        }
    }

    private void initView() {
        etCardNumber.setInputFilter(new InputFilter[]{new LengthFilter(mContext, 23)});

    }

    private void initEvent() {
        etCardNumber.setOnRightImgClickListener(view -> {
            CaptureActivity.launch(this, MY_SCAN_REQUEST_CODE, CaptureActivity.ScanType.BANK_CARD);
        });

        // 银行卡号每隔四位空格
        etCardNumber.clearEditText.addTextChangedListener(new myWatcher());

        etPhone.addTextChangedListener(this);
        etAccount.addTextChangedListener(this);
    }

    @OnClick({R.id.tv_commit, R.id.tv_bind_declare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    toastPopuWindow = ToastPopuWindow.makeText((BaseActivity) mContext, getResources().getString(R.string.please_input_telephone)).parentView(tvCommit);
                    toastPopuWindow.show();
                    return;
                }
                if (TextUtils.isEmpty(etCardNumber.getText().toString().trim())) {
                    toastPopuWindow = ToastPopuWindow.makeText((BaseActivity) mContext, getResources().getString(R.string.please_input_card_number)).parentView(tvCommit);
                    toastPopuWindow.show();
                    return;
                }
                if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
                    toastPopuWindow = ToastPopuWindow.makeText((BaseActivity) mContext, getResources().getString(R.string.please_input_card_owner)).parentView(tvCommit);
                    toastPopuWindow.show();
                    return;
                }
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE_READ_PHONE_STATE);
                } else {
                    doBindCard();
                }
                break;
            case R.id.tv_bind_declare:
                BindBankCardDeclareActivity.launch(mContext, 1, "https://appv6.55haitao.com/template/offlinerebate/bankins.htm");
                //                WebActivity.launch(mContext, "", "http://appv6.dev.55haitao.com/template/offlinerebate/bankins.htm");
                break;
        }
    }

    // 提交绑定
    private void doBindCard() {
        String imei       = getImei();
        String cardNumber = etCardNumber.getText().toString().trim().replaceAll(" ", "");
        // 去授权页面
        BindBankCardDeclareActivity.launch(mContext, 2, cardNumber, etAccount.getText().toString().trim(), imei, "https://appv6.55haitao.com/template/offlinerebate/bankins_authority.htm");

    }

    // 获取手机的imei
    private String getImei() {
        String imei = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                if (slot == 0) {
                    imei = telephonyManager.getDeviceId(slot);
                } else {
                    imei = imei + "," + telephonyManager.getDeviceId(slot);
                }
            }
        } else {
            imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        Logger.d("imei----->" + imei);
        return imei;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doBindCard();
                } else {
                    ToastUtils.show(mContext, "请先打开访问手机权限");
                }
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkCommitEnable();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 提交绑定按钮是否可点击
     */
    private void checkCommitEnable() {
        //        if (etPhone.isHasText() && etCardNumber.isHasText() && etAccount.isHasText()) {
        if (etCardNumber.isHasText() && etCardNumber.getText().toString().length() >= 13 && etCardNumber.getText().toString().startsWith("62") && etAccount.isHasText()) {
            tvCommit.setEnabled(true);
        } else {
            tvCommit.setEnabled(false);
        }
    }

    /**
     * 授权页面成功则接收事件关闭此页面
     *
     * @param event
     */
    @Subscribe
    public void onCardBindChangeEvent(CardBindChangeEvent event) {
        finish();
    }

    /**
     * 跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, BindBankCardActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toastPopuWindow != null && toastPopuWindow.isShowing()) {
            toastPopuWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MY_SCAN_REQUEST_CODE) {
                Logger.d("银行卡号 = " + data.getStringExtra("cardNum"));
                etCardNumber.setText(data.getStringExtra("cardNum"));
                etCardNumber.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CharSequence charSequence = etCardNumber.getText();
                        if (charSequence instanceof Spannable) {
                            Spannable spanText = (Spannable) charSequence;
                            Selection.setSelection(spanText, charSequence.length());
                        }
                    }
                }, 100);
            }
        } else if (requestCode == TransConstant.IS_LOGIN) {
            if (HtApplication.isLogin()) {
                if (!TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
                    etPhone.setText(UserManager.getInstance().getUser().mobile);
                } else {
                    finish();
                }
            } else {
                finish();
            }
        }
    }

    /**
     * 银行卡号每四位空格
     */
    class myWatcher implements TextWatcher {
        int     beforeTextLength = 0;
        int     onTextLength     = 0;
        boolean isChanged        = false;

        int location = 0;// 记录光标的位置
        private char[] tempChar;
        private StringBuffer buffer = new StringBuffer();
        int konggeNumberB = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || onTextLength <= 3
                    || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            konggeNumberB = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    konggeNumberB++;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {
                location = etCardNumber.clearEditText.getSelectionEnd();
                int index = 0;
                while (index < buffer.length()) {
                    if (buffer.charAt(index) == ' ') {
                        buffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < buffer.length()) {
                    if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                        buffer.insert(index, ' ');
                        konggeNumberC++;
                    }
                    index++;
                }

                if (konggeNumberC > konggeNumberB) {
                    location += (konggeNumberC - konggeNumberB);
                }

                tempChar = new char[buffer.length()];
                buffer.getChars(0, buffer.length(), tempChar, 0);
                String str = buffer.toString();
                if (location > str.length()) {
                    location = str.length();
                } else if (location < 0) {
                    location = 0;
                }

                etCardNumber.clearEditText.setText(str);

                Editable etable = etCardNumber.clearEditText.getText();
                Selection.setSelection(etable, location);
                isChanged = false;

            }

            checkCommitEnable();

        }

    }
}
