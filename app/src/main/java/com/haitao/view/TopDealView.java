package com.haitao.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.activity.DiscountActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.TopDealAdapter;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.TraceUtils;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import io.swagger.client.model.DealModel;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 置顶优惠
 *
 * @author 陶声
 * @since 2017/08/16
 */
public class TopDealView extends RelativeLayout {
    private Context              mContext;
    private ViewGroup            layoutDiscount;
    private GridView             gvStore;
    private ArrayList<DealModel> mDealList;
    private TopDealAdapter       mTopDealAdapter;
    private HorizontalScrollView layoutDiscountScroll;
    private int index = 0;
    private String      categoryId;     //  当前tab类型（最新，人气排行，单品推荐...）
    public  HtTitleView mTvTitle;

    @BindDimen(R.dimen.deal_card_size) int CARD_SIZE;

    public TopDealView(Context context, String categoryId) {
        super(context);
        mContext = context;
        this.categoryId = categoryId;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_h_scroll_product, null);
        ButterKnife.bind(this);
        mTvTitle = mView.findViewById(R.id.tv_title);
        layoutDiscount = mView.findViewById(R.id.layoutDiscount);
        gvStore = mView.findViewById(R.id.gvStore);
        layoutDiscountScroll = mView.findViewById(R.id.layoutDiscountScroll);
        mView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        addView(mView);
    }

    /**
     * 设置返利优惠标题是否显示
     *
     * @param visibility 可见性
     */
    public void setDiscountTitleVisibility(int visibility) {
        layoutDiscount.setVisibility(visibility);
    }

    //初始化数据
    private void initData() {
        mDealList = new ArrayList<DealModel>();
        mTopDealAdapter = new TopDealAdapter(mContext, mDealList);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        gvStore.setOnItemClickListener((parent, view, position, id) -> {
            if (null != mDealList.get(position)) {
                // 页面埋点
                YbxTrace.getInstance().event((BaseActivity) mContext, ((BaseActivity) mContext).pref, ((BaseActivity) mContext).prefh, ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Click, "", null, TraceUtils.Chid_Deal_Hot + categoryId);

                // 跳转
                DiscountDetailActivity.launch(mContext, mDealList.get(position).getDealId());
            } else {
                DiscountActivity.launch(mContext, getResources().getString(R.string.main_product_title));
            }
        });
    }

    /**
     * 加载数据
     */
    public void setData(ArrayList<DealModel> mList) {
        mDealList.clear();
        mDealList.addAll(mList);
        //        mDealList.add(null);
        if (null != mDealList && mDealList.size() > 0) {
            int size          = mDealList.size();
            int itemMargin    = DensityUtil.dip2px(mContext, 12);
            int gridviewWidth = (CARD_SIZE + itemMargin) * size;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            gvStore.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gvStore.setColumnWidth(CARD_SIZE); // 设置列表项宽
            gvStore.setHorizontalSpacing(itemMargin); // 设置列表项水平间距
            gvStore.setStretchMode(GridView.NO_STRETCH);

            gvStore.setNumColumns(size); // 设置列数量=列表集合数
            gvStore.setAdapter(mTopDealAdapter);
        }
    }
}
