package com.haitao.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.activity.MainActivity;
import com.haitao.adapter.DealAdapter;
import com.haitao.adapter.SpecialRecommendAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.utils.DeviceUtil;
import com.haitao.model.RefreshSwitchObject;
import com.haitao.utils.AdDialogUtils;
import com.haitao.utils.CalendarUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.TopicLink;
import com.haitao.utils.TraceUtils;
import com.haitao.utils.calendar.CustomDate;
import com.haitao.view.HomeTabView;
import com.haitao.view.HtAdView;
import com.haitao.view.SlideCycleView;
import com.haitao.view.TopDealView;
import com.haitao.view.refresh.XListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.AppWidgetStyleModel;
import io.swagger.client.model.DealIndexModelData;
import io.swagger.client.model.DealModel;
import io.swagger.client.model.DealSlidePicBaseModel;
import io.swagger.client.model.DealsListModelData;
import io.swagger.client.model.LinkWidgetModel;
import io.swagger.client.model.SlidePicModel;
import tom.ybxtracelibrary.YbxTrace;
import zhy.com.highlight.HighLight;

import static com.haitao.R.id.lvList;

/**
 * 首页
 */
public class DiscountFragment extends BaseFragment {
    private Context                  mContext;
    private MainActivity             mActivity;
    private XListView                mLvDiscount;
    private ViewGroup                layoutProgress;
    private ViewGroup                layoutTop;
    //焦点图
    private SlideCycleView           layoutCircle;
    private ArrayList<SlidePicModel> bannerList;

    //快速入口
    private LinearLayout mLlIconContainer, mLlIconContainer2;
    private HomeTabView[] mTabEntries;

    //模栏广告
    //    private FullListView lvAd;
    //    private ArrayList<SlidePicModel> adList;
    //    private AdAdapter                adAdapter;

    //专题推荐
    private ViewGroup                layoutSpecial;
    private GridView                 gvSpecial;
    private ArrayList<SlidePicModel> specialList;
    private SpecialRecommendAdapter  specialRecommendAdapter;

    //人气排行
    private ViewGroup mLlTopDeal;

    // 热门优惠
    private TopDealView          mTopDealView;
    private ArrayList<DealModel> mTopDealList;

    // 最新优惠
    private ArrayList<DealSlidePicBaseModel> latestdiscountList;
    private DealAdapter                      latestDiscountAdapter;

    // 弹窗广告
    private AdDialogUtils adDialogUtils;

    private int    page    = 1;
    private String lastIds = "";

    private String categoryId = "";
    private ViewGroup     mHeaderView;
    private HighLight     mHighLight;
    private View          mContainerView;
    private RecyclerView  mRvEntry;
    private HtAdView      mHtavAd;   // 广告
    private SlidePicModel mCrossBar;
    //    private GridView  mGvShortcuts; // 快速入口列表

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initVars();
        return initView(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    private void initVars() {
        mContext = getActivity();
        mActivity = (MainActivity) getActivity();
        TAG = "优惠首页";
        if (null != getArguments() && getArguments().containsKey(TransConstant.ID)) {
            categoryId = getArguments().getString(TransConstant.ID);
        }
        EventBus.getDefault().register(this);
    }

    private View initView(LayoutInflater inflater) {
        mContainerView = inflater.inflate(R.layout.fragment_discount, null);
        initError(mContainerView);
        mLvDiscount = getView(mContainerView, lvList);
        mLvDiscount.setVisibility(View.GONE);
        layoutProgress = getView(mContainerView, R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        if (isDiscountHome()) {
            mHeaderView = (ViewGroup) View.inflate(mContext, R.layout.layout_home, null);
            // 焦点图
            layoutCircle = getView(mHeaderView, R.id.layoutCircle);
            int                       screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
            LinearLayout.LayoutParams lp          = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth / (2f / 1f)));
            layoutCircle.setLayoutParams(lp);

            // 快速入口
            mLlIconContainer = getView(mHeaderView, R.id.ll_icon_container);
            mLlIconContainer2 = getView(mHeaderView, R.id.ll_icon_container2);
            HomeTabView tabEntryOne   = getView(mHeaderView, R.id.tab_entry_one);
            HomeTabView tabEntryTwo   = getView(mHeaderView, R.id.tab_entry_two);
            HomeTabView tabEntryThree = getView(mHeaderView, R.id.tab_entry_three);
            HomeTabView tabEntryFour  = getView(mHeaderView, R.id.tab_entry_four);
            HomeTabView tabEntryFive  = getView(mHeaderView, R.id.tab_entry_five);
            HomeTabView tabEntrySix   = getView(mHeaderView, R.id.tab_entry_six);
            HomeTabView tabEntrySeven = getView(mHeaderView, R.id.tab_entry_seven);
            HomeTabView tabEntryEight = getView(mHeaderView, R.id.tab_entry_eight);
            mTabEntries = new HomeTabView[]{tabEntryOne, tabEntryTwo, tabEntryThree, tabEntryFour,
                    tabEntryFive, tabEntrySix, tabEntrySeven, tabEntryEight};
            mLlTopDeal = getView(mHeaderView, R.id.ll_top_deal);
            //专题推荐
            layoutSpecial = getView(mHeaderView, R.id.layoutSpecial);
            gvSpecial = getView(mHeaderView, R.id.gvSpecial);
            gvSpecial.setOnItemClickListener((adapterView, view1, i, l) -> TopicLink.jump(mContext, specialList.get(i), TopicLink.SOURCE_TYPE.SPECIAL));
            // 热门优惠
            mTopDealView = new TopDealView(mContext, categoryId);
            mLlTopDeal.addView(mTopDealView);
            // 横栏广告
            mHtavAd = getView(mHeaderView, R.id.htav_ad);
            mHtavAd.setOnClickListener(v -> {
                if (mCrossBar != null) {
                    // 页面埋点
                    YbxTrace.getInstance().event(mActivity, mActivity.pref, mActivity.prefh, mActivity.purl, mActivity.purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Deal_Ads + categoryId);
                    // 页面跳转
                    TopicLink.jump(mContext, mCrossBar, TopicLink.SOURCE_TYPE.CROSS);
                }
            });
            // 优惠列表添加头
            mLvDiscount.addHeaderView(mHeaderView);
        }
        mLvDiscount.setPullRefreshEnable(true);
        mLvDiscount.setPullLoadEnable(false);
        mLvDiscount.setAutoLoadEnable(true);
        return mContainerView;
    }

    /**
     * @return 是否是优惠主页（最新）
     */
    private boolean isDiscountHome() {
        return "10002".equals(categoryId);
    }

    public void initData() {
        showLoadingView();
        //最新优惠
        latestdiscountList = new ArrayList<>();
        latestDiscountAdapter = new DealAdapter(mContext, latestdiscountList);

        if (isDiscountHome()) {
            //广告
            bannerList = new ArrayList<>();

            // 热门优惠
            mTopDealList = new ArrayList<>();
            mTopDealView.mTvTitle.setTitle(getString(R.string.main_product_title));
            mTopDealView.mTvTitle.setIcon(R.mipmap.ic_title_hot);
            mTopDealView.setData(mTopDealList);

            //专题推荐
            specialList = new ArrayList<>();
            specialRecommendAdapter = new SpecialRecommendAdapter(mContext, specialList);
            gvSpecial.setAdapter(specialRecommendAdapter);

            // 优惠列表
            mLvDiscount.setAdapter(latestDiscountAdapter);

            loadData();
        } else {
            mLvDiscount.setAdapter(latestDiscountAdapter);
            loadDeal();
        }
    }

    private void showLoadingView() {
        layoutProgress.setVisibility(View.VISIBLE);
        ll_common_error.setVisibility(View.GONE);
    }

    /**
     * 优惠数据
     */
    private void loadDeal() {
        if (1 == page)
            lastIds = "";
        ForumApi.getInstance().dealCategoryCategoryIdListGet(categoryId, lastIds.replaceFirst(",", ""), String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mLvDiscount == null)
                        return;
                    mLvDiscount.stopRefresh();
                    mLvDiscount.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                    mLvDiscount.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            latestdiscountList.clear();
                        if (null != response.getData()) {
                            List<DealModel> rows = response.getData().getRows();
                            if (null != rows && rows.size() > 0) {
                                if (1 == page) {
                                    SPUtils.put(mContext, "discount" + categoryId, JSON.toJSONString(response.getData()));
                                }
                                lastIds = "";
                                for (DealModel dealModel : rows) {
                                    lastIds += ",";
                                    lastIds += dealModel.getDealId();
                                }
                                latestdiscountList.addAll(rows);
                            }
                            // 每页最后添加一条广告
                            List<SlidePicModel> crossBarPics = response.getData().getCrossBarPics();
                            if (crossBarPics != null && crossBarPics.size() > 0) {
                                latestdiscountList.add(crossBarPics.get(0));
                            }
                            mLvDiscount.setPullLoadEnable("1".equals(response.getData().getHasMore()) && latestdiscountList.size() > 5);
                        }
                        latestDiscountAdapter.notifyDataSetChanged();
                    }
                    if (latestdiscountList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    if (mLvDiscount == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    mLvDiscount.stopRefresh();
                    mLvDiscount.stopLoadMore();
                    String localDiscount = (String) SPUtils.get(mContext, "discount" + categoryId, "");
                    if (!"".equals(localDiscount) && latestdiscountList.isEmpty()) {
                        DealsListModelData data = JSON.parseObject(localDiscount, DealsListModelData.class);
                        if (null != data) {
                            mLvDiscount.setVisibility(View.VISIBLE);
                            latestdiscountList.addAll(data.getRows());
                            latestDiscountAdapter.notifyDataSetChanged();
                        }

                    } else {
                        if (latestdiscountList.isEmpty()) {
                            ll_common_error.setVisibility(View.VISIBLE);
                            setErrorType(1);
                        } else {
                            ll_common_error.setVisibility(View.GONE);
                        }
                    }

                });
    }

    /**
     * 首页数据
     */
    private void loadData() {
        ForumApi.getInstance().dealIndexGet(String.valueOf(PageConstant.pageSize), "6", "20",
                response -> {
                    if (mLvDiscount == null)
                        return;
                    mLvDiscount.stopLoadMore();
                    mLvDiscount.stopRefresh();
                    layoutProgress.setVisibility(View.GONE);
                    mLvDiscount.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        DealIndexModelData data = response.getData();
                        if (null != data) {
                            if (1 == page) {
                                SPUtils.put(mContext, "discount" + categoryId, JSON.toJSONString(data));
                            }
                            // banner
                            if (null != data.getSlidePics() && data.getSlidePics().size() > 0) {
                                layoutCircle.setVisibility(View.VISIBLE);
                                bannerList.clear();
                                bannerList.addAll(data.getSlidePics());
                                layoutCircle.setImageResources(bannerList, mAdCycleViewListener);
                            } else {
                                layoutCircle.setVisibility(View.GONE);
                            }
                            // 4个Icon
                            List<LinkWidgetModel> entries = data.getEntries();
                            /*if (entries != null && mTabEntries != null && mTabEntries.length > 0) {
                                mGvShortcuts.setColumnWidth(ScreenUtils.getScreenWidth(mContext) / 4); // 设置列表项宽
                                mGvShortcuts.setHorizontalSpacing(0); // 设置列表项水平间距
                                mGvShortcuts.setStretchMode(GridView.NO_STRETCH);
                                mGvShortcuts.setNumColumns(entries.size());
                                mShortcutsAdapter = new HomeShortcutsAdapter(mContext, entries);
                            }*/
                            if (entries != null && entries.size() > 0) {
                                mLlIconContainer.setVisibility(View.VISIBLE);
                                mLlIconContainer2.setVisibility(entries.size() > 4 ? View.VISIBLE : View.GONE);
                                int i;
                                for (i = 0; i < entries.size(); i++) {
                                    AppWidgetStyleModel style = mActivity.getStyle(entries.get(i).getStyleId());
                                    mTabEntries[i].setTabName(entries.get(i).getTitle());
                                    if (style != null) {
                                        mTabEntries[i].setTabImg(style.getImageStyle().getNormalImage());
                                    }
                                    // 设置跳转
                                    SlidePicModel jumpData = new SlidePicModel();
                                    jumpData.setType(entries.get(i).getLinkType());
                                    jumpData.setLinkData(entries.get(i).getLinkData());
                                    int position = i;
                                    mTabEntries[i].setOnClickListener(v -> {
                                        // 页面埋点
                                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Deal_Icon + categoryId + "-" + position);

                                        // 跳转
                                        TopicLink.jump(mContext, jumpData, "");
                                    });
                                }
                                if (i < 8) {
                                    for (; i < 3; i++) {
                                        mTabEntries[i].setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                mLlIconContainer.setVisibility(View.GONE);
                                mLlIconContainer2.setVisibility(View.GONE);
                            }
                            // 横栏广告
                            if (null != data.getCrossBarPics() && data.getCrossBarPics().size() > 0) {
                                mCrossBar = data.getCrossBarPics().get(0);
                                mHtavAd.setVisibility(View.VISIBLE);
                                mHtavAd.setView(mCrossBar);
                            } else {
                                mHtavAd.setVisibility(View.GONE);
                            }

                            // 热门优惠
                            if (data.getTopDeals() != null && data.getTopDeals().size() > 0) {
                                mLlTopDeal.setVisibility(View.VISIBLE);
                                mTopDealList.clear();
                                mTopDealList.addAll(response.getData().getTopDeals());
                                mTopDealView.setData(mTopDealList);
                            } else {
                                mLlTopDeal.setVisibility(View.GONE);
                            }

                            //推荐专题
                            if (null != response.getData().getSpecials() && response.getData().getSpecials().size() > 0) {
                                specialList.clear();
                                layoutSpecial.setVisibility(View.VISIBLE);
                                specialList.addAll(response.getData().getSpecials());
                                int            size          = specialList.size();
                                int            length        = 144;
                                DisplayMetrics dm            = mContext.getResources().getDisplayMetrics();
                                float          density       = dm.density;
                                int            gridviewWidth = (int) ((size * (length)) * density);
                                int            itemWidth     = (int) (length * density);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                                gvSpecial.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                                gvSpecial.setColumnWidth(itemWidth); // 设置列表项宽
                                gvSpecial.setHorizontalSpacing(0); // 设置列表项水平间距
                                gvSpecial.setStretchMode(GridView.NO_STRETCH);

                                gvSpecial.setNumColumns(size); // 设置列数量=列表集合数
                                gvSpecial.setAdapter(specialRecommendAdapter);
                            } else {
                                layoutSpecial.setVisibility(View.GONE);
                            }

                            //最新优惠
                            latestdiscountList.clear();
                            if (null != data.getNewestDeals()
                                    && null != data.getNewestDeals().getRows()
                                    && data.getNewestDeals().getRows().size() > 0) {
                                latestdiscountList.addAll(data.getNewestDeals().getRows());
                                // 优惠列表最后一项，插入广告
                                List<SlidePicModel> crossBarPics = data.getNewestDeals().getCrossBarPics();
                                if (crossBarPics != null && crossBarPics.size() > 0) {
                                    latestdiscountList.add(crossBarPics.get(0));
                                }

                                lastIds = "";
                                for (DealModel dealModel : data.getNewestDeals().getRows()) {
                                    lastIds += ",";
                                    lastIds += dealModel.getDealId();
                                }
                            }
                            mLvDiscount.setPullLoadEnable(latestdiscountList.size() > 0);
                            latestDiscountAdapter.notifyDataSetChanged();
                            //                            ((MainActivity) mContext).showDiscountGuide();
                        }
                    }
                    ll_common_error.setVisibility(View.GONE);
                }, error -> {
                    if (mLvDiscount == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    mLvDiscount.stopLoadMore();
                    mLvDiscount.stopRefresh();
                    String localDiscount = (String) SPUtils.get(mContext, "discount" + categoryId, "");
                    if (!"".equals(localDiscount) && latestdiscountList.isEmpty()) {
                        DealIndexModelData data = JSON.parseObject(localDiscount, DealIndexModelData.class);
                        if (null != data) {
                            mLvDiscount.setVisibility(View.VISIBLE);
                            //banner
                            if (null != data.getSlidePics() && data.getSlidePics().size() > 0) {
                                layoutCircle.setVisibility(View.VISIBLE);
                                bannerList.clear();
                                bannerList.addAll(data.getSlidePics());
                                layoutCircle.setImageResources(bannerList, mAdCycleViewListener);
                            } else {
                                layoutCircle.setVisibility(View.GONE);
                            }

                            // 横栏广告
                            if (null != data.getCrossBarPics() && data.getCrossBarPics().size() > 0) {
                                mCrossBar = data.getCrossBarPics().get(0);
                                mHtavAd.setVisibility(View.VISIBLE);
                                mHtavAd.setView(mCrossBar);
                            } else {
                                mHtavAd.setVisibility(View.GONE);
                            }

                            //推荐专题
                            if (null != data.getSpecials() && data.getSpecials().size() > 0) {
                                specialList.clear();
                                layoutSpecial.setVisibility(View.VISIBLE);
                                specialList.addAll(data.getSpecials());
                                int            size          = specialList.size();
                                int            length        = 150;
                                DisplayMetrics dm            = mContext.getResources().getDisplayMetrics();
                                float          density       = dm.density;
                                int            gridviewWidth = (int) ((size * (length)) * density);
                                int            itemWidth     = (int) (length * density);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                                gvSpecial.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                                gvSpecial.setColumnWidth(itemWidth); // 设置列表项宽
                                gvSpecial.setHorizontalSpacing(0); // 设置列表项水平间距
                                gvSpecial.setStretchMode(GridView.NO_STRETCH);

                                gvSpecial.setNumColumns(size); // 设置列数量=列表集合数
                                gvSpecial.setAdapter(specialRecommendAdapter);
                            } else {
                                layoutSpecial.setVisibility(View.GONE);
                            }

                            // 最新优惠
                            latestdiscountList.clear();
                            if (null != data.getNewestDeals()) {
                                // 优惠数据
                                List<DealModel> rows = data.getNewestDeals().getRows();
                                if (rows != null && rows.size() > 0) {
                                    latestdiscountList.addAll(data.getNewestDeals().getRows());
                                }
                                // 广告数据
                                List<SlidePicModel> crossBarPics = data.getCrossBarPics();
                                if (crossBarPics != null && crossBarPics.size() > 0) {
                                    latestdiscountList.add(crossBarPics.get(0));
                                }
                            }
                            latestDiscountAdapter.notifyDataSetChanged();
                        }
                    }
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
                        .setNegativeButton(R.string.refuse, (dialog, which) -> {
                            dialog.dismiss();
                        }).show();
            }
        } else {
            SPUtils.put(mContext, SPConstant.REVIEW_TIPS, versionName + "," + today.toString());
        }
    }

    private void initEvent() {
        btnRefresh.setOnClickListener(v -> initData());
        mLvDiscount.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (isDiscountHome()) {
                    loadData();
                } else {
                    loadDeal();
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                loadDeal();
            }
        });
        mLvDiscount.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvDiscount.getHeaderViewsCount();
            if (index >= 0) {
                DealSlidePicBaseModel model = latestdiscountList.get(index);
                if (model == null)
                    return;
                if (model instanceof DealModel) {
                    // 优惠
                    DealModel dealModel = (DealModel) model;
                    // 页面埋点
                    YbxTrace.getInstance().event(mActivity, mActivity.pref, mActivity.prefh, mActivity.purl, mActivity.purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Deal_Latest + categoryId + "-" + dealModel.getDealId());
                    // 跳转
                    DiscountDetailActivity.launch(mContext, dealModel.getDealId());
                } else if (model instanceof SlidePicModel) {
                    // 广告
                    SlidePicModel slidePicModel = (SlidePicModel) model;
                    TopicLink.jump(mContext, slidePicModel);
                }
            }
        });
    }

    //加载图片资源
    private SlideCycleView.ImageCycleViewListener mAdCycleViewListener = new SlideCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View v) {
            // 页面埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Deal_Banner + categoryId + "-" + position);

            // 跳转
            TopicLink.jump(mContext, (SlidePicModel) v.getTag(), TopicLink.SOURCE_TYPE.BANNER);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        if (null != bannerList && bannerList.size() > 0) {
            layoutCircle.pushImageCycle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != bannerList && bannerList.size() > 0) {
            layoutCircle.startImageCycle();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_entry_one:
                SignActivity.launch(mContext);
                break;
            case R.id.tab_entry_two:
                StoreFilterActivity.launch(mContext);
                break;
            case R.id.tab_entry_three:
                TransportFilterActivity.launch(mContext);
                break;
            case R.id.tab_entry_four:
                InviteFriendsActivity.launch(mContext);
                break;
        }
    }*/

    @Subscribe
    public void onRefreshSwitchObject(RefreshSwitchObject refreshSwitchObject) {
        //        mLvDiscount.switchActivityMode(mContext);
        mLvDiscount.switchActivityMode(mContext);
        if (isDiscountHome()) {
            mLvDiscount.removeHeaderView(mHeaderView);
            mLvDiscount.addHeaderView(mHeaderView);
        }
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        if (mLvDiscount == null)
            return;
        int firstVisiblePosition = mLvDiscount.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            mLvDiscount.autoRefresh();
        } else {
            mLvDiscount.smoothScrollToPosition(0);
        }
    }
}
