package com.haitao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haitao.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/5.
 */
public class BasePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> mFragmentList;
    private String[]                tabs;


    public BasePagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragmentList, String[] tabs) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.tabs = tabs;
    }


    public void setTabs(String[] tabs) {
        this.tabs = tabs;
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
        return tabs[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
