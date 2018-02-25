package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.RoundedImageView;

import java.util.List;

import io.swagger.client.model.MsgNoticeModel;

/**
 * 消息列表Adapter
 */
public class MessageNotificationAdapter extends BaseListAdapter<MsgNoticeModel> {
    public MessageNotificationAdapter(Context context, List<MsgNoticeModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_message_notification, null, false);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.tvNotice = getView(convertView, R.id.tvNotice);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MsgNoticeModel obj = mList.get(position);
        if (null != obj) {
            holder.viewSeparate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            ImageLoaderUtils.showOnlineImage(obj.getNoticeIcon(), holder.ivAvator, R.mipmap.ic_default_avator);
            holder.tvContent.setText(obj.getNoticeSummary());
            holder.tvTime.setText(obj.getLastActivityTime());
            holder.tvName.setText(obj.getNoticeTitle());
            if (Integer.valueOf(obj.getNewNoticesCount()) > 0) {
                holder.tvNotice.setVisibility(View.VISIBLE);
                holder.tvNotice.setText(obj.getNewNoticesCount());
            } else {
                holder.tvNotice.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    private class Holder {
        View             viewSeparate;
        RoundedImageView ivAvator;
        TextView         tvName;
        TextView         tvTime;
        TextView         tvContent;
        TextView         tvNotice;
    }
}
