package com.haitao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.SlidePicModel;

/**
 * Created by tqy on 2015/11/20.
 */
public class AdAdapter extends BaseListAdapter<SlidePicModel>{


    public AdAdapter(Context context, List<SlidePicModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ad, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            int screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
            int width = (int)(screenWidth);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int) (width / (3f / 1f)));
            //lp.setMargins((int)mContext.getResources().getDimension(R.dimen.px15),0,(int)mContext.getResources().getDimension(R.dimen.px15),0);
            holder.ivImage.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SlidePicModel obj = mList.get(position);
        if(null != obj){
            ImageLoaderUtils.showOnlineImage(obj.getPic(), holder.ivImage);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
    }

}
