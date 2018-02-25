package com.haitao.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.model.StoreObject;
import com.haitao.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class StoreRecommendAdapter extends BaseListAdapter<StoreObject>{
    public boolean isParentList = true;
    public StoreRecommendAdapter(Context context, List<StoreObject> data) {
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
            convertView = inflater.inflate(R.layout.item_store_recommend, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvRebate = getView(convertView,R.id.tvRebate);
            holder.tvOldRebate = getView(convertView,R.id.tvOldRebate);
            holder.tvOldRebate.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if(parent.getChildCount() == position)
        {
            final StoreObject obj = mList.get(position);
            if(null != obj){
                ImageLoaderUtils.showOnlineImage(obj.pic,holder.ivImage);
                holder.tvRebate.setText(obj.cashback_view);
                holder.tvOldRebate.setText(obj.old_cashback_view);
                holder.tvOldRebate.setVisibility(TextUtils.isEmpty(obj.old_cashback_view) ? View.GONE : View.VISIBLE);
                convertView.setOnClickListener(v -> StoreDetailActivity.launch(mContext,obj.id));
            }
        }
        return convertView;
    }

    private class Holder {
        ImageView ivImage;
        TextView tvRebate;
        TextView tvOldRebate;
    }

}
