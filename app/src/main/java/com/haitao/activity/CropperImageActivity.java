package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.ImageUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.view.photoView.OnPhotoTapListener;
import com.haitao.view.photoView.OnViewTapListener;
import com.haitao.view.photoView.PhotoDraweeView;

/**
 * 图片裁剪
 * Created by pxl(彭小利) on 2015/9/24.
 */
public class CropperImageActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private PhotoPickParameterObject mPhotoPickParameterObject;
    private TextView btnReselection, btnSelection;
    private PhotoDraweeView cropImage; // 放缩按钮
    private ProgressBar bar;// 等待框
    public Bitmap itbmp; // 需要传递的Bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper_image);
        TAG = "裁剪图片";
        mContext = this;
        initExtra();
        initView();
        initEvent();
        initData();
    }

    //获取传过来的参数
    private void initExtra() {

        Intent intent = getIntent();
        try {
            mPhotoPickParameterObject = (PhotoPickParameterObject) intent.getSerializableExtra(mPhotoPickParameterObject.EXTRA_PARAMETER);
        } catch (Exception e) {
        }

    }

    private void initView() {
        btnReselection = (TextView) findViewById(R.id.btnReselection);
        btnSelection = (TextView) findViewById(R.id.btnSelection);
        cropImage = (PhotoDraweeView) findViewById(R.id.cropImage);
        bar = (ProgressBar) findViewById(R.id.bar);
        // 设置编辑图片View的大小
        ViewGroup.LayoutParams rlp = cropImage.getLayoutParams();
        rlp.height = ScreenUtils.getScreenWidth((Activity) mContext);
        rlp.width = ScreenUtils.getScreenWidth((Activity) mContext);
        cropImage.setLayoutParams(rlp);
    }

    private void initEvent() {
        btnReselection.setOnClickListener(this);
        btnSelection.setOnClickListener(this);
    }

    private void initData() {
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        String url = mPhotoPickParameterObject.image_list.get(0);
        if(url.contains("http://") || url.contains("https://") || url.contains("file://")){
            controller.setUri(Uri.parse(url));
        }else {
            controller.setUri(Uri.parse(String.format("file://%s", url)));
        }
        controller.setOldController(cropImage.getController());
        // You need setControllerListener
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || cropImage == null) {
                    return;
                }
                cropImage.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        cropImage.setController(controller.build());
        cropImage.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
              /*  Toast.makeText(view.getContext(), "onPhotoTap :  x =  " + x + ";" + " y = " + y,
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        cropImage.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
//                Toast.makeText(view.getContext(), "onViewTap", Toast.LENGTH_SHORT).show();
            }
        });

        cropImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(v.getContext(), "onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
       /* ImageLoaderUtils.showLocationImage(mPhotoPickParameterObject.image_list.get(0), cropImage, new ImageSize(480, 800), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });*/
//        cropImage.setImageBitmap(ImageUtil.getNomalBitmap(mPhotoPickParameterObject.image_list.get(0), 480, 800));
//        File imageFile = new File(mPhotoPickParameterObject.getImage_list().get(0));
        //        Picasso.with(mContext)
        //                .load(imageFile)
        //                .error(R.drawable.ic_photo_loading)
        //                .into(cropImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReselection://重选
                finish();
                break;
            case R.id.btnSelection://选取
                bar.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        getViewBitmap();
                        Message m = new Message();
                        m.what = 0x111;
                        mHandler.sendMessage(m);
                    }
                }.start();
                break;
        }
    }


    private void getViewBitmap() {
        cropImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(cropImage.getDrawingCache());
        // 清缓存
        cropImage.destroyDrawingCache();
        int w = cropImage.getWidth();
        itbmp = Bitmap.createBitmap(bitmap, 0, 0, w, w);
    }

    // Handle机制
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x111) {
                bar.setVisibility(View.GONE);
                String path = ImageUtils.saveAsBitmap(CropperImageActivity.this, itbmp, Constant.SHEAR_PATH, null);
                itbmp.recycle();
                if (mPhotoPickParameterObject.filter_image) {//加水印操作

                } else {
                    PhotoPickActivity.instance.getForResultComplate(path);
                }

                finish();
            }
        }
    };
}
