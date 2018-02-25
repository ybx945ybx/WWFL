package com.haitao.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.RVBaseAdapter;
import com.haitao.adapter.RVBaseHolder;

import java.util.ArrayList;

import io.swagger.client.model.CouponModel;

/**
 * 优惠详情去购买 折扣码弹窗
 */
public class CouponDialog extends BottomSheetDialog {

    private Context                mContext;
    private ArrayList<CouponModel> mList;
    private OnCouponClickListener  mOnCouponClickListener;

    public CouponDialog(@NonNull Context context, ArrayList<CouponModel> mList) {
        super(context);
        mContext = context;
        this.mList = mList;
        initDlg();
    }

    private void initDlg() {
        View         parentView     = View.inflate(mContext, R.layout.dialog_coupon, null);
        ImageView    ivClose        = parentView.findViewById(R.id.iv_close);
        TextView     tvEmpty        = parentView.findViewById(R.id.tv_empty);
        RecyclerView rycvCouponList = parentView.findViewById(R.id.rycv_coupon_list);

        if (mList != null && mList.size() > 0) {
            // 有折扣码
            tvEmpty.setVisibility(View.GONE);
            rycvCouponList.setVisibility(View.VISIBLE);
            rycvCouponList.setLayoutManager(new GridLayoutManager(mContext, 2));
            rycvCouponList.setAdapter(new RVBaseAdapter<CouponModel>(mContext, mList, R.layout.coupon_item) {
                @Override
                public void bindView(RVBaseHolder holder, CouponModel item) {
                    holder.setText(R.id.tv_coupon_code, item.getCode());
                    holder.setText(R.id.tv_coupon_limit, item.getDescription());
                    holder.setText(R.id.tv_coupon_end_time, item.getExpirationTime());
                    holder.setVisibility(TextUtils.isEmpty(item.getExpirationTime()) ? View.GONE : View.VISIBLE, R.id.tv_coupon_end_time);
                    holder.setVisibility(TextUtils.equals(item.getIsExclusive(), "1") ? View.VISIBLE : View.GONE, R.id.iv_coupon_unique);

                    holder.getView(R.id.llyt_content).setOnClickListener(v -> {
                        if (mOnCouponClickListener != null){
                            mOnCouponClickListener.onCouponClick(item.getCode());
                        }
                    });
                }
            });
        } else {
            // 无折扣码
            tvEmpty.setVisibility(View.VISIBLE);
            rycvCouponList.setVisibility(View.GONE);
        }

        // 关闭按钮
        ivClose.setOnClickListener(v -> dismiss());

        setContentView(parentView);
    }

    public interface OnCouponClickListener {
        void onCouponClick(String coupon);
    }

    public void setmOnCouponClickListener(OnCouponClickListener mOnCouponClickListener) {
        this.mOnCouponClickListener = mOnCouponClickListener;
    }
}
