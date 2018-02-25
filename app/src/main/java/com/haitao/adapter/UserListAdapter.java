package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.FriendModel;

/**
 * 好友列表 - Adapter
 *
 * @author 陶声
 * @version 5.2.2
 * @since 2017-06-14
 */

public class UserListAdapter extends BaseListAdapter<FriendModel> {
    public UserListAdapter(Context context, List<FriendModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_user, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        FriendModel obj = mList.get(position);
        if (null != obj) {
            // 头像
            ImageLoaderUtils.showOnlineImage(obj.getFriendAvatar(), holder.mImgAvatar);
            // 用户名
            holder.mTvUsername.setText(obj.getFriendName());
            // 等级
            if (!TextUtils.isEmpty(obj.getFriendLevel())) {
                holder.mTvLevel.setVisibility(View.VISIBLE);
                holder.mTvLevel.setText("等级" + obj.getFriendLevel());
            }
            // 组别
            if (!TextUtils.isEmpty(obj.getFriendGroupName())) {
                holder.mTvGroup.setVisibility(View.VISIBLE);
                holder.mTvGroup.setText(obj.getFriendGroupName());
            }
            // 性别
            if ("0".equals(obj.getFriendSex())) {
                holder.mImgGender.setVisibility(View.GONE);
            } else {
                holder.mImgGender.setVisibility(View.VISIBLE);
                holder.mImgGender.setImageResource("1".equals(obj.getFriendSex()) ? R.mipmap.ic_male : R.mipmap.ic_female);
            }
            // 最后一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_avatar)  CustomImageView mImgAvatar;
        @BindView(R.id.tv_username) TextView        mTvUsername;
        @BindView(R.id.tv_level)    TextView        mTvLevel;
        @BindView(R.id.tv_group)    TextView        mTvGroup;
        @BindView(R.id.img_gender)  ImageView       mImgGender;
        @BindView(R.id.ll_content)  LinearLayout    mLlContent;
        @BindView(R.id.divider)     View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
