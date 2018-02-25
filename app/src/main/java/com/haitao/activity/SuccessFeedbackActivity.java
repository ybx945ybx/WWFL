package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.view.HtHeadView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haitao.activity.SuccessFeedbackActivity.SuccessType.ORDER_LOST;
import static com.haitao.activity.SuccessFeedbackActivity.SuccessType.WITHDRAW;

/**
 * 丢单成功 / 提现成功
 *
 * @author 陶声
 * @since 2017/08/10
 */
public class SuccessFeedbackActivity extends BaseActivity {

    @BindView(R.id.hv_title)      HtHeadView mHvTitle;   // 标题
    @BindView(R.id.tv_info_title) TextView   mTvTitle;   // 提示标题
    @BindView(R.id.tv_info_text)  TextView   mTvInfo;    // 提示信息

    @IntDef({WITHDRAW, ORDER_LOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SuccessType {

        int WITHDRAW = 0;   // 提现

        int ORDER_LOST = 1; // 丢单
    }

    @SuccessType private int mSuccessType; // 页面类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_lost_feedback_success);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            mSuccessType = intent.getIntExtra(TransConstant.TYPE, SuccessType.WITHDRAW);
        }
        TAG = (mSuccessType == SuccessType.WITHDRAW) ? getString(R.string.withdraw) : getString(R.string.order_lost_feedback);
    }

    private void initViews(Bundle savedInstanceState) {
        // 标题
        mHvTitle.setCenterText(mSuccessType == SuccessType.WITHDRAW ?
                getString(R.string.withdraw) : getString(R.string.order_lost_feedback));
        // 提示标题
        mTvTitle.setText(mSuccessType == SuccessType.WITHDRAW ?
                getString(R.string.withdraw_apply_success) : getString(R.string.order_lost_feedback_success));
        // 提示信息
        mTvInfo.setText(mSuccessType == SuccessType.WITHDRAW ?
                getString(R.string.withdraw_success_info) : getString(R.string.order_lost_feedback_success_info));
    }

    /**
     * 跳转到本页
     *
     * @param context mContext
     * @param type    类型
     */
    public static void launch(Context context, @SuccessType int type) {
        Intent intent = new Intent(context, SuccessFeedbackActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        ((Activity) context).startActivityForResult(intent, TransConstant.SUCCESS);
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        finish();
    }
}
