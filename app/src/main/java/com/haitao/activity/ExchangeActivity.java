package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.ExchangeAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.ExchangeObject;
import com.haitao.model.TagObject;
import com.haitao.view.HtHeadView;
import com.haitao.view.OrderFilterPopView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 海淘试用
 */
public class ExchangeActivity extends BaseActivity {
    private XListView                 lvList;
    private ArrayList<ExchangeObject> mList;
    private ExchangeAdapter           mAdapter;
    private int mPage = 1;

    private OrderFilterPopView categoryPopListView;
    private ArrayList<TagObject> dealCategoryList        = null;
    private int                  currentCategoryPosition = 0;
    private String               typeId                  = "0";

    protected IViewContext<ExchangeObject, IEntityService<ExchangeObject>> commandViewContext = VF.<ExchangeObject>getDefault(ExchangeObject.class);
    private HtHeadView mHtHeadView;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, ExchangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "金币兑换";
    }

    /**
     * 初始化视图
     */
    private void initView() {
//        initTop();
        initError();
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText(getString(R.string.exchange_title));
        /*tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.discount_filter);
        Drawable filterDrawable;
        filterDrawable = getResources().getDrawable(R.drawable.selector_filter_arrow);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        filterDrawable.setBounds(0, 0, filterDrawable.getMinimumWidth(), filterDrawable.getMinimumHeight());
        tvRight.setCompoundDrawables(null, null, filterDrawable, null); //设置左图标
        tvRight.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px6));*/
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                getData();
            }
        });
        //        tvRight.setOnClickListener(this);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                ExchangeObject obj = mList.get(index);
                if (obj != null) {
                    ExchangeDetailActivity.launch(mContext, obj);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<ExchangeObject>();
        mAdapter = new ExchangeAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        getData();
    }

    private void getData() {
        commandViewContext.getEntity().page = String.valueOf(mPage);
        commandViewContext.getEntity().lpp = String.valueOf(20);
        commandViewContext.getEntity().type = typeId;
        commandViewContext.getService().asynFunction(MethodConstant.EXCHANGE_LIST, commandViewContext.getEntity(), new IAsynServiceHandler<ExchangeObject>() {
            @Override
            public void onSuccess(ExchangeObject entity) throws Exception {
                lvList.stopRefresh();
                lvList.stopLoadMore();
                if (null != entity) {
                    if (1 == mPage) {
                        mAdapter.isOver = false;
                        mList.clear();
                        lvList.setRefreshTime();
                    }
                    if (entity._pagecount <= mPage) {
                        lvList.setPullLoadEnable(false);
                    } else {
                        lvList.setPullLoadEnable(true);
                    }
                    if (null != entity._data) {
                        if (null != entity._data.other && entity._data.other.size() > 0) {
                            mList.addAll(0, entity._data.other);
                        }
                        if (null != entity._data.end && entity._data.end.size() > 0) {
                            mList.addAll(entity._data.end);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (mList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(0);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSuccessPage(PageResult<ExchangeObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                lvList.stopRefresh();
                lvList.stopLoadMore();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
