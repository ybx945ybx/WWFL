package com.haitao.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.view.HtTitleView;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.swagger.client.model.TopicBriefModel;

/**
 * 置顶的贴子
 * Created by tqy on 2015/11/20.
 */
public class TopicTopAdapter extends BaseListAdapter<TopicBriefModel> {
    public boolean isClose   = true;
    public int     largeSize = 3;

    public String  subTitle = "";
    public boolean isTop    = true;


    public interface OnCallbackLitener {
        void onMore();
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    public TopicTopAdapter(Context context, List<TopicBriefModel> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        if (null == mList)
            return 0;
        if (mList.size() > largeSize && isClose) {
            return largeSize + 1;
        } else if (mList.size() > largeSize && !isClose) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        parent.clearFocus();
        parent.setFocusable(false);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_topic_top, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvOpen = getView(convertView, R.id.tvOpen);
            holder.tvTop = getView(convertView, R.id.tvTop);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvComment = getView(convertView, R.id.tvComment);
            holder.tvView = getView(convertView, R.id.tvView);
            holder.title = getView(convertView, R.id.title);
            holder.layoutOpen = getView(convertView, R.id.layoutOpen);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TopicBriefModel obj = null;
        if (mList.size() > largeSize) {
            Logger.d("position:" + position + "/getCount():" + getCount());
            holder.tvOpen.setVisibility((position == getCount() - 1) ? View.VISIBLE : View.GONE);
            holder.layoutContent.setVisibility((position == getCount() - 1) ? View.GONE : View.VISIBLE);
            //            holder.layoutSubTitle.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
            if (position == getCount() - 1) {
                holder.viewSeparate.setVisibility(View.GONE);
                Drawable drawable = mContext.getResources().getDrawable(null != mOnCallbackLitener ? R.drawable.ic_next : R.drawable.sl_arrow_top_topic);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                holder.tvOpen.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px6));
                holder.tvOpen.setCompoundDrawables(null, null, drawable, null);
                holder.tvOpen.setText(null != mOnCallbackLitener ? "查看更多" : mContext.getResources().getString(isClose ? R.string.str_expand : R.string.str_collapse));
                holder.tvOpen.setVisibility(View.VISIBLE);
                holder.tvOpen.setSelected(!isClose);
                holder.layoutContent.setVisibility(View.GONE);
                holder.tvOpen.setOnClickListener(v -> {
                    if (null != mOnCallbackLitener) {
                        mOnCallbackLitener.onMore();
                        return;
                    }
                    isClose = !isClose;
                    notifyDataSetChanged();
                });
                holder.layoutOpen.setOnClickListener(v -> {
                    if (null != mOnCallbackLitener) {
                        mOnCallbackLitener.onMore();
                        return;
                    }
                    isClose = !isClose;
                    notifyDataSetChanged();
                });
            } else {
                holder.viewSeparate.setVisibility(View.VISIBLE);
                holder.tvOpen.setVisibility(View.GONE);
                holder.layoutContent.setVisibility(View.VISIBLE);
                obj = mList.get(position);
            }
        } else {
            holder.viewSeparate.setVisibility(View.VISIBLE);
            holder.tvOpen.setVisibility(View.GONE);
            obj = mList.get(position);
        }
        if (null != obj) {
            holder.tvTitle.setText(obj.getTitle());
            holder.tvTime.setText(obj.getPostTime());
            holder.tvComment.setText(obj.getReplyCount());
            holder.tvView.setText(obj.getViewCount());
            holder.title.setVisibility(!TextUtils.isEmpty(subTitle) && 0 == position ? View.VISIBLE : View.GONE);
            holder.title.setTitle(subTitle);
            holder.tvTop.setVisibility(isTop ? View.VISIBLE : View.GONE);
            final TopicBriefModel finalObj = obj;
            holder.tvComment.setOnClickListener(view -> TopicDetailActivity.launch(mContext, finalObj.getTid(), true));
        }
        return convertView;
    }

    private class Holder {
        View        viewSeparate;
        TextView    tvOpen;
        ViewGroup   layoutContent;
        ImageView   tvTop;
        TextView    tvTitle;
        TextView    tvTime;
        TextView    tvComment;
        TextView    tvView;
        HtTitleView title;
        ViewGroup   layoutOpen;
    }

}
