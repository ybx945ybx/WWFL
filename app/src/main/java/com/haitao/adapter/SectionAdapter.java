package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.SectionObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class SectionAdapter extends BaseListAdapter<SectionObject>{
    private boolean isClose = true;
    public static final int largeSize = 5;
    public boolean isSearch = false;

    public SectionAdapter(Context context, List<SectionObject> data) {
        super(context, data);
    }

    public boolean isDelete = false;
    public List<SectionObject> getList(){
        return this.mList;
    }
    @Override
    public int getCount() {
        if(null == mList)
            return 0;
        if(mList.size() > largeSize && isClose && !isSearch){
            return largeSize + 1;
        }else if(mList.size() > largeSize && !isClose && !isSearch){
            return mList.size() + 1;
        }else{
            return mList.size();
        }
    }

    public void setData(List<SectionObject> data){
        this.mList.clear();
        this.mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_section, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.ivImage = getView(convertView,R.id.ivImage);
            holder.tvThread = getView(convertView,R.id.tvThread);
            holder.tvTitle = getView(convertView,R.id.tvTitle);
            holder.tvToday = getView(convertView,R.id.tvToday);
            holder.btnChoose = getView(convertView,R.id.btnChoose);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SectionObject obj = null;
        if(mList.size() > largeSize){
            holder.layoutContent.setVisibility((position == getCount()-1) ? View.GONE : View.VISIBLE);
            if(position == getCount()-1 && !isSearch){
                holder.layoutContent.setVisibility(View.GONE);
            }else{
                holder.layoutContent.setVisibility(View.VISIBLE);
                obj = mList.get(position);
            }
        }else{
            obj = mList.get(position);
        }
        if(null != obj){
            holder.tvTitle.setText(obj.name);
            holder.tvToday.setText(!TextUtils.isEmpty(obj.todayposts) && !"0".equals(obj.todayposts) ? String.format("(%s)",obj.todayposts) : "");
            holder.tvThread.setText(String.format("主题: %s  |  帖数: %s",obj.threads,obj.posts));
            ImageLoaderUtils.showOnlineImage(obj.icon, holder.ivImage);
            holder.btnChoose.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            obj.isSelected = (obj.isSelected && isDelete);
            mList.set(position, obj);
            holder.btnChoose.setSelected(obj.isSelected);
            final SectionObject finalObj = obj;
            holder.btnChoose.setOnClickListener(v -> {
                finalObj.isSelected = !finalObj.isSelected;
                mList.set(position, finalObj);
                holder.btnChoose.setSelected(finalObj.isSelected);
            });
        }

        return convertView;
    }

    private class Holder {
        View viewSeparate;
        ViewGroup layoutContent;
        CustomImageView ivImage;
        TextView tvTitle;
        TextView tvToday;
        TextView tvThread;
        ImageButton btnChoose;
    }

}
