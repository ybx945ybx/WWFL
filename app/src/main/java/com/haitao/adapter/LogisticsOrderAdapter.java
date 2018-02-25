package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.OrderBriefModel;

/**
 * Created by tqy on 2015/11/20.
 */
public class LogisticsOrderAdapter extends BaseListAdapter<OrderBriefModel> {
    public OrderBriefModel mOrderBriefModel;

    public LogisticsOrderAdapter(Context context, List<OrderBriefModel> data) {
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
        OrderBriefModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.ivImage);
            holder.tvStore.setText(obj.getStoreName());
            holder.tvTime.setText(obj.getOrderTime());
            holder.tvOrder.setText(String.format(mContext.getResources().getString(R.string.logistics_order), obj.getOrderNumber()));
            holder.icStatus.setSelected(null != mOrderBriefModel && mOrderBriefModel.getId().equals(obj.getId()));
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
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
