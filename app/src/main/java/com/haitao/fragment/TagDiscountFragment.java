package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.DealAdapter;
import com.haitao.adapter.StoreFilterTagAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HorizontalListView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.DealModel;
import io.swagger.client.model.IfFilterModel;
import io.swagger.client.model.IfFilterModelValues;

/**
 * 标签-优惠
 */
public class TagDiscountFragment extends BaseFragment {

    @BindView(R.id.content_listview) XListView          lvList;       // 优惠列表
    @BindView(R.id.lv_filter)        HorizontalListView mLvFilter;    // 标签列表
    @BindView(R.id.msv)              MultipleStatusView mMsv;         // 多状态布局

    private Unbinder mUnbinder;

    private String tagId;                                         // 标签id

    private StoreFilterTagAdapter     mFilterAdapter;             // 筛选条件Adapter
    private List<IfFilterModelValues> mFilters;                   // 筛选标签
    private String                    filterId;                   // 当前筛选Id

    private DealAdapter          mAdapter;                        // 优惠列表adapter
    private ArrayList<DealModel> mList;                           // 优惠列表数据

    private int page = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_discount, null);
        mUnbinder = ButterKnife.bind(this, view);
        initVars();
        initView();
        initEvent();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = "标签详情 - 优惠";
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.VALUE)) {
                tagId = bundle.getString(TransConstant.VALUE);
            }
        }
        mList = new ArrayList<>();
        mFilters = new ArrayList<>();
    }

    private void initView() {
        mAdapter = new DealAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
    }

    private void initEvent() {
        mMsv.setOnRetryClickListener(v -> loadData());
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                DealModel dealModel = mList.get(index);
                if (dealModel != null) {
                    DiscountDetailActivity.launch(mContext, dealModel.getDealId());
                }
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
        // 筛选条件点击事件
        mLvFilter.setOnItemClickListener((parent, view, position, id) -> {
            if (mFilters != null && mFilters.get(position) != null) {
                mFilterAdapter.setCurrentPosition(position);
                // 筛选Id
                if (!TextUtils.equals(filterId, mFilters.get(position).getValue())) {
                    filterId = mFilters.get(position).getValue();
                    // 筛选条件修改后，需要刷新
                    page = 1;
                    lvList.smoothScrollToPosition(0);
                    loadData();
                }
            }
        });
    }

    private void initData() {
        mMsv.showLoading();
        loadData();
    }

    private void loadData() {

        Logger.d("tagId = " + tagId + " filterId = " + filterId + " page = " + String.valueOf(page) + " pageSize = " + String.valueOf(PageConstant.pageSize));

        ForumApi.getInstance().tagTagIdDealsListGet(tagId, filterId, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    mMsv.showContent();
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            // 优惠列表
                            if (page == 1)
                                mList.clear();
                            mList.addAll(response.getData().getRows());
                            mAdapter.notifyDataSetChanged();
                            lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));

                            // 筛选标签列表
                            List<IfFilterModel> filterGroup = response.getData().getFilters();
                            if (filterGroup != null && filterGroup.size() > 0) {   //   标签只有一个的话也不要显示
                                List<IfFilterModelValues> filterList = filterGroup.get(0).getValues();
                                if (mFilters.size() == 0 && filterList != null && filterList.size() > 1) {
                                    mFilters.addAll(filterList);
                                    mFilterAdapter = new StoreFilterTagAdapter(mContext, mFilters);
                                    mLvFilter.setAdapter(mFilterAdapter);
                                    mLvFilter.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {
                            if (page == 1)
                                mMsv.showEmpty();
                        }
                    } else {
                        mMsv.showError();
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    mMsv.showError();
                    lvList.stopLoadMore();
                    lvList.stopRefresh();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
