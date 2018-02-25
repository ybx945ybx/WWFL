package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.imp.URILocatorHelper;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.ClearEditText;

/**
 * 设置页面
 */
public class SettingUrlActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup    radioGroup;
    private ClearEditText etUrl, etSwaggerUrl;
    private TextView btnSubmit;
    private String url = "", swaggerUrl = "";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingUrlActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_url);
        TAG = "设置";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText("设置环境url");
        radioGroup = getView(R.id.radioGroup);
        etUrl = getView(R.id.etUrl);
        etSwaggerUrl = getView(R.id.etSwaggerUrl);
        btnSubmit = getView(R.id.btnSubmit);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnSubmit.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbDev:
                    etUrl.setText(Constant.DEBUG_URL);
                    etSwaggerUrl.setText(Constant.SWAGGER_DEBUG_URL);
                    break;
                case R.id.rbQa:
                    etUrl.setText(Constant.QA_URL);
                    etSwaggerUrl.setText(Constant.SWAGGER_QA_URL);
                    break;
                case R.id.rbRelease:
                    etUrl.setText(Constant.RELEASE_URL);
                    etSwaggerUrl.setText(Constant.SWAGGER_PROD_URL);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String url        = (String) SPUtils.get(mContext, SPConstant.SETTING_URL, "");
        String swaggerUrl = (String) SPUtils.get(mContext, SPConstant.SWAGGER_SETTING_URL, "");
        etUrl.setText(url);
        etSwaggerUrl.setText(swaggerUrl);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                url = etUrl.getText().toString().trim();
                swaggerUrl = etSwaggerUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url) || TextUtils.isEmpty(swaggerUrl)) {
                    ToastUtils.show(mContext, "请设置url");
                    return;
                }
                SPUtils.put(mContext, SPConstant.SETTING_URL, url);
                SPUtils.put(mContext, SPConstant.SWAGGER_SETTING_URL, swaggerUrl);
                //初始化URI
                URILocatorHelper.initUrlBase(url);
                URILocatorHelper.init();
                ForumApi.getInstance().setBaseUrl(swaggerUrl);
                setResult(TransConstant.REFRESH);
                finish();
                break;
            default:
                break;
        }
    }

}
