package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.haitao.R;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.CommentChangeEvent;
import com.haitao.utils.SoftKeyboardStateHelper;
import com.haitao.utils.ToastUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用评论页面  灰色透明背景
 */
public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.etContent)  ClearEditText   etContent;
    @BindView(R.id.img_avatar) CustomImageView ivAvator;
    @BindView(R.id.iv_send)    ImageView       ivSend;

    //回复的用户名，临时变量
    String username = "";
    //评论内容
    String content  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        initVars();
        initView();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("userName");
            content = intent.getStringExtra("content");
        }
    }

    private void initView() {
        if (UserManager.getInstance().isLogin()) {
            if (TextUtils.isEmpty(UserManager.getInstance().getUser().avatar)){
                getUserInfo();
            }else {
                ivAvator.setImageURI(UserManager.getInstance().getUser().avatar);
            }
        }

        etContent.setShowClear(false);

        if (!TextUtils.isEmpty(username)) {
            etContent.setHint(String.format("回复 %s ", username));
        }

        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
        }

        etContent.requestFocus();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = etContent.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etContent, InputMethodManager.SHOW_FORCED);

        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(this, findViewById(R.id.activity_main_layout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                Logger.d("键盘关闭");
                back();
            }
        });
    }

    private void getUserInfo() {
        ForumApi.getInstance().userAccountInfoGet(
                response -> {
                    if ("0".equals(response.getCode()))
                        ivAvator.setImageURI(response.getData().getAvatar());
                },
                error -> {

                });
    }

    @OnClick({R.id.activity_main_layout, R.id.iv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_layout:
                // 收起键盘
                InputMethodManager imm = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
                break;
            case R.id.iv_send:
                commit();
                break;

        }
    }

    private void back() {
        EventBus.getDefault().post(new CommentChangeEvent(etContent.getText().toString(), false));
        onBackPressed();
    }

    private void commit() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            ToastUtils.show(this, R.string.store_comment_tips);
            return;
        }
        EventBus.getDefault().post(new CommentChangeEvent(etContent.getText().toString(), true));
        finish();
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String userName, String content) {
        Logger.d("lunch");
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("content", content);
        context.startActivity(intent);
        ((BaseActivity) context).overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
    }

}
