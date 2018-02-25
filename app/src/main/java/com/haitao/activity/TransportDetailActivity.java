package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.TransportCommentFragment;
import com.haitao.fragment.TransportDetailFragment;
import com.haitao.fragment.TransportPostFragment;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.StickyNavLayout;

import java.util.ArrayList;

/**
 * 转运详情
 */
public class TransportDetailActivity extends BaseActivity implements View.OnClickListener {
    private String id = "";
    private LogisticsCompanyObject obj;
    private StickyNavLayout        layoutContent;
    private TextView               tvFav, tvBuy;
    private ViewGroup layoutFav;

    private ViewGroup       layoutProgress;
    private CustomImageView ivImage;
    private ImageView       mImgStoreLabel;
    private TextView        tvTransportTitle;
    private RatingBar       rbStar;
    private TextView        tvStar;
    private TextView        tvCount;

    private ViewPager               vpSwitch;
    private TabLayout               tabLayout;
    private BasePagerAdapter        mPagerAdapter;
    private ArrayList<BaseFragment> fragments;
    private String[]                tabs;


    protected IViewContext<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>> commandViewContext = VF.<LogisticsCompanyObject>getDefault(LogisticsCompanyObject.class);
    private CustomImageView          mImgActivityFab; // 全局活动入口
    private TransportCommentFragment mCommentFragment; // 转运评论Fragment

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, TransportDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_detail);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.ID))
                id = getIntent().getStringExtra(TransConstant.ID);
        }
        TAG = "转运详情";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.transport_detail_title);
        ivImage = getView(R.id.ivImage);
        mImgStoreLabel = getView(R.id.img_store_label);
        tvTransportTitle = getView(R.id.tvTransportTitle);
        mImgActivityFab = getView(R.id.img_event);
        rbStar = getView(R.id.rbStar);
        tvStar = getView(R.id.tvStar);
        tvCount = getView(R.id.tvCount);
        layoutContent = getView(R.id.layoutContent);
        tvFav = getView(R.id.tvFav);
        layoutFav = getView(R.id.layoutFav);
        tvBuy = getView(R.id.tvBuy);

        layoutProgress = getView(R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        tabLayout = getView(R.id.tab);
        vpSwitch = getView(R.id.id_stickynavlayout_viewpager);
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            //            ImageLoaderUtils.showOnlineImage(HtApplication.mActivityFabImg, mImgActivityFab);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvFav.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getData();
    }

    private void getData() {
        commandViewContext.getEntity().id = id;
        commandViewContext.getService().asynFunction(MethodConstant.TRANSPORT_DETAIL, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsCompanyObject>() {
            @Override
            public void onSuccess(LogisticsCompanyObject entity) throws Exception {
                layoutProgress.setVisibility(View.GONE);
                if (null != entity) {
                    obj = entity;
                    layoutContent.setVisibility(View.VISIBLE);
                    renderView();
                } else {
                    ToastUtils.show(mContext, R.string.empty_tips);
                    finish();
                }
            }

            @Override
            public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
            }
        });
    }

    private void renderView() {
        ImageLoaderUtils.showOnlineImage(obj.logo, ivImage);
        //        mImgStoreLabel.setVisibility(HtApplication.isActivityOn ? View.VISIBLE : View.GONE);
        tvTransportTitle.setText(obj.name);
        rbStar.setRating(Float.valueOf(obj.start_number));
        tvStar.setText(Float.valueOf(obj.start_number) > 0 ? obj.start_number + "星" : "暂无评分");
        tvCount.setText(String.format("%s个晒单 | %s人收藏", obj.thread_count, obj.collection_count));
        tvFav.setSelected("1".equals(obj.is_collect));
        layoutFav.setSelected("1".equals(obj.is_collect));
        tvFav.setText("1".equals(obj.is_collect) ? "取消收藏" : "收藏");

        tabs = new String[]{"详情", "帖子", "评价"};
        fragments = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.OBJECT, obj);

        TransportDetailFragment detailFragment = new TransportDetailFragment();
        detailFragment.setArguments(bundle);
        fragments.add(detailFragment);

        TransportPostFragment postFragment = new TransportPostFragment();
        postFragment.setArguments(bundle);
        fragments.add(postFragment);

        mCommentFragment = new TransportCommentFragment();
        mCommentFragment.setArguments(bundle);
        fragments.add(mCommentFragment);

        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        vpSwitch.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(vpSwitch);
        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }
    }

    /**
     * 收藏
     */
    private void addFav() {
        commandViewContext.getEntity().id = id;
        commandViewContext.getService().asynFunction(MethodConstant.TRANSPORT_FAV, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsCompanyObject>() {
            @Override
            public void onSuccess(LogisticsCompanyObject entity) throws Exception {

                tvFav.setEnabled(true);
                ToastUtils.show(mContext, R.string.disocunt_fav_success);
                int collection_count = TextUtils.isEmpty(obj.collection_count) ? 0 : obj.collection_count.contains("k") ? -1 : Integer.parseInt(obj.collection_count);
                collection_count++;
                obj.collection_count = -1 != collection_count ? String.valueOf(collection_count) : obj.collection_count;
                obj.is_collect = "1";

                tvCount.setText(String.format("%s个晒单 | %s人收藏", obj.thread_count, obj.collection_count));
                tvFav.setSelected(true);
                layoutFav.setSelected(true);
                tvFav.setText("取消收藏");
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_STORE_FAV);
                mContext.sendBroadcast(mIntent);
            }

            @Override
            public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
                btnRight.setEnabled(true);
            }
        });
    }

    /**
     * 取消收藏
     */
    private void delFav() {
        commandViewContext.getEntity().id = id;
        commandViewContext.getService().asynFunction(MethodConstant.TRANSPORT_FAV_REMOVE, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsCompanyObject>() {
            @Override
            public void onSuccess(LogisticsCompanyObject entity) throws Exception {
                ToastUtils.show(mContext, R.string.disocunt_del_fav_success);
                tvFav.setEnabled(true);
                tvFav.setSelected(false);
                layoutFav.setSelected(false);
                tvFav.setText("收藏");
                int collection_count = TextUtils.isEmpty(obj.collection_count) ? 0 : obj.collection_count.contains("k") ? -1 : Integer.parseInt(obj.collection_count);
                collection_count--;
                obj.collection_count = -1 != collection_count ? String.valueOf(collection_count) : obj.collection_count;
                obj.is_collect = "0";

                tvCount.setText(String.format("%s个晒单 | %s人收藏", obj.thread_count, obj.collection_count));
                Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_STORE_FAV);
                mContext.sendBroadcast(mIntent);
            }

            @Override
            public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
                btnRight.setEnabled(true);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFav:
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                } else {
                    tvFav.setEnabled(false);
                    if (tvFav.isSelected()) {
                        delFav();
                    } else {
                        addFav();
                    }
                }
                break;
            case R.id.tvBuy:
                if (obj == null) {
                    ToastUtils.show(mContext, "正在加载，请稍后...");
                    return;
                }
                BoardDetailActivity.launch(mContext, obj.forumid);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && resultCode == TransConstant.REFRESH) {
            ((TransportCommentFragment) fragments.get(2)).refresh();
        }
        // 登录之后，需要刷新评论
        if (requestCode == TransConstant.IS_LOGIN && HtApplication.isLogin()) {
            // 刷新收藏状态
            commandViewContext.getEntity().id = id;
            commandViewContext.getService().asynFunction(MethodConstant.TRANSPORT_DETAIL, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsCompanyObject>() {
                @Override
                public void onSuccess(LogisticsCompanyObject entity) throws Exception {
                    obj.is_collect = entity.is_collect;
                    tvFav.setText("1".equals(obj.is_collect) ? "取消收藏" : "收藏");
                    tvFav.setSelected("1".equals(obj.is_collect));

                }

                @Override
                public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {

                }

                @Override
                public void onFailed(String error) {
                    ToastUtils.show(mContext, error);
                }
            });

            // 刷新评论
            if (mCommentFragment.isResumed()) {
                mCommentFragment.loadData();
            }
        }
    }
}
