package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.ForumBoardModel;

/**
 * 版块
 * Created by tqy on 2015/11/20.
 */
public class BoardSelectAdapter extends BaseListAdapter<ForumBoardModel> {
    public String selectBoardId = "";

    public BoardSelectAdapter(Context context, List<ForumBoardModel> data) {
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
            convertView = inflater.inflate(R.layout.item_board_select, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.icArrow = getView(convertView, R.id.icArrow);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ForumBoardModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.ivImage);
            holder.tvName.setText(obj.getBoardName());
            holder.icArrow.setSelected(selectBoardId.equals(obj.getBoardId()));
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        ViewGroup       layoutContent;
        CustomImageView ivImage;
        TextView        tvName;
        ImageView       icArrow;
    }
}
