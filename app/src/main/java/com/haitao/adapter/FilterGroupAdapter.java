package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.haitao.R;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.IfFilterModel;

/**
 * 筛选分组Adapter
 *
 * @author 陶声
 * @since 2017-08-08
 */
public class FilterGroupAdapter extends BaseListAdapter<IfFilterModel> {

    private String[] mSelectedIds;
    private boolean  mHasSelected;

    public FilterGroupAdapter(Context context, List<IfFilterModel> data) {
        super(context, data);
        mSelectedIds = new String[data.size()];
    }

    public FilterGroupAdapter(Context context, List<IfFilterModel> data, String[] selectedIds) {
        super(context, data);
        mSelectedIds = selectedIds;
        Logger.d(mSelectedIds);
        mHasSelected = true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_filter_tag_group, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IfFilterModel obj = mList.get(position);
        if (null != obj) {
            // 分组标题
            holder.tvTitle.setText(obj.getTitle());
            // 分组Tag列表
            FilterTagAdapter filterTagAdapter = new FilterTagAdapter(mContext, obj.getValues());
            if (mHasSelected) {
                filterTagAdapter.setCurrentPositionByValue(mSelectedIds[position]);
            }
            holder.gvTags.setAdapter(filterTagAdapter);
            holder.gvTags.setOnItemClickListener((parent1, view, position1, id) -> {
                mSelectedIds[position] = getItem(position).getValues().get(position1).getValue();
                filterTagAdapter.setCurrentPosition(position1);
            });
        }
        return convertView;
    }

    /**
     * 获取已选的Id
     *
     * @return 已选Id
     */
    public String[] getSelectedIds() {
        return mSelectedIds;
    }

    /**
     * 设置已选的Id
     */
    public void setSelectedIds(String[] selectedIds) {
        mSelectedIds = selectedIds;
    }

    /**
     * 重置
     */
    public void clearSelection() {
        for (String selectedId : mSelectedIds) {
            selectedId = "";
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.gv_tags)  GridView gvTags;
        @BindView(R.id.tv_title) TextView tvTitle;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
