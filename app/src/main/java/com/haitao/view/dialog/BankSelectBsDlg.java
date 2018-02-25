package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.BankSelectAdapter;
import com.haitao.view.HtHeadView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.BanksIfModelData;

/**
 * 添加提现方式（借记卡）银行选择列表
 *
 * @author 陶声
 * @since 2018-01-30
 */

public class BankSelectBsDlg extends BottomSheetDialog {

    @BindView(R.id.lv_content)  ListView   mLvContent;  // 银行列表
    @BindView(R.id.ht_headview) HtHeadView mHtHeadview;

    private List<BanksIfModelData> mBankList; // 国家区号列表数据
    private BankSelectAdapter      mAdapter;
    //    private String                 mSelectId;
    private OnBankSelectListener   mOnBankSelectListener;

    public BankSelectBsDlg(@NonNull Context context, List<BanksIfModelData> bankList) {
        super(context);
        mBankList = bankList;
        //        mSelectId = selectId;
        initDlg(context);
    }

    private void initDlg(final Context context) {
        View layout = View.inflate(context, R.layout.dlg_bank_select, null);
        setContentView(layout);
        ButterKnife.bind(this, layout);

        mAdapter = new BankSelectAdapter(context, mBankList);
        mLvContent.setAdapter(mAdapter);
        mLvContent.setOnTouchListener((v, event) -> {
            if (!mLvContent.canScrollVertically(-1)) {
                mLvContent.requestDisallowInterceptTouchEvent(false);
            } else {
                mLvContent.requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            // 条目点击事件
            if (mOnBankSelectListener != null) {
                BanksIfModelData item = mBankList.get(position);
                mOnBankSelectListener.onSelect(BankSelectBsDlg.this, item.getBankId(), item.getBankName());
                mAdapter.setSelectId(item.getBankId());
            }
        });
    }

    public interface OnBankSelectListener {
        void onSelect(BankSelectBsDlg dlg, String bankId, String bankName);
    }

    public BankSelectBsDlg setOnBankSelectListener(OnBankSelectListener onBankSelectListener) {
        mOnBankSelectListener = onBankSelectListener;
        return this;
    }
}
