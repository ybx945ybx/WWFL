package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

/**
 * 搜索历史 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class SearchHistoryAdapter extends BaseListAdapter<String> {

    public SearchHistoryAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_history, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvName = getView(convertView, R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewSeparate.getLayoutParams();
//        params.setMargins(position == getCount() - 1 ? 0 : (int) mContext.getResources().getDimension(R.dimen.px15), 0, 0, 0);
//        holder.viewSeparate.setLayoutParams(params);
        holder.tvName.setText(mList.get(position));
        holder.viewSeparate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        return convertView;
    }

    private class Holder {
        View     viewSeparate;
        TextView tvName;
    }

}
