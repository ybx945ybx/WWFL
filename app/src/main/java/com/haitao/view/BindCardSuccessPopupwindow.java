package com.haitao.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.activity.UnionPayShopDetailActivity;
import com.haitao.activity.UnionPayShopListActivity;
import com.haitao.adapter.UnionpayShopGridAdapter;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.BindBankCardSuccessModelData;

/**
 * 绑定银行卡成功弹窗
 * Created by a55 on 2017/12/4.
 */

public class BindCardSuccessPopupwindow extends PopupWindow {

    @BindView(R.id.llyt_content)        LinearLayout   llytContent;
    @BindView(R.id.iv_logo)             ImageView      ivLogo;
    @BindView(R.id.iv_close)            ImageView      ivClose;
    @BindView(R.id.rycv_shops)          RecyclerView   rycvShops;
    @BindView(R.id.rlyt_view_all_shops) RelativeLayout rlytViewAllShops;

    private Activity                                mActivity;
    private LayoutInflater                          mInflater;
    private View                                    mParentView;
    private int                                     shopItemHeight;
    private ArrayList<BindBankCardSuccessModelData> shopList;

    public BindCardSuccessPopupwindow(Activity mActivity, ArrayList<BindBankCardSuccessModelData> shopList) {
        this.mActivity = mActivity;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        shopItemHeight = (ScreenUtils.getScreenWidth(mActivity) - DensityUtil.dip2px(mActivity, 32)) / 3 * 94 / 115;
        this.shopList = shopList;

        init();
    }

    private void init() {
        View contentView = mInflater.inflate(R.layout.bind_card_success_popup_window, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        setWidth(ScreenUtils.getScreenWidth(mActivity));
        setHeight(ScreenUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        update();

        renderUi();

    }

    private void renderUi() {
        UnionpayShopGridAdapter mAdapter               = new UnionpayShopGridAdapter(mActivity, shopItemHeight, shopList, R.layout.union_pay_shop_grid_item);
        FullyGridLayoutManager  fullyGridLayoutManager = new FullyGridLayoutManager(mActivity, 3);
        fullyGridLayoutManager.setScrollEnabled(false);
        rycvShops.setLayoutManager(fullyGridLayoutManager);
        rycvShops.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(v -> UnionPayShopDetailActivity.launch(mActivity, (String) v.getTag()));
        // 关闭按钮
        ivClose.setOnClickListener(v -> {
            showOrDismiss(mParentView);
            mActivity.onBackPressed();
        });
        // 商家列表
        rycvShops.setLayoutManager(new GridLayoutManager(mActivity, 3));
        // 查看全部商家
        rlytViewAllShops.setOnClickListener(v -> UnionPayShopListActivity.launch(mActivity));
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;

        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }

        mParentView = parent;
        long duration = 250;
        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            llytContent.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.00f, 0.1f, 1.00f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            ivLogo.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            llytContent.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            ivLogo.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }
}
