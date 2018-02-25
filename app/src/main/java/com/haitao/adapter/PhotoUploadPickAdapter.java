package com.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.model.PhotoImageObject;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.universalimageloader.core.assist.ImageSize;
import com.haitao.utils.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发送帖子选择的图片
 * Created by tqy on 2015/11/20.
 */
public class PhotoUploadPickAdapter extends BaseListAdapter<PhotoImageObject> {
    public int     maxSize = 9;
    public boolean isEdit  = true;
    private int                   mWith;
    private OnPhotoRemoveListener mOnPhotoRemoveListener;

    public PhotoUploadPickAdapter(Context context, List<PhotoImageObject> data) {
        super(context, data);
        mWith = DensityUtil.dip2px(context, 72);
    }

    @Override
    public int getCount() {
        if (mList.size() < maxSize && isEdit) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_photo_show_new, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList.size() < maxSize && position == getCount() - 1 && isEdit) {
            holder.mIvAdd.setVisibility(View.VISIBLE);
            holder.mIvImage.setVisibility(View.GONE);
            holder.mIbClose.setVisibility(View.GONE);
        } else {
            holder.mIvAdd.setVisibility(View.GONE);
            holder.mIvImage.setVisibility(View.VISIBLE);
            holder.mIbClose.setVisibility(View.VISIBLE);
            // 删除图片监听
            holder.mIbClose.setOnClickListener(v -> {
                if (mOnPhotoRemoveListener != null) {
                    mOnPhotoRemoveListener.onRemove(position);
                }
            });
            if (mList.get(position).source_image.startsWith("http")) {
                ImageLoaderUtils.showOnlineImage(mList.get(position).source_image, holder.mIvImage);
            } else {
                ImageLoaderUtils.showLocationImage(mList.get(position).source_image, holder.mIvImage, R.mipmap.ic_default_240, new ImageSize(mWith, mWith), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
            }
            holder.mIvImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivAdd)    LinearLayout    mIvAdd;
        @BindView(R.id.ivImage)  CustomImageView mIvImage;
        @BindView(R.id.ib_close) ImageButton     mIbClose;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnPhotoRemoveListener {
        void onRemove(int position);
    }

    public void setOnPhotoRemoveListener(OnPhotoRemoveListener listener) {
        mOnPhotoRemoveListener = listener;
    }
}
