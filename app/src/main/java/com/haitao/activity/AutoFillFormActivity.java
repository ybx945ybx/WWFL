package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.haitao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自动填充表单页
 */
public class AutoFillFormActivity extends BaseActivity {

    @BindView(R.id.tvTitle) TextView mTvTitle; // 标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_fill_form);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = "自动填充表单";
    }

    private void initViews(Bundle savedInstanceState) {
        mTvTitle.setText(getString(R.string.auto_fill_form));
    }

    private void initData() {

    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, AutoFillFormActivity.class);
        context.startActivity(intent);
    }

    /**
     * 返回上一页
     */
    @OnClick(R.id.btnLeft)
    public void clickLeft() {
        finish();
    }
}
