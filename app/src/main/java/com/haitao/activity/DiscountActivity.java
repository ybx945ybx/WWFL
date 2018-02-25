package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.DealAdapter;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.db.CategoryDB;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.BaseObject;
import com.haitao.model.CategoryListObject;
import com.haitao.model.StoreObject;
import com.haitao.model.TagObject;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ReturnTopUtils;
import com.haitao.utils.doubleclick.DoubleClickUtils;
import com.haitao.utils.doubleclick.OnDoubleClickListener;
import com.haitao.view.DiscountFilterPopView;
import com.haitao.view.PopListView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.DealModel;

/**
 * 优惠折扣
 */
public class DiscountActivity extends BaseActivity implements View.OnClickListener, OnDoubleClickListener {
    private XListView            lvList;
    private ArrayList<DealModel> mList;
    private DealAdapter          mAdapter;
    private String title = "";
    private ViewGroup layoutTop;

    private ViewGroup   layoutProgress;
    //分类
    private ViewGroup   layoutCategory;
    private TextView    tvCategory;
    private PopListView categoryPopListView;
    private ArrayList<TagObject> dealCategoryList        = null;
    private int                  currentCategoryPosition = 0;

    private ViewGroup   layoutStore;
    private TextView    tvStore;
    private PopListView storePopListView;
    private ArrayList<TagObject> dealStoreList        = null;
    private int                  currentStorePosition = 0;

    private ViewGroup layoutFilter;
    private TextView  tvFilter;
    private View      filterView;
    private TextView  tvExpire, tvTransport, tvAlipay, tvReset, tvConfirm;
    private String expire = "0", transport = "0", alipay = "0", category = "";

    //筛选
    private DiscountFilterPopView discountFilterPopView;

    private TagObject   tagObject;
    private StoreObject storeObject;


    private int page = 1;

    protected IViewContext<CategoryListObject, IEntityService<CategoryListObject>> categoryViewContext = VF.<CategoryListObject>getDefault(CategoryListObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String title) {
        Intent intent = new Intent(context, DiscountActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, BaseObject tag) {
        Intent intent = new Intent(context, DiscountActivity.class);
        intent.putExtra(TransConstant.OBJECT, tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        TAG = "优惠列表";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        initError();
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.TITLE))
                title = getIntent().getStringExtra(TransConstant.TITLE);
            if (intent.hasExtra(TransConstant.OBJECT) && intent.getSerializableExtra(TransConstant.OBJECT) instanceof TagObject) {
                tagObject = (TagObject) intent.getSerializableExtra(TransConstant.OBJECT);
            } else if (intent.hasExtra(TransConstant.OBJECT) && intent.getSerializableExtra(TransConstant.OBJECT) instanceof StoreObject) {
                storeObject = (StoreObject) intent.getSerializableExtra(TransConstant.OBJECT);
            }
        }
        layoutTop = getView(R.id.layoutTop);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.discount_filter);
        lvList = getView(R.id.lvList);
        layoutCategory = getView(R.id.layoutCategory);
        tvCategory = getView(R.id.tvCategory);
        layoutStore = getView(R.id.layoutStore);
        tvStore = getView(R.id.tvStore);
        layoutFilter = getView(R.id.layoutFilter);
        tvFilter = getView(R.id.tvFilter);
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
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
        layoutCategory.setOnClickListener(this);
        layoutStore.setOnClickListener(this);
        layoutFilter.setOnClickListener(this);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0 && mList.size() > 0) {
                DealModel dealModel = mList.get(index);
                if (dealModel != null) {
                    DiscountDetailActivity.launch(mContext, dealModel.getDealId());
                }
            }
        });
        DoubleClickUtils.registerDoubleClickListener(layoutTop, this);
        tvRight.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (null != tagObject) {
            tvTitle.setText(tagObject.name);
        } else if (null != storeObject) {
            tvTitle.setText(storeObject.name);
        } else {
            tvTitle.setText("".equals(title) ? getResources().getString(R.string.discount_discount_title) : title);
        }
        mList = new ArrayList<DealModel>();
        mAdapter = new DealAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setPullLoadEnable(false);
        lvList.setAutoLoadEnable(true);
        dealCategoryList = new ArrayList<TagObject>();
        dealStoreList = new ArrayList<TagObject>();
        loadData();
        if (!TextUtils.isEmpty(title)) {
            loadCategory();
        }
    }

    private void loadData() {
        /*if(null != tagObject && tagObject.type.equals(CategoryConstant.TAG_TAG)){
            commandViewContext.getEntity().tag = tagObject.id;
        }*/
        if (null != tagObject && tagObject.type.equals(CategoryConstant.TAG_CATEGORY)) {
            category = tagObject.id;
            currentCategoryPosition = -1;
        }

        if (0 == currentCategoryPosition) {
            category = "";
        } else if (0 != currentCategoryPosition && dealCategoryList.size() > 0) {
            category = dealCategoryList.get(currentCategoryPosition).id;
        }
        /*if(null != storeObject){
            commandViewContext.getEntity().sid = storeObject.id;
            currentStorePosition = -1;
        }
        if(0 == currentStorePosition){
            commandViewContext.getEntity().sid = "";
        }else if(0 != currentStorePosition && dealStoreList.size() > 0){
            commandViewContext.getEntity().sid = dealStoreList.get(currentStorePosition).id;
        }
        commandViewContext.getEntity().alipay = alipay;
        commandViewContext.getEntity().transports = transport;
        commandViewContext.getEntity().expire = expire;*/

        ForumApi.getInstance().dealMostPopularIn24hDealsListGet(expire, transport, alipay, category, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            mList.clear();
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()) && mList.size() > 5);
                        }
                        mAdapter.notifyDataSetChanged();
                        if (1 == page)
                            lvList.setSelection(0);
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });


    }

    /**
     * 获取24小时人气排行的分类和商家
     */
    private void loadCategory() {
        categoryViewContext.getService().asynFunction(MethodConstant.HOT_DICS, categoryViewContext.getEntity(), new IAsynServiceHandler<CategoryListObject>() {
            @Override
            public void onSuccess(CategoryListObject entity) throws Exception {
                if (null != entity) {
                    if (null != entity.deal_category) {
                        dealCategoryList.clear();
                        dealCategoryList.addAll(entity.deal_category);
                        TagObject cateAllObject = new TagObject();
                        cateAllObject.text = getResources().getString(R.string.discount_all_category);
                        dealCategoryList.add(0, cateAllObject);
                    }
                    if (null != entity.store_dict) {
                        dealStoreList.clear();
                        dealStoreList.addAll(entity.store_dict);
                        TagObject cateAllObject = new TagObject();
                        cateAllObject.text = getResources().getString(R.string.discount_all_store);
                        dealStoreList.add(0, cateAllObject);
                    }
                }
            }

            @Override
            public void onSuccessPage(PageResult<CategoryListObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }


    @Override
    public void OnSingleClick(View v) {

    }

    @Override
    public void OnDoubleClick(View v) {
        lvList.setSelection(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                if (PopWindowUtils.isShown()) {
                    PopWindowUtils.dismiss();
                    return;
                }
                if (null == storeObject) {
                    if (null == dealStoreList || dealStoreList.isEmpty()) {
                        /*dealStoreList = CategoryDB.getList(String.valueOf(CategoryConstant.STORE_RECOMMEND));
                        TagObject cateAllObject = new TagObject();
                        cateAllObject.text = getResources().getString(R.string.discount_all_store);
                        dealStoreList.add(0,cateAllObject);*/
                    }
                }
                if (null == tagObject || !tagObject.type.equals(CategoryConstant.TAG_CATEGORY)) {
                    if (null == dealCategoryList || dealCategoryList.isEmpty()) {
                        dealCategoryList = CategoryDB.getList(String.valueOf(CategoryConstant.DEAL_CATEGORY));
                        TagObject cateAllObject = new TagObject();
                        cateAllObject.text = getResources().getString(R.string.discount_all_category);
                        dealCategoryList.add(0, cateAllObject);
                    }
                }

                if (null == discountFilterPopView) {
                    discountFilterPopView = new DiscountFilterPopView(mContext);
                    discountFilterPopView.setOnCallbackLitener(new DiscountFilterPopView.OnCallbackLitener() {
                        @Override
                        public void onConfirm(String expireStr, String transportStr, String alipayStr, int catPosition, int storePosition) {
                            expire = expireStr;
                            transport = transportStr;
                            alipay = alipayStr;
                            if (null != dealCategoryList && dealCategoryList.size() > 0) {
                                currentCategoryPosition = catPosition;
                            }
                            if (null != dealStoreList && dealStoreList.size() > 0) {
                                currentStorePosition = storePosition;
                            }
                            // loadSearchStoreList();
                            PopWindowUtils.dismiss();
                        }

                        @Override
                        public void onChange(String expireStr, String transportStr, String alipayStr, int catPosition, int storePosition) {
                            expire = expireStr;
                            transport = transportStr;
                            alipay = alipayStr;
                            if (null != dealCategoryList && dealCategoryList.size() > 0) {
                                currentCategoryPosition = catPosition;
                            }
                            if (null != dealStoreList && dealStoreList.size() > 0) {
                                currentStorePosition = storePosition;
                            }
                        }
                    });
                    /*if(null == storeObject) {
                        discountFilterPopView.setStoreData(dealStoreList);
                    }*/
                    if (null == tagObject || !tagObject.type.equals(CategoryConstant.TAG_CATEGORY)) {
                        discountFilterPopView.setCategoryData(dealCategoryList);
                    }
                    discountFilterPopView.setOnClickListener(v14 -> {
                        /*if(!expire.equals(commandViewContext.getEntity().expire) || !alipay.equals(commandViewContext.getEntity().alipay) || !transport.equals(commandViewContext.getEntity().alipay)){
                            loadSearchStoreList();
                        }else if(null != dealCategoryList && dealCategoryList.size() > 0 && !dealCategoryList.get(currentCategoryPosition).id.equals(commandViewContext.getEntity().cate)){
                            loadSearchStoreList();
                        }else if(null != dealStoreList && dealStoreList.size() > 0 && !dealStoreList.get(currentStorePosition).id.equals(commandViewContext.getEntity().sid)){
                            loadSearchStoreList();
                        }*/
                        PopWindowUtils.dismiss();
                    });
                }
                PopWindowUtils.show(mContext, v, discountFilterPopView);
                PopWindowUtils.setOnDismissListener(() -> {
                    tvRight.setText(R.string.discount_filter);
                    /*if(!expire.equals(commandViewContext.getEntity().expire) || !alipay.equals(commandViewContext.getEntity().alipay) || !transport.equals(commandViewContext.getEntity().alipay)){
                        lvList.autoRefresh();
                    }else if(null != dealCategoryList && dealCategoryList.size() > 0 && !dealCategoryList.get(currentCategoryPosition).id.equals(commandViewContext.getEntity().cate)){
                        lvList.autoRefresh();
                    }else if(null != dealStoreList && dealStoreList.size() > 0 && !dealStoreList.get(currentStorePosition).id.equals(commandViewContext.getEntity().sid)){
                        lvList.autoRefresh();
                    }*/
                    lvList.autoRefresh();
                });
                tvRight.setText(R.string.discount_filter_up);
                break;
            case R.id.layoutCategory:
                tvCategory.setSelected(true);
                if (null == dealCategoryList || dealCategoryList.isEmpty()) {
                    dealCategoryList = CategoryDB.getList(String.valueOf(CategoryConstant.DEAL_CATEGORY));
                    TagObject cateAllObject = new TagObject();
                    cateAllObject.text = getResources().getString(R.string.discount_all_category);
                    dealCategoryList.add(0, cateAllObject);
                }
                if (null == categoryPopListView) {
                    categoryPopListView = new PopListView(mContext);
                    categoryPopListView.setData(dealCategoryList);
                    categoryPopListView.setOnItemClickLitener(position -> {
                        currentCategoryPosition = position;
                        categoryPopListView.mAdapter.currentPosition = position;
                        tvCategory.setText(dealCategoryList.get(position).text);
                        PopWindowUtils.dismiss();
                        loadData();
                    });
                    categoryPopListView.setOnClickListener(v1 -> PopWindowUtils.dismiss());
                }
                PopWindowUtils.show(mContext, v, categoryPopListView);
                PopWindowUtils.setOnDismissListener(() -> tvCategory.setSelected(false));
                break;
            case R.id.layoutStore:
                tvStore.setSelected(true);
                if (null == dealStoreList || dealStoreList.isEmpty()) {
                    dealStoreList = CategoryDB.getList(String.valueOf(CategoryConstant.STORE_RECOMMEND));
                    TagObject cateAllObject = new TagObject();
                    cateAllObject.text = getResources().getString(R.string.discount_all_store);
                    dealStoreList.add(0, cateAllObject);
                }
                if (null == storePopListView) {
                    storePopListView = new PopListView(mContext);
                    storePopListView.setData(dealStoreList);
                    storePopListView.setOnItemClickLitener(position -> {
                        currentStorePosition = position;
                        storePopListView.mAdapter.currentPosition = position;
                        tvStore.setText(dealStoreList.get(position).text);
                        PopWindowUtils.dismiss();
                        loadData();
                    });
                    storePopListView.setOnClickListener(v12 -> PopWindowUtils.dismiss());
                }
                PopWindowUtils.show(mContext, v, storePopListView);
                PopWindowUtils.setOnDismissListener(() -> tvStore.setSelected(false));
                break;
            case R.id.layoutFilter:
                tvFilter.setSelected(true);
                if (null == filterView) {
                    filterView = View.inflate(mContext, R.layout.layout_pop_deal_filter, null);
                    tvExpire = getView(filterView, R.id.tvExpire);
                    tvTransport = getView(filterView, R.id.tvTransport);
                    tvAlipay = getView(filterView, R.id.tvAlipay);
                    tvReset = getView(filterView, R.id.tvReset);
                    tvConfirm = getView(filterView, R.id.tvConfirm);
                    tvExpire.setOnClickListener(this);
                    tvTransport.setOnClickListener(this);
                    tvAlipay.setOnClickListener(this);
                    filterView.setOnClickListener(v13 -> {
                        loadData();
                        PopWindowUtils.dismiss();
                    });
                    tvReset.setOnClickListener(this);
                    tvConfirm.setOnClickListener(this);
                }
                PopWindowUtils.show(mContext, v, filterView);
                PopWindowUtils.setOnDismissListener(() -> tvFilter.setSelected(false));
                break;
            case R.id.tvTransport:
                tvTransport.setSelected(!tvTransport.isSelected());
                transport = tvTransport.isSelected() ? "1" : "0";
                break;
            case R.id.tvExpire:
                tvExpire.setSelected(!tvExpire.isSelected());
                expire = tvExpire.isSelected() ? "1" : "0";
                break;
            case R.id.tvAlipay:
                tvAlipay.setSelected(!tvAlipay.isSelected());
                alipay = tvAlipay.isSelected() ? "1" : "0";
                break;
            case R.id.tvReset:
                tvTransport.setSelected(false);
                tvAlipay.setSelected(false);
                tvExpire.setSelected(false);
                transport = expire = alipay = "0";
                break;
            case R.id.tvConfirm:
                loadData();
                PopWindowUtils.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReturnTopUtils.dismiss();
    }
}
