package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import io.swagger.client.model.ForumSubBoardModel;

/**
 * 版块
 * Created by tqy on 2015/11/20.
 */
public class SubBoardSelectAdapter extends BaseListAdapter<ForumSubBoardModel> {
    public String selectBoardId = "";

    public SubBoardSelectAdapter(Context context, List<ForumSubBoardModel> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        if (null == mList)
            return 0;
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_sub_board_select, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.icArrow = getView(convertView, R.id.icArrow);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ForumSubBoardModel obj = mList.get(position);
        if (null != obj) {
            holder.tvName.setText(obj.getSubBoardName());
            holder.icArrow.setSelected(selectBoardId.equals(obj.getSubBoardId()));
        }
        return convertView;
    }

    private class Holder {
        View      viewSeparate;
        ViewGroup layoutContent;
        TextView  tvName;
        ImageView icArrow;
    }
}
