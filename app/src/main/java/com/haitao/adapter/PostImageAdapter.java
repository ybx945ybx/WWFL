package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 发送帖子选择的图片
 * Created by tqy on 2015/11/20.
 */
public class PostImageAdapter extends BaseListAdapter<String> {

    public PostImageAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_post_image, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            int with = (ScreenUtils.getScreenWidth(mContext) - 2 * 16 - 2 * 24) / 3;
            holder.ivImage.setLayoutParams(new RelativeLayout.LayoutParams(with, with));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ImageLoaderUtils.showOnlineImage(mList.get(position), holder.ivImage);
        holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
    }

}
