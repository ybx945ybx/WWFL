package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.UserManager;
import com.haitao.utils.KFUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 丢单追踪列表
 *
 * @author 陶声
 * @since 201-18
 */
public class OrderLostTraceActivity extends BaseActivity {

    @BindView(R.id.tvRight)    TextView  mTvRight;
    @BindView(R.id.tvTitle)    TextView  mTvTitle;
    @BindView(R.id.vp_content) ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_lost_trace);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initData();
    }

    /**
     * 跳转到本页面
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, OrderLostTraceActivity.class);
        context.startActivity(intent);
    }

    private void initVars() {
        TAG = getString(R.string.order_lost_trace);
    }

    private void initViews() {
        mTvTitle.setText(getString(R.string.order_lost_trace));
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.online_service));
    }

    private void initData() {

    }

    /**
     * 点击 返回上页
     */
    @OnClick(R.id.btnLeft)
    public void onMBtnLeftClicked() {
        finish();
    }

    /**
     * 点击 在线客服
     */
    @OnClick(R.id.tvRight)
    public void onMTvRightClicked() {
        if (!UserManager.getInstance().isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        KFUtils.startChat(OrderLostTraceActivity.this);
    }

    /**
     * 点击 丢单反馈
     */
    @OnClick(R.id.tv_order_lost_feedback)
    public void onMTvOrderLostFeedbackClicked() {
        OrderLostFeedbackActivity.launch(mContext);
    }
}
