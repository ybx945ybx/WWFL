package com.haitao.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.haitao.view.photoView.PhotoDraweeView;

import java.util.List;

/**
 * 查看大图，可放大缩小的图片
 */
public class PhotoPickShowAdapter extends PagerAdapter {
    private List<String> mData;
    private static ImagePipelineConfig config = null;
    private Context mContext;

    public PhotoPickShowAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
        /*if (null == config) {
            config = ImagePipelineConfig.newBuilder(mContext).setDownsampleEnabled(true).build();
            Fresco.initialize(mContext, config);
        }*/
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        String                          url        = mData.get(position);
        if (url != null && (url.contains("http://") || url.contains("https://") || url.contains("file://"))) {
            controller.setUri(Uri.parse(url));
        } else {
            controller.setUri(Uri.parse(String.format("file://%s", url)));
        }
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());

        try {
            viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoDraweeView;
    }

}
