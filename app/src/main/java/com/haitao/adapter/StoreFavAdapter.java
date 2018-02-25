package com.haitao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.CollectionStoreModel;

/**
 * 收藏商家 - Adapter
 *
 * @author 陶声
 * @version 5.2.2
 * @since 2017-06-14
 */
public class StoreFavAdapter extends BaseListAdapter<CollectionStoreModel> {

    public StoreFavAdapter(Context context, List<CollectionStoreModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_vip_store, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        CollectionStoreModel obj = mList.get(position);
        if (null != obj) {
            // 商家名
            holder.mTvStoreName.setText(obj.getStoreName());
            // 商家类型
            holder.mTvStoreType.setText(obj.getCategoryName());
            // 普通返利
            if (TextUtils.equals(obj.getUserHasRebate(), "1")) {
                holder.mTvNormalRebate.setVisibility(View.VISIBLE);
                holder.mTvNormalRebate.setText(obj.getRebateView());
                holder.mTvNormalRebate.setBackground(TextUtils.equals(obj.getUserHasRebate(), "0") ?
                        ContextCompat.getDrawable(mContext, R.drawable.border_round_corner_grey) : ContextCompat.getDrawable(mContext, R.drawable.border_round_corner_orange));
            } else {
                holder.mTvNormalRebate.setVisibility(View.INVISIBLE);
            }
            // VIP返利
            holder.mTvVipRebate.setVisibility(View.GONE);
            //            holder.mTvVipRebate.setText(obj.getVipRebateView());
            // 数量信息
            holder.mTvCount.setText(String.format("%s | %s", obj.getOrdersCountView(), obj.getCollectionCountView()));
            // 商家图
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.mImgProduct);
            // 商家国旗图
            ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), holder.mImgCountry);
            convertView.setOnClickListener(v -> StoreDetailActivity.launch(mContext, obj.getStoreId()));
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_store_name)    TextView        mTvStoreName;
        @BindView(R.id.img_country)      CustomImageView mImgCountry;
        @BindView(R.id.tv_store_type)    TextView        mTvStoreType;
        @BindView(R.id.tv_normal_rebate) TextView        mTvNormalRebate;
        @BindView(R.id.tv_vip_rebate)    TextView        mTvVipRebate;
        @BindView(R.id.tv_count)         TextView        mTvCount;
        @BindView(R.id.img_product)      CustomImageView mImgProduct;
        @BindView(R.id.divider)          View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
