package com.haitao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.haitao.R;
import com.haitao.utils.universalimageloader.core.DisplayImageOptions;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.utils.universalimageloader.core.assist.FailReason;
import com.haitao.utils.universalimageloader.core.assist.ImageScaleType;
import com.haitao.utils.universalimageloader.core.assist.ImageSize;
import com.haitao.utils.universalimageloader.core.download.ImageDownloader;
import com.haitao.utils.universalimageloader.core.listener.ImageLoadingListener;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;


public class ImageLoaderUtils {

    private static ImagePipelineConfig config  = null;
    private static ResizeOptions       options = new ResizeOptions(320, 320);

    public static void showLocationImage(String path,
                                         ImageView imageView, int loadingImg, ImageSize size, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingImg)
                .showImageForEmptyUri(loadingImg)
                .showImageOnFail(loadingImg)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .considerExifParams(true)
                .build();
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(path);
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, size, listener);

    }


    public static void showLocationImage(String path,
                                         ImageView imageView, ImageSize size, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .considerExifParams(true)
                .build();
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(path);
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, size, listener);

    }

    public static void showLocationImage(String path,
                                         ImageView imageView, int loadingImg) {
        showLocationImage(path, imageView, loadingImg, null, null);

    }

    public static void showLocationImage(String path, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                //设置图片在下载前是否重置，复位
                .build();
        //
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(path);
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ((ImageView) view).setImageResource(R.mipmap.ic_photo_loading);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ((ImageView) view).setImageResource(R.mipmap.ic_photo_loading);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage == null) {
                    ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ((ImageView) view).setImageResource(R.mipmap.ic_photo_loading);
                    return;
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((ImageView) view).setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

    }

    public static void showDrawableImage(int resId,
                                         final ImageView imageView, final int loadingImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingImg)
                .showImageForEmptyUri(loadingImg)
                .showImageOnFail(loadingImg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .resetViewBeforeLoading(true)
                //设置图片在下载前是否重置，复位
                .build();
        //
        String imageUrl = "drawable://" + resId;
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageResource(loadingImg);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageResource(loadingImg);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage == null) {
                    ((ImageView) view).setImageResource(loadingImg);
                    return;
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

    public static void showLocationImage(String path, CustomImageView imageView) {
        imageView.setImageURI(Uri.parse(path));
    }


    public static void showLocationImage(Context mContext, String path, CustomImageView imageView) {
       /* if (null == config) {
            config = ImagePipelineConfig.newBuilder(mContext).setDownsampleEnabled(true).build();
            Fresco.initialize(mContext, config);
        }*/

        //                .setResizeOptions(options)

        Logger.d(String.format("%s, %s", imageView.getWidth(), imageView.getHeight()));
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(path))
                .setResizeOptions(options)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageView.getController())
                .build();
        imageView.setController(controller);
    }

    public static void showOnlineImage(String path,
                                       final ImageView imageView) {
        showOnlineImage(path, imageView, R.mipmap.ic_photo_loading);
    }

    public static void showOnlineImage(String path,
                                       CustomImageView imageView) {
        if (null == path)
            path = "";
        imageView.setImageURI(Uri.parse(path));
    }

    public static void showOnlineGifImage(String path,
                                          CustomImageView imageView) {
        if (null == path)
            path = "";
        Uri uri = Uri.parse(path);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(controller);
    }

    public static void clear() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
    }

    public static void showOnlineImage(String path,
                                       final ImageView imageView, final int loadingImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(loadingImg)
                .showImageOnFail(loadingImg)
                .showImageOnLoading(loadingImg)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
        ImageLoader.getInstance().displayImage(path, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageResource(loadingImg);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageResource(loadingImg);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage == null) {
                    ((ImageView) view).setImageResource(loadingImg);
                    return;
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

    }

    /**
     * 降价提醒列表页的图片
     *
     * @param dealPic
     * @param originPic
     * @param imageView
     */
    public static void showDepreciateImage(String dealPic, String originPic,
                                           final CustomImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
        ImageLoader.getInstance().displayImage(dealPic, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setImageURI(Uri.parse(originPic));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage == null) {
                    return;
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

    }

    /**
     * 下载图片
     *
     * @param mContext
     * @param imageUri
     */
    public static void downloadOnlineImage(Context mContext, final String imageUri) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUri))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(options)
                .setAutoRotateEnabled(true)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, mContext);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     // You can use the bitmap in only limited ways
                                     // No need to do any cleanup.
                                     String picPath = FileUtils.saveBitmap(mContext, bitmap, FileUtils.getFileName(imageUri));
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     // No cleanup required here.
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }


    /**
     * 下载图片
     *
     * @param mContext
     * @param imageUri
     * @param subscriver
     */
    public static void downloadOnlineImage(Context mContext, final String imageUri, BaseBitmapDataSubscriber subscriver) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUri))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(options)
                .setAutoRotateEnabled(true)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, mContext);

        dataSource.subscribe(subscriver, CallerThreadExecutor.getInstance());
    }


    /**
     * 下载图片
     *
     * @param mContext
     * @param imageUri
     */
    public static void downloadOnlineImage(Context mContext, final String imageUri, final OnDownloadCallbackLitener onDownloadCallbackLitener) {
        if (TextUtils.isEmpty(imageUri)) {
            if (null != onDownloadCallbackLitener) {
                onDownloadCallbackLitener.onCallback(false);
            }
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUri))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(options)
                .setAutoRotateEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, mContext);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     // You can use the bitmap in only limited ways
                                     // No need to do any cleanup.
                                     String picPath = FileUtils.saveBitmap(mContext, bitmap, FileUtils.getFileName(imageUri));
                                     if (null != onDownloadCallbackLitener) {
                                         onDownloadCallbackLitener.onCallback(true);
                                     }
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     // No cleanup required here.
                                     if (null != onDownloadCallbackLitener) {
                                         onDownloadCallbackLitener.onCallback(false);
                                     }
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    public interface OnDownloadCallbackLitener {
        void onCallback(boolean status);
    }
}