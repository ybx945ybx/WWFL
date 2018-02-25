package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.adapter.StoreParentAdapter;
import com.haitao.adapter.StoreSuperAdapter;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.HtApplication;
import com.haitao.db.CategoryDB;
import com.haitao.db.StoreDB;
import com.haitao.db.v2.StoreModel;
import com.haitao.framework.http.okhttp.callback.ResultCallback;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.framework.utils.HTTPUtil;
import com.haitao.imp.URILocatorHelper;
import com.haitao.imp.VF;
import com.haitao.model.StoreFilterObject;
import com.haitao.model.StoreObject;
import com.haitao.model.StoreResponseObject;
import com.haitao.model.TagObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.doubleclick.DoubleClickUtils;
import com.haitao.utils.doubleclick.OnDoubleClickListener;
import com.haitao.view.ClearEditText;
import com.haitao.view.CustomImageView;
import com.haitao.view.FullListView;
import com.haitao.view.StoreFilterPopView;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

/**
 * 返利商家
 */
public class StoreFilterActivity extends BaseActivity implements View.OnClickListener, OnDoubleClickListener {
    private ExpandableListView lvList;

    private ViewGroup layoutTop;

    private ArrayMap<String, ArrayList<StoreObject>> storeFilters;


    private ArrayList<StoreFilterObject> filterList;
    private StoreParentAdapter           filterAdapter;

    //筛选
    //分类
    private ArrayList<TagObject> dealCategoryList        = null;
    private int                  currentCategoryPosition = 0;

    private ArrayList<TagObject> countryList    = null;
    private int                  currentCountry = 0;

    private String superRebate = "", directMail = "", transport = "", alipay = "";
    private IViewContext<StoreFilterObject, IEntityService<StoreFilterObject>> storeContext = VF.<StoreFilterObject>getDefault(StoreFilterObject.class);

    private StoreFilterPopView storeFilterPopView;

    //顶部
    private TextView      tvCancle;
    private ClearEditText etSearch;

    private ViewGroup              layoutSuperRebate;
    private ArrayList<StoreObject> superList;
    private StoreSuperAdapter      superAdapter;
    private FullListView           gvSuperRebate;

    private ViewGroup layoutProgress;


    View header = null;

    private String search = "";
    private CustomImageView mImgActivityFab; // 全局活动入口

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, StoreFilterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_filter);
        TAG = "全部商家";
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        layoutTop = getView(R.id.layoutTop);
        mImgActivityFab = getView(R.id.img_event);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.discount_filter);
        lvList = getView(R.id.lvList);
        header = View.inflate(mContext, R.layout.layout_store_header, null);
        header.setVisibility(View.GONE);
        lvList.addHeaderView(header);
        layoutSuperRebate = getView(header, R.id.layoutSuperRebate);
        gvSuperRebate = getView(header, R.id.gvSuperRebate);
        tvCancle = getView(R.id.tvCancle);
        etSearch = getView(R.id.etSearch);
        layoutProgress = getView(R.id.layoutProgress);
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
        tvRight.setOnClickListener(this);
        DoubleClickUtils.registerDoubleClickListener(layoutTop, this);
        lvList.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            StoreMoreActivity.launch(mContext, filterList.get(groupPosition).char_name);
            return true;
        });
        gvSuperRebate.setOnItemClickListener((parent, view, position, id) -> StoreDetailActivity.launch(mContext, superList.get(position).id));
        lvList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            StoreModel storeModel = filterList.get(groupPosition).list.get(childPosition);
            StoreDetailActivity.launch(mContext, storeModel.getStore_id());
            return false;
        });
        tvCancle.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = s.toString().trim();
                if (!TextUtils.isEmpty(search)) {
                    tvCancle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((null == filterList || filterList.isEmpty()) && TextUtils.isEmpty(search))
                    return;
                loadData();
            }
        });
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);

                }
                loadData();

            }
            return false;
        });
        etSearch.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                etSearch.requestFocus();
                tvCancle.setVisibility(View.VISIBLE);
                lvList.setSelection(0);
                layoutSuperRebate.setVisibility(View.GONE);
            }
            return false;
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvTitle.setText(R.string.discount_all_store);
        filterList = new ArrayList<StoreFilterObject>();
        filterAdapter = new StoreParentAdapter(mContext, filterList);
        storeFilters = new ArrayMap<String, ArrayList<StoreObject>>();


        String filterChar = (String) SPUtils.get(mContext, SPConstant.STORE_CHAR, "");
        lvList.setAdapter(filterAdapter);

        superList = new ArrayList<StoreObject>();
        superAdapter = new StoreSuperAdapter(mContext, superList);
        gvSuperRebate.setAdapter(superAdapter);


        new UpdateTask().execute();


    }

    @Override
    public void OnSingleClick(View v) {

    }

    @Override
    public void OnDoubleClick(View v) {
        lvList.setSelection(0);
    }

    public class UpdateTask extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
            layoutProgress.setVisibility(View.VISIBLE);
            // ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        }

        protected String doInBackground(String... params) {
            filterList.addAll(StoreDB.getListGroup());
            return "itcast";
        }

        protected void onPostExecute(String result) {
            ProgressDialogUtils.dismiss();
            filterAdapter.notifyDataSetChanged();
            String superStr = (String) SPUtils.get(mContext, SPConstant.STORE_SUPER_REBATE, "");
            if (!TextUtils.isEmpty(superStr)) {
                superList.addAll(JSON.parseArray(superStr, StoreObject.class));
                layoutSuperRebate.setVisibility(View.VISIBLE);
                superAdapter.notifyDataSetChanged();
            } else {
                layoutSuperRebate.setVisibility(View.GONE);
            }
            if (null == filterList || filterList.size() <= 0) {
                getData();
            } else {
                header.setVisibility(View.VISIBLE);
                filterAdapter.notifyDataSetChanged();
                for (int i = 0; i < filterList.size(); i++) {
                    lvList.expandGroup(i);
                }
            }
            layoutProgress.setVisibility(View.GONE);
        }

        protected void onProgressUpdate(Integer... values) {
        }
    }


    /**
     * 从服务器获取数据
     */
    private void getData() {
        layoutProgress.setVisibility(View.VISIBLE);
        HTTPUtil.postAsyn(URILocatorHelper.getUrlBase().getBaseURI() + "/" + MethodConstant.STORE_LIST, MethodConstant.STORE_LIST, null, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                // FLog.e("=====response",response);
                if (!TextUtils.isEmpty(response)) {
                    StoreResponseObject storeResponseObject = JSON.parseObject(response, StoreResponseObject.class);
                    if ("0".equals(storeResponseObject.code)) {
                        StoreFilterObject entity = storeResponseObject.data;
                        if (null != entity) {
                            if (null != entity.list && entity.list.size() > 0) {
                                header.setVisibility(View.VISIBLE);
                                Logger.d("be going to save");
                                StoreDB.add(entity.list);
                                filterList.addAll(StoreDB.getListGroup());
                                filterAdapter.notifyDataSetChanged();
                                for (int i = 0; i < filterList.size(); i++) {
                                    lvList.expandGroup(i);
                                }
                            } else {
                                Logger.d("entity.list is null");
                            }
                            if (null != entity.char_list) {
                                SPUtils.put(mContext, SPConstant.STORE_CHAR, JSON.toJSONString(entity.char_list));
                            }
                            if (null != entity.version) {
                                SPUtils.put(mContext, SPConstant.STORE_VERSION, entity.version);
                            }

                            if (null != entity.super_rebate) {
                                SPUtils.put(mContext, SPConstant.STORE_SUPER_REBATE, JSON.toJSONString(entity.super_rebate));
                                superList.addAll(entity.super_rebate);
                                superAdapter.notifyDataSetChanged();
                                layoutSuperRebate.setVisibility(View.VISIBLE);
                            } else {
                                layoutSuperRebate.setVisibility(View.GONE);
                            }
                        }
                        layoutProgress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancle:
                if (TextUtils.isEmpty(search)) {

                } else {
                    etSearch.setText("");
                    loadData();
                }
                etSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);

                }
                tvCancle.setVisibility(View.GONE);
                if (superList.size() > 0) {
                    layoutSuperRebate.setVisibility(View.VISIBLE);
                }
                filterAdapter.isSearch = false;
                filterAdapter.notifyDataSetChanged();
                break;
            case R.id.tvRight:
                if (PopWindowUtils.isShown()) {
                    PopWindowUtils.dismiss();
                    return;
                }
                if (null == dealCategoryList || dealCategoryList.isEmpty()) {
                    dealCategoryList = CategoryDB.getList(String.valueOf(CategoryConstant.STORE_CATEGORY));
                    TagObject cateAllObject = new TagObject();
                    cateAllObject.text = getResources().getString(R.string.discount_all_category);
                    dealCategoryList.add(0, cateAllObject);
                }
                if (null == countryList || countryList.isEmpty()) {
                    countryList = CategoryDB.getList(String.valueOf(CategoryConstant.STORE_COUNTRY));
                    TagObject cateAllObject = new TagObject();
                    cateAllObject.text = getResources().getString(R.string.store_all_country);
                    countryList.add(0, cateAllObject);
                }

                if (null == storeFilterPopView) {
                    storeFilterPopView = new StoreFilterPopView(mContext);
                    storeFilterPopView.setOnCallbackLitener(new StoreFilterPopView.OnCallbackLitener() {
                        @Override
                        public void onConfirm(String expireStr, String transportStr, String alipayStr, String superRebateStr, int catPosition, int storePosition) {
                            directMail = expireStr;
                            transport = transportStr;
                            alipay = alipayStr;
                            superRebate = superRebateStr;
                            if (null != dealCategoryList && dealCategoryList.size() > 0) {
                                currentCategoryPosition = catPosition;
                            }
                            if (null != countryList && countryList.size() > 0) {
                                currentCountry = storePosition;
                            }
                            PopWindowUtils.dismiss();
                        }

                        @Override
                        public void onChange(String expireStr, String transportStr, String alipayStr, String superRebateStr, int catPosition, int storePosition) {
                            directMail = expireStr;
                            transport = transportStr;
                            alipay = alipayStr;
                            superRebate = superRebateStr;
                            if (null != dealCategoryList && dealCategoryList.size() > 0) {
                                currentCategoryPosition = catPosition;
                            }
                            if (null != countryList && countryList.size() > 0) {
                                currentCountry = storePosition;
                            }
                        }
                    });
                    storeFilterPopView.setStoreData(countryList);
                    storeFilterPopView.setCategoryData(dealCategoryList);
                    storeFilterPopView.setOnClickListener(v1 -> PopWindowUtils.dismiss());
                }
                PopWindowUtils.show(mContext, v, storeFilterPopView);
                PopWindowUtils.setOnDismissListener(() -> {
                    tvRight.setText(R.string.discount_filter);
                    loadData();
                });
                tvRight.setText(R.string.discount_filter_up);
                break;
            default:
                break;
        }
    }

    private void loadData() {
        StringBuffer      sql    = new StringBuffer();
        ArrayList<String> params = new ArrayList<String>();
        sql.append("1 = 1");
        if (!TextUtils.isEmpty(search)) {
            sql.append(" and ");
            sql.append("name");
            sql.append(" like ?");
            params.add("%" + search + "%");
        }

        if (0 != currentCountry) {
            sql.append(" and ");
            sql.append("country_id");
            sql.append(" = ?");
            params.add(countryList.get(currentCountry).id);
        }
        if ("1".equals(superRebate)) {
            sql.append(" and ");
            sql.append("is_super_rebate");
            sql.append(" = ?");
            params.add(superRebate);
        }
        if ("1".equals(alipay)) {
            sql.append(" and ");
            sql.append("is_alipay");
            sql.append(" = ?");
            params.add(alipay);
        }
        if ("1".equals(transport)) {
            sql.append(" and ");
            sql.append("is_transports");
            sql.append(" = ?");
            params.add(transport);
        }
        if ("1".equals(directMail)) {
            sql.append(" and ");
            sql.append("is_direct_mail");
            sql.append(" = ?");
            params.add(directMail);
        }
        if (0 != currentCategoryPosition) {
            sql.append(" and ");
            sql.append("category");
            sql.append(" like ?");
            filterAdapter.category = dealCategoryList.get(currentCategoryPosition);
            params.add("%" + dealCategoryList.get(currentCategoryPosition).text + "%");
        } else {
            filterAdapter.category = null;
        }
        Logger.d(sql.toString());
        //String[] filter = params.toArray(new String[params.size()]);
        //String sqlStr = sql.toString().replaceFirst(" and ","");
        filterList.clear();
        filterAdapter.isSearch = !TextUtils.isEmpty(search);
        filterList.addAll(StoreDB.getListGroup(sql.toString(), params));
        filterAdapter.notifyDataSetChanged();
        for (int i = 0; i < filterList.size(); i++) {
            lvList.expandGroup(i);
        }
        lvList.setSelection(filterList.size() > 0 ? 1 : 0);
    }


}
