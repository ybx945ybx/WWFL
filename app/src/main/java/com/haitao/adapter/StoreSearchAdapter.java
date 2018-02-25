package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.HtApplication;
import com.haitao.model.StoreObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 搜索 - 商家列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class StoreSearchAdapter extends BaseListAdapter<StoreObject> {
    public boolean isDelete = false;

    public StoreSearchAdapter(Context context, List<StoreObject> data) {
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
            convertView = inflater.inflate(R.layout.item_store_search, null);
            holder.viewSeparate = convertView.findViewById(R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.ic_country = getView(convertView, R.id.ivCountry);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            holder.btnChoose = getView(convertView, R.id.btnChoose);
            holder.tvOrderNum = getView(convertView, R.id.tvOrderNum);
            holder.imgStoreLabel = getView(convertView, R.id.img_store_label);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        final StoreObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            ImageLoaderUtils.showOnlineImage(obj.country_pic, holder.ic_country);
            // 活动角标
            if (HtApplication.isActivityOn && !TextUtils.isEmpty(HtApplication.mStoreTransportActivityLabel) && TextUtils.equals(obj.in_activity, "1")) {
                ImageLoaderUtils.showOnlineImage(HtApplication.mStoreTransportActivityLabel, holder.imgStoreLabel);
                holder.imgStoreLabel.setVisibility(View.VISIBLE);
            } else {
                holder.imgStoreLabel.setVisibility(View.GONE);
            }
            holder.tvRebate.setText(obj.cashback_view);
            holder.tvRebate.setVisibility(TextUtils.equals(obj.cashback_view, "暂无返利") ? View.GONE : View.VISIBLE);
            holder.tvTitle.setText(obj.name);
            String category = "";
            if (null != obj.category && obj.category.size() > 0) {
                category = obj.category.get(0).text;
            }
            holder.tvOrderNum.setText(String.format("%s次成功下单 | %s人收藏", obj.buy_order_count, obj.attention_count));
            holder.tvCategory.setText(category);
            holder.btnChoose.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            holder.btnChoose.setSelected(obj.isSelected);
            holder.btnChoose.setOnClickListener(v -> {
                obj.isSelected = !obj.isSelected;
                mList.set(position, obj);
                holder.btnChoose.setSelected(obj.isSelected);
            });
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
        TextView        tvOrderNum;
        ImageView       imgStoreLabel;
    }

}
