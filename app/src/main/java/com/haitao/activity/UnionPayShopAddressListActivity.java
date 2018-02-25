package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.UnionPayShopAddressListAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.ProviceSelectEvent;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.OfflineStoreAddressListModel;
import io.swagger.client.model.OfflineStoreAddressListModelLists1;
import io.swagger.client.model.OfflineStoreAddressListModelLists2;

/**
 * Created by a55 on 2017/12/20.
 */

public class UnionPayShopAddressListActivity extends BaseActivity {

    @BindView(R.id.ht_headview) HtHeadView         htHeadView;
    @BindView(R.id.msv)         MultipleStatusView msv;

    @BindView(R.id.tv_area_name) TextView           tvProvince;
    @BindView(R.id.tv_shop_num)  TextView           tvShopNum;
    @BindView(R.id.lv_content)   ExpandableListView mLvContent;

    private UnionPayShopAddressListAdapter                mAdapter;
    private ArrayList<OfflineStoreAddressListModelLists2> provinceLists;      // 省份列表
    private ArrayList<String>                             provinceStrings;      // 省份列表

    private String mStoreId   = "";
    private String mStoreName = "";
    private OfflineStoreAddressListModel obj;
    private int                          selectPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay_shop_address_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        selectPos = 0;
        provinceLists = new ArrayList<>();
        provinceStrings = new ArrayList<>();
        Intent intent = getIntent();
        if (null != intent) {
            mStoreId = getIntent().getStringExtra(TransConstant.ID);
            mStoreName = getIntent().getStringExtra(TransConstant.TITLE);
        }
    }

    private void initView() {
        htHeadView.setCenterText(mStoreName);
    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> initData());
    }

    private void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().storeOfflineStoreIdStoreaddresslistGet(mStoreId,
                response -> {
                    Logger.d(response);
                    if (msv == null)
                        return;
                    msv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            obj = response.getData();
                            renderView();
                        } else {
                            ToastUtils.show(mContext, R.string.empty_tips);
                            finish();
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());

                    }
                },
                error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                });
    }

    private void renderView() {
        // 省份列表
        provinceLists = new ArrayList<>(obj.getLists());
        // 添加全部类型
        OfflineStoreAddressListModelLists2 all = new OfflineStoreAddressListModelLists2();
        all.setProvinceName("全部");
        all.setLocationAddressCount(obj.getAddressCount());
        List<OfflineStoreAddressListModelLists1> allList = new ArrayList<>();
        for (int i = 0; i < provinceLists.size(); i++) {
            allList.addAll(provinceLists.get(i).getLists());
        }
        all.setLists(allList);
        provinceLists.add(0, all);
        for (int i = 0; i < provinceLists.size(); i++) {
            provinceStrings.add(provinceLists.get(i).getProvinceName());
        }
        // 默认展示第一个省份名字
        tvProvince.setText(provinceLists.get(0).getProvinceName());
        // 线下门店数量
        tvShopNum.setText(String.format(getResources().getString(R.string.offline_store_num), provinceLists.get(0).getLocationAddressCount()));
        // 省份下门店列表
        mLvContent.setGroupIndicator(null);
        mAdapter = new UnionPayShopAddressListAdapter(this);
        mAdapter.setStoreName(mStoreName);
        mAdapter.setLists(provinceLists.get(0).getLists());
        mLvContent.setAdapter(mAdapter);
        expandAllGroup();
    }

    // 打开所有子项
    private void expandAllGroup() {
        for (int i = 0; i < provinceLists.get(selectPos).getLists().size(); i++) {
            mLvContent.expandGroup(i);
        }
    }

    // 筛选后刷新
    private void refreshList() {
        tvProvince.setText(provinceLists.get(selectPos).getProvinceName());
        tvShopNum.setText(String.format(getResources().getString(R.string.offline_store_num), provinceLists.get(selectPos).getLocationAddressCount()));
        mAdapter.setLists(provinceLists.get(selectPos).getLists());
        mAdapter.notifyDataSetChanged();
        expandAllGroup();

    }

    /**
     * 跳转筛选界面
     */
    @OnClick(R.id.tv_area_name)
    public void onClick() {
        UnionPayShopAddressFilterActivity.launch(mContext, provinceStrings, selectPos, mStoreName);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, String name) {
        Intent intent = new Intent(context, UnionPayShopAddressListActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.TITLE, name);
        context.startActivity(intent);
    }

    /**
     * 筛选界面选定后接收事件刷新列表
     *
     * @param event
     */
    @Subscribe
    public void onProviceSelect(ProviceSelectEvent event) {
        selectPos = event.position;
        refreshList();
    }
}
