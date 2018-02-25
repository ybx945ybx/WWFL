package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.HelpObject;

import java.util.List;

/**
 * 使用帮助列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class HelpAdapter extends BaseListAdapter<HelpObject> {

    public HelpAdapter(Context context, List<HelpObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_help, null, false);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivName = getView(convertView, R.id.ivName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewSeparate.getLayoutParams();
        params.setMargins(position == getCount() - 1 ? 0 : (int) mContext.getResources().getDimension(R.dimen.px15), 0, 0, 0);
        holder.viewSeparate.setLayoutParams(params);
        holder.ivName.setText(mList.get(position).name);
        return convertView;
    }

    private class Holder {
        View     viewSeparate;
        TextView ivName;
    }

}
