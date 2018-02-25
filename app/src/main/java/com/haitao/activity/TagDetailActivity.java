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
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.TagDiscountFragment;
import com.haitao.fragment.TagTopicFragment;
import com.haitao.utils.TraceUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 标签详情
 */
@MLinkRouter(keys = {"tagKey"})
public class TagDetailActivity extends BaseActivity {
    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private String[]                tabs;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    private String tagName, tagId;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String tagName, String tagId) {
        Intent intent = new Intent(context, TagDetailActivity.class);
        intent.putExtra(TransConstant.TITLE, tagName);
        intent.putExtra(TransConstant.ID, tagId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        TAG = "标签详情";
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.TITLE))
                tagName = intent.getStringExtra(TransConstant.TITLE);

            if (intent.hasExtra(TransConstant.ID)) {
                tagId = intent.getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {
                tagId = intent.getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "tagKey");
                kv.put(TraceUtils.Event_Kv_Value, tagId);
                YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

            }
        }
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(tagName);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.vp_order_list);
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
        tabs = new String[]{"优惠", "晒单", "帖子"};
        fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.VALUE, tagId);
        // 优惠
        TagDiscountFragment tagDiscountFragment = new TagDiscountFragment();
        tagDiscountFragment.setArguments(bundle);
        Bundle bundleShaidan = new Bundle();
        bundleShaidan.putString(TransConstant.VALUE, tagId);
        bundleShaidan.putString(TransConstant.TYPE, "2");
        bundleShaidan.putString(TransConstant.TITLE, tagName);
        // 晒单
        TagTopicFragment shaidanFragment = new TagTopicFragment();
        shaidanFragment.setArguments(bundleShaidan);
        Bundle bundleTopic = new Bundle();
        bundleTopic.putString(TransConstant.VALUE, tagId);
        bundleTopic.putString(TransConstant.TYPE, "1");
        bundleTopic.putString(TransConstant.TITLE, tagName);
        // 帖子
        TagTopicFragment topicFragment = new TagTopicFragment();
        topicFragment.setArguments(bundleTopic);
        fragments.add(tagDiscountFragment);
        fragments.add(shaidanFragment);
        fragments.add(topicFragment);

        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);
        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }
    }
}
