package com.haitao.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.SuccessWithStringModel;

import static com.haitao.activity.CaptureActivity.ScanType.BANK_CARD;
import static com.haitao.activity.CaptureActivity.ScanType.ID_CARD;

/**
 * 线下返利扫描银行卡
 */
public class CaptureActivity extends FragmentActivity implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Camera        mCamera;
    SurfaceView    mSurfaceView;
    RelativeLayout layoutRoot;
    private ProgressDialog mDialog;
    private boolean        isPause;

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private int mType; // 类型 （身份证 or 银行卡）

    @IntDef({BANK_CARD, ID_CARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanType {

        int BANK_CARD = 0;   // 银行卡

        int ID_CARD = 1; // 身份证
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);

        initVars();
        initView();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(TransConstant.TYPE, BANK_CARD);
        }
    }

    private void initView() {
        initSurfaceView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            openCamera();
        }
    }

    @OnClick({R.id.ib_left, R.id.btn_take_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                onBackPressed();
                break;
            case R.id.btn_take_picture:
                identification();
                break;
        }
    }

    /**
     * 初始化相机预览
     */
    private void initSurfaceView() {
        layoutRoot = findViewById(R.id.layout_root);
        mSurfaceView = findViewById(R.id.camera_surfaceView);
        mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
    }

    /**
     * 获取权限后初始化相机
     */
    protected void doPermissionAction() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = mCamera.getParameters();
        //照相机宽高参数
        Size size = getSupportedSize(parameters.getSupportedPreviewSizes(), 0);
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
        }
        size = getSupportedSize(parameters.getSupportedPictureSizes(), 1280);
        if (size != null) {
            parameters.setPictureSize(size.width, size.height);
        }

        //        parameters.set("orientation", "portrait");
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90); /* 设置一下preview的大小 */

        mCamera.startPreview();
    }


    /**
     * 拍照上传识别
     */
    private boolean isControl = false;

    protected final void identification() {
        try {
            if (mCamera != null && !isControl) {
                isControl = true;
                mCamera.takePicture(null, null, (bytes, camera) -> {
                    isControl = false;
                    Bitmap bitmap   = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    String fileData = ImageUtil.compressImageBase64(bitmap);
                    showProgressDialog("正在识别...");
                    // 成功回调
                    Response.Listener<SuccessWithStringModel> successListener = response -> {
                        Logger.d(response.toString());
                        dismissProgressDialog();
                        if ("0".equals(response.getCode())) {
                            releaseCamera();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("cardNum", response.getData());
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            releaseCamera();
                            ToastUtils.show(CaptureActivity.this, response.getMsg());
                            finish();

                        }
                    };
                    // 失败回调
                    Response.ErrorListener errorListener = error -> {
                        dismissProgressDialog();
                        if (error.getCause() instanceof UnknownHostException) {
                            ToastUtils.show(CaptureActivity.this, "网络异常");
                        } else {
                            ToastUtils.show(CaptureActivity.this, error.getMessage());
                        }
                        finish();
                    };
                    // 网络请求
                    if (mType == BANK_CARD) {
                        // 银行卡扫描
                        ForumApi.getInstance().userAccountBankCardScanningPost(fileData, successListener, errorListener);
                    } else {
                        // 身份证扫描
                        ForumApi.getInstance().userAccountIdCardScanningPost(fileData, successListener, errorListener);
                    }
                });
            }
        } catch (Exception e) {
            isControl = false;
        }
    }

    /**
     * 释放摄像头
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        if (mCamera != null) {
            releaseCamera();
        }
        checkAndDoAction();
    }

    private void checkAndDoAction() {
        String[] mPermissionGroup = getPermissionGroup();
        if (!(mPermissionGroup != null && mPermissionGroup.length > 0
                && Build.VERSION.SDK_INT >= 23
                && !handPermission(mPermissionGroup, this, PERMISSION_REQUEST_CODE))) {
            //6.0.0以下权限判断，或者6.0以上但是有禁止权限选择的手机
            doActionCatch();
        }
    }

    private void doActionCatch() {
        try {
            doPermissionAction();
        } catch (Exception e) {
            showPermissonDialog();
        }
    }

    /**
     * 6。0以上需要的权限
     *
     * @return
     */
    protected String[] getPermissionGroup() {
        return new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    /**
     * 6。0以上权限判断
     *
     * @param mPermissionGroup
     * @param context
     * @param requestCode
     * @return
     */
    private boolean handPermission(String[] mPermissionGroup, Activity context, int requestCode) {
        // 过滤已持有的权限
        List<String> mRequestList = new ArrayList<>();
        for (String permission : mPermissionGroup) {
            if ((ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED)) {
                mRequestList.add(permission);
            }
        }
        // 申请未持有的权限
        if (Build.VERSION.SDK_INT >= 23 && !mRequestList.isEmpty()) {
            ActivityCompat.requestPermissions(context, mRequestList.toArray(
                    new String[mRequestList.size()]), requestCode);
        } else {
            // 权限都有了，就可以继续后面的操作
            return true;
        }
        return false;
    }

    /**
     * 优先获取1280*720
     *
     * @param list
     * @return
     */
    private Camera.Size getSupportedSize(List<Camera.Size> list, int maxSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Camera.Size s : list) {
            if (s.width == 1280 && s.height == 720) {
                return s;
            }
        }
        int i = 0;
        if (maxSize > 0) {
            int size = list.size();
            for (i = 0; i < size; i++) {
                Camera.Size s = list.get(i);
                if (maxSize >= s.width) {
                    return s;
                }
            }
            i--;
        }
        return list.get(i);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isPause = true;
        releaseCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults.length > 0 && requestCode == PERMISSION_REQUEST_CODE) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    showPermissonDialog();
                    return;
                }
            }
            doActionCatch();
        }
    }

    public static void launch(Activity context, int requestCode, int type) {
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        context.startActivityForResult(intent, requestCode);
    }

    private void showPermissonDialog() {
        ToastUtils.show(CaptureActivity.this, "请先打开权限");
    }

    private void showProgressDialog(String text) {
        showProgressDialog(text, false);
    }

    private void showProgressDialog(String text, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(this);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }
            mDialog.setMessage(text);
            if (!isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    private boolean isShowing() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    private void dismissProgressDialog() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
        }
    }
}
