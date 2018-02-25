package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haitao.R;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.RebateDetailModel;

/**
 * 返利详情
 */
public class RebateDetailActivity extends BaseActivity {
    @BindView(R.id.item_date)          HtItemView mItemDate;          // 日期
    @BindView(R.id.item_type)          HtItemView mItemType;          // 类型
    @BindView(R.id.item_status)        HtItemView mItemStatus;        // 状态
    @BindView(R.id.item_rebate_amount) HtItemView mItemRebateAmount;  // 返利金额
    @BindView(R.id.item_note)          HtItemView mItemNote;          // 备注

    private String mId; // 返利Id

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, RebateDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebate_detail);
        ButterKnife.bind(this);

        initVars();
        initData();
    }

    private void initVars() {
        TAG = "返利详情";
        Intent intent = getIntent();
        if (null != intent) {
            // 获取返利Id
            if (intent.hasExtra("id")) {
                mId = intent.getStringExtra("id");
            }
        }
    }

    private void initData() {
        ForumApi.getInstance().userRebateIdDetailGet(mId,
                response -> {
                    if (mItemDate == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        RebateDetailModel rebateDetail = response.getData();
                        if (null != rebateDetail) {
                            mItemDate.setContent(rebateDetail.getEffectiveTime());
                            mItemType.setContent(rebateDetail.getTypeView());
                            mItemStatus.setContent(rebateDetail.getStatusView());
                            mItemRebateAmount.setContent(rebateDetail.getRebateAmountView());
                            mItemNote.setContent(rebateDetail.getComment());
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                }, error -> {
                    if (mItemDate == null)
                        return;
                    showErrorToast(error);
                });
    }
}
