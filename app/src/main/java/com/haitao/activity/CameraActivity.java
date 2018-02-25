package com.haitao.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.camera.CameraHelper;
import com.haitao.utils.camera.IoUtils;
import com.haitao.utils.universalimageloader.core.assist.ImageSize;
import com.haitao.view.CameraGrid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 照相机
 * Created by pxl(彭小利) on 2015/9/28.
 */
public class CameraActivity extends BaseActivity implements View.OnClickListener {
    private Context        mContext;
    private CameraGrid     masking;
    private RelativeLayout takePhotoPanel, cameraTop;
    private LinearLayout llFlash;
    private SurfaceView  surfaceView;
    private ImageView    ivImage, btnPhoto, btnChange;
    private TextView btnCanlce, btnFlash, btnAuto, btnOn, btnOff;
    private View focusIndex;
    private int mCurrentCameraId = 0;  //1是前置 0是后置

    private Camera cameraInst = null;
    private CameraHelper mCameraHelper;
    private Camera.Parameters parameters  = null;
    private Camera.Size       adapterSize = null;
    private Camera.Size       previewSize = null;
    private Bundle            bundle      = null;
    private int               PHOTO_SIZE  = 2000;
    private float pointX, pointY;
    static final int FOCUS = 1;            // 聚焦
    static final int ZOOM  = 2;            // 缩放
    private int   mode;                      //0是聚焦 1是放大
    private float dist;
    /**
     * 最小预览界面的分辨率
     */
    private static final int     MIN_PREVIEW_PIXELS    = 480 * 320;
    /**
     * 最大宽高比差
     */
    private static final double  MAX_ASPECT_DISTORTION = 0.15;
    private              Handler handler               = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        TAG = "照相机";
        mCameraHelper = new CameraHelper(this);
        mContext = this;
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        masking = (CameraGrid) findViewById(R.id.masking);
        takePhotoPanel = (RelativeLayout) findViewById(R.id.panel_take_photo);
        cameraTop = (RelativeLayout) findViewById(R.id.camera_top);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btnCanlce = (TextView) findViewById(R.id.btnCancle);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnPhoto = (ImageView) findViewById(R.id.btnPhoto);
        btnFlash = (TextView) findViewById(R.id.btnFlash);
        btnChange = (ImageView) findViewById(R.id.btnChange);
        focusIndex = findViewById(R.id.focus_index);
        llFlash = (LinearLayout) findViewById(R.id.llFlash);
        btnOn = (TextView) findViewById(R.id.btnOn);
        btnOff = (TextView) findViewById(R.id.btnOff);
        btnAuto = (TextView) findViewById(R.id.btnAuto);
        initParams();
        initSurfaceView();
        if (!canSwitchCamera()) {
            btnChange.setVisibility(View.GONE);
        } else {
            btnChange.setVisibility(View.VISIBLE);
        }
    }

    private void initSurfaceView() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
    }

    private void initParams() {
        //设置相机界面,照片列表,以及拍照布局的高度(保证相机预览为正方形)
        ViewGroup.LayoutParams layout = masking.getLayoutParams();
        layout.height = ScreenUtils.getScreenWidth((Activity) mContext);
        layout = takePhotoPanel.getLayoutParams();
        layout.height = ScreenUtils.getScreenHeight((Activity) mContext)
                - ScreenUtils.getScreenWidth((Activity) mContext)
                - DensityUtil.dip2px(mContext, 60) - ScreenUtils.getStatusBarHeight(mContext);
    }

    private void initEvent() {
        btnCanlce.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnFlash.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        takePhotoPanel.setOnClickListener(this);
        cameraTop.setOnClickListener(this);
        surfaceView.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 主点按下
                case MotionEvent.ACTION_DOWN:
                    pointX = event.getX();
                    pointY = event.getY();
                    mode = FOCUS;
                    break;
                // 副点按下
                case MotionEvent.ACTION_POINTER_DOWN:
                    dist = spacing(event);
                    // 如果连续两点距离大于10，则判定为多点模式
                    if (spacing(event) > 10f) {
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = FOCUS;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == FOCUS) {
                        //pointFocus((int) event.getRawX(), (int) event.getRawY());
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            float tScale = (newDist - dist) / dist;
                            if (tScale < 0) {
                                tScale = tScale * 10;
                            }
                            addZoomIn((int) tScale);
                        }
                    }
                    break;
            }
            return false;
        });
        surfaceView.setOnClickListener(this);
        btnAuto.setOnClickListener(this);
        btnOff.setOnClickListener(this);
        btnOn.setOnClickListener(this);
    }

    private void initData() {
        String path = getIntent().getStringExtra(TransConstant.OBJECT);
        ImageLoaderUtils.showLocationImage(path, ivImage, R.mipmap.ic_default_240, new ImageSize(200, 200), null);
    }

    private boolean canSwitchCamera() {
        boolean canSwitch = false;
        try {
            canSwitch = mCameraHelper.hasFrontCamera() && mCameraHelper.hasBackCamera();
        } catch (Exception e) {
            //获取相机信息失败
        }
        return canSwitch;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancle://取消
            case R.id.ivImage://图片
                finish();
                break;
            case R.id.btnChange://摄像头转换
                switchCamera();
                break;
            case R.id.btnFlash://闪光操作
                if (llFlash.getVisibility() == View.GONE) {
                    openFlashLightView(cameraInst);
                } else {
                    llFlash.setVisibility(View.GONE);
                }

                break;
            case R.id.btnPhoto://拍照
                try {
                    cameraInst.takePicture(null, null, new PictureCallback());
                } catch (Throwable t) {
                    t.printStackTrace();
                    ToastUtils.show(mContext, R.string.camera_take_photo_error);
                    try {
                        cameraInst.startPreview();
                    } catch (Throwable e) {

                    }
                }
                break;
            case R.id.surfaceView:
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
                layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
                focusIndex.setLayoutParams(layout);
                focusIndex.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(800);
                focusIndex.startAnimation(sa);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusIndex.setVisibility(View.INVISIBLE);
                    }
                }, 800);
                break;
            case R.id.btnAuto://自动
                setFlashLight(Camera.Parameters.FLASH_MODE_AUTO, cameraInst);
                break;
            case R.id.btnOn://打开
                setFlashLight(Camera.Parameters.FLASH_MODE_ON, cameraInst);
                break;
            case R.id.btnOff://关闭
                setFlashLight(Camera.Parameters.FLASH_MODE_OFF, cameraInst);
                break;
            case R.id.panel_take_photo:
            case R.id.camera_top:
                break;
            default:
        }
    }

    private final class PictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            bundle = new Bundle();
            bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
            new SavePicTask(data).execute();
            camera.startPreview(); // 拍完照后，重新开始预览
        }
    }

    /**
     * 保存图片线程
     */

    private class SavePicTask extends AsyncTask<Void, Void, String> {
        private byte[] data;

        protected void onPreExecute() {
            ProgressDialogUtils.show(mContext, R.string.camera_operating);
            btnPhoto.setClickable(false);
        }


        SavePicTask(byte[] data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return saveToSDCard(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ProgressDialogUtils.dismiss();
            btnPhoto.setClickable(true);
            if (!TextUtils.isEmpty(result)) {//说明保存后图片的路径，
                Intent intent = new Intent();
                intent.putExtra(TransConstant.OBJECT, result);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.show(mContext, R.string.camera_take_photo_error);
            }
        }
    }

    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;

    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    public String saveToSDCard(byte[] data) throws IOException {
        Bitmap croppedImage;
        if (data == null) {
            Toast.makeText(CameraActivity.this, "拍摄失败！", Toast.LENGTH_SHORT).show();
            return "";
        }
        croppedImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        // saveBitmap(bm,"00000.jpg");
        if (croppedImage.getWidth() > croppedImage.getHeight()) {
            croppedImage = rotaingImageView(90, croppedImage);
        }
        int    width   = ScreenUtils.getScreenWidth((Activity) mContext);
        int    h       = width * croppedImage.getHeight() / croppedImage.getWidth();
        Bitmap mBitmap = Bitmap.createScaledBitmap(croppedImage.copy(Bitmap.Config.ARGB_8888, true), width, h, false);

        // saveBitmap(mBitmap,"1111.jpg");
        croppedImage.recycle();

        // 截图
        mBitmap = Bitmap.createBitmap(mBitmap.copy(Bitmap.Config.ARGB_8888, true), masking.getLeft(), masking.getTop(),
                masking.getWidth(), masking.getHeight());


        //        //获得图片大小
        //        BitmapFactory.Options options = new BitmapFactory.Options();
        //        options.inJustDecodeBounds = true;
        //        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        //
        //        PHOTO_SIZE = options.outHeight > options.outWidth ? options.outWidth : options.outHeight;
        //        int height = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
        //        options.inJustDecodeBounds = false;
        //
        //        if (PHOTO_SIZE>2000){
        //            PHOTO_SIZE=2000;
        //        }
        //        FLog.e("tg", " outHeight = " + PHOTO_SIZE);
        //        Rect r;
        //        if (mCurrentCameraId == 1) {
        //            r = new Rect(height - PHOTO_SIZE, 0, height, PHOTO_SIZE);
        //        } else {
        //            r = new Rect(0, 0, PHOTO_SIZE, PHOTO_SIZE);
        //        }
        //        try {
        //            croppedImage = decodeRegionCrop(data, r);
        //        } catch (Exception e) {
        //            return null;
        //        }
        String imagePath = ImageUtils.saveAsBitmap(mContext, mBitmap, Constant.CAMERA_PATH, null);
        mBitmap.recycle();
        return imagePath;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private Bitmap decodeRegionCrop(byte[] data, Rect rect) {

        InputStream is = null;
        System.gc();
        Bitmap croppedImage = null;
        try {
            is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
            } catch (IllegalArgumentException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IoUtils.closeStream(is);
        }
        Matrix m = new Matrix();
        m.setRotate(90, PHOTO_SIZE / 2, PHOTO_SIZE / 2);
        if (mCurrentCameraId == 1) {
            m.postScale(1, -1);
        }
        Bitmap rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, PHOTO_SIZE, PHOTO_SIZE, m, true);
        if (rotatedImage != croppedImage)
            croppedImage.recycle();
        return rotatedImage;
    }

    //切换前后置摄像头
    private void switchCamera() {
        mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
        releaseCamera();
        setUpCamera(mCurrentCameraId);
    }

    private void releaseCamera() {
        if (cameraInst != null) {
            cameraInst.setPreviewCallback(null);
            cameraInst.release();
            cameraInst = null;
        }
        adapterSize = null;
        previewSize = null;
    }

    /**
     * @param mCurrentCameraId2
     */
    private void setUpCamera(int mCurrentCameraId2) {
        cameraInst = getCameraInstance(mCurrentCameraId2);
        if (cameraInst != null) {
            try {
                cameraInst.setPreviewDisplay(surfaceView.getHolder());
                initCamera();
                cameraInst.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.show(mContext, R.string.camera_switch_error);

        }
    }

    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = mCameraHelper.openCamera(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /*SurfaceCallback*/
    private final class SurfaceCallback implements SurfaceHolder.Callback {

        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (cameraInst != null) {
                    cameraInst.stopPreview();
                    cameraInst.release();
                    cameraInst = null;
                }
            } catch (Exception e) {
                //相机已经关了
            }

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (null == cameraInst) {
                try {
                    cameraInst = Camera.open();
                    cameraInst.setPreviewDisplay(holder);
                    initCamera();
                    cameraInst.startPreview();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            autoFocus();
        }
    }

    private void initCamera() {
        parameters = cameraInst.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        //if (adapterSize == null) {
        setUpPreviewSize(parameters);
        //        setUpPicSize(parameters);

        //}
        if (previewSize != null) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setPictureSize(previewSize.width, previewSize.height);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        setDispaly(parameters, cameraInst);
        try {
            cameraInst.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraInst.startPreview();
        cameraInst.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    private void setUpPicSize(Camera.Parameters parameters) {

        if (adapterSize != null) {
            return;
        } else {
            adapterSize = findBestPictureResolution();
            return;
        }
    }

    private void setUpPreviewSize(Camera.Parameters parameters) {

        if (previewSize != null) {
            return;
        } else {
            previewSize = findBestPreviewResolution();
        }
    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
//            Log.e("Came_e", "图像出错");
        }
    }

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    private Camera.Size findBestPreviewResolution() {
        Camera.Parameters cameraParameters         = cameraInst.getParameters();
        Camera.Size       defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Camera.Size> supportedPreviewResolutions = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }
        Log.v(TAG, "Supported preview resolutions: " + previewResolutionSb);


        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtils.getScreenHeight((Activity) mContext)
                / (double) ScreenUtils.getScreenHeight((Activity) mContext);
        Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int         width                      = supportedPreviewResolution.width;
            int         height                     = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int     maybeFlippedWidth   = isCandidatePortrait ? height : width;
            int     maybeFlippedHeight  = isCandidatePortrait ? width : height;
            double  aspectRatio         = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double  distortion          = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == ScreenUtils.getScreenWidth((Activity) mContext)
                    && maybeFlippedHeight == ScreenUtils.getScreenHeight((Activity) mContext)) {
                return supportedPreviewResolution;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        //        if (!supportedPreviewResolutions.isEmpty()) {
        //            Camera.Size largestPreview = supportedPreviewResolutions.get(0);
        //            return largestPreview;
        //        }

        // 没有找到合适的，就返回默认的

        return defaultPreviewResolution;
    }

    private Camera.Size findBestPictureResolution() {
        Camera.Parameters cameraParameters        = cameraInst.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }
        Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);

        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
        Log.d(TAG, "default picture resolution " + defaultPictureResolution.width + "x"
                + defaultPictureResolution.height);

        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtils.getScreenWidth((Activity) mContext)
                / (double) ScreenUtils.getScreenHeight((Activity) mContext);
        Iterator<Camera.Size> it = sortedSupportedPicResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int         width                      = supportedPreviewResolution.width;
            int         height                     = supportedPreviewResolution.height;

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int     maybeFlippedWidth   = isCandidatePortrait ? height : width;
            int     maybeFlippedHeight  = isCandidatePortrait ? width : height;
            double  aspectRatio         = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double  distortion          = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        //        if (!sortedSupportedPicResolutions.isEmpty()) {
        //            return sortedSupportedPicResolutions.get(0);
        //        }

        // 没有找到合适的，就返回默认的
        return defaultPictureResolution;
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (cameraInst == null) {
                    return;
                }
                cameraInst.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }

    private void openFlashLightView(Camera mCamera) {
        llFlash.setVisibility(View.VISIBLE);
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        String flashMode = mCamera.getParameters().getFlashMode();

        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();

        for (int i = 0; i < supportedModes.size(); i++) {
            switch (supportedModes.get(i)) {
                case Camera.Parameters.FLASH_MODE_ON:
                    btnOn.setVisibility(View.VISIBLE);
                    if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                        btnOn.setTextColor(getResources().getColor(R.color.midOrange));
                    } else {
                        btnOn.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
                case Camera.Parameters.FLASH_MODE_AUTO:
                    btnAuto.setVisibility(View.VISIBLE);
                    if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
                        btnAuto.setTextColor(getResources().getColor(R.color.midOrange));
                    } else {
                        btnAuto.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
                case Camera.Parameters.FLASH_MODE_OFF:
                    btnOff.setVisibility(View.VISIBLE);
                    if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                        btnOff.setTextColor(getResources().getColor(R.color.midOrange));
                    } else {
                        btnOff.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
            }
        }

    }

    /**
     * 设置闪光
     *
     * @param flasmode
     * @param mCamera
     */
    private void setFlashLight(String flasmode, Camera mCamera) {
        llFlash.setVisibility(View.GONE);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(flasmode);
        mCamera.setParameters(parameters);
        Drawable drawable = null;
        switch (flasmode) {
            case Camera.Parameters.FLASH_MODE_ON:
                btnFlash.setText(R.string.camera_open);
                btnFlash.setTextColor(getResources().getColor(R.color.midOrange));
                drawable = getResources().getDrawable(R.mipmap.ic_camera_flash_on);
                break;
            case Camera.Parameters.FLASH_MODE_AUTO:
                btnFlash.setText(R.string.camera_auto);
                drawable = getResources().getDrawable(R.mipmap.ic_camera_flash_off);
                btnFlash.setTextColor(getResources().getColor(R.color.white));
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                btnFlash.setText(R.string.camera_close);
                drawable = getResources().getDrawable(R.mipmap.ic_camera_flash_off);
                btnFlash.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        btnFlash.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        btnFlash.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.px10));
    }


    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //放大缩小
    int curZoomValue = 0;

    private void addZoomIn(int delta) {

        try {
            Camera.Parameters params = cameraInst.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                cameraInst.setParameters(params);
                return;
            } else {
                cameraInst.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定点对焦的代码
    private void pointFocus(int x, int y) {
        cameraInst.cancelAutoFocus();
        parameters = cameraInst.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        cameraInst.setParameters(parameters);
        autoFocus();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / ScreenUtils.getScreenWidth((Activity) mContext) + 1000;
            int rectX = y * 2000 / ScreenUtils.getScreenHeight((Activity) mContext) - 1000;

            int  left   = rectX < -900 ? -1000 : rectX - 100;
            int  top    = rectY < -900 ? -1000 : rectY - 100;
            int  right  = rectX > 900 ? 1000 : rectX + 100;
            int  bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1  = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

}
