package com.haitao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.GoldRecordObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的金币 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class GoldRecordAdapter extends BaseListAdapter<GoldRecordObject> {

    public GoldRecordAdapter(Context context, List<GoldRecordObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gold_record, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoldRecordObject obj = mList.get(position);
        if (null != obj) {
            // 图标
//            ImageLoaderUtils.showOnlineImage(obj.pic, holder.mImg);
            // 类型
            holder.mTvDesc.setText(obj.operation);
            // 金额
            holder.mTvGold.setText(obj.gold);
            holder.mTvGold.setTextColor(obj.gold.startsWith("+") ?
                    ContextCompat.getColor(mContext, R.color.orangeFF804D) : ContextCompat.getColor(mContext, R.color.warmGrey));
            holder.mTvTime.setText(obj.time_view);
            // 最后一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        //        @BindView(R.id.img)     CustomImageView mImg;
        @BindView(R.id.tv_time) TextView mTvTime;
        @BindView(R.id.tv_desc) TextView mTvDesc;
        @BindView(R.id.tv_gold) TextView mTvGold;
        @BindView(R.id.divider) View     mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
