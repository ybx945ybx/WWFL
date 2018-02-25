package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.adapter.BoardPagerAdapter;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.fragment.BoardFragment;
import com.haitao.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.ForumSectionModel;


/**
 * 全部版块
 */
public class BoardActivity extends BaseActivity {
    private ArrayList<ForumSectionModel> navList;
    private TabLayout                    tabLayout;
    private ViewPager                    pager;
    private BoardPagerAdapter            adapter;
    private ArrayList<BoardFragment> fragments = null;
    private ViewGroup         llProgress_common_progress;
    private ArrayList<String> mTabNames;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, BoardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "全部版块";
        mTabNames = new ArrayList<>();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.drawable.ic_search);
        tvTitle.setText("全部版块");
        tabLayout = getView(R.id.tab);
        pager = getView(R.id.pager);
        llProgress_common_progress = getView(R.id.llProgress_common_progress);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnRight.setOnClickListener(v -> BoardSearchActivity.launch(mContext));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        navList = new ArrayList<ForumSectionModel>();
        String cats = (String) SPUtils.get(mContext, SPConstant.SECTION_CATEGORY, "");
        if (!TextUtils.isEmpty(cats)) {
            llProgress_common_progress.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            List<ForumSectionModel> sections = JSON.parseArray(cats, ForumSectionModel.class);
            navList.addAll(sections);
            for (ForumSectionModel nav : navList) {
                mTabNames.add(nav.getSectionName());
            }
        } else {
            llProgress_common_progress.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            initSection();
        }
        fragments = new ArrayList<BoardFragment>();
        for (int i = 0; i < navList.size(); i++) {
            BoardFragment fragment = new BoardFragment();
            Bundle        bundle   = new Bundle();
            bundle.putSerializable(TransConstant.VALUE, navList.get(i).getSectionId());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter = new BoardPagerAdapter(getSupportFragmentManager(), fragments, navList);
        pager.setAdapter(adapter);
        // 设置tab
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < mTabNames.size(); i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(mTabNames.get(i));
        }
    }

    private void initSection() {
        ForumApi.getInstance().forumBoardsIndexGet("",
                response -> {
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData().getSections()) {
                            SPUtils.put(HtApplication.getInstance(), SPConstant.SECTION_CATEGORY, JSON.toJSONString(response.getData().getSections()));
                            initData();
                        }
                    }
                }, this::showErrorToast);
    }
}
