package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.ForumIndexBoardModel;

/**
 * Created by tqy on 2015/11/20.
 */
public class SectionIndexAdapter extends BaseListAdapter<ForumIndexBoardModel>{

    public SectionIndexAdapter(Context context, List<ForumIndexBoardModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_section_index, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvName = getView(convertView,R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ForumIndexBoardModel obj = mList.get(position);
        if(null != obj){
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.ivImage);
            holder.tvName.setText(obj.getIconTitle());
            GenericDraweeHierarchy hierarchy = holder.ivImage.getHierarchy();
            hierarchy.setActualImageScaleType(position < (getCount()-1) ? ScalingUtils.ScaleType.FIT_CENTER : ScalingUtils.ScaleType.CENTER_INSIDE);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView tvName;
    }

}
