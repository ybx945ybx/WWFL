package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.model.StoreObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtil;

import java.util.ArrayList;

import io.swagger.client.model.BindBankCardSuccessModelData;
import io.swagger.client.model.EnteredStoreModel;

/**
 * 线下返利绑卡成功后弹窗九宫格商家列表适配器
 * Created by a55 on 2017/12/4.
 */

public class UnionpayShopGridAdapter extends RVBaseAdapter<BindBankCardSuccessModelData> {

    private int itemHeight;     //  item的高度

    public UnionpayShopGridAdapter(Context context, int itemHeight, ArrayList<BindBankCardSuccessModelData> arrayList, int resId) {
        super(context, arrayList, resId);
        this.itemHeight = itemHeight;
    }

    @Override
    public void bindView(RVBaseHolder holder, BindBankCardSuccessModelData storeObject) {
        String rebate = storeObject.getRebateView();
        if (!TextUtils.isEmpty(rebate) && rebate.contains("线下返利最高")) {
            rebate = rebate.replace("线下返利最高", "");
        } else if (!TextUtils.isEmpty(rebate) && rebate.contains("线下返利")) {
            rebate = rebate.replace("线下返利", "");
        }
        holder.setText(R.id.tv_back_num, rebate);
        ImageLoaderUtils.showOnlineImage(storeObject.getStoreLogo(), holder.getView(R.id.iv_shop));
        holder.itemView.setTag(storeObject.getStoreId());
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = itemHeight;
        holder.itemView.setLayoutParams(lp);
    }
}
