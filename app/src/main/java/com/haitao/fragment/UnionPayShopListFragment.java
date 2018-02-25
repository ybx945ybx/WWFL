package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.common.Constant.TransConstant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a55 on 2017/12/4.
 */

public class UnionPayShopListFragment extends BaseFragment {

    public static UnionPayShopListFragment newInstance(String tabName) {
        UnionPayShopListFragment fragment = new UnionPayShopListFragment();
        Bundle               bundle   = new Bundle();
        bundle.putString(TransConstant.VALUE, tabName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTabName = getArguments().getString(KEY_TAB_NAME);
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_firstpage_tab, container, false);
//
//        initViews(view);
//        initEvents();
//        initData();

        return null;
    }
}
