package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.ExchangeObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 金币兑换 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class ExchangeAdapter extends BaseListAdapter<ExchangeObject> {
    public boolean isOver            = false;
    public int     overFirstPosition = 0;
    //是否申请成功
    public boolean isSuccessApply    = false;

    public ExchangeAdapter(Context context, List<ExchangeObject> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exchange, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.layoutStatus = getView(convertView, R.id.layoutStatus);
            holder.tvStatus = getView(convertView, R.id.tvStatus);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvAmount = getView(convertView, R.id.tvAmount);
            holder.tvOver = getView(convertView, R.id.tvOver);
            holder.tvHot = getView(convertView, R.id.tvHot);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ExchangeObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
            holder.tvTitle.setText(obj.name);
            holder.tvStatus.setText("1".equals(obj.status) ? "即将开始" : "2".equals(obj.status) ? "正在进行" : "已结束");
            holder.layoutStatus.setBackgroundResource("1".equals(obj.status) ? R.color.half_blue : "2".equals(obj.status) ? R.color.half_orange : R.color.half_transparent);
            String amount = String.format(mContext.getResources().getString(R.string.exchange_price), obj.prices, obj.timeformat);
            //CharSequence charsProvider = ColorPhrase.from(parseSampleValue(amount)).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
            holder.tvAmount.setText(amount);
            holder.tvHot.setText(obj.hot);
            if ("3".equals(obj.status)) {
                if (isOver && position == overFirstPosition) {
                    holder.tvOver.setVisibility(View.VISIBLE);
                } else if (!isOver) {
                    holder.tvOver.setVisibility(View.VISIBLE);
                    overFirstPosition = position;
                    isOver = true;
                } else {
                    holder.tvOver.setVisibility(View.GONE);
                }
            } else {
                holder.tvOver.setVisibility(View.GONE);
            }
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivImage;
        ViewGroup       layoutStatus;
        TextView        tvStatus;
        TextView        tvTitle;
        TextView        tvAmount;
        TextView        tvOver;
        TextView        tvHot;
    }

    /**
     * 重组试用中的数字
     *
     * @param str
     * @return
     */
    public static String parseSampleValue(String str) {
        String regApp = "([0-9\\.]+)";
        str = str.replaceAll(regApp, "{$1}");
        return str;
    }
}
