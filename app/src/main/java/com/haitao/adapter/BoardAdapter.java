package com.haitao.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.ForumBoardModel;

/**
 * 版块
 * Created by tqy on 2015/11/20.
 */
public class BoardAdapter extends BaseListAdapter<ForumBoardModel> {
    public boolean isClose   = true;
    public int     largeSize = 2;

    public String  subTitle = "";
    public boolean isTop    = true;

    public interface OnCallbackLitener {
        void onMore();
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    public BoardAdapter(Context context, List<ForumBoardModel> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_board, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.layoutContent = getView(convertView, R.id.layoutContent);
            holder.tvOpen = getView(convertView, R.id.tvOpen);
            holder.tvNotice = getView(convertView, R.id.tvNotice);
            holder.tvTopicCount = getView(convertView, R.id.tvTopicCount);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvSubTitle = getView(convertView, R.id.tvSubTitle);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ForumBoardModel obj = null;
        if (mList.size() > largeSize) {
            holder.tvOpen.setVisibility((position == getCount() - 1) ? View.VISIBLE : View.GONE);
            holder.layoutContent.setVisibility((position == getCount() - 1) ? View.GONE : View.VISIBLE);
            if (position == getCount() - 1) {
                holder.viewSeparate.setVisibility(View.GONE);
                Drawable drawable = mContext.getResources().getDrawable(null != mOnCallbackLitener ? R.drawable.ic_next : R.drawable.sl_arrow_top_topic);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                holder.tvOpen.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px6));
                holder.tvOpen.setCompoundDrawables(null, null, drawable, null);
                holder.tvOpen.setText(null != mOnCallbackLitener ? "全部版块" : (isClose ? "查看全部" : "收起部分"));
                holder.tvOpen.setVisibility(View.VISIBLE);
                holder.layoutContent.setVisibility(View.GONE);
                holder.tvOpen.setOnClickListener(v -> {
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
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.ivImage);
            holder.tvName.setText(obj.getBoardName());
            holder.tvTopicCount.setText(String.format("帖子：%s", obj.getTopicsCount()));
            holder.tvNotice.setText(obj.getTodayPostsCount());
            holder.tvNotice.setVisibility(TextUtils.isEmpty(obj.getTodayPostsCount()) || "0".equals(obj.getTodayPostsCount()) ? View.GONE : View.VISIBLE);
            holder.tvSubTitle.setVisibility(!TextUtils.isEmpty(subTitle) && 0 == position ? View.VISIBLE : View.GONE);
            holder.tvSubTitle.setText(subTitle);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        TextView        tvOpen;
        ViewGroup       layoutContent;
        CustomImageView ivImage;
        TextView        tvName;
        TextView        tvTopicCount;
        TextView        tvNotice;
        TextView        tvSubTitle;
    }
}
