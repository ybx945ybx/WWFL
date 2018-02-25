package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.DiscountObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 优惠列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class DiscountAdapter extends BaseListAdapter<DiscountObject> {
    public boolean isDelete = false;

    public DiscountAdapter(Context context, List<DiscountObject> data) {
        super(context, data);
    }

    public List<DiscountObject> getList() {
        return this.mList;
    }

    public void setData(int position, DiscountObject obj) {
        mList.set(position, obj);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_deal, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.tvStore = getView(convertView, R.id.tvStore);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvPrice = getView(convertView, R.id.tvPrice);
            holder.tvAddress = getView(convertView, R.id.tvAddress);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvComment = getView(convertView, R.id.tvComment);
            holder.tvAgree = getView(convertView, R.id.tvAgree);
            holder.btnChoose = getView(convertView, R.id.btnChoose);
            holder.ivCountry = getView(convertView, R.id.ivCountry);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.viewSeparate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        final DiscountObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            ImageLoaderUtils.showOnlineImage(obj.country_pic, holder.ivCountry);
            holder.tvStore.setText(obj.store_name);
            holder.tvTitle.setText(obj.title);


            // 如果有原价就显示原价，否则显示折扣
            if (!TextUtils.isEmpty(obj.discount)) {
                holder.tvPrice.setText(obj.discount);
            } else {
                holder.tvPrice.setText(obj.price);
            }
            holder.tvAddress.setText(obj.time_view);
            if (!obj.cashback_view.contains("暂无返利")) {
                holder.tvRebate.setVisibility(View.VISIBLE);
                holder.tvRebate.setText(obj.cashback_view);
            } else {
                holder.tvRebate.setVisibility(View.GONE);
            }
            holder.tvTime.setText("剩余：" + obj.surplus_time_view);
            holder.tvTime.setVisibility("".equals(obj.surplus_time_view) ? View.GONE : View.VISIBLE);
            holder.tvComment.setText(obj.comment_count);
            holder.tvAgree.setText(obj.praise_count);
            holder.btnChoose.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            obj.isSelected = (obj.isSelected && isDelete);
            mList.set(position, obj);
            holder.btnChoose.setSelected(obj.isSelected);
            holder.btnChoose.setOnClickListener(v -> {
                obj.isSelected = !obj.isSelected;
                mList.set(position, obj);
                holder.btnChoose.setSelected(obj.isSelected);
            });
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        TextView        tvRebate;
        CustomImageView ivImage;
        CustomImageView ivCountry;
        TextView        tvTitle;
        TextView        tvPrice;
        //        TextView        tvOldPrice;
        TextView        tvAddress;
        TextView        tvTime;
        TextView        tvComment;
        TextView        tvAgree;
        TextView        tvStore;
        ImageButton     btnChoose;
    }

}
