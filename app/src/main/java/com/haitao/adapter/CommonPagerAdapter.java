package com.haitao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 通用 PagerAdapter
 * Created by Administrator on 2016/7/5.
 */
public class CommonPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String>   mTabNames;


    public CommonPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, ArrayList<String> tabNames) {
        super(fm);
        this.mFragmentList = fragmentList;
        mTabNames = tabNames;
    }

    public void setTabs(ArrayList<String> tabs) {
        mTabNames = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
