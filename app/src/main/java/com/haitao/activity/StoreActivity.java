package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.StoreAdapter;
import com.haitao.adapter.StoreFilterTagAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HorizontalListView;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.EnteredStoreModel;
import io.swagger.client.model.EnteredStoresListModelData;
import io.swagger.client.model.IfFilterModel;
import io.swagger.client.model.IfFilterModelValues;

/**
 * 商家页面
 *
 * @author 陶声
 * @since 2017-12-05
 */
public class StoreActivity extends BaseActivity {

    @BindView(R.id.content_listview) XListView          mLvStore;    // 商家列表
    @BindView(R.id.lv_filter)        HorizontalListView mLvFilter;   // 筛选标签列表
    @BindView(R.id.ht_headview)      HtHeadView         mHtHeadview; // 标题
    @BindView(R.id.msv)              MultipleStatusView mMsv;        // 多状态布局

    public static final String TYPE_DIRECT_POST   = "1";    // 直邮中国商家
    public static final String TYPE_FAV_STORE     = "2";    // 收藏的商家
    public static final String TYPE_COUNTRY_STORE = "3";    // 收藏的某个国家的商家

    private StoreAdapter              mStoreAdapter;              // 商家列表Adapter
    private StoreFilterTagAdapter     mFilterAdapter;             // 商家筛选条件Adapter
    private int                       mPage;                      // 页数
    private String                    mType;                      // 页面类型
    private List<EnteredStoreModel>   mData;                      // 数据
    private List<IfFilterModelValues> mFilters;                   // 筛选标签

    private String filterId;                   // 筛选Id
    private String mCountryId;                 // 国家Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            // 页面类型
            if (intent.hasExtra(TransConstant.TYPE))
                mType = intent.getStringExtra(TransConstant.TYPE);
            // 国家类型，需要传国家Id
            if (intent.hasExtra(TransConstant.CODE))
                mCountryId = intent.getStringExtra(TransConstant.CODE);
        }
        mData = new ArrayList<>();
        mFilters = new ArrayList<>();
    }

    private void initViews() {
        // 设置标题
        if (TextUtils.equals(mType, TYPE_FAV_STORE)) {
            // 收藏的商家
            mHtHeadview.setCenterText(getString(R.string.fav_store));
            TAG = "商家列表页-收藏";
        } else if (TextUtils.equals(mType, TYPE_COUNTRY_STORE)) {
            TAG = "商家列表页-国家";
        } else {
            TAG = "商家列表页-直邮中国";
        }
        mStoreAdapter = new StoreAdapter(mContext, mData);
        mLvStore.setAdapter(mStoreAdapter);
        mLvStore.setAutoLoadEnable(true);
        // 下拉刷新 & 加载更多事件
        mLvStore.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadData();
            }
        });
        // 条目点击事件
        mLvStore.setOnItemClickListener((parent, view, position, id) -> {
            if (mData != null) {
                int index = position - mLvStore.getHeaderViewsCount();
                if (index >= 0 && mData.size() > index) {
                    // 跳转商家详情页
                    StoreDetailActivity.launch(mContext, mData.get(index).getStoreId());
                }
            }
        });
        // 筛选条件点击事件
        mLvFilter.setOnItemClickListener((parent, view, position, id) -> {
            if (mFilters != null && mFilters.get(position) != null) {
                mFilterAdapter.setCurrentPosition(position);
                // 筛选Id
                if (!TextUtils.equals(filterId, mFilters.get(position).getValue())) {
                    if (TextUtils.equals(mType, TYPE_FAV_STORE)) {
                        mCountryId = mFilters.get(position).getValue();
                    } else {
                        filterId = mFilters.get(position).getValue();
                    }
                    // 筛选条件修改后，需要刷新
                    mPage = 1;
                    loadData();
                }
            }
        });
        // 重试
        mMsv.setOnRetryClickListener(v -> initData());
    }

    private void initData() {
        mPage = 1;
        mMsv.showLoading();
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().storeIndexEnteredStoresListGet(mType, filterId, mCountryId,
                String.valueOf(mPage), String.valueOf(PageConstant.pageSize), "",
                response -> {
                    if (mMsv == null)
                        return;
                    mLvStore.stopRefresh();
                    mLvStore.stopLoadMore();
                    mMsv.showContent();
                    if (TextUtils.equals("0", response.getCode())) {
                        if (1 == mPage) {
                            mData.clear();
                        }
                        EnteredStoresListModelData data = response.getData();
                        if (null != data) {
                            // 筛选标签列表
                            List<IfFilterModel> filterGroup = data.getFilters();
                            if (filterGroup != null && filterGroup.size() > 0) {
                                List<IfFilterModelValues> filterList = filterGroup.get(0).getValues();
                                if (mFilters.size() == 0 && filterList != null && filterList.size() > 0) {
                                    mFilters.addAll(filterList);
                                    mFilterAdapter = new StoreFilterTagAdapter(mContext, mFilters);
                                    mLvFilter.setAdapter(mFilterAdapter);
                                    mLvFilter.setVisibility(View.VISIBLE);
                                }
                            }
                            if (mFilters.size() == 0)
                                mLvFilter.setVisibility(View.GONE);

                            // 商家列表
                            if (null != data.getRows() && data.getRows().size() > 0) {
                                mData.addAll(data.getRows());
                            }
                            mLvStore.setPullLoadEnable(TextUtils.equals(data.getHasMore(), "1"));
                        }
                        if (mData.isEmpty()) {
                            mMsv.showEmpty();
                        }
                        mStoreAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    mMsv.showError();
                    mLvStore.stopRefresh();
                    mLvStore.stopLoadMore();
                });
    }

    /**
     * 跳转到本页面
     *
     * @param context   mContext
     * @param type      类型
     * @param countryId 国家分类 没有传空
     */
    public static void launch(Context context, String type, String countryId) {
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        intent.putExtra(TransConstant.CODE, countryId);
        context.startActivity(intent);
    }
}
