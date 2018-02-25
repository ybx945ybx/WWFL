package com.haitao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.PreviewActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.PostObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class PostAdapter extends BaseListAdapter<PostObject> {
    public boolean isTimeShow    = true;
    public boolean isAvatorClick = true;

    public PostAdapter(Context context, List<PostObject> data) {
        super(context, data);
    }

    public boolean isDelete = false;

    public List<PostObject> getList() {
        return this.mList;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_post, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            holder.tvAuthor = getView(convertView, R.id.tvAuthor);
            holder.tvTime = getView(convertView, R.id.tvTime);
            //            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvComment = getView(convertView, R.id.tvComment);
            holder.tvView = getView(convertView, R.id.tvView);
            holder.tvForum = getView(convertView, R.id.tvForum);
            holder.gvList = getView(convertView, R.id.gv_order_pics);
            //            holder.btnChoose = getView(convertView, R.id.btnChoose);
            holder.tvBest = getView(convertView, R.id.tvBest);
            holder.tvHot = getView(convertView, R.id.tvHot);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTime.setVisibility(isTimeShow ? View.VISIBLE : View.GONE);
        final PostObject obj = mList.get(position);
        if (null != obj) {
            /*if (!TextUtils.isEmpty(obj.pic) && (null == obj.pics)) {
                ImageLoaderUtils.showOnlineImage(obj.pic, holder.ivImage);
                holder.ivImage.setVisibility(View.VISIBLE);
            } else {
                holder.ivImage.setVisibility(View.GONE);
            }*/
            ImageLoaderUtils.showOnlineGifImage(TextUtils.isEmpty(obj.avator) ? obj.avatar : obj.avator, holder.ivAvator);
            holder.tvAuthor.setText(obj.author);
            holder.tvTitle.setText(obj.title);
            holder.tvTime.setText(obj.time_view);
            holder.tvComment.setText(obj.reply_count);
            holder.tvView.setText(obj.view_count);
            holder.tvBest.setVisibility("1".equals(obj.digest) ? View.VISIBLE : View.GONE);
            holder.tvHot.setVisibility("1".equals(obj.is_hot) ? View.VISIBLE : View.GONE);
            holder.tvForum.setText(obj.class_name);
            holder.tvForum.setVisibility(TextUtils.isEmpty(obj.class_name) ? View.GONE : View.VISIBLE);
            //            holder.btnChoose.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            obj.isSelected = (obj.isSelected && isDelete);
            mList.set(position, obj);
            //            holder.btnChoose.setSelected(obj.isSelected);
            /*holder.btnChoose.setOnClickListener(v -> {
                obj.isSelected = !obj.isSelected;
                mList.set(position, obj);
                holder.btnChoose.setSelected(obj.isSelected);
            });*/
            if (null != obj.pics && obj.pics.length > 0) {
                holder.gvList.setVisibility(View.VISIBLE);
                List             list     = Arrays.asList(obj.pics);
                PostImageAdapter mAdapter = new PostImageAdapter(mContext, list);

                holder.gvList.setAdapter(mAdapter);
                obj.mPhotoPickParameterInfo = new PhotoPickParameterObject();
                obj.mPhotoPickParameterInfo.image_list = new ArrayList<>();
                obj.mPhotoPickParameterInfo.image_list.addAll(list);
                holder.gvList.setOnItemClickListener((parent1, view, position1, id) -> openImagePreview(obj.mPhotoPickParameterInfo, position1));
            } else {
                holder.gvList.setVisibility(View.GONE);
            }
            if (isAvatorClick) {
                holder.ivAvator.setOnClickListener(v -> TalentDetailActivity.launch(mContext, obj.authorid));
            }
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivAvator;
        //        CustomImageView ivImage;
        TextView        tvTitle;
        TextView        tvComment;
        TextView        tvView;
        TextView        tvAuthor;
        TextView        tvTime;
        TextView        tvForum;
        ImageView       tvBest;
        ImageView       tvHot;
        //        ImageButton     btnChoose;
        GridView        gvList;
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
