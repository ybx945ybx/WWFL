package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.SampleFragment;
import com.haitao.fragment.SampleReportFragment;

import java.util.ArrayList;

/**
 * 我的试用
 */
public class MySampleActivity extends BaseActivity {
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
        Intent intent = new Intent(context, MySampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sample);
        TAG = "我的试用";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.sample_mine);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.viewpager);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
    }


    /**
     * 初始化数据
     */
    private void initData() {
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        tabs = new String[]{getString(R.string.sample_application_in),
                getString(R.string.sample_application_success),
                getString(R.string.sample_application_failed),
                getString(R.string.sample_report)};
        fragments = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(TransConstant.TYPE, i);
            if (i == (tabs.length - 1)) {
                // 试用报告
                fragments.add(new SampleReportFragment());
            } else {
                SampleFragment sampleFragment = new SampleFragment();
                sampleFragment.setArguments(bundle);
                fragments.add(sampleFragment);
            }
        }
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
        } else if (requestCode == TransConstant.REFRESH && resultCode == TransConstant.REFRESH) {
            if (fragments.get(1) instanceof SampleFragment) {
                SampleFragment sampleFragment = (SampleFragment) fragments.get(1);
                sampleFragment.initData();
            } else if (fragments.get(3) instanceof SampleReportFragment) {
                SampleReportFragment sampleReportFragment = (SampleReportFragment) fragments.get(3);
                sampleReportFragment.initData();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
