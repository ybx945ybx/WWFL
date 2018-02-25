package com.haitao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.UnionPayWebActivity;

import java.util.List;

import io.swagger.client.model.OfflineStoreAddressListModelLists;
import io.swagger.client.model.OfflineStoreAddressListModelLists1;

/**
 * Created by a55 on 2017/12/20.
 */

public class UnionPayShopAddressListAdapter extends BaseExpandableListAdapter {

    private Activity                                      mActivity;
    private List<OfflineStoreAddressListModelLists1> lists;
    private String storeName;

    public UnionPayShopAddressListAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setLists(List<OfflineStoreAddressListModelLists1> lists) {
        this.lists = lists;
    }

    @Override
    public int getGroupCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (lists == null) {
            return 0;
        } else {
            OfflineStoreAddressListModelLists1 listModelLists1 = lists.get(groupPosition);
            if (listModelLists1 == null || listModelLists1.getLists() == null) {
                return 0;
            } else {
                return listModelLists1.getLists().size();
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lists == null ? null : lists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (lists == null) {
            return null;
        } else {
            OfflineStoreAddressListModelLists1 listModelLists1 = lists.get(groupPosition);
            if (listModelLists1 == null || listModelLists1.getLists() == null) {
                return null;
            } else {
                return listModelLists1.getLists().get(childPosition);
            }
        }
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.union_pay_shop_address_group, parent, false);
            groupViewHolder.llytGroup = convertView.findViewById(R.id.llyt_group);
            groupViewHolder.tvArea = convertView.findViewById(R.id.tv_area_name);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        OfflineStoreAddressListModelLists1 listModelLists1 = lists.get(groupPosition);
        groupViewHolder.tvArea.setText(listModelLists1.getCityName());

        groupViewHolder.llytGroup.setOnClickListener(v -> {
            return;     //  禁止手动展开收起
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.union_pay_shop_address_child, parent, false);
            childViewHolder.llytChild = convertView.findViewById(R.id.llyt_child);
            childViewHolder.tvAddress = convertView.findViewById(R.id.tv_address);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        OfflineStoreAddressListModelLists listModelLists = lists.get(groupPosition).getLists().get(childPosition);
        childViewHolder.tvAddress.setText(listModelLists.getAddress());
        childViewHolder.llytChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnionPayWebActivity.launch(mActivity, listModelLists.getLatitude(), listModelLists.getLongitude(), storeName);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        LinearLayout llytGroup;
        TextView     tvArea;
    }

    class ChildViewHolder {
        View     viewLine;
        LinearLayout llytChild;
        TextView tvAddress;
    }
}
