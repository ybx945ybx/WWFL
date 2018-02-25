package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.KFUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.HtItemTextView;
import com.haitao.view.NewWithdrawProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.WithdrawingRecordDetailModel;

/**
 * 提现详情
 */
public class WithdrawDetailActivity extends BaseActivity {

    @BindView(R.id.hv_title)             HtHeadView              mHvTitle;           // 标题
    @BindView(R.id.hitv_date)            HtItemTextView          mHitvDate;          // 日期
    @BindView(R.id.hitv_withdraw_type)   HtItemTextView          mHitvWithdrawType;  // 提现方式
    @BindView(R.id.hitv_account_name)    HtItemTextView          mHitvAccountName;   // 账号姓名
    @BindView(R.id.hitv_status)          HtItemTextView          mHitvStatus;        // 状态
    @BindView(R.id.hitv_withdraw_amount) HtItemTextView          mHitvWithdrawAmount;// 提现金额
    @BindView(R.id.pv_withdraw)          NewWithdrawProgressView mPvWithdraw;        // 提现进度
    @BindView(R.id.tv_note_label)        TextView                mTvNoteLabel;       // 备注标签
    @BindView(R.id.tv_note)              TextView                mTvNote;            // 备注内容
    @BindView(R.id.tv_online_service)    TextView                mTvOnlineService;   // 在线客服

    private String mId = "";    // ID

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, WithdrawDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_detail);
        ButterKnife.bind(this);
        initVars();
        initView();
        loadData();
    }

    private void initVars() {
        TAG = getString(R.string.cash_detail);
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(TransConstant.ID)) {
            mId = intent.getStringExtra(TransConstant.ID);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHvTitle.setOnRightClickListener(view -> KFUtils.startChat((Activity) mContext));

    }

    /**
     * 加载数据
     */
    private void loadData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        // 网络请求
        ForumApi.getInstance().userWithdrawingRecordIdGet(mId,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode()) && response.getData() != null) {
                        WithdrawingRecordDetailModel withdrawDetail = response.getData();
                        // 失败的3种状态 显示客服
                        String status = withdrawDetail.getStatus();
                        if (!TextUtils.isEmpty(status)) {
                            Integer statusCode = Integer.valueOf(status);
                            if (statusCode >= 4 && statusCode <= 6) {
                                // 显示客服
                                mTvOnlineService.setVisibility(View.VISIBLE);
                            }
                            // 提现进度
                            mPvWithdraw.setProgress(status);
                        }
                        // 日期
                        mHitvDate.setRightText(withdrawDetail.getWithdrawingTime());
                        // 提现方式
                        //                        StringBuilder str = new StringBuilder().append(withdrawDetail.getModeName());
                        //                        if (!TextUtils.isEmpty(withdrawDetail.getBankName())) {
                        //                            str.append("（").append(withdrawDetail.getBankName()).append("）");
                        //                        }
                        //                        str.append(" ").append(withdrawDetail.getAccount());
                        //                        mHitvWithdrawType.setRightText(str.toString());
                        mHitvWithdrawType.setRightText(withdrawDetail.getModeName() + " " + withdrawDetail.getAccount());
                        // 账号姓名
                        mHitvAccountName.setRightText(withdrawDetail.getRealname());
                        // 状态
                        if (!TextUtils.isEmpty(withdrawDetail.getStatusView())) {
                            mHitvStatus.setVisibility(View.VISIBLE);
                            mHitvStatus.setRightText(withdrawDetail.getStatusView());
                        } else {
                            mHitvStatus.setVisibility(View.GONE);
                        }
                        // 提现金额
                        mHitvWithdrawAmount.setRightText(withdrawDetail.getAmountView());
                        // 备注
                        if (!TextUtils.isEmpty(withdrawDetail.getComment())) {
                            mTvNote.setVisibility(View.VISIBLE);
                            mTvNoteLabel.setVisibility(View.VISIBLE);
                            mTvNote.setText(withdrawDetail.getComment());
                            mTvNote.setOnLongClickListener(v -> {
                                copyToClipboard(mContext, withdrawDetail.getComment());
                                return true;
                            });
                        }
                    }
                },
                this::showErrorToast);
    }

    /**
     * 在线客服
     */
    @OnClick(R.id.tv_online_service)
    public void onClickService() {
        if (!UserManager.getInstance().isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        KFUtils.startChat(this);
    }
}
