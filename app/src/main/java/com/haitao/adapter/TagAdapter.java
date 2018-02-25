package com.haitao.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.TagModel;


/**
 * 热门标签列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TagAdapter extends BaseListAdapter<TagModel> {

    public TagAdapter(Context context, List<TagModel> data) {
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
            convertView = inflater.inflate(R.layout.item_tags, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvName = getView(convertView, R.id.tvName);
            DisplayMetrics dm      = mContext.getResources().getDisplayMetrics();
            float          density = dm.density;
            int            with    = (ScreenUtils.getScreenWidth((Activity) mContext) - 2 * 24 - 2 * 16) / 3;
            holder.ivImage.setLayoutParams(new RelativeLayout.LayoutParams(with, with));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            layoutParams.width = with;
            layoutParams.height = with;
            holder.ivImage.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TagModel obj = mList.get(position);
        if (null != obj) {
            holder.tvName.setText("#" + obj.getTagName());
            ImageLoaderUtils.showOnlineImage(obj.getPic(), holder.ivImage);
        } else {
            holder.tvName.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView        tvName;
    }

}
