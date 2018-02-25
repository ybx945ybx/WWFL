package com.haitao.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import io.swagger.client.model.ForumSubBoardModel;


/**
 * tag适配器
 */
public class BoardTagBaseAdapter extends BaseListAdapter<ForumSubBoardModel> {
    public int currentPosition = 0;
    public boolean isFillParent = false;

    SparseIntArray rightItem = new SparseIntArray();


    public BoardTagBaseAdapter(Context context, List<ForumSubBoardModel> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_board_tag, null);
            holder = new ViewHolder();
            holder.tagBtn = (TextView) convertView.findViewById(R.id.tag_btn);
            holder.ivGou = (ImageView) convertView.findViewById(R.id.ivGou);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(isFillParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tagBtn.setLayoutParams(params);
            if(isFillParent) {
                holder.tagBtn.setPadding(0, (int)mContext.getResources().getDimension(R.dimen.px15), 0, (int)mContext.getResources().getDimension(R.dimen.px15));
            }
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ForumSubBoardModel obj = getItem(position);
        holder.tagBtn.setText(obj.getSubBoardName());
        holder.tagBtn.setSelected(position == currentPosition);
        //holder.ivGou.setVisibility(position == currentPosition ? View.VISIBLE : View.GONE);
        return convertView;
    }


    public void setWidth(SparseIntArray rightItems){
        this.rightItem = rightItems;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tagBtn;
        ImageView ivGou;
    }
}
