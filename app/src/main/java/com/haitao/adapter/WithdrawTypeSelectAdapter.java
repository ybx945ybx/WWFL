package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.UserWithdrawingModeModel;

/**
 * 选择提现方式列表 - Adapter
 *
 * @author 陶声
 * @since 2018-01-29
 */
public class WithdrawTypeSelectAdapter extends BaseListAdapter<UserWithdrawingModeModel> {
    private String mSelectAccountId;

    public WithdrawTypeSelectAdapter(Context context, List<UserWithdrawingModeModel> data, String selectAccountId) {
        super(context, data);
        mSelectAccountId = selectAccountId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pay_style_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserWithdrawingModeModel obj = mList.get(position);
        if (null != obj) {
            holder.mTvName.setText(String.format("%s %s", obj.getModeName(), obj.getAccount()));
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.mIvImage);
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
            holder.mImgStatus.setSelected(TextUtils.equals(obj.getAccountId(), mSelectAccountId));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)    CustomImageView mIvImage;
        @BindView(R.id.tvName)     TextView        mTvName;
        @BindView(R.id.img_status) ImageView       mImgStatus;
        @BindView(R.id.divider)    View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
