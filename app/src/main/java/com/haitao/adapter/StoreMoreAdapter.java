package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.haitao.R;
import com.haitao.db.v2.StoreModel;
import com.haitao.model.TagObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class StoreMoreAdapter extends BaseListAdapter<StoreModel> implements SectionIndexer {
    public TagObject category = null;
    public StoreMoreAdapter(Context context, List<StoreModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            holder.tvRebate = getView(convertView,R.id.tvRebate);
            holder.ic_country = getView(convertView,R.id.ic_country);
            holder.tvCategory = getView(convertView,R.id.tvCategory);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        StoreModel obj = mList.get(position);
        if(null != obj) {
            holder.tvName.setText(obj.getName());
            holder.tvRebate.setText(obj.getCashback_view());
            ImageLoaderUtils.showOnlineImage(obj.getCountry_pic(), holder.ic_country);
            String categoryStr = obj.getCategory();
            ArrayList<TagObject> tags = null;
            if(!TextUtils.isEmpty(categoryStr))
                tags = JSON.parseObject(categoryStr, new TypeReference<ArrayList<TagObject>>(){});
            if(null != tags && tags.size() > 0){
                holder.tvCategory.setText(null != category ? category.text : tags.get(0).text);
            }
           /* //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);*/

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            //if (position == getPositionForSection(section)) {
            if(false){
                holder.layoutCatalog.setVisibility(View.VISIBLE);
                holder.catalog.setVisibility(View.VISIBLE);
                holder.catalog.setText(obj.getCharacter());
            } else {
                holder.layoutCatalog.setVisibility(View.GONE);
                if (position == mList.size() - 1) {
                    holder.viewSeparate.setVisibility(View.VISIBLE);
                    holder.viewSeparate.setPadding(0, 0, 0, 0);
                }
            }
        }

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getCharacter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).getCharacter().charAt(0);
    }


    private class Holder {
        ViewGroup layoutCatalog;
        TextView catalog;
        ViewGroup layoutContent;
        TextView tvName;
        View viewSeparate;
        TextView tvRebate;
        CustomImageView ic_country;
        TextView tvCategory;
    }

}
