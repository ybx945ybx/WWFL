package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.InvitedFriendModel;

/**
 * 我的邀请 - Adapter
 *
 * @author 陶声
 * @since 2017-08-15
 */

public class MyInviteAdapter extends BaseListAdapter<InvitedFriendModel> {
    public MyInviteAdapter(Context context, List<InvitedFriendModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_my_invite, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        InvitedFriendModel obj = mList.get(position);
        if (null != obj) {
            // 头像
            ImageLoaderUtils.showOnlineImage(obj.getFriendAvatar(), holder.mImgAvatar);
            convertView.setOnClickListener(v -> TalentDetailActivity.launch(mContext, obj.getFriendUid()));
            // 用户名
            holder.mTvUsername.setText(obj.getFriendName());
            // 分组名
            if (!TextUtils.isEmpty(obj.getFriendGroupName())) {
                holder.mTvGroup.setVisibility(View.VISIBLE);
                holder.mTvGroup.setText(obj.getFriendGroupName());
            }
            // 性别
            if (!TextUtils.isEmpty(obj.getFriendSex()) && !TextUtils.equals(obj.getFriendSex(), "0")) {
                holder.mImgGender.setVisibility(View.VISIBLE);
                holder.mImgGender.setImageResource(TextUtils.equals(obj.getFriendSex(), "1") ? R.mipmap.ic_male : R.mipmap.ic_female);
            } else {
                holder.mImgGender.setVisibility(View.GONE);
            }
            // 如果分组名和性别都没有，则不显示
            if (TextUtils.equals(obj.getFriendSex(), "0") && TextUtils.isEmpty(obj.getFriendGroupName())) {
                Logger.d("invite friend invisible");
                holder.mLlSexGroup.setVisibility(View.INVISIBLE);
            } else {
                holder.mLlSexGroup.setVisibility(View.VISIBLE);
            }
            // 金额
            holder.mTvAmount.setText(obj.getInviteReward());
            // 最后一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_avatar)   CustomImageView mImgAvatar;
        @BindView(R.id.tv_username)  TextView        mTvUsername;
        @BindView(R.id.img_gender)   ImageView       mImgGender;
        @BindView(R.id.tv_group)     TextView        mTvGroup;
        @BindView(R.id.tv_amount)    TextView        mTvAmount;
        @BindView(R.id.divider)      View            mDivider;
        @BindView(R.id.ll_sex_group) LinearLayout    mLlSexGroup;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
