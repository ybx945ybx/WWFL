package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.SystemMessageAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.TopicLink;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.MsgsListModelDataRows;
import io.swagger.client.model.SlidePicModel;

/**
 * 系统提醒
 * Created by penley on 16/3/1.
 */
public class NoticeSystemMessageActivity extends BaseActivity {

    @BindView(R.id.ht_headview) HtHeadView mHtHeadview;

    private XListView                        lvList;
    private ArrayList<MsgsListModelDataRows> mList;
    private SystemMessageAdapter             mAdapter;
    private String type = "";

    private ViewGroup layoutProgress;
    private int page = 1;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String type) {
        Intent intent = new Intent(context, NoticeSystemMessageActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        type = getIntent().getStringExtra(TransConstant.TYPE);
        TAG = type.equals(TransConstant.NoticeType.SYSTEM) ? "系统提醒" : "精选推荐";
    }

    /**
     * 初始化视图
     */
    private void initView() {
//        initTop();
        mHtHeadview.setCenterText(type.equals(TransConstant.NoticeType.SYSTEM) ? "系统提醒" : "精选推荐");
        //        tvTitle.setText(type.equals(TransConstant.NoticeType.SYSTEM) ? "系统提醒" : "精选推荐");
        lvList = getView(R.id.lvList);

        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);

        ll_common_error = getView(R.id.ll_common_error);
        tvErrorMsg = getView(R.id.tvErrorMsg);
        btnRefresh = getView(R.id.btnRefresh);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                MsgsListModelDataRows obj = mList.get(index);
                if (obj != null && !TextUtils.isEmpty(obj.getJumpType()) && !TextUtils.isEmpty(obj.getJumpValue())) {
                    SlidePicModel slidePicModel = new SlidePicModel();
                    slidePicModel.setType(obj.getJumpType());
                    slidePicModel.setLinkData(obj.getJumpValue());
                    TopicLink.jump(mContext, slidePicModel, "");
                }
            }
        });
        btnRefresh.setOnClickListener(v -> getData());
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<>();
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        mAdapter = new SystemMessageAdapter(mContext, mList);
        mAdapter.isSystemMessage = type.equals(TransConstant.NoticeType.SYSTEM);
        lvList.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        ForumApi.getInstance().userMsgsListGet(type.equals(TransConstant.NoticeType.SYSTEM) ? "1" : "2", String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            mList.clear();
                        if (null != response.getData() && null != response.getData().getRows()) {
                            mList.addAll(response.getData().getRows());
                        }
                        lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(1);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                });
    }
}
