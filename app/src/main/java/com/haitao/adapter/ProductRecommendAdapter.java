package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.swagger.client.model.DealModel;

/**
 * 推荐优惠
 * Created by tqy on 2015/11/20.
 */
public class ProductRecommendAdapter extends BaseListAdapter<DealModel> {

    public ProductRecommendAdapter(Context context, List<DealModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        parent.clearFocus();
        parent.setFocusable(false);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_product_recommend, null);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            //            holder.tvMarketPrice = getView(convertView, R.id.tvMarketPrice);
            //            holder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvPrice = getView(convertView, R.id.tvPrice);
            holder.tvTime = getView(convertView, R.id.tvTime);
            //            holder.tvMarketPrice = getView(convertView, R.id.tvMarketPrice);
            DisplayMetrics              dm             = mContext.getResources().getDisplayMetrics();
            float                       density        = dm.density;
            int                         gridviewWidth  = (int) (140 * density);
            int                         gridviewHeight = (int) (gridviewWidth * 1 / 1);
            RelativeLayout.LayoutParams layoutParams   = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            layoutParams.width = gridviewWidth;
            layoutParams.height = gridviewHeight;
            holder.ivImage.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        DealModel obj = mList.get(position);
        if (null != obj) {

            Logger.d("推荐优惠\n" + obj.toString());

            holder.layoutContent.setVisibility(View.VISIBLE);

            holder.tvPrice.setText(obj.getPriceView());

            //如果有原价就显示原价，否则显示折扣
            /*if (!TextUtils.isEmpty(obj.getDiscountView())) {
                holder.tvMarketPrice.setVisibility(View.GONE);
                holder.tvPrice.setText(obj.getDiscountView());
            } else {
                holder.tvPrice.setText(obj.getNowPrice());
                //如果原价跟现价一样，原价不显示
                if (!obj.getOriginalPrice().equals(obj.getNowPrice())) {
                    holder.tvMarketPrice.setVisibility(View.VISIBLE);
                    holder.tvMarketPrice.setText(obj.getOriginalPrice());
                } else {
                    holder.tvMarketPrice.setVisibility(View.GONE);
                }
            }*/
            holder.tvTime.setVisibility(TextUtils.isEmpty(obj.getLeftTime()) ? View.GONE : View.VISIBLE);
            holder.tvTime.setText("剩余：" + obj.getLeftTime());
            holder.tvTitle.setText(obj.getTitle());
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
