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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.VisitedStoreRecordModel;

/**
 * 选择商家 & 时间 - 搜索列表 - Adapter
 */
public class StoreTimeListAdapter extends BaseListAdapter<VisitedStoreRecordModel> {
    public StoreTimeListAdapter(Context context, ArrayList<VisitedStoreRecordModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_order_lost_feedback, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VisitedStoreRecordModel obj = mList.get(position);
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
            // 第一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == 0 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_store_logo)          CustomImageView mImgStoreLogo;
        @BindView(R.id.tv_store_name)           TextView        mTvStoreName;
        @BindView(R.id.tv_time)                 TextView        mTvTime;
        @BindView(R.id.img_check)               ImageView       mImgCheck;
        @BindView(R.id.tv_feedback_not_support) TextView        mTvFeedbackNotSupport;
        @BindView(R.id.divider)                 View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
