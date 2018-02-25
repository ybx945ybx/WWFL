package com.haitao.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.DealDailyModel;

/**
 * 优惠日报 - Adapter
 *
 * @author 陶声
 * @since 2017-09-21
 */

public class DiscountDailyPagerAdapter extends PagerAdapter {

    private final List<DealDailyModel> mData;
    private       LayoutInflater       mInflater;
    private       Context              mContext;

    private OnDailyClickListener mOnDailyClickListener; // 日报相关点击事件监听

    public DiscountDailyPagerAdapter(Context context, List<DealDailyModel> dailyList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = dailyList;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View       layout = mInflater.inflate(R.layout.item_discount_daily, container, false);
        ViewHolder holder = new ViewHolder(layout);
        if (mData != null) {
            DealDailyModel data = mData.get(position);
            if (data != null) {
                if (mOnDailyClickListener != null) {
                    // 查看优惠详情
                    layout.setOnClickListener(v -> mOnDailyClickListener.onItemClick(data.getDealId()));
                    // 保存图片
                    holder.mLlSave.setOnClickListener(v -> mOnDailyClickListener.onSaveClick(data.getDealPic()));
                    // 分享
                    holder.mLlShare.setOnClickListener(v -> mOnDailyClickListener.onShareClick(data.getShareTitle(), data.getShareContent(), data.getShareContentWeibo(), data.getShareUrl(), data.getSharePic()));
                    // 购买
                    holder.mLlBuy.setOnClickListener(v -> mOnDailyClickListener.onBuyClick(data));
                }
                // 商品图
                ImageLoaderUtils.showOnlineImage(data.getDealPic(), holder.mImgDiscount);
                // 返利
                if (!TextUtils.isEmpty(data.getRebateView())) {
                    holder.mTvReabte.setVisibility(View.VISIBLE);
                    holder.mTvReabte.setText(data.getRebateView());
                } else {
                    holder.mTvReabte.setVisibility(View.GONE);
                }
                // 标题
                holder.mTvTitle.setText(data.getTitle());
                // 价格
                holder.mTvPrice.setText(data.getPriceView());
                // 返利信息
                holder.mTvStoreRebateInfo.setText(data.getStoreName() + " · " + data.getRebateInfluenceView());
                // 支付宝
                holder.mTvAlipaySupport.setVisibility(TextUtils.equals(data.getAlipaySupported(), "1") ? View.VISIBLE : View.GONE);
                // 直邮
                holder.mTvDirectTransportSupport.setVisibility(TextUtils.equals(data.getDirectPostSupported(), "1") ? View.VISIBLE : View.GONE);
                // 时间
                //                List<String> timeArr = data.getPublishTimeArray();
                //                if (timeArr != null && timeArr.size() >= 3) {
                //                    holder.mTvMonth.setText(timeArr.get(0));
                //                    holder.mTvDay.setText(timeArr.get(1));
                //                    holder.mTvWeekday.setText(timeArr.get(2));
                //                }
            }
        }
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    static class ViewHolder {
        @BindView(R.id.img_discount)                CustomImageView mImgDiscount;
        @BindView(R.id.tv_day)                      TextView        mTvDay;
        @BindView(R.id.tv_month)                    TextView        mTvMonth;
        @BindView(R.id.tv_weekday)                  TextView        mTvWeekday;
        @BindView(R.id.tv_reabte)                   TextView        mTvReabte;
        @BindView(R.id.tv_title)                    TextView        mTvTitle;
        @BindView(R.id.tv_price)                    TextView        mTvPrice;
        @BindView(R.id.tv_store_rebate_info)        TextView        mTvStoreRebateInfo;
        @BindView(R.id.tv_direct_transport_support) TextView        mTvDirectTransportSupport;
        @BindView(R.id.tv_alipay_support)           TextView        mTvAlipaySupport;
        @BindView(R.id.ll_save)                     LinearLayout    mLlSave;
        @BindView(R.id.ll_share)                    LinearLayout    mLlShare;
        @BindView(R.id.ll_buy)                      LinearLayout    mLlBuy;
        @BindView(R.id.ll_container)                LinearLayout    mLlContainer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 日报相关点击事件监听
     */
    public interface OnDailyClickListener {
        void onItemClick(String id);

        void onSaveClick(String imgUrl);

        void onShareClick(String shareTitle, String shareContent, String shareContentWeibo, String shareUrl, String sharePic);

        void onBuyClick(DealDailyModel dailyModel);
    }

    public void setOnDailyClickListener(OnDailyClickListener onDailyClickListener) {
        mOnDailyClickListener = onDailyClickListener;
    }
}
