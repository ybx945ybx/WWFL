package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.CurrenciesIfModelData;

/**
 * 汇率换算 - adapter
 * Created by tqy on 2015/11/20.
 */
public class DiscountExchangeAdapter extends BaseListAdapter<CurrenciesIfModelData> {
    public String currency_abbr = "";

    public DiscountExchangeAdapter(Context context, List<CurrenciesIfModelData> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_discount_exchange, null);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivLogo = getView(convertView, R.id.ivLogo);
            holder.ivStatus = getView(convertView, R.id.ivStatus);
            holder.divider = getView(convertView, R.id.divider);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        CurrenciesIfModelData obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getCountryFlag(), holder.ivLogo);
            holder.tvName.setText(obj.getCurrencyView());
            holder.ivStatus.setSelected(currency_abbr.equals(obj.getCurrencyAbbr()));
            // 最后一条隐藏分割线
            holder.divider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        CustomImageView ivLogo;
        ImageView       ivStatus;
        TextView        tvName;
        View            divider;
    }

}
