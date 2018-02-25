package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.InviterModel;

/**
 * 邀请排行榜 - Adapter
 *
 * @author 陶声
 * @since 2017-08-15
 */

public class InviteRankingAdapter extends BaseListAdapter<InviterModel> {
    public InviteRankingAdapter(Context context, List<InviterModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_invite_ranking, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        InviterModel obj = mList.get(position);
        if (null != obj) {
            // 用户名
            holder.mTvUsername.setText(obj.getUsername());
            // 分组名
            holder.mTvDesc.setText(obj.getInvitedCountView());
            // 金额
            holder.mTvAmount.setText(obj.getRewardView());
            // 头像
            ImageLoaderUtils.showOnlineImage(obj.getUserAvatar(), holder.mImgAvatar);
            convertView.setOnClickListener(v -> TalentDetailActivity.launch(mContext, obj.getUid()));
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ?
                    View.GONE : View.VISIBLE);
        }
        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.img_avatar)  CustomImageView mImgAvatar;
        @BindView(R.id.tv_username) TextView        mTvUsername;
        @BindView(R.id.tv_desc)     TextView        mTvDesc;
        @BindView(R.id.tv_amount)   TextView        mTvAmount;
        @BindView(R.id.divider)     View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
