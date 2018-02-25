package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.DealModel;

/**
 * 置顶优惠 - Adapter
 *
 * @author 陶声
 * @since 2017/08/16
 */
public class TopDealAdapter extends BaseListAdapter<DealModel> {

    public TopDealAdapter(Context context, List<DealModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        parent.clearFocus();
        parent.setFocusable(false);
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_top_deal, null);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            //            holder.tvMarketPrice = getView(convertView, R.id.tvMarketPrice);
            //            holder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvPrice = getView(convertView, R.id.tvPrice);
            holder.tvTime = getView(convertView, R.id.tvTime);
            //            holder.tvMarketPrice = getView(convertView, R.id.tvMarketPrice);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        DealModel obj = mList.get(position);
        if (null != obj) {
            holder.layoutContent.setVisibility(View.VISIBLE);
            //            holder.layoutMore.setVisibility(View.GONE);
            holder.tvPrice.setText(obj.getPriceView());

            holder.tvTitle.setText(obj.getTitle());
            if (!TextUtils.isEmpty(obj.getLeftTime())) {
                holder.tvTime.setText("剩余：" + obj.getLeftTime());
            } else {
                holder.tvTime.setVisibility(View.GONE);
            }
            ImageLoaderUtils.showOnlineImage(obj.getDealPic(), holder.ivImage);
        } else {
            holder.layoutContent.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class Holder {
        //        TextView        tvMarketPrice;
        CustomImageView ivImage;
        TextView        tvTitle;
        TextView        tvPrice;
        ViewGroup       layoutContent;
        TextView        tvTime;
    }

}
