package com.haitao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.PreviewActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.TextUtil;
import com.haitao.view.FullGirdView;
import com.haitao.view.RoundedImageView;

import java.util.List;

import io.swagger.client.model.PostDynamicsMsgsListModelDataRows;

/**
 * 帖子动态 - Adapter
 * Created by penley on 16/3/8.
 */
public class DynamicPostAdapter extends BaseListAdapter<PostDynamicsMsgsListModelDataRows> {
    public DynamicPostAdapter(Context context, List<PostDynamicsMsgsListModelDataRows> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dynamic_post_item, null);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.tvReply = getView(convertView, R.id.tvReply);
            holder.gvList = getView(convertView, R.id.gv_order_pics);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PostDynamicsMsgsListModelDataRows obj = mList.get(position);
        ImageLoaderUtils.showOnlineImage(obj.getSenderAvatar(), holder.ivAvator, R.mipmap.ic_default_avator);
        holder.tvContent.setText(TextUtil.getClickableHtml(mContext, obj.getContent()));
        holder.tvTime.setText(obj.getSentTime());
        holder.tvName.setText(obj.getSenderName());
        if (null != obj.getQuotation()) {
            holder.tvReply.setVisibility(View.VISIBLE);
            holder.tvReply.setText(TextUtil.getClickableHtml(mContext, obj.getQuotation().getContent()));
        } else {
            holder.tvReply.setVisibility(View.GONE);
        }
        holder.tvContent.setVisibility(TextUtils.isEmpty(obj.getContent()) ? View.GONE : View.VISIBLE);
        /*if(null != obj.pic && obj.pic.length > 0){
            holder.gvList.setVisibility(View.VISIBLE);
            List list = Arrays.asList(obj.pic);
            CommentImageAdapter mAdapter = new CommentImageAdapter(mContext,list);
            holder.gvList.setAdapter(mAdapter);
            obj.mPhotoPickParameterInfo = new PhotoPickParameterObject();
            obj.mPhotoPickParameterInfo.image_list = new ArrayList<>();
            obj.mPhotoPickParameterInfo.image_list.addAll(list);
            holder.gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openImagePreview(obj.mPhotoPickParameterInfo, position);
                }
            });
        }else {
            holder.gvList.setVisibility(View.GONE);
        }*/
        return convertView;

    }

    private class Holder {
        RoundedImageView ivAvator;
        TextView         tvName;
        TextView         tvTime;
        TextView         tvContent;
        TextView         tvReply;
        FullGirdView     gvList;
    }

    //图片预览

    public void openImagePreview(PhotoPickParameterObject mPhotoPickParameterInfo, int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);
        mContext.startActivity(intent);
    }
}
