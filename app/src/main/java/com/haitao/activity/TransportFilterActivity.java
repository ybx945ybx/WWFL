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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.adapter.TransportAdapter;
import com.haitao.adapter.TransportParentAdapter;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.HtApplication;
import com.haitao.db.CategoryDB;
import com.haitao.db.TransportDB;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.TagObject;
import com.haitao.model.TransportFilterObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.doubleclick.DoubleClickUtils;
import com.haitao.utils.doubleclick.OnDoubleClickListener;
import com.haitao.view.ClearEditText;
import com.haitao.view.CustomImageView;
import com.haitao.view.FullListView;
import com.haitao.view.TransportFilterPopView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * 海淘转运
 */
public class TransportFilterActivity extends BaseActivity implements View.OnClickListener, OnDoubleClickListener {
    private ExpandableListView lvList;

    private ViewGroup layoutTop;

    private ArrayMap<String, ArrayList<LogisticsCompanyObject>> storeFilters;


    private ArrayList<TransportFilterObject> filterList;
    private TransportParentAdapter           filterAdapter;

    //筛选
    private ArrayList<TagObject>                               countryList    = null;
    private int                                                currentCountry = 0;
    private IViewContext<TagObject, IEntityService<TagObject>> countyContext  = VF.<TagObject>getDefault(TagObject.class);

    private IViewContext<TransportFilterObject, IEntityService<TransportFilterObject>> storeContext = VF.<TransportFilterObject>getDefault(TransportFilterObject.class);


    private TransportFilterPopView storeFilterPopView;

    //顶部
    private TextView      tvCancle;
    private ClearEditText etSearch;

    private ViewGroup                         layoutSuperRebate;
    private TextView                          tvSectionName;
    private ArrayList<LogisticsCompanyObject> superList;
    private TransportAdapter                  superAdapter;
    private FullListView                      gvSuperRebate;

    private TextView tvError;


    View header = null;

    private String search = "";
    private CustomImageView mImgActivityFab; // 活动入口

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, TransportFilterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_filter);
        TAG = "海淘转运";
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
        /*tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.discount_filter);*/
        lvList = getView(R.id.lvList);
        mImgActivityFab = getView(R.id.img_event);
        header = View.inflate(mContext, R.layout.layout_store_header, null);
        header.setVisibility(View.GONE);
        lvList.addHeaderView(header);
        layoutSuperRebate = getView(header, R.id.layoutSuperRebate);
        gvSuperRebate = getView(header, R.id.gvSuperRebate);
        tvSectionName = getView(header, R.id.tvSectionName);
        tvError = getView(R.id.tvError);
        tvCancle = getView(R.id.tvCancle);
        etSearch = getView(R.id.etSearch);
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
        gvSuperRebate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransportDetailActivity.launch(mContext, superList.get(position).id);
            }
        });
        lvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TransportDetailActivity.launch(mContext, filterList.get(groupPosition).list.get(childPosition).getTransport_id());
                return false;
            }
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
        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    etSearch.requestFocus();
                    tvCancle.setVisibility(View.VISIBLE);
                    lvList.setSelection(0);
                    layoutSuperRebate.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvTitle.setText("海淘转运");
        filterList = new ArrayList<TransportFilterObject>();
        filterAdapter = new TransportParentAdapter(mContext, filterList);
        storeFilters = new ArrayMap<String, ArrayList<LogisticsCompanyObject>>();


        lvList.setAdapter(filterAdapter);
        tvSectionName.setText("热门转运");
        etSearch.setHint("搜索转运");
        superList = new ArrayList<LogisticsCompanyObject>();
        superAdapter = new TransportAdapter(mContext, superList);
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
            ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        }

        protected String doInBackground(String... params) {
            getData();
            countyContext.asynQuery(MethodConstant.TRANSPORT_COUNTRY, countyContext.getEntity(), new IAsynServiceHandler<TagObject>() {
                @Override
                public void onSuccess(TagObject entity) throws Exception {
                    Logger.d("======== TagObject ======");
                }

                @Override
                public void onSuccessPage(PageResult<TagObject> entity) throws Exception {
                    if (null != entity && null != entity.entityList) {
                        if (null == countryList)
                            countryList = new ArrayList<TagObject>();
                        countryList.clear();
                        countryList.addAll(entity.entityList);
                        TagObject cateAllObject = new TagObject();
                        cateAllObject.text = getResources().getString(R.string.store_all_country);
                        countryList.add(0, cateAllObject);
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
            return "itcast";
        }

        protected void onPostExecute(String result) {
            /*ProgressDialogUtils.dismiss();
            String superStr = (String) SPUtils.get(mContext,SPConstant.TRANSPORT_HOT,"");
            if(!TextUtils.isEmpty(superStr)) {
                superList.addAll(JSON.parseArray(superStr, LogisticsCompanyObject.class));
                superAdapter.notifyDataSetChanged();
            }
            loadData();
            header.setVisibility(View.VISIBLE);
            filterAdapter.notifyDataSetChanged();
            for (int i = 0; i < filterList.size(); i++) {
                lvList.expandGroup(i);
            }*/
        }

        protected void onProgressUpdate(Integer... values) {
        }
    }


    /**
     * 从服务器获取数据
     */
    private void getData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        storeContext.getService().asynFunction(MethodConstant.TRANSPORT_LIST, storeContext.getEntity(), new IAsynServiceHandler<TransportFilterObject>() {
            @Override
            public void onSuccess(TransportFilterObject entity) throws Exception {
                if (null != entity) {

                    if (null != entity.hot) {
                        superList.clear();
                        SPUtils.put(mContext, SPConstant.TRANSPORT_HOT, JSON.toJSONString(entity.hot));
                        superList.addAll(entity.hot);
                        superAdapter.notifyDataSetChanged();

                    }

                    if (null != entity.list && entity.list.size() > 0) {
                        header.setVisibility(View.VISIBLE);
                        TransportDB.add(entity.list);
                        filterList.addAll(TransportDB.getListGroup());
                        filterAdapter.notifyDataSetChanged();
                        for (int i = 0; i < filterList.size(); i++) {
                            lvList.expandGroup(i);
                        }
                    } else {
                        Logger.d("======== entity.list is null ======");

                    }
                }
                ProgressDialogUtils.dismiss();
            }

            @Override
            public void onSuccessPage(PageResult<TransportFilterObject> entity) throws Exception {
            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
                ToastUtils.show(mContext, error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancle:
                if (superList.size() > 0) {
                    layoutSuperRebate.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                }
                tvCancle.setVisibility(View.GONE);
                if (TextUtils.isEmpty(search)) {
                    loadData();
                } else {
                    etSearch.setText("");
                    search = "";
                    loadData();
                }
                etSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);

                }
                break;
            case R.id.tvRight:
                if (PopWindowUtils.isShown()) {
                    PopWindowUtils.dismiss();
                    return;
                }
                if (null == countryList || countryList.isEmpty()) {
                    countryList = CategoryDB.getList(String.valueOf(CategoryConstant.STORE_COUNTRY));
                    TagObject cateAllObject = new TagObject();
                    cateAllObject.text = getResources().getString(R.string.store_all_country);
                    countryList.add(0, cateAllObject);
                }

                if (null == storeFilterPopView) {
                    storeFilterPopView = new TransportFilterPopView(mContext);
                    storeFilterPopView.setOnCallbackLitener(countryPosition -> {
                        if (null != countryList && countryList.size() > 0) {
                            currentCountry = countryPosition;
                            PopWindowUtils.dismiss();
                        }
                    });
                    storeFilterPopView.setStoreData(countryList);
                    storeFilterPopView.setOnClickListener(v1 -> PopWindowUtils.dismiss());
                }
                PopWindowUtils.show(mContext, v, storeFilterPopView);
                PopWindowUtils.setOnDismissListener(() -> {
                    tvRight.setText(R.string.discount_filter);
                    layoutSuperRebate.setVisibility(View.GONE);
                    loadData();

                });
                tvRight.setText(R.string.discount_filter_up);
                break;
            default:
                break;
        }
    }

    private void loadData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        StringBuffer      sql    = new StringBuffer();
        ArrayList<String> params = new ArrayList<String>();
        if (!TextUtils.isEmpty(search)) {
            sql.append("name");
            sql.append(" like ?");
            params.add("%" + search + "%");
            if (0 == currentCountry) {
                sql.append(" or ");
                sql.append("character");
                sql.append(" = ?");
                params.add(search.toUpperCase());
            }
        } else {
            sql.append("1 = 1");
        }


        filterAdapter.isSearch = !TextUtils.isEmpty(search) || 0 != currentCountry;

        if (0 != currentCountry) {
            sql.append(" and ");
            sql.append("country_id");
            sql.append(" = ?");
            params.add(countryList.get(currentCountry).id);
        }
        Logger.d(sql.toString());
        filterList.clear();
        filterList.addAll(TransportDB.getListGroup(sql.toString(), params));
        filterAdapter.notifyDataSetChanged();
        for (int i = 0; i < filterList.size(); i++) {
            lvList.expandGroup(i);
        }

        if (filterList.size() <= 0 && layoutSuperRebate.getVisibility() != View.VISIBLE) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("没有找到相关转运");
        } else {
            tvError.setVisibility(View.GONE);
        }
        if (filterList.size() > 0) {
            lvList.setSelection(tvCancle.getVisibility() == View.VISIBLE ? 0 : 1);
        }
        ProgressDialogUtils.dismiss();
    }


}
