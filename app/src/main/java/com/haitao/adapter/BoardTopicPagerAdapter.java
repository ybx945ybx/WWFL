package com.haitao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haitao.fragment.BoardTopicFragment;

import java.util.ArrayList;

import io.swagger.client.model.ForumSubBoardModel;

/**
 * Created by Administrator on 2016/7/5.
 */
public class BoardTopicPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BoardTopicFragment> mFragmentList;
    private ArrayList<ForumSubBoardModel> tabs;


    public BoardTopicPagerAdapter(FragmentManager fm, ArrayList<BoardTopicFragment> fragmentList, ArrayList<ForumSubBoardModel> tabs) {
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
        return tabs.get(position).getSubBoardName();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
