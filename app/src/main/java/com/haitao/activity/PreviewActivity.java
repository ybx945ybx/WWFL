package com.haitao.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.haitao.R;
import com.haitao.adapter.PhotoPickShowAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.photoView.MultiTouchViewPager;

import java.util.ArrayList;

/**
 * 图片预览
 * Created by pxl(彭小利) on 2015/9/24.
 */
public class PreviewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private   PhotoPickParameterObject mPhotoPickParameterInfo;
    protected MultiTouchViewPager      vpView;
    private   PhotoPickShowAdapter     mAdapter;
    private   ArrayList<String>        mImages;
    private boolean isEdit = true;
    private ImageButton btnDownload;
    private boolean isUpdate = false;
    //    ConfirmDialogUtils dialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "大图查看";
        Intent intent = getIntent();
        try {
            Bundle bundle = intent.getExtras();
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
            mImages = mPhotoPickParameterInfo.image_list;
            if (bundle.containsKey(TransConstant.TYPE)) {
                isEdit = false;
            }
        } catch (Exception e) {
        }

    }

    private void initView() {
        btnLeft = getView(R.id.btnLeft);
        tvTitle = getView(R.id.tvTitle);
        btnRight = getView(R.id.btnRight);
        btnRight.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        vpView = getView(R.id.vpView);
        btnDownload = getView(R.id.btnDownload);
        btnDownload.setVisibility(isEdit ? View.GONE : View.VISIBLE);
    }

    private void initEvent() {
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        vpView.setOnPageChangeListener(this);
        btnDownload.setOnClickListener(this);
    }

    private void initData() {
        mAdapter = new PhotoPickShowAdapter(mContext, mImages);
        vpView.setAdapter(mAdapter);
        vpView.setCurrentItem(mPhotoPickParameterInfo.position);
        tvTitle.setText((mPhotoPickParameterInfo.position + 1) + "/" + mImages.size());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft://返回
                selectComplate();
                finish();
                break;
            case R.id.btnRight://删除
                btnRight.setEnabled(false);
                isUpdate = true;
                if (mImages.size() <= 1) {
                    mImages.clear();
                    selectComplate();
                    finish();
                } else {
                    mImages.remove(mImages.get(vpView.getCurrentItem()));
                    if (1 == mImages.size())
                        mPhotoPickParameterInfo.position = 0;
                    tvTitle.setText((mPhotoPickParameterInfo.position + 1) + "/" + mImages.size());
                    mAdapter.setData(mImages);
                    vpView.setAdapter(mAdapter);
                    vpView.setCurrentItem(mPhotoPickParameterInfo.position);
                }
                btnRight.setEnabled(true);
                break;
            case R.id.btnDownload://下载
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.save_to_album)
                        .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            new DownAsyncTask().execute(mImages.get(vpView.getCurrentItem()));
                            dialog.dismiss();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        }).show();

                /*if (null == dialogUtils) {
                    dialogUtils = new ConfirmDialogUtils(mContext);
                }
                dialogUtils.show(getString(R.string.save_to_album));
                dialogUtils.setOnItemClickLitener(new ConfirmDialogUtils.OnItemClickLitener() {
                    @Override
                    public void onLeftClick() {
                        dialogUtils.dismiss();
                    }

                    @Override
                    public void onRightClick() {
                        dialogUtils.dismiss();
                        new DownAsyncTask().execute(mImages.get(vpView.getCurrentItem()));
                    }
                });*/
                break;
        }
    }

    class DownAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String picName = String.format("%d.jpg", System.currentTimeMillis());
            FileUtils.downloadPicAndSaveToDCIM(mContext, params[0], picName);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ToastUtils.show(mContext, "图片已保存到相册");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvTitle.setText((position + 1) + "/" + vpView.getAdapter().getCount());
        mPhotoPickParameterInfo.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            selectComplate();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void selectComplate() {
        if (!isUpdate) {
            return;
        }
        mPhotoPickParameterInfo.image_list = mImages;
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
