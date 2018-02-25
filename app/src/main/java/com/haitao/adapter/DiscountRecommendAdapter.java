package com.haitao.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.DiscountObject;
import com.haitao.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class DiscountRecommendAdapter extends BaseListAdapter<DiscountObject>{
    public DiscountRecommendAdapter(Context context, List<DiscountObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_discount_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvPrice = getView(convertView, R.id.tvPrice);
            holder.tvOldPrice = getView(convertView, R.id.tvOldPrice);
            holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        DiscountObject obj = mList.get(position);
        if(null != obj){
            ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            holder.tvTitle.setText(obj.title);
            if(TextUtils.isEmpty(obj.price)){
                holder.tvOldPrice.setVisibility(View.GONE);
                holder.tvPrice.setText(obj.discount);
            }else{
                holder.tvPrice.setText(obj.price);
                holder.tvOldPrice.setText(obj.old_price);
            }
        }
        return convertView;
    }

    private class Holder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvOldPrice;
    }

}
