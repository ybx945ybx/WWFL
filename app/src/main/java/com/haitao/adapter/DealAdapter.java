package com.haitao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtAdView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.DealModel;
import io.swagger.client.model.DealSlidePicBaseModel;
import io.swagger.client.model.SlidePicModel;

/**
 * 优惠 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class DealAdapter<T extends DealSlidePicBaseModel> extends BaseListAdapter<T> {

    private static int TYPE_DEAL      = 0;
    private static int TYPE_SLIDE_PIC = 1;

    public boolean isDelete = false;

    public DealAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public List<T> getList() {
        return this.mList;
    }

    public void setData(int position, T obj) {
        mList.set(position, obj);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DealViewHolder     dealHolder;
        final SlidePicViewHolder slidePicHolder;
        int                      type = getItemViewType(position);

        if (type == TYPE_DEAL) {
            // 优惠
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_deal, null);
                dealHolder = new DealViewHolder(convertView);
                convertView.setTag(dealHolder);
            } else {
                dealHolder = (DealViewHolder) convertView.getTag();
            }
            dealHolder.mViewSeparate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            final DealModel obj = (DealModel) mList.get(position);
            if (null != obj) {
                ImageLoaderUtils.showOnlineImage(obj.getDealPic(), dealHolder.mIvImage);
                ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), dealHolder.mIvCountry);
                dealHolder.mTvStore.setText(obj.getStoreName());
                dealHolder.mTvTitle.setText(obj.getTitle());
                dealHolder.mTvPrice.setText(obj.getPriceView());
                dealHolder.mTvAddress.setText(obj.getPublishTime());
                dealHolder.mTvRebate.setText(obj.getRebateView());
                dealHolder.mTvRebate.setVisibility("1".equals(obj.getHasRebate()) ? View.VISIBLE : View.GONE);
                dealHolder.mTvTime.setText(String.format("剩余：%s", obj.getLeftTime()));
                dealHolder.mTvTime.setVisibility(TextUtils.isEmpty(obj.getLeftTime()) ? View.GONE : View.VISIBLE);
                dealHolder.mTvComment.setText(TextUtils.equals(obj.getCommentCount(), "0") ? "评论" : obj.getCommentCount());
                dealHolder.mTvAgree.setText(TextUtils.equals(obj.getPraiseCount(), "0") ? "赞" : obj.getPraiseCount());
                dealHolder.mTvAddress.setSelected("1".equals(obj.getIsPraised()));
                dealHolder.mBtnChoose.setVisibility(View.GONE);
                //                mList.set(position, obj);
            }
        } else {
            // 广告
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_slide_pic, null);
                slidePicHolder = new SlidePicViewHolder(convertView);
                convertView.setTag(slidePicHolder);
            } else {
                slidePicHolder = (SlidePicViewHolder) convertView.getTag();
            }
            final SlidePicModel obj = (SlidePicModel) mList.get(position);
            if (null != obj) {
                // 广告
                slidePicHolder.mAd.setView(obj);
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        DealSlidePicBaseModel data = mList.get(position);
        if (data != null) {
            return data instanceof DealModel ? TYPE_DEAL : TYPE_SLIDE_PIC;
        }
        return TYPE_DEAL;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 优惠 - ViewHolder
     */
    static class DealViewHolder {
        @BindView(R.id.viewSeparate) View            mViewSeparate;
        @BindView(R.id.btnChoose)    ImageButton     mBtnChoose;
        @BindView(R.id.ivImage)      CustomImageView mIvImage;
        @BindView(R.id.tvTime)       TextView        mTvTime;
        @BindView(R.id.ivCountry)    CustomImageView mIvCountry;
        @BindView(R.id.tvStore)      TextView        mTvStore;
        @BindView(R.id.tvRebate)     TextView        mTvRebate;
        @BindView(R.id.layoutStore)  RelativeLayout  mLayoutStore;
        @BindView(R.id.tvTitle)      TextView        mTvTitle;
        @BindView(R.id.tvPrice)      TextView        mTvPrice;
        @BindView(R.id.tvAddress)    TextView        mTvAddress;
        @BindView(R.id.tvComment)    TextView        mTvComment;
        @BindView(R.id.tvAgree)      TextView        mTvAgree;

        DealViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 广告 - ViewHolder
     */
    static class SlidePicViewHolder {
        @BindView(R.id.htav_ad) HtAdView mAd;

        SlidePicViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
