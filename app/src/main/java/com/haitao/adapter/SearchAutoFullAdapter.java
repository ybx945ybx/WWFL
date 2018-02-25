package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.CompletingWordsIfModelData;

/**
 * 搜索autofill列表-Adapter
 * Created by a55 on 2017/9/25.
 */

public class SearchAutoFullAdapter extends BaseListAdapter<CompletingWordsIfModelData> {

    public SearchAutoFullAdapter(Context context, List<CompletingWordsIfModelData> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_search_history, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CompletingWordsIfModelData item = mList.get(position);
        if (item != null) {
            // 商家名
            holder.mTvName.setText(item.getKeywords());
            // 返利信息
            if (!TextUtils.isEmpty(item.getRebateView())) {
                holder.mLlRebate.setVisibility(View.VISIBLE);

                String rebateView = item.getRebateView();
                if (rebateView.contains("返利最高")) {
                    holder.mTvRebateView.setText("返利最高");
                    rebateView = rebateView.replace("返利最高", "");
                    rebateView = rebateView.replace("%", "");
                    holder.mTvRebateRate.setText(rebateView);
                } else if (rebateView.contains("返利")) {
                    holder.mTvRebateView.setText("返利");
                    rebateView = rebateView.replace("返利", "");
                    rebateView = rebateView.replace("%", "");
                    holder.mTvRebateRate.setText(rebateView);
                }
            } else {
                holder.mLlRebate.setVisibility(View.GONE);
            }
        }
        holder.mViewLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvName)         TextView     mTvName;
        @BindView(R.id.llyt_rebate)    LinearLayout mLlRebate;
        @BindView(R.id.tv_rebate_view) TextView     mTvRebateView;
        @BindView(R.id.tv_rebate_rate) TextView     mTvRebateRate;
        @BindView(R.id.viewSeparate)   View         mViewLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}