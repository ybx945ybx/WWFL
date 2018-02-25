package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.SearchActivity;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Enum.SearchType;

import java.util.ArrayList;

/**
 * 社区
 */
public class ForumFragment extends BaseFragment implements View.OnClickListener {
    private ViewGroup               layoutSearch;
    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    private String[] tabs = new String[]{"晒单", "热帖", "动态"};
    private ForumShaiDanFragment  mShaidanFragment;
    private ForumHotPostFragment  mHotPostFragment;
    private ForumMovementFragment mMovementFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "社区";
        View messageLayout = initView(inflater);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_forum, null);
        tabLayout = getView(view, R.id.tab);
        viewpager = getView(view, R.id.vp_order_list);
        layoutSearch = getView(view, R.id.layoutSearch);
        return view;
    }

    public void refreshData() {
        if (fragments != null && fragments.size() >= 3) {
            mShaidanFragment.refreshData();
            mHotPostFragment.refreshData();
            mMovementFragment.refreshData();
        }
    }

    public void initData() {
        fragments = new ArrayList<>();
        mShaidanFragment = new ForumShaiDanFragment();
        mHotPostFragment = new ForumHotPostFragment();
        mMovementFragment = new ForumMovementFragment();
        fragments.add(mShaidanFragment);
        fragments.add(mHotPostFragment);
        fragments.add(mMovementFragment);

        mPagerAdapter = new BasePagerAdapter(getChildFragmentManager(), fragments, tabs);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.item_tab);//给每一个tab设置view
            tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.INVISIBLE);//第一个tab被选中
            tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.VISIBLE);//第一个tab被选中
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);//第一个tab被选中
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(tabs[i]);//设置tab上的文字
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (null == tab || null == tab.getCustomView())
                    return;
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.VISIBLE);
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
                tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.setCurrentItem(1);
        viewpager.setOffscreenPageLimit(2);
    }


    private void initEvent() {
        layoutSearch.setOnClickListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutSearch:
                SearchActivity.launch(mContext, SearchType.ALL);
                break;
            default:
                break;
        }
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        if (fragments != null && fragments.size() > 0) {
            int position = tabLayout.getSelectedTabPosition();
            fragments.get(position).returnTop();
        }
    }
}
