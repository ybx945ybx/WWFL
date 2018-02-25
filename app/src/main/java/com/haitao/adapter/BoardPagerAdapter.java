package com.haitao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haitao.fragment.BoardFragment;

import java.util.ArrayList;

import io.swagger.client.model.ForumSectionModel;

/**
 * Created by Administrator on 2016/7/5.
 */
public class BoardPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BoardFragment> mFragmentList;
    private ArrayList<ForumSectionModel> tabs;


    public BoardPagerAdapter(FragmentManager fm, ArrayList<BoardFragment> fragmentList, ArrayList<ForumSectionModel> tabs) {
        super(fm);
        this.mFragmentList = fragmentList;
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
        return tabs.get(position).getSectionName();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
