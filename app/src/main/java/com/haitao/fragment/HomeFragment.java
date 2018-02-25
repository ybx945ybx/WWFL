package com.haitao.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.activity.SearchActivity;
import com.haitao.activity.WebActivity;
import com.haitao.adapter.DiscountPagerAdapter;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.Enum.SearchType;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.framework.utils.DeviceUtil;
import com.haitao.utils.CalendarUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.calendar.CustomDate;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.swagger.client.model.DealCategoryModel;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TabLayout                    tabLayout;
    private CustomImageView              mImgTabActivity;
    private ViewPager                    viewpager;
    private ArrayList<DealCategoryModel> tabs;
    private ArrayList<BaseFragment>      fragments;
    private DiscountPagerAdapter         mPagerAdapter;
    private ViewGroup                    layoutProgress;

    private ViewGroup       layoutSearch;
    private CustomImageView mImgActivityFab;
    private String          mTabActivityImg;
    private boolean         hasActivity; // 是否有活动
    private int             mCurrentItem;

    /**
     * 设置活动Tab图标
     */
    public void setTabActivity(String img, String url) {
        mImgTabActivity.setVisibility(View.VISIBLE);
        ImageLoaderUtils.showOnlineImage(img, mImgTabActivity);
        mImgTabActivity.setOnClickListener(v -> WebActivity.launch(mContext, "", url, true));
        //        mImgTabActivity.requestLayout();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initVars(savedInstanceState);
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars(Bundle savedInstanceState) {
        TAG = "优惠首页";
        EventBus.getDefault().register(this);
        if (savedInstanceState != null) {
            mCurrentItem = savedInstanceState.getInt("TAB_INDEX", 0);
        }
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initError(view);
        tabLayout = getView(view, R.id.tab);
        mImgTabActivity = getView(view, R.id.img_tab_activity);
        viewpager = getView(view, R.id.vp_order_list);
        layoutSearch = getView(view, R.id.layoutSearch);
        layoutProgress = getView(view, R.id.llProgress_common_progress);
        mImgActivityFab = getView(view, R.id.img_event);
        layoutProgress.setVisibility(View.VISIBLE);
        btnRefresh.setOnClickListener(v -> initData());
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            //            ImageLoaderUtils.showOnlineImage(HtApplication.mActivityFabImg, mFabActivity);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
        return view;
    }

    public void loadData() {
        tabs = new ArrayList<>();
        String tagStr = (String) SPUtils.get(mContext, SPConstant.DEAL_CATEGORY_NEW, "");
        Logger.d("tagStr:" + tagStr);
        if (TextUtils.isEmpty(tagStr)) {
            initData();
            return;
        } else {
            layoutProgress.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            tabs.clear();
            tabs.addAll(JSON.parseArray(tagStr, DealCategoryModel.class));
        }
        if (TextUtils.equals(tabs.get(0).getCid(), "0")) {
            tabs.remove(0);
        }

        fragments = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString(TransConstant.ID, tabs.get(i).getCid());
            DiscountFragment discountFragment = new DiscountFragment();
            discountFragment.setArguments(bundle);
            fragments.add(discountFragment);
        }
        mPagerAdapter = new DiscountPagerAdapter(getChildFragmentManager(), fragments, tabs);
        viewpager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager.setCurrentItem(mCurrentItem);

        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.item_tab);//给每一个tab设置view
            tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.INVISIBLE);//第一个tab被选中
            tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
            if (i == mCurrentItem) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.VISIBLE);//第一个tab被选中
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);//第一个tab被选中
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(tabs.get(i).getWidget().getTitle());//设置tab上的文字
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                    tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.VISIBLE);
                }
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
                    tab.getCustomView().findViewById(R.id.viewIndicator).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initData() {
        ForumApi.getInstance().dealCategoriesGet(
                response -> {
                    if ("0".equals(response.getCode())) {
                        ll_common_error.setVisibility(View.GONE);
                        layoutProgress.setVisibility(View.GONE);
                        if (null != response.getData() && response.getData().size() > 0) {
                            SPUtils.put(mContext, SPConstant.DEAL_CATEGORY_NEW, JSON.toJSONString(response.getData()));
                            loadData();
                        }
                    }
                }, error -> {
                    ll_common_error.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    setErrorType(1);
                    showErrorToast(error);
                });
    }

    /**
     * 提示进行评论
     */
    private void review() {
        String     reviewTips  = (String) SPUtils.get(mContext, SPConstant.REVIEW_TIPS, "");
        CustomDate today       = new CustomDate();
        String     lastDay     = CalendarUtils.formatCalendar(CalendarUtils.getBeforeDay(CalendarUtils.setCalendar(today.year, today.month, today.day)));
        String     llastDay    = CalendarUtils.formatCalendar(CalendarUtils.getBeforeDay(CalendarUtils.getBeforeDay(CalendarUtils.setCalendar(today.year, today.month, today.day))));
        String     versionName = DeviceUtil.getSoftWareVersion(mContext);
        if (reviewTips.contains(versionName)) {
            if (reviewTips.contains(lastDay)) {
                SPUtils.put(mContext, SPConstant.REVIEW_TIPS, versionName + "," + llastDay);

                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.new_version_please_give_rate)
                        .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            try {
                                Uri    uri           = Uri.parse("market://details?id=" + mContext.getPackageName());
                                Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(commentIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton(R.string.refuse, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        } else {
            SPUtils.put(mContext, SPConstant.REVIEW_TIPS, versionName + "," + today.toString());
        }
    }


    private void initEvent() {
        layoutSearch.setOnClickListener(this);
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
            fragments.get(tabLayout.getSelectedTabPosition()).returnTop();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("TAB_INDEX", viewpager.getCurrentItem());
    }

    /**
     * 全局活动入口设置事件
     *
     * @param event 事件
     */
    @Subscribe
    public void onActivityFabImgSetEvent(ActivityFabImgSetEvent event) {
        mImgActivityFab.setVisibility(View.VISIBLE);
        mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
        ImageLoaderUtils.showOnlineImage(event.img, mImgActivityFab);
    }
}
