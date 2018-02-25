package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.FollowingDealModel;

/**
 * 降价提醒列表adapter
 * Created by a55 on 2017/9/25.
 */

public class DepreciateAdapter extends BaseListAdapter<FollowingDealModel> {

    public DepreciateAdapter(Context context, List<FollowingDealModel> data) {
        super(context, data);
    }

    public List<FollowingDealModel> getList() {
        return this.mList;
    }

    public void setData(int position, FollowingDealModel obj) {
        mList.set(position, obj);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_depreciate, null);
            holder.itemView = getView(convertView, R.id.item_view);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.tvStore = getView(convertView, R.id.tvStore);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvPrice = getView(convertView, R.id.tvPrice);
            holder.ivCountry = getView(convertView, R.id.ivCountry);
            holder.tvTime = getView(convertView, R.id.tvTime);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final FollowingDealModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showDepreciateImage(obj.getDealPic(), obj.getDealPicOrigin(), holder.ivImage);
            //            ImageLoaderUtils.showOnlineImage(obj.getDealPic(), holder.ivImage);
            ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), holder.ivCountry);
            holder.tvStore.setText(obj.getStoreName());
            holder.tvTitle.setText(obj.getTitle());
            holder.tvPrice.setText(obj.getPriceView());
            holder.tvRebate.setVisibility(View.GONE);
            holder.tvTime.setVisibility(TextUtils.equals(obj.getIsExpired(), "1") ? View.VISIBLE : View.GONE);
            mList.set(position, obj);

        }
        return convertView;
    }

    private class Holder {
        LinearLayout    itemView;
        TextView        tvRebate;
        CustomImageView ivImage;
        CustomImageView ivCountry;
        TextView        tvTitle;
        TextView        tvPrice;
        TextView        tvStore;
        TextView        tvTime;
    }
}
