package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.IfFilterModelValues;

/**
 * 商家列表页面{@link com.haitao.activity.StoreActivity} 筛选标签Adapter
 *
 * @author 陶声
 * @since 2017-12-07
 */
public class StoreFilterTagAdapter extends BaseListAdapter<IfFilterModelValues> {
    private int               mCurrentPosition; // 当前选中位置
    private ArrayList<String> mValues;          // Value值列表

    public StoreFilterTagAdapter(Context context, List<IfFilterModelValues> data) {
        super(context, data);
        mValues = new ArrayList<>(data.size());
        for (IfFilterModelValues modelValues : data) {
            mValues.add(modelValues.getValue());
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_store_filter_tag, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final IfFilterModelValues obj = mList.get(position);
        if (null != obj) {
            // 第一个元素，设置左边距
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mTvTagName.getLayoutParams();
            lp.leftMargin = DensityUtil.dip2px(mContext, mList.indexOf(obj) == 0 ? 16 : 0);
            holder.mTvTagName.setLayoutParams(lp);
            // 标签名
            holder.mTvTagName.setText(obj.getText());
            // 标签状态
            holder.mTvTagName.setSelected(position == mCurrentPosition);
        }
        return convertView;
    }

    /**
     * 设置当前选中位置
     *
     * @param position 位置
     */
    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 根据Value值设置当前选中位置
     *
     * @param value value
     */
    public void setCurrentPositionByValue(String value) {
        mCurrentPosition = mValues.indexOf(value);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_tag_name) TextView mTvTagName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
