package com.haitao.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.PhotoFolderAdapter;
import com.haitao.adapter.PhotoPickAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoFolderObject;
import com.haitao.model.PhotoPickImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片选择
 * Created by pxl(彭小利) on 2015/9/18.
 */
public class PhotoPickActivity extends BaseActivity implements View.OnClickListener, PhotoPickAdapter.OnChekClickLitener {
    // 不同loader定义
    private static final int               LOADER_ALL      = 0;
    private static final int               LOADER_CATEGORY = 1;
    static               PhotoPickActivity instance        = null;
    private GridView                 gvList;
    private TextView                 tvTimeLine;
    private PhotoPickAdapter         mPhotoPickAdapter;
    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private ArrayList<String> resultList = new ArrayList<String>();// 结果数据
    private RelativeLayout llDropBox;      //列表分类
    private ListView       lvList;
    private View           mask;
    private ArrayList<PhotoFolderObject> mResultFolder  = new ArrayList<PhotoFolderObject>();// 文件夹数据
    private boolean                      hasFolderGened = false;
    private PhotoFolderAdapter mPhotoFolderAdapter;
    private Animation          mShowAction, mHiddenAction, mMaskAction;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, PhotoPickParameterObject mPhotoPickParameterInfo) {
        Intent intent = new Intent(context, PhotoPickActivity.class);
        Bundle b      = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        intent.putExtras(b);
        ((Activity) context).startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick);

        initVars();
        initView();
        initEvent();
        initData();
        getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
    }

    //获取传过来的参数
    private void initVars() {
        TAG = "选择图片";
        instance = this;
        Intent intent = getIntent();
        try {
            mPhotoPickParameterInfo = (PhotoPickParameterObject) intent.getSerializableExtra(PhotoPickParameterObject.EXTRA_PARAMETER);
            resultList = null == mPhotoPickParameterInfo.image_list ? new ArrayList<String>() : mPhotoPickParameterInfo.image_list;
        } catch (Exception e) {
        }

    }

    private void initView() {
        initTop();
        tvTitle.setText(R.string.photo_pick_all);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_arrow_down);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        tvTitle.setCompoundDrawablePadding(10);
        if (!mPhotoPickParameterInfo.single_mode) {
            tvRight.setText("完成");
            if (resultList != null && resultList.size() > 0) {
                tvRight.setVisibility(View.VISIBLE);
            }
        }
        gvList = (GridView) findViewById(R.id.gv_order_pics);
        tvTimeLine = (TextView) findViewById(R.id.tvTimeLine);
        llDropBox = (RelativeLayout) findViewById(R.id.llDropBox);
        lvList = (ListView) findViewById(R.id.lvList);
        mask = findViewById(R.id.mask);

    }

    private void initEvent() {
        tvTitle.setOnClickListener(this);
        mask.setOnClickListener(this);
        /*gvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

//                final Picasso picasso = Picasso.with(mContext);
//                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
//                    picasso.resumeTag(mContext);
//                } else {
//                    picasso.pauseTag(mContext);
//                }

                if (state == SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    tvTimeLine.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING) {
                    tvTimeLine.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (tvTimeLine.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    PhotoPickImageObject photoPickImageInfo = (PhotoPickImageObject) view.getAdapter().getItem(index);
                    if (photoPickImageInfo != null) {
                        tvTimeLine.setText(DateUtil.formatPhotoDate(photoPickImageInfo.path));
                    }
                }
            }
        });*/
        gvList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {
                int columnWidth = ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 2 * 3);
                mPhotoPickAdapter.setItemSize(columnWidth / 4);
                gvList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        gvList.setOnItemClickListener((adapterView, view, i, l) -> {

            if (mPhotoPickAdapter.isShowCamera()) {
                if (i == 0) {
                    if (mPhotoPickParameterInfo.max_image == resultList.size()) {
                        ToastUtils.show(mContext, R.string.photo_pick_msg_amount_limit);
                    } else {
                        showCameraAction();
                    }
                    return;
                }
            }
            selectImageFromGrid((PhotoPickImageObject) adapterView.getAdapter().getItem(i), i);
        });
        tvRight.setOnClickListener(this);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            mPhotoFolderAdapter.setSelectIndex(position);
            lvList.startAnimation(mHiddenAction);
            mask.clearAnimation();
            mask.startAnimation(mMaskAction);
            final int index = position;
            new Handler().postDelayed(() -> {
                if (index == 0) {
                    getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    tvTitle.setText(R.string.photo_pick_all);
                    mPhotoPickAdapter.setShowCamera(mPhotoPickParameterInfo.show_camera);
                } else {
                    PhotoFolderObject folderInfo = mPhotoFolderAdapter.getItem(index);
                    if (null != folderInfo) {
                        mPhotoPickAdapter.setData(folderInfo.imageInfos);
                        tvTitle.setText(folderInfo.name);
                        // 设定默认选择
                        if (resultList != null && resultList.size() > 0) {
                            mPhotoPickAdapter.setSelectedList(resultList);
                        }
                    }
                    // mImageAdapter.setShowCamera(false);
                }
                // 滑动到最初始位置
                gvList.smoothScrollToPosition(0);

            }, 600);
        });
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_CAMERA);
        } else {
            toCameraActivity();
        }
    }

    private void toCameraActivity() {
        // 跳转到系统照相机
        Intent intent = new Intent(mContext, CameraActivity.class);
        String path   = "";
        if (mPhotoPickAdapter.isShowCamera() && mPhotoPickAdapter.getImages().size() != 0) {
            path = mPhotoPickAdapter.getItem(1).path;
        }
        intent.putExtra(TransConstant.OBJECT, path);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_FROM_CAMERA);
    }

    /**
     * 请求权限回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCameraAction();
                } else {
                    ToastUtils.show(mContext, "请先打开相机权限");
                }
                break;
            }
        }
    }

    private void initData() {
        mPhotoPickAdapter = new PhotoPickAdapter(mContext, mPhotoPickParameterInfo.show_camera, mPhotoPickParameterInfo.single_mode);
        mPhotoPickAdapter.setOnChekClickLitener(this);
        gvList.setAdapter(mPhotoPickAdapter);
        mPhotoFolderAdapter = new PhotoFolderAdapter(mContext);
        lvList.setAdapter(mPhotoFolderAdapter);
        mShowAction = AnimationUtils.loadAnimation(this, R.anim.dropdown_in);
        mHiddenAction = AnimationUtils.loadAnimation(this, R.anim.dropdown_out);
        mMaskAction = AnimationUtils.loadAnimation(this, R.anim.dropdown_mask_out);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llDropBox.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvRight://完成
                if (resultList != null && resultList.size() > 0) {
                    selectComplate();
                }
                break;
            case R.id.tvTitle://
                if (llDropBox.getVisibility() == View.VISIBLE) {
                    lvList.startAnimation(mHiddenAction);
                    mask.clearAnimation();
                    mask.startAnimation(mMaskAction);
                } else if (llDropBox.getVisibility() == View.GONE) {
                    lvList.startAnimation(mShowAction);
                    llDropBox.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.mask://
                lvList.startAnimation(mHiddenAction);
                mask.clearAnimation();
                mask.startAnimation(mMaskAction);
                break;
            default:
        }

    }

    /**
     * 选择图片操作
     *
     * @param photoPickImageInfo
     */
    private void selectImageFromGrid(PhotoPickImageObject photoPickImageInfo, int postion) {
        if (photoPickImageInfo != null) {
            // 多选模式
            if (!mPhotoPickParameterInfo.single_mode) {
                mPhotoPickParameterInfo.position = postion - 1;
                mPhotoPickParameterInfo.image_list = resultList;
                mPhotoPickParameterInfo.image_data = mPhotoPickAdapter.getImages();
                Intent intent = new Intent(mContext, PhotoPickShowAcitivity.class);
                intent.putExtra(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
                startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
            } else {
                // 单选模式
                resultList.clear();
                resultList.add(photoPickImageInfo.path);
                selectComplate();
            }
        }
    }

    //选择完成实现跳转
    private void selectComplate() {
        mPhotoPickParameterInfo.image_list = resultList;
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        Intent intent = new Intent();
        intent.putExtras(b);

        if (mPhotoPickParameterInfo.single_mode) {
            //单选模式
            if (mPhotoPickParameterInfo.croper_image) {
                //跳转到图片裁剪
                intent = new Intent(this, CropperImageActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            } else if (mPhotoPickParameterInfo.filter_image) {
                //跳转到滤镜
                //                intent = new Intent(this, FilterImageActivity.class);
                //                intent.putExtras(b);
                //                startActivity(intent);
            } else {
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            //多选模式
            if (mPhotoPickParameterInfo.filter_image) {
                //跳转到滤镜
                //                intent = new Intent(this, FilterImageActivity.class);
                //                intent.putExtras(b);
                //                startActivity(intent);
            } else {
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    /**
     * 加载图片地址
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {

                ArrayList<PhotoPickImageObject> photoPickImageInfos = new ArrayList<PhotoPickImageObject>();
                int                             count               = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {

                        String path     = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name     = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long   dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int    size     = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        //                        boolean show_flag = size > 1024 * 100; //是否大于10K
                        PhotoPickImageObject imageInfo = new PhotoPickImageObject(path, name, dateTime);

                        if (new File(path).exists()) {
                            if (!path.contains("./")) {
                                photoPickImageInfos.add(imageInfo);
                            }
                        }


                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File              imageFile  = new File(path);
                            File              folderFile = imageFile.getParentFile();
                            PhotoFolderObject folderInfo = new PhotoFolderObject();
                            folderInfo.name = folderFile.getName();
                            folderInfo.path = folderFile.getAbsolutePath();
                            folderInfo.cover = imageInfo;
                            if (!mResultFolder.contains(folderInfo)) {
                                ArrayList<PhotoPickImageObject> imageList = new ArrayList<PhotoPickImageObject>();
                                imageList.add(imageInfo);
                                folderInfo.imageInfos = imageList;
                                mResultFolder.add(folderInfo);
                            } else {
                                // 更新
                                PhotoFolderObject f = mResultFolder.get(mResultFolder.indexOf(folderInfo));
                                f.imageInfos.add(imageInfo);
                            }
                        }

                    } while (data.moveToNext());
                    mPhotoPickAdapter.setData(photoPickImageInfos);
                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mPhotoPickAdapter.setSelectedList(resultList);
                    }
                    mPhotoFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == PhotoPickParameterObject.TAKE_PICTURE_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mPhotoPickParameterInfo.single_mode) {
                    resultList.clear();
                }
                resultList.add(data.getStringExtra(TransConstant.OBJECT));
                Log.i("tg", resultList.get(0));

                selectComplate();
            }
        } else if (requestCode == PhotoPickParameterObject.TAKE_PICTURE_PREVIEW) {//选择浏览图片
            if (resultCode == Activity.RESULT_OK) {
                PhotoPickParameterObject extra = (PhotoPickParameterObject) data.getSerializableExtra(mPhotoPickParameterInfo.EXTRA_PARAMETER);
                resultList = extra.image_list;
                switch (extra.type) {
                    case 0:
                        updateSelectPick();
                        break;
                    case 1:
                        if (resultList != null && resultList.size() > 0) {
                            selectComplate();
                        } else {
                            tvRight.setVisibility(View.INVISIBLE);
                        }
                        break;
                    default:
                }
            }
        }
    }

    private void updateSelectPick() {
        mPhotoPickAdapter.setSelectedList(resultList);
        if (resultList == null || resultList.size() <= 0) {
            tvRight.setVisibility(View.GONE);
        } else {
            tvRight.setText(String.format("(%d/%d)完成", resultList.size(), mPhotoPickParameterInfo.max_image));
            tvRight.setVisibility(View.VISIBLE);
        }
        for (int i = gvList.getFirstVisiblePosition(); i <= gvList.getLastVisiblePosition(); i++) {
            if (mPhotoPickAdapter.getItem(i) == null) {
                continue;
            }
            View v = gvList.getChildAt(i - gvList.getFirstVisiblePosition()).findViewById(R.id.checkmark);
            if (resultList.contains(mPhotoPickAdapter.getItem(i).path)) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

    @Override
    public void onCheckedClick(PhotoPickImageObject info, View view) {
        if (resultList.contains(info.path)) {
            resultList.remove(info.path);
            view.setSelected(false);
        } else {
            // 判断选择数量问题
            if (mPhotoPickParameterInfo.max_image == resultList.size()) {
                ToastUtils.show(mContext, R.string.photo_pick_msg_amount_limit);
                return;
            }
            view.setSelected(true);
            resultList.add(info.path);
        }

        if (resultList != null && resultList.size() > 0) {
            tvRight.setText(String.format("(%d/%d)完成", resultList.size(), mPhotoPickParameterInfo.max_image));
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.INVISIBLE);
        }
        mPhotoPickAdapter.select(info);
    }

    //返回裁剪后的图片
    public void getForResultComplate(String path) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(path);
        Intent intent = new Intent();
        mPhotoPickParameterInfo.image_list = list;
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = HtApplication.getRefWatcher();
        refWatcher.watch(this);
    }*/
}
