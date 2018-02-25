package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import io.swagger.client.model.TopicModelTags;


/**
 * tag适配器
 */
public class ForumTagAdapter extends BaseListAdapter<TopicModelTags> {


    public ForumTagAdapter(Context context, List<TopicModelTags> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tag, null);
            holder = new ViewHolder();
            holder.tagBtn = (TextView) convertView.findViewById(R.id.tag_btn);
            holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.midGrey));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        TopicModelTags obj = getItem(position);
        holder.tagBtn.setText(obj.getTagName());
        return convertView;
    }

    static class ViewHolder {
        TextView tagBtn;
    }
}
