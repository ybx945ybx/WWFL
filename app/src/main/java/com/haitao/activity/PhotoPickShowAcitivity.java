package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.PhotoPickShowAdapter;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.photoView.MultiTouchViewPager;

import java.util.ArrayList;


/**
 * 相册中图片展示
 * Created by pxl(彭小利) on 2015/9/23.
 */
public class PhotoPickShowAcitivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private Context             mContext;
    private MultiTouchViewPager vpView;
    private ImageView           ivImage;
    private TextView            tvNum, btnComplete;
    private ArrayList<String>        mData;
    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private ArrayList<String>        mSelectedImages;
    private ArrayList<String>        images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick_show);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "相册中图片展示";
        Intent intent = getIntent();
        try {
            mPhotoPickParameterInfo = (PhotoPickParameterObject) intent.getSerializableExtra(mPhotoPickParameterInfo.EXTRA_PARAMETER);
            mSelectedImages = mPhotoPickParameterInfo.image_list;
            images = mPhotoPickParameterInfo.image_list;
            mData = mPhotoPickParameterInfo.image_data;
        } catch (Exception e) {
        }
    }

    private void initView() {
        vpView = findViewById(R.id.vpView);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        ivImage = findViewById(R.id.ivImage);
        tvNum = findViewById(R.id.tvNum);
        btnComplete = findViewById(R.id.btnComplete);
    }

    private void initEvent() {
        vpView.addOnPageChangeListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
    }

    private void initData() {
        vpView.setOffscreenPageLimit(1);
        updateNumText();
        vpView.setAdapter(new PhotoPickShowAdapter(mContext, mData));
        vpView.setCurrentItem(mPhotoPickParameterInfo.position);
        btnRight.setSelected(mSelectedImages.contains(mData.get(mPhotoPickParameterInfo.position)));//说明是选中的图片
    }

    private void updateNumText() {
        if (mSelectedImages.size() > 0) {
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText(mSelectedImages.size() + "");
        } else {
            tvNum.setVisibility(View.GONE);
        }
    }

    //

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btnRight.setSelected(mSelectedImages.contains(mData.get(position)));//说明是选中的图片
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft://返回
                selectComplate(0);
                finish();
                break;
            case R.id.btnRight:
                btnRight.setSelected(!btnRight.isSelected());
                selectImagesOptions(btnRight.isSelected());
                break;
            case R.id.btnComplete://完成
                selectComplate(1);
                finish();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            selectComplate(0);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //选择完成实现跳转
    private void selectComplate(int type) {
        if (mSelectedImages.size() <= 0 && type == 1) {
            mSelectedImages.add(mData.get(vpView.getCurrentItem()));
        }
        mPhotoPickParameterInfo.type = type;
        mPhotoPickParameterInfo.image_list = mSelectedImages;
        Intent intent = new Intent();
        intent.putExtra(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 图处操作
     */
    private void selectImagesOptions(boolean isSelected) {

        if (isSelected) {//选中
            if (mSelectedImages.size() == mPhotoPickParameterInfo.max_image) {
                ToastUtils.show(mContext, R.string.photo_pick_msg_amount_limit);
                btnRight.setSelected(false);
            } else {
                mSelectedImages.add(mData.get(vpView.getCurrentItem()));
            }

        } else {
            mSelectedImages.remove(mData.get(vpView.getCurrentItem()));
        }
        updateNumText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
