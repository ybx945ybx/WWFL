package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.SampleFragment;
import com.haitao.fragment.SampleReportFragment;

import java.util.ArrayList;


/**
 * 海淘试用
 */
public class SampleActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private String[]                tabs;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    /**
     * 跳转到当前页
     *
     * @param context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, SampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sample);
        TAG = "免费试用";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.sample_title);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.viewpager);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.sample_mine);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvRight.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        tabs = new String[]{mContext.getResources().getString(R.string.sample_product),
                mContext.getResources().getString(R.string.sample_selected_report)
        };
        fragments = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(TransConstant.TYPE, i);
            bundle.putString(TransConstant.KEY, "list");
            if (i == (tabs.length - 1)) {
                SampleReportFragment reportFragment = new SampleReportFragment();
                bundle.remove(TransConstant.TYPE);
                reportFragment.setArguments(bundle);
                fragments.add(reportFragment);
            } else {
                SampleFragment sampleFragment = new SampleFragment();
                sampleFragment.setArguments(bundle);
                fragments.add(sampleFragment);
            }
        }
        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                MySampleActivity.launch(mContext);
                break;
        }
    }


}
