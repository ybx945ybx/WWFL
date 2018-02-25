package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.haitao.R;
import com.haitao.adapter.ChooseStoreTimeAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.StoresRecordsSection;
import io.swagger.client.model.StoresRecordsSectionsListModel;
import io.swagger.client.model.VisitedStoreRecordModel;

/**
 * 选择商家和日期
 *
 * @author 陶声
 * @since 2017/08/14
 */
public class StoreTimeChooseActivity extends BaseActivity {
    @BindView(R.id.hv_title)   HtHeadView          mHvTitle;
    @BindView(R.id.lv_content) XExpandableListView mLvContent;
    @BindView(R.id.msv)        MultipleStatusView  mMsv;

    //    private                    ListView mLvContent;

    private ArrayList<StoresRecordsSection> mList;
    private ChooseStoreTimeAdapter          mAdapter;

    private int    mPage;
    private String mSelectStoreId;
    private String mSelectTime;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String selectId, String time) {
        Intent intent = new Intent(context, StoreTimeChooseActivity.class);
        intent.putExtra(TransConstant.ID, selectId);
        intent.putExtra(TransConstant.TIME, time);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_time_choose);
        ButterKnife.bind(this);

        initVars();
        initView();
        initData();
    }

    private void initVars() {
        mPage = 1;
        TAG = getString(R.string.choose_store_time);
        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            mSelectStoreId = intent.getStringExtra(TransConstant.ID);
            mSelectTime = intent.getStringExtra(TransConstant.TIME);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHvTitle.setCenterText(getString(R.string.choose_store_time));
        mHvTitle.setOnRightClickListener(view -> StoreTimeChooseSearchActivity.launch(mContext));
        mAdapter = new ChooseStoreTimeAdapter(mContext, mList);
        mAdapter.mSelectStoreId = mSelectStoreId;
        mAdapter.mSelectTime = mSelectTime;

        View headerView = View.inflate(mContext, R.layout.header_choose_store_time, null);
        mLvContent.addHeaderView(headerView);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setAdapter(mAdapter);
        // listener
        mLvContent.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            VisitedStoreRecordModel data = mList.get(groupPosition).getStoresRecords().get(childPosition);
            if (TextUtils.equals(data.getAllowAppeal(), "1")) {
                Intent intent = getIntent();
                intent.putExtra(TransConstant.OBJECT, data);
                setResult(TransConstant.REFRESH, intent);
                finish();
                return true;
            } else {
                return false;
            }
        });
        mLvContent.setIXExpandableListViewListener(new XExpandableListView.IXExpandableListViewListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadData();
            }
        });
        /*mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = getIntent();
            intent.putExtra(TransConstant.TIME, mList.get(position));
            intent.putExtra(TransConstant.ID, mList.get(position));
            setResult(TransConstant.REFRESH, intent);
            finish();
        });*/
    }

    /**
     * 初始化事件
     */
    /*private void initEvent() {
        mLvContent.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = getIntent();
            intent.putExtra(TransConstant.OBJECT, mList.get(position));
            setResult(TransConstant.REFRESH, intent);
            finish();
        });
    }*/

    /**
     * 初始化数据
     */
    private void initData() {
        mPage = 1;
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        loadData();
    }

    /**
     * 从服务器获取数据
     */
    private void loadData() {
        ForumApi.getInstance().userVisitedStoresListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (mMsv == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        mMsv.showContent();
                        if (response.getData() != null) {
                            if (1 == mPage) {
                                mList.clear();
                            }

                            List<StoresRecordsSection> data = response.getData().getSections();
                            if (data != null && data.size() > 0) {
                                combineData(response);
                                mLvContent.setPullLoadEnable(TextUtils.equals(response.getData().getHasMore(), "1"));
                                mAdapter.notifyDataSetChanged();
                                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                                    mLvContent.expandGroup(i);
                                }
                            }
                        } else {
                            mMsv.showEmpty("暂时没有订单");
                        }
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
     * 合并不同分页数据
     *
     * @param response 新分页数据
     */
    private void combineData(StoresRecordsSectionsListModel response) {
        List<StoresRecordsSection> newData = response.getData().getSections();
        if (mList.size() > 0) {
            // 上次加载最后一组数据
            StoresRecordsSection lastRecord = mList.get(mList.size() - 1);
            // 下次加载第一组数据
            StoresRecordsSection nextfirstRecord = newData.get(0);
            if (TextUtils.equals(lastRecord.getSectionTitle(), nextfirstRecord.getSectionTitle())) {
                // 合并第一次最后一组和第二次第一组数据
                lastRecord.getStoresRecords().addAll(nextfirstRecord.getStoresRecords());
                mList.addAll(newData.subList(1, newData.size()));
            } else {
                mList.addAll(newData);
            }
        }
        mList.addAll(newData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.REFRESH && requestCode == resultCode) {
            if (null != data && data.hasExtra(TransConstant.OBJECT)) {
                VisitedStoreRecordModel storeRecordModel = (VisitedStoreRecordModel) data.getSerializableExtra(TransConstant.OBJECT);
                Intent                  intent           = getIntent();
                intent.putExtra(TransConstant.OBJECT, storeRecordModel);
                setResult(TransConstant.REFRESH, intent);
                finish();
            }
        }
    }
}
