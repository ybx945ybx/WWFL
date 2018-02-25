package com.haitao.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.TagObject;

import java.util.List;


/**
 * tag适配器
 */
public class StoreTagBaseAdapter extends BaseListAdapter<TagObject> {
    public int currentPosition = 0;
    //    public boolean isFillParent    = false;

    SparseIntArray rightItem = new SparseIntArray();


    public StoreTagBaseAdapter(Context context, List<TagObject> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_tag, null);
            holder = new ViewHolder();
            holder.tagBtn = (TextView) convertView.findViewById(R.id.tag_btn);
            //            holder.ivGou = (ImageView) convertView.findViewById(R.id.ivGou);
            //            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(isFillParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tagBtn.setLayoutParams(params);
            /*if (isFillParent) {
                holder.btnTag.setPadding((int) mContext.getResources().getDimension(R.dimen.px15), 0, (int) mContext.getResources().getDimension(R.dimen.px15), 0);
            }*/
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TagObject obj = getItem(position);
        holder.tagBtn.setText(obj.text);
        holder.tagBtn.setSelected(position == currentPosition);
        //holder.ivGou.setVisibility(position == currentPosition ? View.VISIBLE : View.GONE);
        return convertView;
    }


    public void setWidth(SparseIntArray rightItems) {
        this.rightItem = rightItems;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tagBtn;
        //        ImageView ivGou;
    }
}
