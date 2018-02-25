package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收货地址管理页
 *
 * @author 陶声
 * @since 2017-07-11
 */
public class AddressManagementActivity extends BaseActivity {

    @BindView(R.id.tvTitle) TextView mTvTitle;
    @BindView(R.id.tvRight) TextView mTvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = getString(R.string.address_management);
    }

    private void initViews(Bundle savedInstanceState) {
        mTvTitle.setText(getString(R.string.address_management));
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("保存");
    }

    private void initData() {

    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, AddressManagementActivity.class);
        context.startActivity(intent);
    }

    /**
     * 返回上一页
     */
    @OnClick(R.id.btnLeft)
    public void clickBack() {
        finish();
    }

    /**
     * 保存
     */
    @OnClick(R.id.tvRight)
    public void clickSave() {
        ToastUtils.show(mContext, "保存");
    }
}
