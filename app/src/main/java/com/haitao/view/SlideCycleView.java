package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.haitao.R;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ImageLoaderUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.SlidePicModel;

/**
 * 广告图片自动轮播控件</br>
 */
public class SlideCycleView extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ViewPager mVpAd = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    /**
     * 图片轮播指示器控件
     */
    private ViewGroup         mGroup;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    private ImageView mImgAdTag; // 广告标签


    /**
     * 图片滚动当前图片下标
     */

    private boolean isStop;


    /**
     * @param context mContext
     */
    public SlideCycleView(Context context) {
        super(context);
    }

    /**
     * @param context mContext
     * @param attrs
     */
    @SuppressLint("Recycle")
    public SlideCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
        mVpAd = findViewById(R.id.adv_pager);
        mImgAdTag = findViewById(R.id.img_ad_tag);
        // 滚动图片右下指示器视
        mGroup = findViewById(R.id.viewGroup);
    }

    /**
     * 触摸停止计时器，抬起启动计时器
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 开始图片滚动
            startImageTimerTask();
        } else {
            // 停止图片滚动
            stopImageTimerTask();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 装填图片数据
     *
     * @param imageUrlList
     * @param imageCycleViewListener
     */
    public void setImageResources(List<SlidePicModel> imageUrlList, ImageCycleViewListener imageCycleViewListener) {
        // 清除
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = DensityUtil.dip2px(mContext, 2);
            params.rightMargin = DensityUtil.dip2px(mContext, 2);
            mImageView.setScaleType(ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(params);

            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.ic_banner_focus);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.ic_banner_default);
            }
            mGroup.addView(mImageViews[i]);
        }

        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageCycleViewListener);
        mVpAd.setAdapter(mAdvAdapter);
        mVpAd.addOnPageChangeListener(new GuidePageChangeListener(imageUrlList));

        /*mVpAd.setCurrentItem(Integer.MAX_VALUE/2);*/
        startImageTimerTask();
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mVpAd.setCurrentItem(mVpAd.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mHandler.postDelayed(mImageTimerTask, 3000);
                }

            }
        }
    };

    /**
     * 轮播图片监听
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {
        private List<SlidePicModel> adList;

        public GuidePageChangeListener(List<SlidePicModel> imageUrlList) {
            adList = imageUrlList;
            // 广告角标 针对第一个初始化的情况
            SlidePicModel item = adList.get(0);
            mImgAdTag.setVisibility(TextUtils.equals(item.getIsCommercial(), "1") ? VISIBLE : GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            // 设置当前显示的图片
            index = index % mImageViews.length;
            // 设置图片滚动指示器背
            mImageViews[index].setBackgroundResource(R.drawable.ic_banner_focus);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.ic_banner_default);
                }
            }
            SlidePicModel item = adList.get(index);
            mImgAdTag.setVisibility(TextUtils.equals(item.getIsCommercial(), "1") ? VISIBLE : GONE);
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private List<SlidePicModel> mAdList = new ArrayList<SlidePicModel>();

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, List<SlidePicModel> adList, ImageCycleViewListener imageCycleViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            SlidePicModel imageObject = mAdList.get(position % mAdList.size());
//            Logger.d("imageUrl = " + imageObject.getPic());
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                //test
                imageView.setScaleType(ScaleType.CENTER_CROP);
                // 设置图片点击监听
                imageView.setOnClickListener(v -> mImageCycleViewListener.onImageClick(position % mAdList.size(), v));
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            imageView.setTag(imageObject);
            container.addView(imageView);
            ImageLoaderUtils.showOnlineImage(imageObject.getPic(), imageView, R.mipmap.ic_default_750);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            mVpAd.removeView(view);
            mImageViewCacheList.add(view);
        }
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        public void onImageClick(int position, View imageView);
    }

}
