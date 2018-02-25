package com.haitao.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;

/**
 * 去购买页面 更多弹窗
 */
public class DiscountMoreDialog extends BottomSheetDialog implements View.OnClickListener {

    private Context mContext;
    private String  storeName;
    private OnInnerListener mOnInnerListener = null;

    public DiscountMoreDialog(Context context, String storeName) {
        super(context);
        mContext = context;
        this.storeName = storeName;
        initDlg();
    }

    private void initDlg() {
        View     parentView = View.inflate(mContext, R.layout.layout_pop_web, null);
        TextView tvRefresh  = parentView.findViewById(R.id.tv_refresh);
        TextView tvExchange = parentView.findViewById(R.id.tv_exchange_rate);
        TextView tvBrower   = parentView.findViewById(R.id.tv_browser);
        TextView tvGoStore  = parentView.findViewById(R.id.tv_goto_store);
        tvGoStore.setText(String.format(mContext.getResources().getString(R.string.go_to_store), storeName));

        tvRefresh.setOnClickListener(this);
        tvExchange.setOnClickListener(this);
        tvBrower.setOnClickListener(this);
        tvGoStore.setOnClickListener(this);

        setContentView(parentView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_refresh:
                if (null != mOnInnerListener) {
                    mOnInnerListener.onRefreshClick();
                }
                break;
            case R.id.tv_exchange_rate:
                if (null != mOnInnerListener) {
                    mOnInnerListener.onExchangeClick();
                }
                break;
            case R.id.tv_browser:
                if (null != mOnInnerListener) {
                    mOnInnerListener.onBrowserClick();
                }
                break;
            case R.id.tv_goto_store:
                if (null != mOnInnerListener) {
                    mOnInnerListener.onGoStoreClick();
                }
                break;
        }
        dismiss();

    }

    public void setOnInnerListener(OnInnerListener onInnerListener) {
        this.mOnInnerListener = onInnerListener;
    }

    public interface OnInnerListener {
        void onRefreshClick();

        void onExchangeClick();

        void onBrowserClick();

        void onGoStoreClick();
    }

}
