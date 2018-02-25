package com.haitao.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.PostObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 无利转让
 * Created by tqy on 2015/11/20.
 */
public class TransferRecommendAdapter extends BaseListAdapter<PostObject>{

    public TransferRecommendAdapter(Context context, List<PostObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        parent.clearFocus();
        parent.setFocusable(false);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_transfer_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView,R.id.tvTitle);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            float density = dm.density;
            int gridviewWidth = (int) (130*density);
            holder.ivImage.setLayoutParams(new RelativeLayout.LayoutParams(gridviewWidth,gridviewWidth));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PostObject obj = mList.get(position);
        if(null != obj){
            ImageLoaderUtils.showOnlineImage(obj.pic,holder.ivImage);
            holder.tvTitle.setText(obj.title);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView tvTitle;
    }

}
