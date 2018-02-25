package com.haitao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import com.haitao.fragment.MyCommentFragment;
import com.haitao.fragment.TopicFragment;
import com.haitao.utils.doubleclick.DoubleClickUtils;
import com.haitao.utils.doubleclick.OnDoubleClickListener;

import java.util.ArrayList;

/**
 * 我的贴子
 */
public class MyPostActivity extends BaseActivity implements OnDoubleClickListener {
    private ViewGroup               layoutTop;
    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private String[]                tabs;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, MyPostActivity.class);
        context.startActivity(intent);
    }

    private ChangReceiver mChangReceiver;

    @Override
    public void OnSingleClick(View v) {

    }

    @Override
    public void OnDoubleClick(View v) {
        if (0 == viewpager.getCurrentItem()) {
            ((TopicFragment) fragments.get(0)).returnTop();
        } else {
            ((MyCommentFragment) fragments.get(1)).returnTop();
        }
    }

    private class ChangReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_POST_FAV:
                            if (null != fragments && fragments.size() >= 3 && fragments.get(2) instanceof TopicFragment) {
                                TopicFragment myFavPost = (TopicFragment) fragments.get(2);
                                myFavPost.loadData();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        TAG = "我的帖子";
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.my_post);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.vp_order_list);
        layoutTop = getView(R.id.layoutTop);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        DoubleClickUtils.registerDoubleClickListener(layoutTop, this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mChangReceiver = new ChangReceiver();
        tabs = new String[]{getString(R.string.my_post_send), getString(R.string.my_post_comment)};

        TopicFragment myTopicFragment = new TopicFragment();
        Bundle        bundle          = new Bundle();
        bundle.putSerializable(TransConstant.TYPE, PostFragmentType.MY_POST);
        myTopicFragment.setArguments(bundle);

        fragments = new ArrayList<>();
        fragments.add(myTopicFragment);
        fragments.add(new MyCommentFragment());
        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }

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
