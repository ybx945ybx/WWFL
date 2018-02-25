package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.OrderObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.OrderModel;

/**
 * 转运评价 - 选择订单
 */
public class LogisticsOrderTransAdapter extends BaseListAdapter<OrderModel> {
    public OrderObject orderObject;

    public LogisticsOrderTransAdapter(Context context, List<OrderModel> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_logistics_order, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.icStatus = getView(convertView, R.id.icStatus);
            holder.tvStore = getView(convertView, R.id.tvStore);
            holder.tvOrder = getView(convertView, R.id.tvOrder);
            holder.tvTime = getView(convertView, R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        OrderModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.ivImage);
            holder.tvStore.setText(obj.getStoreName());
            holder.tvTime.setText("日期：" + obj.getOrderTime());
            holder.tvOrder.setText(String.format(mContext.getResources().getString(R.string.logistics_order), obj.getOrderId()));
            holder.icStatus.setVisibility(null != orderObject && orderObject.id.equals(obj.getOrderId()) ? View.VISIBLE : View.GONE);
        }

        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivImage;
        ImageView       icStatus;
        TextView        tvStore;
        TextView        tvTime;
        TextView        tvOrder;
    }

}
