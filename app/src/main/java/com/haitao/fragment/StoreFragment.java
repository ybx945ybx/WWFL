package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.SearchActivity;
import com.haitao.activity.StoreActivity;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.adapter.CountryIconAdapter;
import com.haitao.adapter.StoreBaseAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Enum.SearchType;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.event.LoginStateChangedEvent;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.HorizontalListView;
import com.haitao.view.HtAdView;
import com.haitao.view.HtIconTitlesView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.swagger.client.model.BaseStoreListObject;
import io.swagger.client.model.EnteredStoreModel;
import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.StoreIndexIfModelData;
import io.swagger.client.model.StoreIndexIfModelDataAreasBriefs;
import io.swagger.client.model.StoreIndexIfModelDataSuperRebate;
import io.swagger.client.model.StoreWithDealsModel;
import io.swagger.client.model.StoresWithDealsListModelData;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 主页-商家Fragment
 *
 * @author 陶声
 * @since 2017-12-05
 */
public class StoreFragment extends BaseFragment {
    @BindView(R.id.lv_store)  XListView          mLvStore;     // 商家列表
    @BindView(R.id.img_event) CustomImageView    mFabActivity; // 活动入口
    @BindView(R.id.msv)       MultipleStatusView mMsv;         // 多状态布局

    private RelativeLayout     mRlBanner;         // 限时超级返利
    private HtIconTitlesView   mHtFavStore;       // 收藏的商家
    private HtIconTitlesView   mHtChinaDirect;    // 直购中国
    private HorizontalListView mLvCountry;        // 国家
    private CustomImageView    mImgSuperStore;    // 限时超级返利图
    private TextView           mTvSuperStoreInfo; // 限时超级返利信息
    private TextView           mTvSuperRebate;    // 限时超级返利比例

    private CountryIconAdapter mCountryAdapter;   // 国家分类Adapter
    private StoreBaseAdapter   mAdapter;          // 今日加倍返利 + 精选商家Adapter

    private List<StoreIndexIfModelDataAreasBriefs> mCountryList;        // 国家分类
    private List<BaseStoreListObject>              mBaseStoreList;      // 加倍返利 & 商家优惠

    private Unbinder                         unbinder;
    private StoreIndexIfModelDataSuperRebate mSuperRebate;
    private HtAdView                         mHtavAd;   // 广告
    private int                              mPage;     // 页数


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = initView(inflater);
        initVars();
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMsv.showLoading();
        loadData();
    }

    private void initVars() {
        TAG = "商家首页";
        mPage = 1;
        EventBus.getDefault().register(this);
        // 国家分类
        mCountryList = new ArrayList<>();
        // 加倍返利&商家优惠列表
        mBaseStoreList = new ArrayList<>();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_store, null);
        unbinder = ButterKnife.bind(this, view);
        //// 头布局 ////
        ViewGroup headerView = (ViewGroup) View.inflate(mContext, R.layout.layout_store, null);
        // 限时超级返利banner
        mRlBanner = headerView.findViewById(R.id.rl_banner);
        // 收藏的商家
        mHtFavStore = headerView.findViewById(R.id.ht_fav_store);
        // 直购中国
        mHtChinaDirect = headerView.findViewById(R.id.ht_china_direct);
        // 广告
        mHtavAd = headerView.findViewById(R.id.htav_ad);
        // 国家
        mLvCountry = headerView.findViewById(R.id.lv_country);

        mImgSuperStore = headerView.findViewById(R.id.img_super_store);
        mTvSuperStoreInfo = headerView.findViewById(R.id.tv_super_store_info);
        mTvSuperRebate = headerView.findViewById(R.id.tv_super_rebate);

        mLvStore.addHeaderView(headerView);
        mLvStore.setAutoLoadEnable(true);
        mLvStore.setPullLoadEnable(false);
        // 设置活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mFabActivity.setVisibility(View.VISIBLE);
            mFabActivity.setOnClickListener(v -> goEvent(mContext));
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mFabActivity);
        }
        return view;
    }

    public void refreshData() {
        mPage = 1;
        loadData();
    }

    private void initEvent() {
        // 直购中国
        mHtChinaDirect.setOnClickListener(v -> {
            // 页面埋点
            YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Direct_China);

            // 跳转
            StoreActivity.launch(mContext, StoreActivity.TYPE_DIRECT_POST, null);
        });
        // 收藏的商家
        mHtFavStore.setOnClickListener(v -> {
            if (!HtApplication.isLogin()) {
                QuickLoginActivity.launch(mContext);
            } else {
                // 页面埋点
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Collect_Store_List);

                // 跳转
                StoreActivity.launch(mContext, StoreActivity.TYPE_FAV_STORE, null);
            }
        });
        mLvStore.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadMore();
            }
        });
        // 今日加倍返利&商家优惠列表 点击事件
        mLvStore.setOnItemClickListener((parent, view, position, id) -> {
            if (mBaseStoreList != null) {
                int index = position - mLvStore.getHeaderViewsCount();
                if (mBaseStoreList.size() > index) {
                    BaseStoreListObject item = mBaseStoreList.get(index);
                    if (item instanceof EnteredStoreModel) {
                        // 页面埋点
                        YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Double_Store);

                        // 跳转
                        // 商家详情
                        StoreDetailActivity.launch(mContext, ((EnteredStoreModel) item).getStoreId());
                    } else if (item instanceof StoreWithDealsModel) {
                        // 页面埋点
                        YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Sale_Store);

                        // 跳转
                        // 商家详情
                        StoreDetailActivity.launch(mContext, ((StoreWithDealsModel) item).getStoreId());
                    }
                }
            }
        });
        // 国家列表
        mLvCountry.setOnItemClickListener((parent, view, position, id) -> {
            if (mCountryList.size() > position) {
                // 页面埋点
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Store_list);

                // 跳转
                StoreActivity.launch(mContext, StoreActivity.TYPE_COUNTRY_STORE, mCountryList.get(position).getAreaId());
            }
        });
        // 断网重试
        mMsv.setOnRetryClickListener(v -> {
            mMsv.showLoading();
            mPage = 1;
            loadData();
        });
        // 限时超级返利
        mRlBanner.setOnClickListener(v -> {
            if (mSuperRebate != null && mSuperRebate.getId() != null) {
                // 页面埋点
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Discover_Banner);

                // 跳转
                StoreDetailActivity.launch(mContext, mSuperRebate.getId());
            }
        });
    }

    private void loadData() {
        ForumApi.getInstance().storeIndexGet(String.valueOf(PageConstant.pageSize / 2),
                response -> {
                    if (TextUtils.equals(response.getCode(), "0")) {
                        mLvStore.stopLoadMore();
                        mLvStore.stopRefresh();
                        mMsv.showContent();
                        if (mPage == 1) {
                            mBaseStoreList.clear();
                        }
                        StoreIndexIfModelData data = response.getData();
                        if (data != null) {
                            // 限时超级返利
                            mSuperRebate = data.getSuperRebate();
                            if (mSuperRebate != null) {
                                mTvSuperStoreInfo.setText(mSuperRebate.getTitle());
                                mTvSuperRebate.setText(mSuperRebate.getRebateView());
                                mTvSuperRebate.setVisibility(TextUtils.isEmpty(mSuperRebate.getRebateView()) ? View.INVISIBLE : View.VISIBLE);
                                ImageLoaderUtils.showOnlineImage(mSuperRebate.getPic(), mImgSuperStore);
                            }
                            // 收藏商家个数
                            String favCounts = data.getFavoriteStoresCount();
                            if (TextUtils.isEmpty(favCounts)) {
                                mHtFavStore.setIconText("0个");
                            } else {
                                mHtFavStore.setIconText(favCounts + "个");
                            }
                            // 国家
                            if (data.getAreasBriefs() != null && data.getAreasBriefs().size() > 0) {
                                if (mCountryList.size() != 0)
                                    mCountryList.clear();
                                mLvCountry.setVisibility(View.VISIBLE);
                                mCountryList.addAll(data.getAreasBriefs());
                                mCountryAdapter = new CountryIconAdapter(mContext, mCountryList);
                                mLvCountry.setAdapter(mCountryAdapter);
                            } else {
                                mLvCountry.setVisibility(View.GONE);
                            }
                            // 广告
                            List<SlidePicModel> crossBarPics = data.getCrossBarPics();
                            if (crossBarPics != null && crossBarPics.size() > 0) {
                                mHtavAd.setVisibility(View.VISIBLE);
                                mHtavAd.setView(crossBarPics.get(0));
                            } else {
                                mHtavAd.setVisibility(View.GONE);
                            }
                            int doubleRebateStoresCount = 0;
                            // 今日加倍返利
                            if (data.getDoubleRebateStores() != null && data.getDoubleRebateStores().size() > 0) {
                                mBaseStoreList.addAll(data.getDoubleRebateStores());
                                doubleRebateStoresCount = data.getDoubleRebateStores().size();
                            }
                            // 商家及置顶优惠
                            if (data.getStoresWithDeals() != null && data.getStoresWithDeals().size() > 0) {
                                mBaseStoreList.addAll(data.getStoresWithDeals());

                                mAdapter = new StoreBaseAdapter(mContext, mBaseStoreList);
                                mAdapter.setDoubleRebateStoresCount(doubleRebateStoresCount);
                                mLvStore.setAdapter(mAdapter);
                                mLvStore.setPullLoadEnable(true);
                            }
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    mLvStore.stopLoadMore();
                    mLvStore.stopRefresh();
                    mMsv.showError();
                    showErrorToast(error);
                });
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        ForumApi.getInstance().storeStoresWithDealsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize / 2), "",
                response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        // 成功
                        StoresWithDealsListModelData data = response.getData();
                        if (data != null) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mBaseStoreList.addAll(response.getData().getRows());
                            }
                            mLvStore.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                this::showErrorToast);
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        int firstVisiblePosition = mLvStore.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            mLvStore.autoRefresh();
        } else {
            mLvStore.smoothScrollToPosition(0);
        }
    }

    /**
     * 全局活动入口设置事件
     *
     * @param event 事件
     */
    @Subscribe
    public void onActivityFabImgSetEvent(ActivityFabImgSetEvent event) {
        mFabActivity.setVisibility(View.VISIBLE);
        mFabActivity.setOnClickListener(v -> goEvent(mContext));
        ImageLoaderUtils.showOnlineImage(event.img, mFabActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 搜索
     */
    @OnClick(R.id.ll_search)
    public void onClickSearch() {
        SearchActivity.launch(mContext, SearchType.ALL);
    }

    @Subscribe
    public void onLoginStateChangedEvent(LoginStateChangedEvent event) {
        if (!event.isLogin) {
            refreshData();
        }
    }
}
