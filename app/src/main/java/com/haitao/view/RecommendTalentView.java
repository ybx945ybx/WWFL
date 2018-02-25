package com.haitao.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TalentActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.adapter.TalentRecommendAdapter;
import com.haitao.model.TalentObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class RecommendTalentView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    public TextView tvStoreSuperTile;
    private TextView tvStoreMore;
    private GridView gvStore;
    private ArrayList<TalentObject> storeList;
    private TalentRecommendAdapter recommendStoreAdapter;
    private HorizontalScrollView layoutScroll;

    public RecommendTalentView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_h_scroll_talent, null);
        tvStoreSuperTile = (TextView) mView.findViewById(R.id.tvStoreSuperTitle);
        tvStoreMore = (TextView) mView.findViewById(R.id.tvStoreMore);
        gvStore = (GridView) mView.findViewById(R.id.gvStore);
        layoutScroll = (HorizontalScrollView) mView.findViewById(R.id.layoutScroll);
        addView(mView);
    }

    //初始化数据
    private void initData(){
        storeList = new ArrayList<TalentObject>();
        //recommendStoreAdapter = new TalentRecommendAdapter(mContext, storeList);
    }

    private int index = 0;
    /**
     * 初始化事件
     */
    private void initEvent(){
        gvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TalentDetailActivity.launch(mContext,storeList.get(position).uid);
            }
        });
        tvStoreMore.setOnClickListener(this);
        layoutScroll.setOnTouchListener(new OnTouchListener() {
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
                        TalentActivity.launch(mContext);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 加载数据
     */
    public void setData(ArrayList<TalentObject> mList) {
        storeList.clear();
        storeList.addAll(mList);
        if (null != storeList && storeList.size() > 0) {
            int size = storeList.size();
            int length = 90;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 0) * density);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvStoreMore:
                TalentActivity.launch(mContext);
                break;
            default:
                break;
        }
    }
}
