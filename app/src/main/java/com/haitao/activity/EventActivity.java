package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.EventAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.PostObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 活动中心
 */
public class EventActivity extends BaseActivity {

    private XListView             lvList;
    private EventAdapter          mAdapter;
    private ArrayList<PostObject> mList;
    protected IViewContext<PostObject, IEntityService<PostObject>> commandViewContext = VF.<PostObject>getDefault(PostObject.class);
    private HtHeadView mHtHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        TAG = "活动列表";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText(getString(R.string.forum_event_title));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadNext();
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                PostObject postObject = mList.get(index);
                if (postObject != null) {
                    TopicDetailActivity.launch(mContext, postObject.tid);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new EventAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        loadData();
    }

    private void loadData() {
        commandViewContext.getPage().page = 1;
        commandViewContext.getEntity().category = "0";
        commandViewContext.asynQuery(MethodConstant.SHIPPING_ACTIVITY, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.getEntity().category = "0";
        commandViewContext.asynQueryNext(MethodConstant.SHIPPING_ACTIVITY, commandViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<PostObject> {

        @Override
        public void onSuccess(PostObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<PostObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            lvList.setVisibility(View.VISIBLE);
            if (1 == commandViewContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == commandViewContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= commandViewContext.getPage().page) {
                    lvList.setPullLoadEnable(false);
                } else {
                    lvList.setPullLoadEnable(true);
                }
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
        }
    }

    /**
     * 跳转到当前页
     *
     * @param context context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        context.startActivity(intent);
    }
}
