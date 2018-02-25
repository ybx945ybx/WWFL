package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.CollectionTransshipperModel;
import io.swagger.client.model.TransshipperModel;

/**
 * 转运列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TransportNewAdapter extends BaseListAdapter<TransshipperModel> {

    public TransportNewAdapter(Context context, List<TransshipperModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_transport, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TransshipperModel obj = mList.get(position);
        if (null != obj) {
            holder.mTvTitle.setText(obj.getTransshipperName());
            holder.mRbStar.setRating(Float.valueOf(obj.getPoints()));
            holder.mTvStar.setText(Float.valueOf(obj.getPoints()) > 0 ? obj.getPoints() + "星" : "暂无评分");
            holder.mTvCount.setText(String.format("%s个评论 | %s人收藏", obj.getCommentsCount(), obj.getCollectionCount()));
            ImageLoaderUtils.showOnlineImage(obj.getTransshipperLogo(), holder.mIvImage);
            // 最后一条隐藏分割线
            holder.mViewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.ivImage)      CustomImageView mIvImage;
        @BindView(R.id.tvTitle)      TextView        mTvTitle;
        @BindView(R.id.rbStar)       RatingBar       mRbStar;
        @BindView(R.id.tvStar)       TextView        mTvStar;
        @BindView(R.id.layoutStar)   LinearLayout    mLayoutStar;
        @BindView(R.id.tvCount)      TextView        mTvCount;
        @BindView(R.id.viewSeparate) View            mViewSeparate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
