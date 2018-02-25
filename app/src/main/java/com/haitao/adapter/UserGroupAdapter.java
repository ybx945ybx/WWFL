package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.TagObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.FriendModel;
import io.swagger.client.model.FriendsSectionsModelData;

/**
 * 好友分组列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class UserGroupAdapter extends BaseExpandableListAdapter {
    public TagObject category = null;
    private   ArrayList<FriendsSectionsModelData> mList;
    private   Context                             mContext;
    protected LayoutInflater                      mInflater;
    public boolean isSearch = false;

    public UserGroupAdapter(Context mContext, ArrayList<FriendsSectionsModelData> mList) {
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
        if (null == mList.get(groupPosition).getFriends())
            return 0;
        return mList.get(groupPosition).getFriends().size();
    }

    @Override
    public FriendsSectionsModelData getGroup(int groupPosition) {
        return null != mList ? mList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getFriends().get(childPosition);
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
        UserSectionViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_section_user, null);
            holder = new UserSectionViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserSectionViewHolder) convertView.getTag();
        }
        FriendsSectionsModelData obj = mList.get(groupPosition);
        if (null != obj) {
            holder.mTvSectionName.setText(obj.getLetter());
            holder.mLlSectionName.setVisibility(isSearch ? View.GONE : View.VISIBLE);
            holder.mViewDivider.setVisibility(mList.indexOf(obj) == 0 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        UserViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user_new, null);
            holder = new UserViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }
        FriendModel obj = mList.get(groupPosition).getFriends().get(childPosition);
        if (null != obj) {
            // 头像
            ImageLoaderUtils.showOnlineImage(obj.getFriendAvatar(), holder.mImgAvatar);
            // 用户名
            holder.mTvUsername.setText(obj.getFriendName());
            // 等级
            /*if (!TextUtils.isEmpty(obj.getFriendLevel())) {
                holder.mTvLevel.setVisibility(View.VISIBLE);
                holder.mTvLevel.setText("等级" + obj.getFriendLevel());
            }*/
            // 组别
            if (!TextUtils.isEmpty(obj.getFriendGroupName())) {
                holder.mTvGroup.setVisibility(View.VISIBLE);
                holder.mTvGroup.setText(obj.getFriendGroupName());
            } else {
                holder.mTvGroup.setVisibility(View.GONE);
            }
            // 性别
            if ("0".equals(obj.getFriendSex())) {
                holder.mImgGender.setVisibility(View.GONE);
            } else {
                holder.mImgGender.setVisibility(View.VISIBLE);
                holder.mImgGender.setImageResource("1".equals(obj.getFriendSex()) ? R.mipmap.ic_male : R.mipmap.ic_female);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class UserViewHolder {
        @BindView(R.id.img_avatar)  CustomImageView mImgAvatar;
        @BindView(R.id.tv_username) TextView        mTvUsername;
        @BindView(R.id.tv_group)    TextView        mTvGroup;
        @BindView(R.id.img_gender)  ImageView       mImgGender;

        UserViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class UserSectionViewHolder {
        @BindView(R.id.tv_section_name) TextView     mTvSectionName;
        @BindView(R.id.ll_section_name) LinearLayout mLlSectionName;
        @BindView(R.id.view_divider)    View         mViewDivider;

        UserSectionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
