package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.MultipleStatusView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.OfflineStoreDetailModel;

/**
 * 线下返利商家详情
 */
public class UnionPayShopDetailActivity extends BaseActivity {

    @BindView(R.id.msv)            MultipleStatusView msv;
    @BindView(R.id.iv_store)       CustomImageView    ivStore;
    @BindView(R.id.ivCountry)      CustomImageView    ivCountry;
    @BindView(R.id.tv_store_group) TextView           tvStoreGroup;
    @BindView(R.id.tv_rebate)      TextView           tvRebate;
    @BindView(R.id.tv_store_name)  TextView           tvStoreName;
    @BindView(R.id.tv_store_fav)   TextView           tvStoreFav;

    @BindView(R.id.tv_store_desc) TextView tvStoreDesc;

    @BindView(R.id.tv_department)  TextView tvDepartment;
    @BindView(R.id.tv_offical_net) TextView tvOfficalNet;
    @BindView(R.id.tv_open_time)   TextView tvOpenTime;

    @BindView(R.id.rlyt_offline_store)    RelativeLayout rlytOfflineStore;
    @BindView(R.id.rlyt_offline_discount) RelativeLayout rlytOfflineDiscount;

    private String mStoreId = "";
    private OfflineStoreDetailModel obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay_shop_detail);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent) {
            mStoreId = getIntent().getStringExtra(TransConstant.ID);
        }
    }

    private void initView() {

    }

    private void initEvent() {
        msv.setOnRetryClickListener(v -> initData());
    }

    private void initData() {
        msv.showLoading();
        getData();
    }

    private void getData() {
        ForumApi.getInstance().storeOfflineStoreIdStoredetailGet(mStoreId,
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

    /**
     * 渲染界面
     */
    private void renderView() {
        // 商家图片
        ImageLoaderUtils.showOnlineImage(obj.getStoreLogo(), ivStore);
        // 国旗图标
        ImageLoaderUtils.showOnlineImage(obj.getCountryFlagPic(), ivCountry);
        // 商家名
        tvStoreName.setText(obj.getStoreName());
        // 商家类型
        tvStoreGroup.setText(obj.getCategoryName());
        // 有无返利
        if ("1".equals(obj.getHasRebate())) {   // 有返利
            if (!TextUtils.isEmpty(obj.getRebateView())) {
                tvRebate.setVisibility(View.VISIBLE);
                tvRebate.setText(obj.getRebateView());
            }
            // 收藏和下单人数（获得返利人数）
            tvStoreFav.setText(String.format(getResources().getString(R.string.store_order_fav_has_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));

        } else {                                // 无返利
            tvRebate.setVisibility(View.GONE);
            // 收藏和下单人数（获得返利人数）
            tvStoreFav.setText(String.format(getResources().getString(R.string.store_order_fav_no_rebate), obj.getRebateInfluenceView(), obj.getCollectionsCountView()));

        }
        // 商家描述
        tvStoreDesc.setText(obj.getStoreDescription());
        // 总部
        tvDepartment.setText(obj.getAddress());
        // 官网
        tvOfficalNet.setText(obj.getWebsite());
        // 营业时间
        tvOpenTime.setText(obj.getBusinessHours());
        // 线下门店
        rlytOfflineStore.setVisibility("1".equals(obj.getHasOfflineStore()) ? View.VISIBLE : View.GONE);
        // 相关优惠
        rlytOfflineDiscount.setVisibility("1".equals(obj.getHasRelatedDeals()) ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.rlyt_offline_store, R.id.rlyt_offline_discount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlyt_offline_store:     // 线下门店
                UnionPayShopAddressListActivity.launch(mContext, mStoreId, obj.getStoreName());
                break;
            case R.id.rlyt_offline_discount:  // 优惠
                StoreDetailActivity.launch(mContext, mStoreId, 1);
                break;
        }
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, UnionPayShopDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }
}
