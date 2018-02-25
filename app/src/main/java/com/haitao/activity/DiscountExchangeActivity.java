package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.adapter.DiscountExchangeAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.swagger.client.model.CurrenciesIfModelData;

/**
 * 汇率换算 - 选择币种
 */
public class DiscountExchangeActivity extends BaseActivity {

    private MultipleStatusView               msv;
    private XListView                        lvList;
    private ArrayList<CurrenciesIfModelData> mList;
    private DiscountExchangeAdapter          mAdapter;

    private int    changePosition = 0;
    private String currency_abbr  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_exchange);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "选择币种";
        mList = new ArrayList<>();
        if (null != getIntent()) {
            if (getIntent().hasExtra(TransConstant.OBJECT)) {
                currency_abbr = getIntent().getStringExtra(TransConstant.OBJECT);
            }
            if (getIntent().hasExtra("position")) {
                changePosition = getIntent().getIntExtra("position", 0);
            }
        }
    }

    private void initView() {
        msv = getView(R.id.msv);
        lvList = getView(R.id.content_view);
        lvList.setPullLoadEnable(false);
        lvList.setPullRefreshEnable(false);
        lvList.setAutoLoadEnable(false);
        lvList.setVisibility(View.GONE);
        mAdapter = new DiscountExchangeAdapter(mContext, mList);
        mAdapter.currency_abbr = currency_abbr;
        lvList.setAdapter(mAdapter);
    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> getData());

        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                CurrenciesIfModelData object = mList.get(index);
                if (object != null) {
                    Intent intent = new Intent();
                    intent.putExtra(TransConstant.OBJECT, JSON.toJSONString(object));
                    intent.putExtra(TransConstant.TYPE, "exchange");
                    intent.putExtra("position", changePosition);
                    setResult(TransConstant.REFRESH, intent);
                    finish();
                }
            }
        });
    }

    private void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().commonCurrenciesGet(response -> {
            Logger.d(response);
            if (lvList == null)
                return;
            msv.showContent();
            lvList.setVisibility(View.VISIBLE);
            if ("0".equals(response.getCode()) && null != response.getData()) {
                mList.addAll(response.getData());
            }
            mAdapter.notifyDataSetChanged();
        }, error -> {
            if (lvList == null)
                return;
            showErrorToast(error);
            msv.showError();

        });
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, DiscountExchangeActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * @param context       mContext
     * @param currency_abbr 当前选择的货币类型
     * @param position      当前选择的顺序 0，1，2
     */
    public static void launch(Context context, String currency_abbr, int position) {
        Intent intent = new Intent(context, DiscountExchangeActivity.class);
        intent.putExtra(TransConstant.OBJECT, currency_abbr);
        intent.putExtra("position", position);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

}
