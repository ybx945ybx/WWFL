package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.IfFilterModelValues;


/**
 * 筛选Tag Adapter
 */
public class FilterTagAdapter extends BaseListAdapter<IfFilterModelValues> {
    private int               mCurrentPosition; // 当前选中位置
    private ArrayList<String> mValues;          // Value值列表

    public FilterTagAdapter(Context context, List<IfFilterModelValues> data) {
        super(context, data);
        mCurrentPosition = 0;
        mValues = new ArrayList<>(data.size());
        for (IfFilterModelValues modelValues : data) {
            mValues.add(modelValues.getValue());
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_tag, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mTagBtn.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IfFilterModelValues obj = getItem(position);
        if (obj != null) {
            holder.mTagBtn.setText(obj.getText());
            holder.mTagBtn.setSelected(position == mCurrentPosition);
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
        @BindView(R.id.tag_btn) TextView mTagBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
