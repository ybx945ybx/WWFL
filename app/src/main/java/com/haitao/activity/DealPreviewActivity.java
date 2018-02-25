package com.haitao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.PhotoPickShowAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.photoView.MultiTouchViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haitao.R.id.vpView;


/**
 * 图片预览
 * Created by pxl(彭小利) on 2015/9/24.
 */
public class DealPreviewActivity extends BaseActivity {
    @BindView(vpView)             MultiTouchViewPager mVpView;       // 图片ViewPager
    @BindView(R.id.tvTitle)       TextView            mTvTitle;      // 标题
    @BindView(R.id.view_divider)  View                mViewDivider;  // 底部2个按钮间的分割线
    @BindView(R.id.tv_check_link) TextView            mTvCheckLink;  // 复制链接

    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private PhotoPickShowAdapter     mAdapter;
    private ArrayList<String>        mImages;
    private ArrayList<String>        mLinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_preview);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(true);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        initVars();
        initView();
    }

    private void initVars() {
        TAG = "大图查看";
        Intent intent = getIntent();
        try {
            Bundle bundle = intent.getExtras();
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(mPhotoPickParameterInfo.EXTRA_PARAMETER);
            mImages = mPhotoPickParameterInfo.image_list;
            mLinks = mPhotoPickParameterInfo.link_list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        // 切换链接显示状态
        switchLinkView(!TextUtils.isEmpty(mLinks.get(mPhotoPickParameterInfo.position)));
        mAdapter = new PhotoPickShowAdapter(mContext, mImages);
        mVpView.setAdapter(mAdapter);
        mVpView.setCurrentItem(mPhotoPickParameterInfo.position);
        // 标题索引
        mTvTitle.setText((mPhotoPickParameterInfo.position + 1) + "/" + mImages.size());
        mVpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 标题索引
                mTvTitle.setText((position + 1) + "/" + mVpView.getAdapter().getCount());
                mPhotoPickParameterInfo.position = position;
                // 切换链接显示状态
                switchLinkView(!TextUtils.isEmpty(mLinks.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 链接为空时，隐藏复制链接按钮，反之显示
     */
    private void switchLinkView(boolean visible) {
        mViewDivider.setVisibility(visible ? View.VISIBLE : View.GONE);
        mTvCheckLink.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 查看链接
     */
    @OnClick(R.id.tv_check_link)
    public void onClickCheckLink() {
        Intent intent = new Intent(TransConstant.CHANGE_BROADCAST);
        intent.putExtra(TransConstant.TYPE, 0x1022);
        intent.putExtra("link", mLinks.get(mVpView.getCurrentItem()));
        mContext.sendBroadcast(intent);
        finish();
    }

    /**
     * 保存图片
     */
    @OnClick(R.id.tv_save_pic)
    public void onSavePic() {
        savePicWrapper();
    }

    /**
     * 保存图片-wrapper(包含权限申请)
     */
    private void savePicWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            savePic();
        }
    }

    /**
     * 保存图片
     */
    private void savePic() {
        new DownAsyncTask().execute(mImages.get(mVpView.getCurrentItem()));
    }

    /**
     * 下载图片任务
     */
    private class DownAsyncTask extends AsyncTask<String, Void, String> {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意授权，则保存图片
                    savePicWrapper();
                } else {
                    ToastUtils.show(mContext, "请授予读写权限，以保存图片");
                }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
