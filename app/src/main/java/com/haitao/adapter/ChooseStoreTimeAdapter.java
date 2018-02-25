package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.TagObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.StoresRecordsSection;
import io.swagger.client.model.VisitedStoreRecordModel;

/**
 * 选择商家 & 时间 - Adapter
 */
public class ChooseStoreTimeAdapter extends BaseExpandableListAdapter {
    public TagObject category       = null;
    public String    mSelectStoreId = "";
    public String    mSelectTime    = "";


    private   ArrayList<StoresRecordsSection> mList;
    private   Context                         mContext;
    protected LayoutInflater                  mInflater;
    public boolean isSearch = false;

    public ChooseStoreTimeAdapter(Context context, ArrayList<StoresRecordsSection> list) {
        mList = list;
        mContext = context;
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
        if (null == mList.get(groupPosition).getStoresRecords())
            return 0;
        return mList.get(groupPosition).getStoresRecords().size();
    }

    @Override
    public StoresRecordsSection getGroup(int groupPosition) {
        return null != mList ? mList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getStoresRecords().get(childPosition);
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
        StoreTimeSectionHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_section_store_time, null);
            holder = new StoreTimeSectionHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StoreTimeSectionHolder) convertView.getTag();
        }
        StoresRecordsSection obj = mList.get(groupPosition);
        if (null != obj) {
            holder.mTvSectionName.setText(obj.getSectionTitle());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_order_lost_feedback, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        VisitedStoreRecordModel obj = mList.get(groupPosition).getStoresRecords().get(childPosition);
        if (null != obj) {
            // 商家图
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.mImgStoreLogo);
            // 商家名
            holder.mTvStoreName.setText(obj.getStoreName());
            // 时间
            holder.mTvTime.setText(obj.getVisitTime());
            // 商家不允许丢单反馈
            holder.mTvFeedbackNotSupport.setVisibility(TextUtils.equals(obj.getAllowAppeal(), "0") ? View.VISIBLE : View.GONE);
            // 是否显示勾选圆圈
            holder.mImgCheck.setVisibility(TextUtils.equals(obj.getAllowAppeal(), "0") ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(mSelectStoreId) && !TextUtils.isEmpty(mSelectTime)) {
                holder.mImgCheck.setSelected(mSelectStoreId.equals(obj.getStoreId()) && mSelectTime.equals(obj.getVisitTime()));
            }
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class StoreTimeSectionHolder {
        @BindView(R.id.tv_section_name) TextView mTvSectionName;

        StoreTimeSectionHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    static class ChildHolder {
        @BindView(R.id.img_store_logo)          CustomImageView mImgStoreLogo;
        @BindView(R.id.tv_store_name)           TextView        mTvStoreName;
        @BindView(R.id.tv_time)                 TextView        mTvTime;
        @BindView(R.id.img_check)               ImageView       mImgCheck;
        @BindView(R.id.tv_feedback_not_support) TextView        mTvFeedbackNotSupport;

        ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
