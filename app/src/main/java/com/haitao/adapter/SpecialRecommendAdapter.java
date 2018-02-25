package com.haitao.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.SlidePicModel;

/**
 * 推荐专题-Adapter
 * Created by tqy on 2015/11/20.
 */
public class SpecialRecommendAdapter extends BaseListAdapter<SlidePicModel> {

    public SpecialRecommendAdapter(Context context, List<SlidePicModel> data) {
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
            convertView = inflater.inflate(R.layout.item_special_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            DisplayMetrics              dm             = mContext.getResources().getDisplayMetrics();
            float                       density        = dm.density;
            int                         gridviewWidth  = (int) (144 * density);
            int                         gridviewHeight = gridviewWidth / 2;
            RelativeLayout.LayoutParams layoutParams   = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            layoutParams.width = gridviewWidth;
            layoutParams.height = gridviewHeight;
            holder.ivImage.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SlidePicModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getPic(), holder.ivImage);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
    }

}
