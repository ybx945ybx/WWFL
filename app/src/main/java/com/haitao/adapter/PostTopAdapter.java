package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.PostObject;

import java.util.List;

/**
 * 置顶的贴子
 * Created by tqy on 2015/11/20.
 */
public class PostTopAdapter extends BaseListAdapter<PostObject>{
    private boolean isClose = true;
    private static final int largeSize = 3;

    public PostTopAdapter(Context context, List<PostObject> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        if(null == mList)
            return 0;
        if(mList.size() > largeSize && isClose){
            return largeSize + 1;
        }else if(mList.size() > largeSize && !isClose){
            return mList.size() + 1;
        }else{
            return mList.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_topic_top, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvOpen = getView(convertView, R.id.tvOpen);
            holder.tvTop = getView(convertView,R.id.tvTop);
            holder.tvTitle = getView(convertView,R.id.tvTitle);
            holder.tvTime = getView(convertView,R.id.tvTime);
            holder.tvComment = getView(convertView,R.id.tvComment);
            holder.tvView = getView(convertView,R.id.tvView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PostObject obj = null;
        if(mList.size() > largeSize){
            holder.tvOpen.setVisibility((position == getCount()-1) ? View.VISIBLE : View.GONE);
            holder.layoutContent.setVisibility((position == getCount()-1) ? View.GONE : View.VISIBLE);
            if(position == getCount()-1){
                holder.tvOpen.setText(isClose ? R.string.forum_open_all : R.string.forum_close_all);
                holder.tvOpen.setVisibility(View.VISIBLE);
                holder.layoutContent.setVisibility(View.GONE);
                holder.tvOpen.setOnClickListener(v -> {
                    isClose = !isClose;
                    notifyDataSetChanged();
                });
            }else{
                holder.tvOpen.setVisibility(View.GONE);
                holder.layoutContent.setVisibility(View.VISIBLE);
                obj = mList.get(position);
            }
        }else{
            holder.tvOpen.setVisibility(View.GONE);
            obj = mList.get(position);
        }
        if(null != obj){
            holder.tvTitle.setText(obj.title);
            holder.tvTime.setText(obj.time_view);
            holder.tvComment.setText(obj.reply_count);
            holder.tvView.setText(obj.view_count);
        }

        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.viewSeparate.getLayoutParams();
        params.setMargins(position == getCount()-1 ? 0 : (int) mContext.getResources().getDimension(R.dimen.px15), 0, 0, 0);
        holder.viewSeparate.setLayoutParams(params);*/
        return convertView;
    }

    private class Holder {
        View viewSeparate;
        TextView tvOpen;
        ViewGroup layoutContent;
        ImageView tvTop;
        TextView tvTitle;
        TextView tvTime;
        TextView tvComment;
        TextView tvView;
    }

}
