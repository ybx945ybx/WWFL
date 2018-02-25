package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.db.v2.TransportModel;
import com.haitao.model.TagObject;
import com.haitao.model.TransportFilterObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;

/**
 * 转运列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TransportParentAdapter extends BaseExpandableListAdapter {
    public TagObject category = null;
    private   ArrayList<TransportFilterObject> mList;
    private   Context                          mContext;
    protected LayoutInflater                   mInflater;
    public boolean isSearch = false;

    public TransportParentAdapter(Context mContext, ArrayList<TransportFilterObject> mList) {
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
        return mList.get(groupPosition).list.size();
    }

    @Override
    public TransportFilterObject getGroup(int groupPosition) {
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
            convertView = inflater.inflate(R.layout.item_transport_group, null);
            holder.layoutCatalog = getView(convertView, R.id.layoutCatalog);
            holder.catalog = getView(convertView, R.id.catalog);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.rbStar = getView(convertView, R.id.rbStar);
            holder.tvStar = getView(convertView, R.id.tvStar);
            holder.tvCount = getView(convertView, R.id.tvCount);
            holder.imgStoreLabel = getView(convertView, R.id.img_store_label);
            holder.layoutCatalog.setVisibility(View.VISIBLE);
            holder.layoutContent.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TransportFilterObject obj = mList.get(groupPosition);
        if (null != obj) {
            holder.catalog.setText(obj.char_name);
            holder.layoutCatalog.setVisibility(isSearch ? View.GONE : View.VISIBLE);
        }
//        holder.imgStoreLabel.setVisibility(HtApplication.isActivityOn ? View.VISIBLE : View.GONE);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_transport_group, null);
            holder.layoutCatalog = getView(convertView, R.id.layoutCatalog);
            holder.catalog = getView(convertView, R.id.catalog);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.rbStar = getView(convertView, R.id.rbStar);
            holder.tvStar = getView(convertView, R.id.tvStar);
            holder.tvCount = getView(convertView, R.id.tvCount);
            holder.layoutCatalog.setVisibility(View.GONE);
            holder.layoutContent.setVisibility(View.VISIBLE);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TransportModel obj = mList.get(groupPosition).list.get(childPosition);
        if (null != obj) {
            holder.tvTitle.setText(obj.getName());
            holder.rbStar.setRating(Float.valueOf(obj.getStart_number()));
            holder.tvStar.setText(Float.valueOf(obj.getStart_number()) > 0 ? obj.getStart_number() + "星" : "暂无评分");
            holder.tvCount.setText(String.format("%s个晒单 | %s人收藏", obj.getThread_count(), obj.getCollection_count()));
            ImageLoaderUtils.showOnlineImage(obj.getLogo(), holder.ivImage);
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
        View            viewSeparate;
        CustomImageView ivImage;
        TextView        tvTitle;
        RatingBar       rbStar;
        TextView        tvStar;
        TextView        tvCount;
        ImageView       imgStoreLabel;
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
