package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.fragment.StoreFragment;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.EnteredStoreModel;

/**
 * 商家{@link StoreFragment} 今日加倍返利-Adapter
 *
 * @author 陶声
 * @since 2017-12-05
 */
public class StoreAdapter extends BaseListAdapter<EnteredStoreModel> {


    public StoreAdapter(Context context, List<EnteredStoreModel> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_store, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EnteredStoreModel obj = mList.get(position);
        if (null != obj) {
            // 商家Logo
            ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.mImgLogo);
            // 国旗
            ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), holder.mImgCountry);
            // 返利比例
            holder.mTvRebateRate.setVisibility(TextUtils.isEmpty(obj.getRebateView()) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(obj.getRebateView()))
                holder.mTvRebateRate.setText(obj.getRebateView());
            // 商家类型
            holder.mTvStoreType.setText(obj.getCategoryName());
            // 商家名
            holder.mTvStoreName.setText(obj.getStoreName());
            // 返利信息
            holder.mTvInfo.setText(String.format("%s · %s", obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
            // 标签
            List<String> tags = obj.getPropertyTags();
            Logger.d(tags.toString());
            int i;
            for (i = 0; i < tags.size(); i++) {
                holder.tags[i].setVisibility(View.VISIBLE);
                holder.tags[i].setText(tags.get(i));
            }
            if (i < 3) {
                for (; i < 3; i++) {
                    holder.tags[i].setVisibility(View.GONE);
                }
            }
            // 最后一条隐藏分割线
            holder.mViewDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_logo)       CustomImageView mImgLogo;
        @BindView(R.id.img_country)    CustomImageView mImgCountry;
        @BindView(R.id.tv_store_type)  TextView        mTvStoreType;
        @BindView(R.id.tv_rebate_rate) TextView        mTvRebateRate;
        @BindView(R.id.rl_store_info)  RelativeLayout  mRlStoreInfo;
        @BindView(R.id.tv_store_name)  TextView        mTvStoreName;
        @BindView(R.id.tv_info)        TextView        mTvInfo;
        @BindView(R.id.tv_tag1)        TextView        mTvTag1;
        @BindView(R.id.tv_tag2)        TextView        mTvTag2;
        @BindView(R.id.tv_tag3)        TextView        mTvTag3;
        @BindView(R.id.view_divider)   View            mViewDivider;

        public TextView[] tags;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            tags = new TextView[]{mTvTag1, mTvTag2, mTvTag3};
        }
    }
}
