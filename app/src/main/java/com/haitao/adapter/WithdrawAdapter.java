package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.WithdrawingRecordModel;

/**
 * 提现记录 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class WithdrawAdapter extends BaseListAdapter<WithdrawingRecordModel> {

    public WithdrawAdapter(Context context, List<WithdrawingRecordModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_rebate_withdraw, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WithdrawingRecordModel obj = mList.get(position);
        if (null != obj) {
            // 图片
            ImageLoaderUtils.showOnlineImage(obj.getModeIcon(), holder.mImgRebate);
            // 提现类型
            holder.mTvRebateType.setText(obj.getModeName());
            // 提现状态
            if (!TextUtils.isEmpty(obj.getStatusView())) {
                holder.mTvRebateStatus.setText(obj.getStatusView());
                //                holder.mTvRebateStatus.setTextColor(obj.getStatus());
                setStatusView(holder.mTvRebateStatus, obj.getStatus());
            } else {
                holder.mTvRebateStatus.setVisibility(View.GONE);
            }
            // 提现时间
            holder.mTvRebateTime.setText(obj.getWithdrawingTime());
            // 提现金额
            holder.mTvRebateAmount.setText(obj.getAmountView());
            // 最后一条隐藏分割线
            holder.mViewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.img_rebate)       CustomImageView mImgRebate;
        @BindView(R.id.tv_rebate_type)   TextView        mTvRebateType;
        @BindView(R.id.tv_rebate_status) TextView        mTvRebateStatus;
        @BindView(R.id.tv_rebate_time)   TextView        mTvRebateTime;
        @BindView(R.id.tv_rebate_amount) TextView        mTvRebateAmount;
        @BindView(R.id.img_next)         ImageView       mImgNext;
        @BindView(R.id.viewSeparate)     View            mViewSeparate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setStatusView(TextView tv, String status) {
        switch (status) {
            case "1": // 审核中
            case "2": // 待付款
                tv.setEnabled(false);
                //                tv.setTextColor(ContextCompat.getColor(mContext, R.color.grey4F4F53));
                //                tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_round_corner_gray_f5f5f5_4dp));
                break;
            case "3": // 提现成功
                tv.setEnabled(true);
                //                tv.setTextColor(ContextCompat.getColor(mContext, R.color.orangeFF804D));
                //                tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_round_corner_orange_1a_4dp));
                break;
            default:
                tv.setEnabled(false);
                //                tv.setTextColor(ContextCompat.getColor(mContext, R.color.grey4F4F53));
                //                tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_round_corner_gray_f5f5f5_4dp));
                break;
        }
    }
}
