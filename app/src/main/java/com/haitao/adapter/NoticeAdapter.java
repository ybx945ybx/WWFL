package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.NoticeObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.RoundedImageView;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class NoticeAdapter extends BaseListAdapter<NoticeObject> {

    public NoticeAdapter(Context context, List<NoticeObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_notice, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvTpye = getView(convertView, R.id.tvTpye);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewSeparate.getLayoutParams();
        params.setMargins(position == getCount() - 1 ? 0 : (int) mContext.getResources().getDimension(R.dimen.px15), 0, 0, 0);
        holder.viewSeparate.setLayoutParams(params);


        NoticeObject noticeObject = getItem(position);
        if (noticeObject != null) {
            holder.tvTpye.setVisibility("1".equals(noticeObject.isnew) ? View.VISIBLE : View.GONE);
            holder.tvName.setText(noticeObject.author);
            holder.tvTime.setText(noticeObject.dateline);
            holder.tvContent.setText(noticeObject.content);
            ImageLoaderUtils.showOnlineImage(noticeObject.avatar, holder.ivAvator);
            holder.tvContent.setTextColor(mContext.getResources().getColor(!"1".equals(noticeObject.isnew) ? R.color.lightGrey : R.color.darkGrey));
            holder.tvName.setTextColor(mContext.getResources().getColor(!"1".equals(noticeObject.isnew) ? R.color.lightGrey : R.color.midGrey));
        }

        return convertView;
    }

    private class Holder {
        View viewSeparate;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        RoundedImageView ivAvator;
        TextView tvTpye;
    }

}
