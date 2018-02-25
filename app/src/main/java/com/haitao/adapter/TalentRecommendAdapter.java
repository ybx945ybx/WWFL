package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.TalentModel;

/**
 * 推荐达人 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TalentRecommendAdapter extends BaseListAdapter<TalentModel> {

    public TalentRecommendAdapter(Context context, List<TalentModel> data) {
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
            convertView = inflater.inflate(R.layout.item_talent_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.layoutMore = getView(convertView, R.id.layoutMore);

            /*DisplayMetrics dm             = mContext.getResources().getDisplayMetrics();
            float          density        = dm.density;
            int            gridviewWidth  = (int) (60 * density);
            int            gridviewHeight = gridviewWidth / 1;

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            layoutParams.width = gridviewWidth;
            layoutParams.height = gridviewHeight;
            holder.ivImage.setLayoutParams(layoutParams);*/
            holder.tvName = getView(convertView, R.id.tvName);
            holder.tvLevel = getView(convertView, R.id.tvLevel);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TalentModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getAvatar(), holder.ivImage);
            holder.tvName.setText(obj.getUsername());
            holder.tvLevel.setText(obj.getCategory());
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvLevel.setVisibility(View.VISIBLE);
            holder.layoutMore.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
        } else {
            holder.tvName.setVisibility(View.GONE);
            holder.tvLevel.setVisibility(View.GONE);
            holder.layoutMore.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView        tvName;
        TextView        tvLevel;
        ViewGroup       layoutMore;
    }

}
