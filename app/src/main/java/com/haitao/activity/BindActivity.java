package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.model.UserObject;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;

/**
 * 第三方登录绑定账号引导
 */
public class BindActivity extends BaseActivity implements View.OnClickListener {
    private HtHeadView      htHeadView;
    private CustomImageView ivAvator;
    private TextView        tvName;
    private TextView        tvBindSelect;
    private TextView        tvRegisterSelect;
    private TextView        tvNext;
    private UserObject      obj;
    private String          type;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, UserObject obj) {
        Intent intent = new Intent(context, BindActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.OBJECT, obj);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "社交登录 - 绑定提示";
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            obj = (UserObject) bundle.getSerializable(TransConstant.OBJECT);
            type = "1".equals(obj.type) ? "微博" : "2".equals(obj.type) ? "QQ" : "微信";
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        htHeadView = getView(R.id.head_view);
        ivAvator = getView(R.id.iv_my_avator);
        tvName = getView(R.id.tv_name);
        tvBindSelect = getView(R.id.tv_bind);
        tvBindSelect.setSelected(true);
        tvRegisterSelect = getView(R.id.tv_register);
        tvNext = getView(R.id.tv_next);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvBindSelect.setOnClickListener(this);
        tvRegisterSelect.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        htHeadView.setCenterText("使用" + type + "登录");
        ivAvator.setImageURI(obj.avatar);
        tvName.setText(obj.username);
        tvBindSelect.setText("将我的" + type + "账号绑定55海淘账号");
        tvRegisterSelect.setText("直接用" + type + "账号注册");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bind:
                tvBindSelect.setSelected(true);
                tvRegisterSelect.setSelected(false);
                break;
            case R.id.tv_register:
                tvBindSelect.setSelected(false);
                tvRegisterSelect.setSelected(true);
                break;
            case R.id.tv_next:
                if (tvRegisterSelect.isSelected()) {
                    CompleteUserInfoActivity.launch(mContext, obj, 1);
                } else {
                    BindAccountActivity.launch(mContext, obj);
                }
                break;
            default:
                break;
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
}
