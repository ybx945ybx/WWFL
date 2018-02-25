package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 转运列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TransportAdapter extends BaseListAdapter<LogisticsCompanyObject> {

    public TransportAdapter(Context context, List<LogisticsCompanyObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_transport_border_top, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.rbStar = getView(convertView, R.id.rbStar);
            holder.tvStar = getView(convertView, R.id.tvStar);
            holder.tvCount = getView(convertView, R.id.tvCount);
            holder.imgStoreLabel = getView(convertView, R.id.img_store_label);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//        holder.imgStoreLabel.setVisibility(HtApplication.isActivityOn ? View.VISIBLE : View.GONE);
        LogisticsCompanyObject obj = mList.get(position);
        if (null != obj) {
            holder.tvTitle.setText(obj.name);
            holder.rbStar.setRating(Float.valueOf(obj.start_number));
            holder.tvStar.setText(Float.valueOf(obj.start_number) > 0 ? obj.start_number + "星" : "暂无评分");
            holder.tvCount.setText(String.format("%s个晒单 | %s人收藏", obj.thread_count, obj.collection_count));
            ImageLoaderUtils.showOnlineImage(obj.logo, holder.ivImage);
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivImage;
        TextView        tvTitle;
        RatingBar       rbStar;
        TextView        tvStar;
        TextView        tvCount;
        ImageView       imgStoreLabel;
    }

}
