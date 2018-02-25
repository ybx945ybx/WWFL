package com.haitao.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.LogisticsCompanyAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.db.TransferDB;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.view.ClearEditText;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 物流转运公司
 */

public class LogisticsCompanyActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText etSearch;
    private TextView      tvCancle;

    private XListView                         lvList;
    private ArrayList<LogisticsCompanyObject> mList;
    private LogisticsCompanyAdapter           mAdapter;

    private LogisticsCompanyObject companyObject;
    protected IViewContext<LogisticsCompanyObject, IEntityService<LogisticsCompanyObject>> commandViewContext = VF.<LogisticsCompanyObject>getDefault(LogisticsCompanyObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, LogisticsCompanyActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, LogisticsCompanyObject obj) {
        Intent intent = new Intent(context, LogisticsCompanyActivity.class);
        intent.putExtra(TransConstant.OBJECT, obj);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_company);
        TAG = "物流查询-转运公司";
        mContext = LogisticsCompanyActivity.this;
        if (null != getIntent() && getIntent().hasExtra(TransConstant.OBJECT)) {
            companyObject = (LogisticsCompanyObject) getIntent().getSerializableExtra(TransConstant.OBJECT);
        }
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initView() {
        initTop();
        lvList = getView(R.id.lvList);
        lvList.setPullLoadEnable(false);
        lvList.setPullRefreshEnable(false);
        lvList.setAutoLoadEnable(true);
        tvTitle.setText(R.string.logistics_company_title);
        etSearch = getView(R.id.etSearch);
        tvCancle = getView(R.id.tvCancle);
        tvCancle.setVisibility(View.GONE);
    }

    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                LogisticsCompanyObject value = mList.get(index);
                if (value != null) {
                    Intent intent = new Intent();
                    intent.putExtra(TransConstant.OBJECT, value);
                    intent.putExtra(TransConstant.TYPE, "transfer");
                    setResult(TransConstant.REFRESH, intent);
                    finish();
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mList.clear();
                mList.addAll(TransferDB.getList(s.toString().trim()));
                mAdapter.notifyDataSetChanged();
                tvCancle.setVisibility(TextUtils.isEmpty(s.toString().trim()) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvCancle.setOnClickListener(this);
    }

    private void initData() {
        mList = new ArrayList<LogisticsCompanyObject>();
        mAdapter = new LogisticsCompanyAdapter(mContext, mList);
        if (null != companyObject) {
            mAdapter.companyObject = companyObject;
        }
        lvList.setAdapter(mAdapter);
        mList.addAll(TransferDB.getList(""));
        if (mList.isEmpty()) {
            loadData();
        }
    }

    private void loadData() {
        commandViewContext.asynQuery(MethodConstant.TRANSFER_COMPANY, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsCompanyObject>() {
            @Override
            public void onSuccess(LogisticsCompanyObject entity) throws Exception {
            }

            @Override
            public void onSuccessPage(PageResult<LogisticsCompanyObject> entity) throws Exception {
                if (null == entity || null == entity.entityList)
                    return;
                TransferDB.add(entity.entityList);
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancle:
                etSearch.setText("");
                break;
        }
    }
}
