package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.CommonPagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.OrderType;
import com.haitao.fragment.OrderFragment;
import com.haitao.utils.KFUtils;
import com.haitao.view.HtHeadView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.hv_title)      HtHeadView mHvTitle;
    @BindView(R.id.tab)           TabLayout  mTab;
    @BindView(R.id.vp_order_list) ViewPager  mVpOrderList;

    //    private TabLayout           mTab;
    //    private ViewPager           mVpOrderList;
    private ArrayList<String>   mStatusTypes;
    private ArrayList<String>   mStatusNames;
    private ArrayList<Fragment> fragments;
    private CommonPagerAdapter  mPagerAdapter;

    @OrderType private int mOrderType;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, @OrderType int orderType) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra(TransConstant.TYPE, orderType);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
            FirstBindPhoneActivity.launch(mContext);
            return;
        }
        initData();
    }


    private void initVars() {
        if (getIntent() != null) {
            mOrderType = getIntent().getIntExtra(TransConstant.TYPE, OrderType.ORDER_NORMAL);
        }
        if (mOrderType == OrderType.ORDER_NORMAL) {
            TAG = getString(R.string.my_order);
            mStatusTypes = new ArrayList<>(4);
            mStatusNames = new ArrayList<>(4);
            mStatusTypes.add("");
            mStatusTypes.add("2");
            mStatusTypes.add("1");
            mStatusTypes.add("3");
            mStatusNames.add("全部");
            mStatusNames.add("待生效");
            mStatusNames.add("已生效");
            mStatusNames.add("无效订单");
        } else {
            TAG = getString(R.string.order_lost_trace);
            mStatusTypes = new ArrayList<>(4);
            mStatusNames = new ArrayList<>(4);
            mStatusTypes.add("");
            mStatusTypes.add("1");
            mStatusTypes.add("2");
            mStatusTypes.add("3");
            mStatusNames.add("全部");
            mStatusNames.add("丢单处理中");
            mStatusNames.add("待商家确认");
            mStatusNames.add("无效丢单");
        }
    }

    /**
     * 初始化视图
     */
    private void initViews(Bundle savedInstanceState) {
        initError();
        if (mOrderType == OrderType.ORDER_LOST) {
            mHvTitle.setCenterText(getString(R.string.order_lost_trace));
        }
        //        mHvTitle.setOnRightClickListener(view -> KFUtils.startChat(MyOrderActivity.this));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragments = new ArrayList<Fragment>();
        for (int i = 0; i < mStatusTypes.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(TransConstant.TYPE, mOrderType);
            bundle.putSerializable(TransConstant.STATUS, mStatusTypes.get(i));
            bundle.putString(TransConstant.VALUE, mStatusNames.get(i));
            OrderFragment discountFragment = new OrderFragment();
            discountFragment.setArguments(bundle);
            fragments.add(discountFragment);
        }
        mTab.setTabMode(mOrderType == OrderType.ORDER_NORMAL ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        mPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments, mStatusNames);
        mVpOrderList.setAdapter(mPagerAdapter);
        mTab.setupWithViewPager(mVpOrderList);
        for (int i = 0; i < mStatusNames.size(); i++) {
            TabLayout.Tab tabItem = mTab.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(mStatusNames.get(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else if (TextUtils.isEmpty(UserManager.getInstance().getUser().mobile)) {
                finish();
            } else {
                initData();
            }
        } else if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            mVpOrderList.setCurrentItem(0);
            ((OrderFragment) fragments.get(0)).initData();
        }
    }

    /**
     * 丢单反馈
     */
    @OnClick(R.id.tv_order_lost_feedback)
    public void onClickOrderLostFeedback() {
        OrderLostFeedbackActivity.launch(mContext);
    }

    /**
     * 在线客服
     */
    @OnClick(R.id.tv_online_service)
    public void onClickService() {
        KFUtils.startChat(this);
    }
}
