package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DiscountActivity;
import com.haitao.activity.StoreFilterActivity;
import com.haitao.adapter.StoreRecommendAdapter;
import com.haitao.model.StoreObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class RecommendStoreView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    private ViewGroup layoutDiscount;
    public TextView tvStoreSuperTile;
    private TextView tvStoreMore;
    private GridView gvStore;
    private ArrayList<StoreObject> storeList;
    private StoreRecommendAdapter recommendStoreAdapter;
    private TextView tvStoreSuperTitle;
    private TextView tvDiscountMore;
    private HorizontalScrollView layoutScrollStore;
    /**
     * 0:今日返返利商家 1：热门商家
     */
    public int type = 0;

    public RecommendStoreView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_h_scroll_store, null);
        tvStoreSuperTile = (TextView) mView.findViewById(R.id.tvStoreSuperTitle);
        layoutDiscount = (ViewGroup) mView.findViewById(R.id.layoutDiscount);
        tvStoreSuperTitle = (TextView) mView.findViewById(R.id.tvStoreSuperTitle);
        tvStoreMore = (TextView) mView.findViewById(R.id.tvStoreMore);
        gvStore = (GridView) mView.findViewById(R.id.gvStore);
        tvDiscountMore = (TextView) mView.findViewById(R.id.tvDiscountMore);
        layoutScrollStore = (HorizontalScrollView) mView.findViewById(R.id.layoutScrollStore);
        addView(mView);
    }

    /**
     * 动态设置ListView组建的高度
     */
    public void setListViewHeightBasedOnChildren(GridView listView,StoreRecommendAdapter adapter) {
        StoreRecommendAdapter listAdapter = (StoreRecommendAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        if (null != storeList && storeList.size() > 0) {
            int size = storeList.size();
            int length = 150;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            float density = dm.density;
            int gridviewWidth = (int) ((size * length + 7) * density);
            int itemWidth = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            gvStore.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gvStore.setColumnWidth(itemWidth); // 设置列表项宽
            gvStore.setHorizontalSpacing(0); // 设置列表项水平间距
            gvStore.setStretchMode(GridView.NO_STRETCH);
            gvStore.setNumColumns(size); // 设置列数量=列表集合数
            gvStore.setAdapter(recommendStoreAdapter);
        }
    }

    /**
     * 设置返利优惠标题是否显示
     * @param visibility
     */
    public void setDiscountTitleVisibility(int visibility) {
        layoutDiscount.setVisibility(visibility);
    }

    //初始化数据
    private void initData() {
        storeList = new ArrayList<StoreObject>();
        recommendStoreAdapter = new StoreRecommendAdapter(mContext, storeList);
    }

    private int index = 0;

    /**
     * 初始化事件
     */
    @SuppressLint("NewApi")
    private void initEvent() {
        tvStoreMore.setOnClickListener(this);
        tvDiscountMore.setOnClickListener(this);
        layoutScrollStore.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        index++;
                        break;
                    default:
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
                    index = 0;
                    View view = ((HorizontalScrollView) v).getChildAt(0);
                    if (view.getMeasuredWidth() <= v.getScrollX() + v.getWidth()) {
                        //加载数据代码
                        StoreFilterActivity.launch(mContext);
                    }
                }
                return false;
            }
        });
        /*gvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreDetailActivity.launch(mContext, storeList.get(position).id);
            }
        });*/
    }

    /**
     * 加载数据
     */
    public void setData(ArrayList<StoreObject> mList) {
        recommendStoreAdapter.isParentList = true;
        storeList.clear();
        storeList.addAll(mList);
        if (null != storeList && storeList.size() > 0) {
            int size = storeList.size();
            int length = 150;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            float density = dm.density;
            int gridviewWidth = (int) ((size * length + 7) * density);
            int itemWidth = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            gvStore.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gvStore.setColumnWidth(itemWidth); // 设置列表项宽
            gvStore.setHorizontalSpacing(0); // 设置列表项水平间距
            gvStore.setStretchMode(GridView.NO_STRETCH);
            gvStore.setNumColumns(size); // 设置列数量=列表集合数
            gvStore.setAdapter(recommendStoreAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvStoreMore:
                StoreFilterActivity.launch(mContext);
                break;
            case R.id.tvDiscountMore:
                DiscountActivity.launch(mContext,"");
                break;
            default:
                break;
        }
    }
}
