package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.haitao.R;
import com.haitao.db.v2.StoreModel;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.TagObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;

/**
 * Created by tqy on 2015/11/20.
 */
public class StoreParentAdapter extends BaseExpandableListAdapter {
    public TagObject category = null;
    private   ArrayList<StoreFilterObject> mList;
    private   Context                      mContext;
    protected LayoutInflater               mInflater;
    public boolean isSearch = false;

    public StoreParentAdapter(Context mContext, ArrayList<StoreFilterObject> mList) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        if (null == mList)
            return 0;
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null == mList)
            return 0;
        if (null == mList.get(groupPosition))
            return 0;
        if (null == mList.get(groupPosition).list)
            return 0;
        return mList.get(groupPosition).list.size() > 3 && !isSearch ? 3 : mList.get(groupPosition).list.size();
    }

    @Override
    public StoreFilterObject getGroup(int groupPosition) {
        return null != mList ? mList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_store_group, null);
            holder.layoutCatalog = getView(convertView, R.id.layoutCatalog);
            holder.catalog = getView(convertView, R.id.catalog);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.ic_country = getView(convertView, R.id.ic_country);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            holder.layoutCatalog.setVisibility(View.VISIBLE);
            holder.layoutContent.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        StoreFilterObject obj = mList.get(groupPosition);
        if (null != obj) {
            holder.catalog.setText(obj.char_name);
            holder.layoutCatalog.setVisibility(isSearch ? View.GONE : View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_store_group, null);
            holder.layoutCatalog = getView(convertView, R.id.layoutCatalog);
            holder.catalog = getView(convertView, R.id.catalog);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvRebate = getView(convertView, R.id.tvRebate);
            holder.ic_country = getView(convertView, R.id.ic_country);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            holder.layoutCatalog.setVisibility(View.GONE);
            holder.layoutContent.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        StoreModel obj = mList.get(groupPosition).list.get(childPosition);
        if (null != obj) {
            holder.tvName.setText(obj.getName());
            holder.tvRebate.setText(obj.getCashback_view());
            ImageLoaderUtils.showOnlineImage(obj.getCountry_pic(), holder.ic_country);
            String               categoryStr = obj.getCategory();
            ArrayList<TagObject> tags        = null;
            if (!TextUtils.isEmpty(categoryStr))
                tags = JSON.parseObject(categoryStr, new TypeReference<ArrayList<TagObject>>() {
                });
            if (null != tags && tags.size() > 0) {
                holder.tvCategory.setText(null != category ? category.text : tags.get(0).text);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class Holder {
        ViewGroup       layoutCatalog;
        TextView        catalog;
        ViewGroup       layoutContent;
        TextView        tvName;
        View            viewSeparate;
        TextView        tvRebate;
        CustomImageView ic_country;
        TextView        tvCategory;
    }

    /**
     * 通过泛型来简化findViewById
     */
    protected final <E extends View> E getView(View v, int id) {
        try {
            return (E) v.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

}
