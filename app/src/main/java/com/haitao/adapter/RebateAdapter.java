package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.RebateModel;

/**
 * 返利记录 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class RebateAdapter extends BaseListAdapter<RebateModel> {

    public RebateAdapter(Context context, List<RebateModel> data) {
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

        RebateModel obj = mList.get(position);
        if (null != obj) {
            // 返利图标
            //            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.mImgRebate);
            holder.mImgRebate.setVisibility(View.GONE);
            // 返利类型
            holder.mTvRebateTime.setText(obj.getEffectiveTime());
            // 返利状态
            holder.mTvRebateStatus.setText(obj.getStatusView());
            holder.mTvRebateStatus.setEnabled(TextUtils.equals(obj.getStatus(), "0"));
            /*holder.mTvRebateStatus.setTextColor(TextUtils.equals(obj.getStatus(), "1") ?
                    ContextCompat.getColor(mContext, R.color.green) : ContextCompat.getColor(mContext, R.color.midBlue));
            holder.mTvRebateStatus.setBackground(TextUtils.equals(obj.getStatus(), "1") ?
                    ContextCompat.getDrawable(mContext, R.drawable.border_round_corner_green) : ContextCompat.getDrawable(mContext, R.drawable.border_round_corner_blue));
           */ // 返利时间
            holder.mTvRebateType.setText(obj.getTypeView());
            // 返利金额
            holder.mTvRebateAmount.setText(obj.getRebateAmountView());
            // 跳转类型
            try {
                int type = Integer.parseInt(obj.getType());
                holder.mImgNext.setVisibility(type == 2 || type == 3 || type == 12 ? View.VISIBLE : View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
