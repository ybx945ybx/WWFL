package com.haitao.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.LogisticsOrderAdapter;
import com.haitao.adapter.LogisticsOrderTransAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.OrderObject;
import com.haitao.utils.ToastUtils;
import com.haitao.view.refresh.XListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import io.swagger.client.model.OrderBriefModel;
import io.swagger.client.model.OrderModel;

import static com.haitao.activity.LogisticsOrderActivity.OrderSelectType.TRANSPORT_COMMENT;
import static com.haitao.activity.LogisticsOrderActivity.OrderSelectType.WITHDRAW;

/**
 * 订单选择页面
 */
public class LogisticsOrderActivity extends BaseActivity {
    private OrderBriefModel            mOrderBriefModel;
    private XListView                  mLvList;
    private ArrayList<OrderBriefModel> mWithdrawList;
    private ArrayList<OrderModel>      mTransList;
    private LogisticsOrderAdapter      mWithdrawAdapter;
    private LogisticsOrderTransAdapter mTransAdapter;
    private int                        mPage;

    private ViewGroup layoutProgress;

    protected IViewContext<OrderObject, IEntityService<OrderObject>> commandViewContext = VF.<OrderObject>getDefault(OrderObject.class);
    private String mTransId; // 转运ID

    @IntDef({WITHDRAW, TRANSPORT_COMMENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderSelectType {
        int WITHDRAW = 0;   // 提现

        int TRANSPORT_COMMENT = 1; // 转运评价
    }

    @OrderSelectType int mType; // 页面类型

    /**
     * 跳转到当前页
     *
     * @param context mContext
     * @param type    页面类型
     */
    public static void launch(Context context, @OrderSelectType int type) {
        Intent intent = new Intent(context, LogisticsOrderActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     * @param transId 转运公司Id
     */
    public static void launch(Context context, String transId) {
        Intent intent = new Intent(context, LogisticsOrderActivity.class);
        intent.putExtra(TransConstant.TYPE, OrderSelectType.TRANSPORT_COMMENT);
        intent.putExtra(TransConstant.ID, transId);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, OrderBriefModel obj) {
        Intent intent = new Intent(context, LogisticsOrderActivity.class);
        intent.putExtra(TransConstant.OBJECT, obj);
        intent.putExtra(TransConstant.TYPE, OrderSelectType.WITHDRAW);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "选择订单";
        mPage = 1;

        Intent intent = getIntent();

        if (null != intent) {
            if (intent.hasExtra(TransConstant.OBJECT)) {
                mOrderBriefModel = (OrderBriefModel) intent.getSerializableExtra(TransConstant.OBJECT);
            }
            if (intent.hasExtra(TransConstant.TYPE)) {
                mType = intent.getIntExtra(TransConstant.TYPE, WITHDRAW);
            }
            if (intent.hasExtra(TransConstant.ID)) {
                mTransId = intent.getStringExtra(TransConstant.ID);
            }
        }
        if (mType == WITHDRAW) {
            mWithdrawList = new ArrayList<>();
        } else {
            mTransList = new ArrayList<>();
        }
    }

    private void initView() {
        initTop();
        tvTitle.setText(R.string.logistics_order_title);

        mLvList = getView(R.id.lvHistory);
        mLvList.setPullLoadEnable(false);
        mLvList.setPullRefreshEnable(mType == WITHDRAW);
        mLvList.setAutoLoadEnable(mType == WITHDRAW);
        if (mType == WITHDRAW) {
            mWithdrawAdapter = new LogisticsOrderAdapter(mContext, mWithdrawList);
            if (null != mOrderBriefModel) {
                mWithdrawAdapter.mOrderBriefModel = mOrderBriefModel;
            }
            mLvList.setAdapter(mWithdrawAdapter);
        } else {
            mTransAdapter = new LogisticsOrderTransAdapter(mContext, mTransList);
            mLvList.setAdapter(mTransAdapter);
        }
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
        initError();
    }

    private void initEvent() {
        mLvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                initData();
            }
        });
        mLvList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            int    index  = position - mLvList.getHeaderViewsCount();
            if (index >= 0) {
                if (mType == WITHDRAW) {
                    OrderBriefModel value = mWithdrawList.get(index);
                    if (value == null)
                        return;
                    intent.putExtra(TransConstant.OBJECT, value);
                } else {
                    OrderModel value = mTransList.get(index);
                    if (value == null)
                        return;
                    intent.putExtra(TransConstant.OBJECT, value);
                }
                intent.putExtra(TransConstant.TYPE, "order");
                setResult(TransConstant.REFRESH, intent);
                finish();
            }
        });
        btnRefresh.setOnClickListener(v -> initData());
    }

    private void initData() {
        if (mType == WITHDRAW) {
            loadWithdrawOrderList();
        } else {
            loadTransportCommmentOrderList();
        }
    }

    /**
     * 转运评价 - 订单列表
     */
    private void loadTransportCommmentOrderList() {
        ForumApi.getInstance().userTransshipperOrdersListPost(mTransId, String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (tvTitle == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);

                    if (TextUtils.equals("0", response.getCode())) {
                        if (1 == mPage)
                            mTransList.clear();
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mTransList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        }
                        mTransAdapter.notifyDataSetChanged();
                    }
                    if (mTransList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                },
                error -> {
                    if (tvTitle == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    showErrorToast(error);
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(1);
                });


        /*ForumApi.getInstance().userOrdersListGet("4", String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (tvTitle == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);

                    if (TextUtils.equals("0", response.getCode())) {
                        if (1 == mPage)
                            mTransList.clear();
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mTransList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        }
                        mTransAdapter.notifyDataSetChanged();
                    }
                    if (mTransList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                },
                error -> {
                    if (tvTitle == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    showErrorToast(error);
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(1);
                });*/

        /*commandViewContext.asynQuery(MethodConstant.LOGISTICS_ORDER, commandViewContext.getEntity(), new IAsynServiceHandler<OrderObject>() {
            @Override
            public void onSuccess(OrderObject entity) throws Exception {

            }

            @Override
            public void onSuccessPage(PageResult<OrderObject> entity) throws Exception {
                //                mLvList.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.GONE);
                if (null != entity) {
                    if (null != entity.entityList && entity.entityList.size() > 0) {
                        mTransList.addAll(entity.entityList);
                        mTransAdapter.notifyDataSetChanged();
                    }
                }

                if (mWithdrawList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(0);
                    setErrorMessage("暂无有效订单");
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String error) {
                layoutProgress.setVisibility(View.GONE);
                if (error.equals("暂无订单")) {
                    if (mWithdrawList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                        setErrorMessage("暂无有效订单");
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }
            }
        });*/
    }

    /**
     * 提现 - 订单列表
     */
    private void loadWithdrawOrderList() {
        ForumApi.getInstance().userOrdersBriefsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mLvList == null)
                        return;
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mWithdrawList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mWithdrawList.addAll(response.getData().getRows());
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mWithdrawList.isEmpty()) {
                            //                            mMsv.showEmpty("暂无有效订单");
                            ll_common_error.setVisibility(View.VISIBLE);
                            setErrorType(0);
                            setErrorMessage("暂无有效订单");
                        }
                        mWithdrawAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }

                }, error -> {
                    if (mLvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    if (error.getMessage().equals("暂无订单")) {
                        if (mWithdrawList.isEmpty()) {
                            ll_common_error.setVisibility(View.VISIBLE);
                            setErrorType(0);
                            setErrorMessage("暂无有效订单");
                        } else {
                            ll_common_error.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
