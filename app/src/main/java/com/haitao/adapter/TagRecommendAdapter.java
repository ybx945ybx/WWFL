package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.TopicModelTags;


/**
 * 热门标签
 * Created by tqy on 2015/11/20.
 */
public class TagRecommendAdapter extends BaseListAdapter<TopicModelTags> {

    public TagRecommendAdapter(Context context, List<TopicModelTags> data) {
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
            convertView = inflater.inflate(R.layout.item_tag_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.layoutMore = getView(convertView, R.id.layoutMore);
            //            DisplayMetrics              dm           = mContext.getResources().getDisplayMetrics();
            //            float                       density      = dm.density;
            //            int                         imgSize      = (int) (90 * density);
            //            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            //            layoutParams.width = imgSize;
            //            layoutParams.height = imgSize;
            //            holder.ivImage.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TopicModelTags obj = mList.get(position);
        if (null != obj) {
            holder.tvName.setText("#" + obj.getTagName());
            ImageLoaderUtils.showOnlineImage(obj.getPic(), holder.ivImage);
            holder.layoutMore.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.VISIBLE);
        } else {
            holder.tvName.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.INVISIBLE);
            holder.layoutMore.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView        tvName;
        ViewGroup       layoutMore;
    }

}
