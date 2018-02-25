package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.fragment.StoreFragment;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.BaseStoreListObject;
import io.swagger.client.model.EnteredStoreModel;
import io.swagger.client.model.StoreWithDealsModel;
import io.swagger.client.model.StoreWithDealsModelDeals;

/**
 * 商家{@link StoreFragment} 今日加倍返利-Adapter
 *
 * @author 陶声
 * @since 2017-12-05
 */
public class StoreBaseAdapter extends BaseListAdapter<BaseStoreListObject> {
    private static int TYPE_STORE_LIST     = 0;
    private static int TYPE_STORE_DISCOUNT = 1;

    private int doubleRebateStoresCount;        //  今日加倍返利商家数

    public StoreBaseAdapter(Context context, List<BaseStoreListObject> data) {
        super(context, data);
    }

    public void setDoubleRebateStoresCount(int doubleRebateStoresCount) {
        this.doubleRebateStoresCount = doubleRebateStoresCount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Logger.d("get view");
        int                           type = getItemViewType(position);
        final ViewHolderStoreList     storeListHolder;
        final ViewHolderStoreDiscount holder;

        if (type == TYPE_STORE_LIST) {
            // 商家列表
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_store, null);
                storeListHolder = new ViewHolderStoreList(convertView);
                convertView.setTag(storeListHolder);
            } else {
                storeListHolder = (ViewHolderStoreList) convertView.getTag();
            }
            BaseStoreListObject baseObj = mList.get(position);
            if (null != baseObj) {
                EnteredStoreModel obj = (EnteredStoreModel) baseObj;
                // 商家Logo
                ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), storeListHolder.mImgLogo);
                // 国旗
                ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), storeListHolder.mImgCountry);
                // 返利比例
                storeListHolder.mTvRebateRate.setVisibility(TextUtils.isEmpty(obj.getRebateView()) ? View.GONE : View.VISIBLE);
                if (!TextUtils.isEmpty(obj.getRebateView()))
                    storeListHolder.mTvRebateRate.setText(obj.getRebateView());
                // 商家类型
                storeListHolder.mTvStoreType.setText(obj.getCategoryName());
                // 商家名
                storeListHolder.mTvStoreName.setText(obj.getStoreName());
                // 标签
                List<String> tags = obj.getPropertyTags();
                Logger.d(tags.toString());
                int i;
                for (i = 0; i < tags.size(); i++) {
                    storeListHolder.tags[i].setVisibility(View.VISIBLE);
                    storeListHolder.tags[i].setText(tags.get(i));
                }
                if (i < 3) {
                    for (; i < 3; i++) {
                        storeListHolder.tags[i].setVisibility(View.GONE);
                    }
                }
                // 返利信息
                storeListHolder.mTvInfo.setText(String.format("%s · %s", obj.getRebateInfluenceView(), obj.getCollectionsCountView()));
                // 最后一条隐藏分割线
                storeListHolder.mViewDivider.setVisibility(mList.indexOf(obj) == doubleRebateStoresCount - 1 ? View.GONE : View.VISIBLE);

            }
        } else {
            // 商家优惠列表
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_store_with_discount, null);
                holder = new ViewHolderStoreDiscount(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderStoreDiscount) convertView.getTag();
            }
            final BaseStoreListObject baseObj = mList.get(position);
            if (null != baseObj) {
                StoreWithDealsModel obj = (StoreWithDealsModel) baseObj;
                // 商家Logo
                ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), holder.mImgStoreLogo);
                // 商家名
                holder.mTvStoreName.setText(obj.getStoreName());
                // 返利信息
                if (!TextUtils.isEmpty(obj.getRebateView())) {
                    holder.mLlRebate.setVisibility(View.VISIBLE);
                    String rebateView = obj.getRebateView();
                    if (rebateView.contains("返利最高")) {
                        holder.mTvRebateView.setText("返利最高");
                        rebateView = rebateView.replace("返利最高", "");
                        rebateView = rebateView.replace("%", "");
                        holder.mTvRebateRate.setText(rebateView);
                    } else if (rebateView.contains("返利")) {
                        holder.mTvRebateView.setText("返利");
                        rebateView = rebateView.replace("返利", "");
                        rebateView = rebateView.replace("%", "");
                        holder.mTvRebateRate.setText(rebateView);
                    }

                } else {
                    holder.mLlRebate.setVisibility(View.GONE);
                }
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
                // 优惠图
                List<StoreWithDealsModelDeals> deals = obj.getDeals();
                if (deals != null && deals.size() > 0) {
                    holder.mLlDiscount.setVisibility(View.VISIBLE);
                    holder.mTitleDivider.setVisibility(View.VISIBLE);

                    //// 优惠1 ////
                    if (deals.get(0) != null) {
                        holder.mRlDiscount1.setVisibility(View.VISIBLE);
                        // 优惠图
                        ImageLoaderUtils.showOnlineImage(deals.get(0).getDealPic(), holder.mImgDiscount1);
                        // 限时
                        holder.mTvTimeout1.setVisibility(TextUtils.isEmpty(deals.get(0).getLeftTime()) ? View.GONE : View.VISIBLE);
                        if (!TextUtils.isEmpty(deals.get(0).getLeftTime())) {
                            holder.mTvTimeout1.setText(deals.get(0).getLeftTime());
                        }
                        // 优惠标题
                        holder.mTvDiscountName1.setText(deals.get(0).getTitle());
                        // 优惠金额
                        holder.mTvPrice1.setText(deals.get(0).getPriceView());
                        // 跳转优惠详情
                        holder.mRlDiscount1.setOnClickListener(v -> DiscountDetailActivity.launch(mContext, deals.get(0).getDealId()));
                    } else {
                        holder.mRlDiscount1.setVisibility(View.GONE);
                    }

                    //// 优惠2 ////
                    if (deals.size() > 1 && deals.get(1) != null) {
                        holder.mRlDiscount2.setVisibility(View.VISIBLE);
                        // 优惠图
                        ImageLoaderUtils.showOnlineImage(deals.get(1).getDealPic(), holder.mImgDiscount2);
                        // 限时
                        holder.mTvTimeout2.setVisibility(TextUtils.isEmpty(deals.get(1).getLeftTime()) ? View.GONE : View.VISIBLE);
                        if (!TextUtils.isEmpty(deals.get(1).getLeftTime())) {
                            holder.mTvTimeout2.setText(deals.get(1).getLeftTime());
                        }
                        // 优惠标题
                        holder.mTvDiscountName2.setText(deals.get(1).getTitle());
                        // 优惠金额
                        holder.mTvPrice2.setText(deals.get(1).getPriceView());
                        // 跳转优惠详情
                        holder.mRlDiscount2.setOnClickListener(v -> DiscountDetailActivity.launch(mContext, deals.get(1).getDealId()));
                    } else {
                        holder.mRlDiscount2.setVisibility(View.GONE);
                    }
                } else {
                    // 商家不包含优惠信息，则隐藏
                    holder.mTitleDivider.setVisibility(View.GONE);
                    holder.mLlDiscount.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        BaseStoreListObject data = mList.get(position);
        if (data != null) {
            return data instanceof EnteredStoreModel ? TYPE_STORE_LIST : TYPE_STORE_DISCOUNT;
        }
        return TYPE_STORE_LIST;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolderStoreList {
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

        ViewHolderStoreList(View view) {
            ButterKnife.bind(this, view);
            tags = new TextView[]{mTvTag1, mTvTag2, mTvTag3};
        }
    }

    static class ViewHolderStoreDiscount {
        @BindView(R.id.img_store_logo)     CustomImageView mImgStoreLogo;
        @BindView(R.id.tv_store_name)      TextView        mTvStoreName;
        @BindView(R.id.img_discount_1)     CustomImageView mImgDiscount1;
        @BindView(R.id.tv_timeout_1)       TextView        mTvTimeout1;
        @BindView(R.id.tv_discount_name_1) TextView        mTvDiscountName1;
        @BindView(R.id.tv_price_1)         TextView        mTvPrice1;
        @BindView(R.id.rl_discount1)       RelativeLayout  mRlDiscount1;
        @BindView(R.id.img_discount_2)     CustomImageView mImgDiscount2;
        @BindView(R.id.tv_timeout_2)       TextView        mTvTimeout2;
        @BindView(R.id.tv_discount_name_2) TextView        mTvDiscountName2;
        @BindView(R.id.tv_price_2)         TextView        mTvPrice2;
        @BindView(R.id.tv_tag1)            TextView        mTvTag1;
        @BindView(R.id.tv_tag2)            TextView        mTvTag2;
        @BindView(R.id.tv_tag3)            TextView        mTvTag3;
        @BindView(R.id.llyt_rebate)        LinearLayout    mLlRebate;
        @BindView(R.id.tv_rebate_view)     TextView        mTvRebateView;
        @BindView(R.id.tv_rebate_rate)     TextView        mTvRebateRate;
        @BindView(R.id.rl_discount2)       RelativeLayout  mRlDiscount2;
        @BindView(R.id.ll_discount)        LinearLayout    mLlDiscount;
        @BindView(R.id.view_divider)       View            mViewDivider;
        @BindView(R.id.title_divider)      View            mTitleDivider;

        public TextView[] tags;

        ViewHolderStoreDiscount(View view) {
            ButterKnife.bind(this, view);
            tags = new TextView[]{mTvTag1, mTvTag2, mTvTag3};
        }
    }
}
