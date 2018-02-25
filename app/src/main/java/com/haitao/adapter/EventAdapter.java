package com.haitao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.PostObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 进行中的活动 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class EventAdapter extends BaseListAdapter<PostObject> {

    public String[] colors = new String[]{"#22b7a1", "#c15ccd", "#de8631", "#55b737", "#6b68e3", "#e85363", "#81a309"};

    public EventAdapter(Context context, List<PostObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_event, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvSubTitle = getView(convertView, R.id.tvSubTitle);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.layoutPic = getView(convertView, R.id.layoutPic);
            holder.tvTitle1 = getView(convertView, R.id.tvTitle1);
            holder.tvTitle2 = getView(convertView, R.id.tvTitle2);
            holder.tvTitle3 = getView(convertView, R.id.tvTitle3);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PostObject obj = mList.get(position);
        if (null != obj) {
            holder.tvTitle.setText(obj.title);
            holder.tvSubTitle.setText(obj.subtitle);
            holder.tvTime.setText(obj.start_time + " - " + obj.end_time);
            holder.tvCategory.setText(obj.category);
            if (null != obj.pic_info && obj.pic_info.length > 2) {
                holder.tvTitle1.setVisibility(View.VISIBLE);
                holder.tvTitle2.setVisibility(View.VISIBLE);
                holder.tvTitle3.setVisibility(View.VISIBLE);
                holder.ivImage.setVisibility(View.GONE);
                holder.tvTitle1.setText(obj.pic_info[0]);
                holder.tvTitle2.setText(obj.pic_info[1]);
                holder.tvTitle3.setText(obj.pic_info[2]);
                holder.layoutPic.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
            } else {
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.tvTitle1.setVisibility(View.GONE);
                holder.tvTitle2.setVisibility(View.GONE);
                holder.tvTitle3.setVisibility(View.GONE);
                ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            }
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivImage;
        TextView        tvTitle;
        TextView        tvSubTitle;
        TextView        tvTime;
        TextView        tvTitle1, tvTitle2, tvTitle3;
        ViewGroup layoutPic;
        TextView  tvCategory;
    }

}
