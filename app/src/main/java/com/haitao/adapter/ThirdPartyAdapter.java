package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.ThirdPartyPlatformModel;

/**
 * 社交账号绑定 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class ThirdPartyAdapter extends BaseListAdapter<ThirdPartyPlatformModel> {

    public ThirdPartyAdapter(Context context, List<ThirdPartyPlatformModel> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_third_party, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 填充数据
        final ThirdPartyPlatformModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.mIvImage);
            holder.mTvName.setText(obj.getTppName());
            holder.mTvBind.setText("0".equals(obj.getBinded()) ? "立即绑定" : "解除绑定");
            //            holder.mTvBind.setTextColor("0".equals(obj.getBinded()) ? ContextCompat.getColor(mContext, R.color.orangeFF804D) : ContextCompat.getColor(mContext, R.color.middle_grey2));
            holder.mTvBind.setEnabled("0".equals(obj.getBinded()));
            holder.mTvBind.setSelected("1".equals(obj.getBinded()));
            // 最后一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)  CustomImageView mIvImage;
        @BindView(R.id.tvName)   TextView        mTvName;
        @BindView(R.id.tvBind)   TextView        mTvBind;
        @BindView(R.id.layoutQQ) RelativeLayout  mLayoutQQ;
        @BindView(R.id.divider)  View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
