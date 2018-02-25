package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import io.swagger.client.model.FriendsRequestionsListModelDataRows;

/**
 * 好友申请
 * Created by penley on 16/3/8.
 */
public class NoticeFriendAdapter extends BaseListAdapter<FriendsRequestionsListModelDataRows> {
    public interface OnInnerClickLitener {
        void onClick(int position);
    }

    private OnInnerClickLitener mOnItemClickLitener;

    public void setOnInnerClickLitener(OnInnerClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public NoticeFriendAdapter(Context context, List<FriendsRequestionsListModelDataRows> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_notice_friend, null);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvAdd = getView(convertView, R.id.tvAdd);
            holder.divider = getView(convertView, R.id.viewSeparate);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        FriendsRequestionsListModelDataRows obj = mList.get(position);
        if (null != obj) {
            holder.tvContent.setText(obj.getNote());
            holder.tvTime.setText(obj.getDateline());
            //1：别人添加我为好友，我已同意 2：别人添加我为好友，我未同意 3：我添加别人为好友，正在等待验证 4：我添加别人为好友，别人已同意
            if ("1".equals(obj.getStatus())) {
                holder.tvAdd.setVisibility(View.VISIBLE);
                holder.tvAdd.setText("已同意");
                holder.tvAdd.setBackgroundResource(R.color.transparent);
            } else if ("2".equals(obj.getStatus())) {
                holder.tvAdd.setVisibility(View.VISIBLE);
                holder.tvAdd.setText("同意");
                int left   = holder.tvAdd.getPaddingLeft();
                int top    = holder.tvAdd.getPaddingTop();
                int right  = holder.tvAdd.getPaddingRight();
                int bottom = holder.tvAdd.getPaddingBottom();
                holder.tvAdd.setBackgroundResource(R.drawable.shape_grey_empty_rectangle);
                holder.tvAdd.setPadding(left, top, right, bottom);
            } else if ("3".equals(obj.getStatus())) {
                holder.tvAdd.setVisibility(View.GONE);
            } else if ("4".equals(obj.getStatus())) {
                holder.tvAdd.setVisibility(View.GONE);
            }
            holder.tvAdd.setOnClickListener(view -> {
                if (null != mOnItemClickLitener && "2".equals(obj.getStatus()))
                    mOnItemClickLitener.onClick(position);
            });
            // 最后一条隐藏分割线
            holder.divider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    private class Holder {
        TextView tvContent;
        TextView tvTime;
        TextView tvAdd;
        View     divider;
    }
}
