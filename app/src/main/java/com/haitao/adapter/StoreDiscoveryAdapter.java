package com.haitao.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.StoreObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 加倍返利商家 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class StoreDiscoveryAdapter extends BaseListAdapter<StoreObject> {
    public StoreDiscoveryAdapter(Context context, List<StoreObject> data) {
        super(context, data);
    }

    public List<StoreObject> getList() {
        return this.mList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_store_discovery, null);
            holder.viewSeparate = convertView.findViewById(R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.ic_country = getView(convertView, R.id.ivCountry);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            holder.tvOldRebate = getView(convertView, R.id.tvOldRebate);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final StoreObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            ImageLoaderUtils.showOnlineImage(obj.country_pic, holder.ic_country);
            holder.tvRebate.setText(obj.cashback_view);
            holder.tvOldRebate.setText(obj.old_cashback_view);
            holder.tvOldRebate.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setText(obj.name);
            if (null != obj.category && obj.category.size() > 0) {
                holder.tvCategory.setText(TextUtils.isEmpty(obj.old_cashback_view) ? obj.category.get(0).text : obj.category.get(0).text + "  |");
            }
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        ImageButton     btnChoose;
        CustomImageView ivImage;
        TextView        tvRebate;
        TextView        tvTitle;
        CustomImageView ic_country;
        TextView        tvCategory;
        TextView        tvOldRebate;
    }

}
