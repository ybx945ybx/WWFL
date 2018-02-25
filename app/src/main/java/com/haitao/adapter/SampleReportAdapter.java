package com.haitao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.PreviewActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.SampleReportObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.FullGirdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 试用报告 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class SampleReportAdapter extends BaseListAdapter<SampleReportObject> {
    public boolean isTimeShow = true;

    public SampleReportAdapter(Context context, List<SampleReportObject> data) {
        super(context, data);
    }

    public boolean isDelete = false;

    public List<SampleReportObject> getList() {
        return this.mList;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_sample_report, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            holder.tvAuthor = getView(convertView, R.id.tvAuthor);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvComment = getView(convertView, R.id.tvComment);
            holder.tvView = getView(convertView, R.id.tvView);
            holder.tvForum = getView(convertView, R.id.tvForum);
            holder.gvList = getView(convertView, R.id.gv_order_pics);
            holder.tvBest = getView(convertView, R.id.tvBest);
            holder.tvCategory = getView(convertView, R.id.tvCategory);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTime.setVisibility(isTimeShow ? View.VISIBLE : View.GONE);
        final SampleReportObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineGifImage(obj.avatar, holder.ivAvator);
            holder.tvAuthor.setText(obj.author);
            holder.tvTitle.setText(obj.subject);
            holder.tvTime.setText(obj.dateline);
            holder.tvComment.setText(obj.replies);
            holder.tvView.setText(obj.views);
            holder.tvCategory.setText(obj.class_name);
            holder.tvBest.setVisibility("1".equals(obj.digest) ? View.VISIBLE : View.GONE);
            if (null != obj.pic && obj.pic.length > 0) {
                holder.gvList.setVisibility(View.VISIBLE);
                List                list     = Arrays.asList(obj.pic);
                CommentImageAdapter mAdapter = new CommentImageAdapter(mContext, list);

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
            } else {
                holder.gvList.setVisibility(View.GONE);
            }
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivAvator;
        TextView        tvTitle;
        TextView        tvComment;
        TextView        tvView;
        TextView        tvAuthor;
        TextView        tvTime;
        TextView        tvForum;
        FullGirdView    gvList;
        ImageView       tvBest;
        TextView        tvCategory;
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
