package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.WithdrawTypeSelectAdapter;
import com.haitao.view.FullListView;
import com.haitao.view.HtHeadView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.UserWithdrawingModeModel;

/**
 * 提现方式选择 - Dialog
 *
 * @author 陶声
 * @since 2018-01-29
 */
public class WithdrawTypeSelectBsDlg extends BottomSheetDialog {

    @BindView(R.id.ht_headview) HtHeadView   mHtHeadview;   // 标题
    @BindView(R.id.lv_content)  FullListView mLvContent;    // 列表

    private WithdrawTypeSelectAdapter mAdapter;
    private OnTypeSelectCallback      mOnTypeSelectCallback;

    public WithdrawTypeSelectBsDlg(@NonNull Context context, List<UserWithdrawingModeModel> data, String accountId) {
        super(context);
        initDlg(context, data, accountId);
    }

    private void initDlg(final Context context, List<UserWithdrawingModeModel> modelList, String accountId) {
        View layout = View.inflate(context, R.layout.dlg_withdraw_type_select, null);
        setContentView(layout);
        ButterKnife.bind(this);

        mHtHeadview.setOnLeftClickListener(view -> dismiss());
        mAdapter = new WithdrawTypeSelectAdapter(context, modelList, accountId);
        mLvContent.setAdapter(mAdapter);
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            if (mOnTypeSelectCallback != null) {
                UserWithdrawingModeModel model = modelList.get(position);
                if (model != null) {
                    mOnTypeSelectCallback.onSelect(WithdrawTypeSelectBsDlg.this, model.getAccountId(), model.getAccount(), model.getModeName(), model.getAmountLimit());
                }
            }
        });
    }

    public WithdrawTypeSelectBsDlg setOnTypeSelectCallback(OnTypeSelectCallback callback) {
        mOnTypeSelectCallback = callback;
        return this;
    }

    public interface OnTypeSelectCallback {
        /**
         * @param dlg         dialog
         * @param accountId   账号id
         * @param accountName 账号名
         * @param modeName    类型名
         * @param amountLimit 限额
         */
        void onSelect(WithdrawTypeSelectBsDlg dlg, String accountId, String accountName, String modeName, String amountLimit);
    }
}
