package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.OrderModel;

/**
 * 订单列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class OrderAdapter extends BaseListAdapter<OrderModel> {

    public OrderAdapter(Context context, List<OrderModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_order, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.mImgStoreLogo);
            holder.mTvStoreName.setText(obj.getStoreName());
            holder.mTvTime.setText(obj.getOrderTime());
            holder.mTvRebate.setText(obj.getRebateView());
            holder.mTvMoney.setText(obj.getCostView());
            holder.mTvStatus.setText(obj.getStatusView());
            switch (obj.getStatus()) {
                case "1":
                    holder.mTvStatus.setEnabled(true);
                    break;
                case "2":
                case "3":
                    holder.mTvStatus.setEnabled(false);
                    break;
            }
            // 最后一条隐藏分割线
            holder.mViewDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_store_logo) CustomImageView mImgStoreLogo;
        @BindView(R.id.tv_store_name)  TextView        mTvStoreName;
        @BindView(R.id.tv_status)      TextView        mTvStatus;
        @BindView(R.id.tv_rebate)      TextView        mTvRebate;
        @BindView(R.id.tv_money)       TextView        mTvMoney;
        @BindView(R.id.tv_time)        TextView        mTvTime;
        @BindView(R.id.view_divider)   View            mViewDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
