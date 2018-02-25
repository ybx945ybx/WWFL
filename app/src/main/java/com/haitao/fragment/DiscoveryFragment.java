package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.EventActivity;
import com.haitao.activity.ExchangeActivity;
import com.haitao.activity.SampleActivity;
import com.haitao.activity.SampleDetailActivity;
import com.haitao.activity.SearchActivity;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.adapter.EventAdapter;
import com.haitao.adapter.PostAdapter;
import com.haitao.adapter.SampleAdapter;
import com.haitao.adapter.StoreDiscoveryAdapter;
import com.haitao.adapter.TransportAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Enum.SearchType;
import com.haitao.common.HtApplication;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.DiscoveryHomeObject;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.PostObject;
import com.haitao.model.SampleObject;
import com.haitao.model.StoreObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.FullListView;
import com.haitao.view.HtTitleView;
import com.haitao.view.refresh.XListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 发现
 */
public class DiscoveryFragment extends BaseFragment implements View.OnClickListener {
    private Context     mContext;
    private ViewGroup   layoutSearch;
    private XListView   layoutScroll;
    private ViewGroup   layoutProgress;
    private PostAdapter mAdapter;

    private TextView tvActivity, tvExchange, tvTrial, tvSail;

    private HtTitleView mTitleRecommendActivity;  // 推荐活动 标题
    private HtTitleView mTitleRecommendTrail;     // 推荐试用 标题
    private HtTitleView mTitleRecommendStore;     // 推荐商家 标题
    private HtTitleView mTitleRecommendTransport; // 推荐转运 标题

    private FullListView          lvActivity;
    private ArrayList<PostObject> eventList;
    private EventAdapter          eventAdapter;

    private FullListView            lvTrial;
    private ArrayList<SampleObject> sampleList;
    private SampleAdapter           sampleAdapter;

    private FullListView           lvStore;
    private ArrayList<StoreObject> storeList;
    private StoreDiscoveryAdapter  storeAdapter;

    private FullListView                      lvTransport;
    private ArrayList<LogisticsCompanyObject> transportList;
    private TransportAdapter                  transportAdapter;

    protected IViewContext<DiscoveryHomeObject, IEntityService<DiscoveryHomeObject>> commandViewContext = VF.<DiscoveryHomeObject>getDefault(DiscoveryHomeObject.class);
    private CustomImageView mImgActivityFab; // 全局活动入口图

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View messageLayout = initView(inflater);

        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    private View initView(LayoutInflater inflater) {
        TAG = "发现";
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        initError(view);
        layoutSearch = getView(view, R.id.layoutSearch);
        layoutScroll = getView(view, R.id.layoutScroll);
        layoutProgress = getView(view, R.id.layoutProgress);
        mImgActivityFab = getView(view, R.id.img_event);
        layoutScroll.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
        ViewGroup viewContent = (ViewGroup) View.inflate(mContext, R.layout.layout_discovery, null);
        // 推荐活动
        mTitleRecommendActivity = getView(viewContent, R.id.title_recommend_activity);
        mTitleRecommendActivity.setVisibility(View.GONE);
        // 推荐试用
        mTitleRecommendTrail = getView(viewContent, R.id.title_recommend_trail);
        mTitleRecommendTrail.setVisibility(View.GONE);
        // 推荐商家
        mTitleRecommendStore = getView(viewContent, R.id.title_recommend_store);
        mTitleRecommendStore.setVisibility(View.GONE);
        // 推荐转运
        mTitleRecommendTransport = getView(viewContent, R.id.title_recommend_transport);
        mTitleRecommendTransport.setVisibility(View.GONE);

        tvActivity = getView(viewContent, R.id.tvActivity);
        lvActivity = getView(viewContent, R.id.lvActivity);
        tvExchange = getView(viewContent, R.id.tvExchange);
        tvTrial = getView(viewContent, R.id.tvTrial);
        tvSail = getView(viewContent, R.id.tvSail);
        lvTrial = getView(viewContent, R.id.lvTrial);

        lvTransport = getView(viewContent, R.id.lvTransport);

        lvStore = getView(viewContent, R.id.lvStore);

        layoutScroll.addHeaderView(viewContent);
        mAdapter = new PostAdapter(mContext, new ArrayList<PostObject>());
        layoutScroll.setAdapter(mAdapter);
        layoutScroll.setAutoLoadEnable(true);
        layoutScroll.setPullLoadEnable(false);
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            //            ImageLoaderUtils.showOnlineImage(HtApplication.mActivityFabImg, mFabActivity);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
        return view;
    }


    public void initData() {
        eventList = new ArrayList<PostObject>();
        eventAdapter = new EventAdapter(mContext, eventList);
        lvActivity.setAdapter(eventAdapter);
        sampleList = new ArrayList<SampleObject>();
        sampleAdapter = new SampleAdapter(mContext, sampleList);
        lvTrial.setAdapter(sampleAdapter);

        transportList = new ArrayList<LogisticsCompanyObject>();
        transportAdapter = new TransportAdapter(mContext, transportList);
        lvTransport.setAdapter(transportAdapter);

        storeList = new ArrayList<StoreObject>();
        storeAdapter = new StoreDiscoveryAdapter(mContext, storeList);
        lvStore.setAdapter(storeAdapter);
        getData();
    }

    private void initEvent() {
        layoutSearch.setOnClickListener(this);
        tvActivity.setOnClickListener(this);
        tvExchange.setOnClickListener(this);
        tvTrial.setOnClickListener(this);
        tvSail.setOnClickListener(this);
//        mTitleRecommendActivity.setOnMoreClickListener(view -> EventActivity.launch(mContext));
//        mTitleRecommendTrail.setOnMoreClickListener(view -> SampleActivity.launch(mContext));
//        mTitleRecommendStore.setOnMoreClickListener(v -> StoreFilterActivity.launch(mContext));
//        mTitleRecommendTransport.setOnMoreClickListener(v -> TransportFilterActivity.launch(mContext));

        layoutScroll.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        lvTrial.setOnItemClickListener((parent, view, position, id) -> SampleDetailActivity.launch(mContext, sampleList.get(position)));
        lvActivity.setOnItemClickListener((parent, view, position, id) -> TopicDetailActivity.launch(mContext, eventList.get(position).id));
        lvStore.setOnItemClickListener((parent, view, position, id) -> StoreDetailActivity.launch(mContext, storeList.get(position).id));
        lvTransport.setOnItemClickListener((parent, view, position, id) -> TransportDetailActivity.launch(mContext, transportList.get(position).id));
        btnRefresh.setOnClickListener(v -> getData());
    }

    private void getData() {
        commandViewContext.getService().asynFunction(MethodConstant.DISCOVERY_INDEX, commandViewContext.getEntity(), new IAsynServiceHandler<DiscoveryHomeObject>() {
            @Override
            public void onSuccess(DiscoveryHomeObject entity) throws Exception {
                layoutProgress.setVisibility(View.GONE);
                layoutScroll.setVisibility(View.VISIBLE);
                layoutScroll.stopLoadMore();
                layoutScroll.stopRefresh();
                if (null != entity) {
                    layoutScroll.setRefreshTime();
                    // 推荐活动
                    eventList.clear();
                    if (null != entity.activity_list && !entity.activity_list.isEmpty()) {
                        eventList.addAll(entity.activity_list);
                        mTitleRecommendActivity.setVisibility(View.VISIBLE);
                    } else {
                        mTitleRecommendActivity.setVisibility(View.GONE);
                    }
                    eventAdapter.notifyDataSetChanged();

                    // 推荐试用
                    sampleList.clear();
                    if (null != entity.freetrial_list && !entity.freetrial_list.isEmpty()) {
                        sampleList.addAll(entity.freetrial_list);
                        mTitleRecommendTrail.setVisibility(View.VISIBLE);
                    } else {
                        mTitleRecommendTrail.setVisibility(View.GONE);
                    }
                    sampleAdapter.notifyDataSetChanged();

                    // 推荐商家
                    storeList.clear();
                    if (null != entity.hot_store && !entity.hot_store.isEmpty()) {
                        storeList.addAll(entity.hot_store);
                        mTitleRecommendStore.setVisibility(View.VISIBLE);
                    } else {
                        mTitleRecommendStore.setVisibility(View.GONE);
                    }
                    storeAdapter.notifyDataSetChanged();

                    // 推荐转运
                    transportList.clear();
                    if (null != entity.shipping_list && !entity.shipping_list.isEmpty()) {
                        transportList.addAll(entity.shipping_list);
                        mTitleRecommendTransport.setVisibility(View.VISIBLE);
                    } else {
                        mTitleRecommendTransport.setVisibility(View.GONE);
                    }
                    transportAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSuccessPage(PageResult<DiscoveryHomeObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                layoutProgress.setVisibility(View.GONE);
                layoutScroll.setVisibility(View.VISIBLE);
                layoutScroll.stopLoadMore();
                layoutScroll.stopRefresh();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutSearch:
                SearchActivity.launch(mContext, SearchType.ALL);
                break;
            case R.id.tvActivity:
                EventActivity.launch(mContext);
                break;
            case R.id.tvExchange:
                ExchangeActivity.launch(mContext);
                break;
            case R.id.tvTrial:
                SampleActivity.launch(mContext);
                break;
            case R.id.tvSail:
                BoardDetailActivity.launch(mContext, "96");
                break;
        }
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        int firstVisiblePosition = layoutScroll.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            //            lvList.autoRefresh();
            layoutScroll.autoRefresh();
        } else {
            layoutScroll.smoothScrollToPosition(0);
        }
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
