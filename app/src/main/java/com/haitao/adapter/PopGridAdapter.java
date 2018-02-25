package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.TagObject;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class PopGridAdapter extends BaseListAdapter<TagObject>{
    public int currentPosition = 0;

    public PopGridAdapter(Context context, List<TagObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_pop_grid, null);
            holder.tvName = getView(convertView, R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvName.setSelected(currentPosition == position);
        holder.tvName.setText(mList.get(position).text);
        return convertView;
    }

    private class Holder {
        TextView tvName;
    }

}
