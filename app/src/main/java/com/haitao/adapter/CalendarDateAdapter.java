package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Enum.CalanderType;
import com.haitao.model.CalanderCellObject;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class CalendarDateAdapter extends BaseListAdapter<CalanderCellObject> {
    private int today;

    public CalendarDateAdapter(Context context, List<CalanderCellObject> data) {
        super(context, data);

    }

    public void setToday(int today) {
        this.today = today;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calander, null);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivImage = getView(convertView, R.id.ivImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvName.setTextColor(mContext.getResources().getColor(mList.get(position).state == CalanderType.CURRENT_MONTH_DAY ? R.color.darkGrey : R.color.lightGrey));
        if (mList.get(position).state == CalanderType.SIGN_DAY) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.darkGrey));
            if (today == mList.get(position).date.day) {
                holder.ivImage.setImageResource(R.mipmap.ic_sign_today);
            } else {
                holder.ivImage.setImageResource(R.mipmap.ic_sign_check);
            }

        }
        holder.tvName.setText(String.valueOf(mList.get(position).date.day));
        return convertView;
    }

    private class Holder {
        TextView tvName;
        ImageView ivImage;
    }

}
