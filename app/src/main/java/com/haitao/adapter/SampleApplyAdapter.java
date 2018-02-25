package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.SampleApplyObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class SampleApplyAdapter extends BaseListAdapter<SampleApplyObject>{

    public boolean isSuccess = false;
    public int successPosition = 0;
    public boolean isFail = false;
    public int failPosition = 0;

    public SampleApplyAdapter(Context context, List<SampleApplyObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_sample_apply, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvName = getView(convertView,R.id.tvName);
            holder.tvStatus = getView(convertView,R.id.tvStatus);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SampleApplyObject obj = mList.get(position);
        if(null != obj){
            ImageLoaderUtils.showOnlineImage(obj.avatar, holder.ivImage);
            holder.tvName.setText(obj.username);
            if(isSuccess && position == successPosition && "1".equals(obj.status)){
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(R.string.sample_detail_success);
            }else if(!isSuccess && "1".equals(obj.status)){
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(R.string.sample_detail_success);
                successPosition = position;
                isSuccess = true;
            }else if(isFail && position == failPosition && "0".equals(obj.status)){
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(R.string.sample_detail_member);
            }else if(!isFail && "0".equals(obj.status)){
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(R.string.sample_detail_member);
                failPosition = position;
                isFail = true;
            }else {
                holder.tvStatus.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView tvName;
        TextView tvStatus;
    }

}
