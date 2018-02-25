package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.head_view)  HtHeadView mHtHeadView;
    @BindView(R.id.et_content) EditText   mEtContent;

    private String content = "";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initVars();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initViews(savedInstanceState);
    }

    private void initVars() {
        TAG = "意见反馈";
    }

    private void initViews(Bundle savedInstanceState) {
        // 文本内容不为空，才可提交
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.d(s.toString());
                mHtHeadView.setRightTextEnabled(!TextUtils.isEmpty(s.toString()));
            }
        });
        // 提交按钮点击事件
        mHtHeadView.setOnRightClickListener(view -> {
            content = mEtContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.show(mContext, R.string.feedback_empty);
                return;
            }
            submit();
        });
    }

    /**
     * 提交 请求
     */
    private void submit() {
        ProgressDialogUtils.show(mContext, "正在提交……");
        ForumApi.getInstance().userFeedbackPost(content,
                response -> {
                    if (mHtHeadView == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, R.string.feedback_success);
                        finish();
                    } else {
                        ToastUtils.show(mContext, R.string.feedback_fail);
                        mHtHeadView.setRightTextEnabled(true);
                    }
                }, error -> {
                    if (mHtHeadView == null)
                        return;
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            }
        }
    }
}
