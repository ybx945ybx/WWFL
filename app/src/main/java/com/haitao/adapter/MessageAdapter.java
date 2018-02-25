package com.haitao.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.TextUtil;
import com.haitao.utils.ToastUtils;
import com.haitao.view.PopupList;
import com.haitao.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.ChatMsgModel;

/**
 * Created by penley on 16/3/8.
 */
public class MessageAdapter extends BaseListAdapter<ChatMsgModel> {
    private List<String> popupMenuItemList = new ArrayList<>();
    public MessageAdapter(Context context, List<ChatMsgModel> data) {
        super(context, data);
        popupMenuItemList.add("复制");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_message, null);
            holder.tvTime = getView(convertView, R.id.tvTime);

            holder.leftLayout = getView(convertView, R.id.leftLayout);
            holder.leftImage = getView(convertView, R.id.leftImage);
            holder.leftContent = getView(convertView, R.id.leftContent);

            holder.rightLayout = getView(convertView, R.id.rightLayout);
            holder.rigthImage = getView(convertView, R.id.rightImage);
            holder.rightContent = getView(convertView, R.id.rightContent);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ChatMsgModel obj = mList.get(position);
        holder.tvTime.setText(obj.getSentTime());
        if ("1".equals(obj.getIsMe())) {//判断当前是否是自己的
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            //holder.rightContent.setText(Html.fromHtml(obj.content));
            holder.rightContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.rightContent.setText(TextUtil.getClickableHtml(mContext,obj.getMsg()));
            ImageLoaderUtils.showOnlineImage(obj.getSenderAvatar(), holder.rigthImage, R.mipmap.ic_default_avator);
        } else {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            //holder.leftContent.setText(Html.fromHtml(obj.content));
            holder.leftContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.leftContent.setText(TextUtil.getClickableHtml(mContext,obj.getMsg()));
            ImageLoaderUtils.showOnlineImage(obj.getSenderAvatar(), holder.leftImage, R.mipmap.ic_default_avator);
            holder.leftImage.setOnClickListener(view -> TalentDetailActivity.launch(mContext,obj.getSenderUid()));
        }
        PopupList popupList = new PopupList(mContext);
        popupList.setIndicatorView(null);
        popupList.bind(holder.leftContent, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                TextUtil.Copy(obj.getMsg());
                ToastUtils.show(mContext,"复制成功");
            }
        });
        popupList.bind(holder.rightContent, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                TextUtil.Copy(obj.getMsg());
                ToastUtils.show(mContext,"复制成功");
            }
        });

        return convertView;
    }

    private class Holder {

        TextView tvTime;
        RelativeLayout leftLayout;
        RoundedImageView leftImage;
        TextView leftContent;

        RelativeLayout rightLayout;
        RoundedImageView rigthImage;
        TextView rightContent;

    }
}
