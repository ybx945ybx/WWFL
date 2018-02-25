package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.StoreTimeListAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.VisitedStoreRecordModel;

/**
 * 选择商家和日期 - 搜索
 *
 * @author 陶声
 * @since 2017/08/14
 */
public class StoreTimeChooseSearchActivity extends BaseActivity {
    @BindView(R.id.hv_title)   HtHeadView         mHvTitle;
    @BindView(R.id.lv_content) XListView          mLvContent;
    @BindView(R.id.msv)        MultipleStatusView mMsv;
    @BindView(R.id.et_search)  ClearEditText      mEtSearch;
    @BindView(R.id.tv_cancel)  TextView           mTvCancel;

    private ArrayList<VisitedStoreRecordModel> mList;
    private StoreTimeListAdapter               mAdapter;
    private int                                mPage;

    private String search = "";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, StoreTimeChooseSearchActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_time_choose_search);
        ButterKnife.bind(this);

        initVars();
        initView();
    }

    private void initVars() {
        mPage = 1;
        TAG = getString(R.string.choose_store_time);
        mList = new ArrayList<>();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHvTitle.setCenterText(getString(R.string.choose_store_time));
        mEtSearch.requestFocus();
        mAdapter = new StoreTimeListAdapter(mContext, mList);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setVisibility(View.INVISIBLE);
        mLvContent.setAdapter(mAdapter);
        // listener
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvContent.getHeaderViewsCount();
            if (index >= 0) {
                VisitedStoreRecordModel data = mList.get(index);
                if (data != null && TextUtils.equals(data.getAllowAppeal(), "1")) {
                    Intent intent = getIntent();
                    intent.putExtra(TransConstant.OBJECT, data);
                    setResult(TransConstant.REFRESH, intent);
                    finish();
                }
            }
        });

        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                if (!TextUtils.isEmpty(search)) {
                    loadListData();
                } else {
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                }
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadListData();
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                search = s.toString().trim();
                mTvCancel.setText("".equals(search) ? R.string.cancel : R.string.search_txt);
                if (TextUtils.isEmpty(search)) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    mLvContent.setPullLoadEnable(false);
                    mMsv.showContent();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtSearch.setOnKeyListener((arg0, keyCode, arg2) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                search();
            }
            return false;
        });
        mEtSearch.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus) {//如果有焦点就显示软件盘
                imm.showSoftInputFromInputMethod(mEtSearch.getWindowToken(), 0);
            } else {//否则就隐藏
                imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
            }
        });
        //点击空格键隐藏软键盘
        /*mEtSearch.setOnKeyListener((v, keyCode, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
                search();
                return true;
            }
            return false;
        });*/

    }

    private void search() {
        mLvContent.setVisibility(View.VISIBLE);
        mMsv.showLoading();
        mPage = 1;
        loadListData();
    }

    private void loadListData() {
        ForumApi.getInstance().searchingKeywordsVisitedStoresListGet(search, String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mMsv.showContent();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                            }
                            mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有符合条件的数据");
                        }
                        mAdapter.notifyDataSetChanged();
                        Logger.d(mList.toString());
                    }
                },
                error -> {
                    if (mMsv == null)
                        return;
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                });
    }

    /**
     * 取消
     */
    @OnClick(R.id.tv_cancel)
    public void onClickCancel() {
        if (TextUtils.isEmpty(search)) {
            finish();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
            search();
        }
    }


    /**
     * 初始化数据
     */
    /*private void loadData() {
        mPage = 1;
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        //        loadData();
    }*/
}
