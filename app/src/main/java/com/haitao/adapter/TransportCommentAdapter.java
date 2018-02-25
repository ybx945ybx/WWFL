package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.TransportCommentItemObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 转运 - 评论
 * Created by tqy on 2015/11/20.
 */
public class TransportCommentAdapter extends BaseListAdapter<TransportCommentItemObject> {
    public String username = "";

    public interface OnCallbackLitener {
        void onAgreeClick(int position);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }

    public TransportCommentAdapter(Context context, List<TransportCommentItemObject> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_transport_comment, null);
//            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivAvator = getView(convertView, R.id.ivAvator);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.rbStar = getView(convertView, R.id.rbStar);
            holder.tvAgree = getView(convertView, R.id.tvAgree);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TransportCommentItemObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineGifImage(obj.avator, holder.ivAvator);
            holder.tvName.setText(obj.username);
            holder.tvAgree.setSelected("1".equals(obj.is_my_priase));
            holder.tvAgree.setText(obj.praise_num);
            holder.tvContent.setText(obj.comments);
            holder.tvTime.setText(obj.dateline);
            holder.rbStar.setRating(Float.valueOf(obj.start_number));
            holder.tvAgree.setOnClickListener(v -> {
                if (null != mOnCallbackLitener) {
                    mOnCallbackLitener.onAgreeClick(position);
                }
            });
        }
        return convertView;
    }

    private class Holder {
//        View            viewSeparate;
        CustomImageView ivAvator;
        RatingBar       rbStar;
        TextView        tvName;
        TextView        tvTime;
        TextView        tvContent;
        TextView        tvAgree;
    }
}
