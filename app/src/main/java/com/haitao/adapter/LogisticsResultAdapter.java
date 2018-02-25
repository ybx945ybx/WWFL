package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.LogisticsObject;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class LogisticsResultAdapter extends BaseListAdapter<LogisticsObject>{
    public LogisticsResultAdapter(Context context, List<LogisticsObject> data) {
        super(context, data);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_logistics_result, null);
            holder.tvInfo = getView(convertView, R.id.tvInfo);
            holder.tvTime = getView(convertView, R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvInfo.setTextColor(mContext.getResources().getColor(0 == position ? R.color.midOrange : R.color.darkGrey));
        holder.tvTime.setTextColor(mContext.getResources().getColor(0 == position ? R.color.midOrange : R.color.lightGrey));
        LogisticsObject obj = mList.get(position);
        if(null != obj){
            holder.tvInfo.setText(obj.content);
            holder.tvTime.setText(obj.time);
        }
        return convertView;
    }

    private class Holder {
        TextView tvInfo;
        TextView tvTime;
    }

}
