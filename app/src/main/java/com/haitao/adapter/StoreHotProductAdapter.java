package com.haitao.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;

import java.util.List;

import io.swagger.client.model.HotGoodsModel;

/**
 * Created by a55 on 2017/12/7.
 */

public class StoreHotProductAdapter extends BaseQuickAdapter<HotGoodsModel, BaseViewHolder> {

    private Context mContext;

    public StoreHotProductAdapter(Context mContext, @Nullable List<HotGoodsModel> data) {
        super(R.layout.store_hot_product_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotGoodsModel item) {
        //  热品图
        ImageLoaderUtils.showOnlineImage(item.getImage(), helper.getView(R.id.iv_product));
        helper.setText(R.id.tv_title, item.getBrandName())
                .setText(R.id.tv_sub_title, item.getTitle())
                .setText(R.id.tv_price, TextUtils.isEmpty(item.getOriginalPrice()) ? item.getCurrencySymbol() + item.getOriginalPrice() : item.getCurrencySymbol() + item.getNowPrice());

        //        // 如果有原价就显示原价，否则显示折扣
        //        if (!TextUtils.isEmpty(obj.discount)) {
        //            holder.tvPrice.setText(obj.discount);
        //        } else {
        //            holder.tvPrice.setText(obj.price);
        //        }
    }
}
