package com.haitao.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.utils.DeviceUtil;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 验证码弹窗
 * <p>
 * Created by a55 on 2018/1/26.
 */

public class VerifyCodeDlg extends Dialog {

    @BindView(R.id.iv_close)   ImageView       ivClose;         // 关闭按钮
    @BindView(R.id.ivImage)    CustomImageView ivCodeImg;       // 验证码图形
    @BindView(R.id.etPassword) EditText        etCode;          // 验证码输入框
    @BindView(R.id.tv_confirm) TextView        tvConfirm;       // 确定按钮

    private OnClickListener mOnClickListener;
    private String          imageUrl;
    private Context         mContext;

    public VerifyCodeDlg(@NonNull Context context) {
        super(context);
        mContext = context;
        initDlg(context);
    }

    private void initDlg(Context context) {
        imageUrl = String.format(ForumApi.getInstance().getBasePath() + "/common/verifying_code/image?device_id=%s", DeviceUtil.getDeviceId(HtApplication.getInstance()));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            // 顶部透明
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View layout   = LayoutInflater.from(context).inflate(R.layout.dialog_verify, null);
        int  margin   = DensityUtil.dip2px(context, 32); // 32dp
        int  dlgWidth = ScreenUtils.getScreenWidth(context) - margin * 2;
        setContentView(layout, new LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this);

        ImageLoaderUtils.showOnlineImage(imageUrl + "&random=" + String.valueOf(System.currentTimeMillis()), ivCodeImg);
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvConfirm.setEnabled(!TextUtils.isEmpty(s.toString().trim()) ? true : false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setCancelable(false);

        // 延迟200ms，弹出输入法
        etCode.postDelayed(() -> {
            etCode.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etCode, 0);
        }, 200);
    }

    @OnClick({R.id.iv_close, R.id.ivImage, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:                     // 关闭
                if (null != mOnClickListener) {
                    mOnClickListener.onCloseClick();
                } else {
                    dismiss();
                }
                break;
            case R.id.ivImage:                      // 点击验证码图形刷新
                ImageLoaderUtils.showOnlineImage(imageUrl + "&random=" + String.valueOf(System.currentTimeMillis()), ivCodeImg);
                break;
            case R.id.tv_confirm:                   // 确定
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etCode.getWindowToken(), 0);

                String code = etCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show(mContext, "请输入验证码");
                    return;
                }

                if (null != mOnClickListener) {
                    mOnClickListener.onConfirmClick(code);
                } else {
                    dismiss();
                }
                break;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        //  关闭按钮监听
        void onCloseClick();

        // 确定按钮监听
        void onConfirmClick(String code);
    }

    // 刷新界面
    public void refresh() {
        ImageLoaderUtils.showOnlineImage(imageUrl + "&random=" + String.valueOf(System.currentTimeMillis()), ivCodeImg);
        etCode.setText("");
        // 延迟200ms，弹出输入法
        etCode.postDelayed(() -> {
            etCode.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etCode, 0);
        }, 200);

    }
}
