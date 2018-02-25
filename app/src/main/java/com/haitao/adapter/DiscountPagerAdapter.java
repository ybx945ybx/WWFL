package com.haitao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haitao.fragment.BaseFragment;

import java.util.ArrayList;

import io.swagger.client.model.DealCategoryModel;

/**
 * 优惠 - 分类Adapter
 * Created by Administrator on 2016/7/5.
 */
public class DiscountPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment>      mFragmentList;
    private ArrayList<DealCategoryModel> tabs;


    public DiscountPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragmentList, ArrayList<DealCategoryModel> tabs) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.tabs = tabs;
    }

    public void setTabs(ArrayList<DealCategoryModel> tabs) {
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
        //        return tabs.get(position).getWidget().getTitle();
        return "";
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
