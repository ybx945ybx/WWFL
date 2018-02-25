package com.haitao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.annotation.PostFragmentType;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.FavDiscountFragment;
import com.haitao.fragment.FavStoreFragment;
import com.haitao.fragment.TopicFragment;
import com.haitao.fragment.TransportFragment;
import com.haitao.inner.OnFavChangeListener;
import com.haitao.utils.doubleclick.DoubleClickUtils;
import com.haitao.utils.doubleclick.OnDoubleClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的收藏
 */
public class MyFavActivity extends BaseActivity implements View.OnClickListener, OnFavChangeListener, OnDoubleClickListener {
    @BindView(R.id.divider) View      mDivider;
    //    private ViewGroup layoutBottom;
    private                 ViewGroup layoutTop;

    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private String[]                tabs;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    //    private SearchDiscountFragment mFavDiscountFragment;
    private FavDiscountFragment mFavDiscountFragment;
    //    private SearchStoreFragment    mFavStoreFragment;
    private FavStoreFragment    mFavStoreFragment;
    private TopicFragment       mFavTopicFragment;
    //    private SectionFragment        mySectionFragment;
    private TransportFragment   mTransportFragment;

    private ChangReceiver mChangReceiver;

    @Override
    public void onRefresh() {

    }

    @Override
    public void OnSingleClick(View v) {

    }

    @Override
    public void OnDoubleClick(View v) {
        if (0 == viewpager.getCurrentItem()) {
            mFavDiscountFragment.returnTop();
        } else if (1 == viewpager.getCurrentItem()) {
            mFavStoreFragment.returnTop();
        } else if (2 == viewpager.getCurrentItem()) {
            mFavTopicFragment.returnTop();
        } else if (3 == viewpager.getCurrentItem()) {
            mTransportFragment.returnTop();
        }
    }

    private class ChangReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_DEAL_FAV:
                            if (null != mFavDiscountFragment)
                                mFavDiscountFragment.loadData();
                            break;
                        case TransConstant.BROAD_POST_FAV:
                            if (null != mFavTopicFragment)
                                mFavTopicFragment.loadData();
                            break;
                        case TransConstant.BROAD_STORE_FAV:
                            //                            if (null != mySectionFragment)
                            //                                mySectionFragment.loadSearchStoreList();
                            if (null != mTransportFragment)
                                mTransportFragment.loadData();
                            //                            break;
                            //                        case TransConstant.BROAD_STORE_FAV:
                            if (null != mFavStoreFragment)
                                mFavStoreFragment.initData();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, MyFavActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "我的收藏";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        initError();
        tvTitle.setText(R.string.my_fav);
        layoutTop = getView(R.id.layoutTop);
        //        layoutBottom = getView(layoutBottom);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.vp_order_list);
        mDivider.setVisibility(View.GONE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //        tvRight.setOnClickListener(this);
        //        layoutBottom.setOnClickListener(this);
        DoubleClickUtils.registerDoubleClickListener(layoutTop, this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mChangReceiver = new ChangReceiver();

        tabs = new String[]{getString(R.string.fav_discount), getString(R.string.fav_post), getString(R.string.store), getString(R.string.fav_transfer)};
        // 优惠
        mFavDiscountFragment = new FavDiscountFragment();
        //        Bundle bundle = new Bundle();
        //        bundle.putSerializable(TransConstant.TYPE, SearchDiscountFragment.MY_FAV);
        //        mFavDiscountFragment.setArguments(bundle);
        //        mFavDiscountFragment.setOnCallbackLitener(this);
        // 帖子
        mFavTopicFragment = new TopicFragment();
        Bundle bundlePost = new Bundle();
        bundlePost.putSerializable(TransConstant.TYPE, PostFragmentType.MY_FAV);
        mFavTopicFragment.setArguments(bundlePost);
        mFavTopicFragment.setOnFavChangeListener(this);
        // 商家
        mFavStoreFragment = new FavStoreFragment();
        //        Bundle bundleStore = new Bundle();
        //        bundleStore.putSerializable(TransConstant.TYPE, SearchStoreFragment.MY_FAV);
        //        mFavStoreFragment.setArguments(bundleStore);
        //        mFavStoreFragment.setOnCallbackLitener(this);

        mTransportFragment = new TransportFragment();
        //        mySectionFragment = new SectionFragment();
        mTransportFragment.setOnFavChangeListener(this);

        fragments = new ArrayList<>();
        fragments.add(mFavDiscountFragment);
        fragments.add(mFavTopicFragment);
        fragments.add(mFavStoreFragment);
        //        fragments.add(mySectionFragment);
        fragments.add(mTransportFragment);
        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        /*viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((0 == viewpager.getCurrentItem() && mFavDiscountFragment.getCount() > 0)
                        || (1 == viewpager.getCurrentItem() && mFavStoreFragment.getCount() > 0)
                        || (2 == viewpager.getCurrentItem() && mFavTopicFragment.getCount() > 0)
                        || (3 == viewpager.getCurrentItem() && mySectionFragment.getCount() > 0)) {
                    //                    tvRight.setVisibility(View.VISIBLE);
                    //                    layoutBottom.setVisibility(View.GONE);
                    if ((0 == viewpager.getCurrentItem() && mFavDiscountFragment.isEdit)
                            || (1 == viewpager.getCurrentItem() && mFavStoreFragment.isEdit)
                            || (2 == viewpager.getCurrentItem() && mFavTopicFragment.isEdit)
                            || (3 == viewpager.getCurrentItem() && mySectionFragment.isEdit)) {
                        //                        layoutBottom.setVisibility(View.VISIBLE);
                        //                        tvRight.setText(R.string.cancel);
                    } else {
                        //                        layoutBottom.setVisibility(View.GONE);
                        //                        tvRight.setText(R.string.my_post_edit);
                    }
                } else {
                    //                    tvRight.setVisibility(View.GONE);
                    //                    layoutBottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        viewpager.setOffscreenPageLimit(4);
        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }
        //        tvRight.setText(R.string.my_post_edit);
        //        tvRight.setVisibility(View.VISIBLE);
        //        layoutBottom.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                initData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.tvRight:
                if (0 == viewpager.getCurrentItem()) {
                    mFavDiscountFragment.setEdit(!mFavDiscountFragment.isEdit);
                    layoutBottom.setVisibility(mFavDiscountFragment.isEdit ? View.VISIBLE : View.GONE);
                    tvRight.setText(mFavDiscountFragment.isEdit ? R.string.cancel : R.string.my_post_edit);
                } else if (1 == viewpager.getCurrentItem()) {
                    mFavStoreFragment.setEdit(!mFavStoreFragment.isEdit);
                    layoutBottom.setVisibility(mFavStoreFragment.isEdit ? View.VISIBLE : View.GONE);
                    tvRight.setText(mFavStoreFragment.isEdit ? R.string.cancel : R.string.my_post_edit);
                } else if (2 == viewpager.getCurrentItem()) {
                    mFavTopicFragment.setEdit(!mFavTopicFragment.isEdit);
                    layoutBottom.setVisibility(mFavTopicFragment.isEdit ? View.VISIBLE : View.GONE);
                    tvRight.setText(mFavTopicFragment.isEdit ? R.string.cancel : R.string.my_post_edit);
                } else if (3 == viewpager.getCurrentItem()) {
                    mySectionFragment.setEdit(!mySectionFragment.isEdit);
                    layoutBottom.setVisibility(mySectionFragment.isEdit ? View.VISIBLE : View.GONE);
                    tvRight.setText(mySectionFragment.isEdit ? R.string.cancel : R.string.my_post_edit);
                }
                break;*/
           /* case R.id.layoutBottom:
                if (0 == viewpager.getCurrentItem()) {
                    mFavDiscountFragment.del();
                } else if (1 == viewpager.getCurrentItem()) {
                    mFavStoreFragment.del();
                } else if (2 == viewpager.getCurrentItem()) {
                    mFavTopicFragment.del();
                } else if (3 == viewpager.getCurrentItem()) {
                    mySectionFragment.del();
                }

                break;*/
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != mChangReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(TransConstant.CHANGE_BROADCAST);
            mContext.registerReceiver(mChangReceiver, filter);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mChangReceiver) {
            mContext.unregisterReceiver(mChangReceiver);
        }
    }


}
