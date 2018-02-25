package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.EnteredStoreModel;

/**
 * 线下返利商家列表适配器
 * Created by a55 on 2017/12/4.
 */

public class UnionpayShopListAdapter extends BaseListAdapter<EnteredStoreModel> {

    public UnionpayShopListAdapter(Context context, List<EnteredStoreModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.union_pay_store_list_item, null);
            //            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivStore = getView(convertView, R.id.iv_store);
            holder.ivCountry = getView(convertView, R.id.ivCountry);
            holder.tvGroup = getView(convertView, R.id.tv_store_group);
            holder.tvRebate = getView(convertView, R.id.tv_rebate);
            holder.tvStore = getView(convertView, R.id.tv_store_name);
            holder.tvFav = getView(convertView, R.id.tv_store_fav);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        EnteredStoreModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.ivStore);
            ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), holder.ivCountry);
            holder.tvStore.setText(obj.getStoreName());
            holder.tvGroup.setText(obj.getCategoryName());
            holder.tvRebate.setText(obj.getRebateView());
            holder.tvRebate.setVisibility(!TextUtils.isEmpty(obj.getRebateView()) ? View.VISIBLE : View.GONE);
            holder.tvFav.setText(String.format(obj.getRebateInfluenceView() + "  ·  " +  obj.getCollectionsCountView()));

        }

        return convertView;
    }

    private class Holder {
        //        View            viewSeparate;
        CustomImageView ivStore;
        CustomImageView ivCountry;
        TextView        tvGroup;
        TextView        tvRebate;
        TextView        tvStore;
        TextView        tvFav;
    }
}
