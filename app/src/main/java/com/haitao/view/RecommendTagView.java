package com.haitao.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.haitao.R;


/**
 * Created by Administrator on 2015/11/3.
 */
public class RecommendTagView extends RelativeLayout implements View.OnClickListener{
        public RecommendTagView(Context context) {
            super(context);
        }
    /*Context mContext;
    private ViewGroup layoutDiscount;
    private TextView tvSubTitle;
    private GridView gvStore;
    private ArrayList<ForumTagObject> productList;
    private TagRecommendAdapter recommendProductAdapter;
    private HorizontalScrollView layoutDiscountScroll;
    private int index = 0;

    public RecommendTagView(Context mContext) {
        super(mContext);
        mContext = mContext;
        initView();
        loadData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.ht_layout_h_scroll_tag, null);
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvSubTitle = (TextView) mView.findViewById(R.id.tvSubTitle);
        layoutDiscount = (ViewGroup) mView.findViewById(R.id.layoutDiscount);
        gvStore = (GridView) mView.findViewById(R.id.gvStore);
        layoutDiscountScroll = (HorizontalScrollView) mView.findViewById(R.id.layoutDiscountScroll);
        addView(mView);
    }

    *//**
     * 设置次标题
     * @param title
     *//*
    public void setSubTitle(String title){
        tvSubTitle.setText(title);
    }

    *//**
     * 设置次标题
     * @param resId
     *//*
    public void setSubTitle(int resId){
        tvSubTitle.setText(resId);
    }
    //初始化数据
    private void loadData(){
        productList = new ArrayList<ForumTagObject>();
        recommendProductAdapter = new TagRecommendAdapter(mContext, productList);
    }

    *//**
     * 初始化事件
     *//*
    private void initEvent(){
        gvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != productList.get(position)) {
                    //DiscountDetailActivity.launch(mContext, productList.get(position).id);
                }else {
                    MobclickAgent.onEvent(mContext, "Home", getResources().getString(R.string.main_product_title));
                    DiscountActivity.launch(mContext,getResources().getString(R.string.main_product_title));
                }
            }
        });
        layoutDiscountScroll.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :

                        break;
                    case MotionEvent.ACTION_MOVE :
                        index++;
                        break;
                    default :
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP &&  index > 0) {
                    index = 0;
                    View view = ((HorizontalScrollView) v).getChildAt(0);
                    if (view.getMeasuredWidth() <= v.getScrollX() + v.getWidth()) {
                        FLog.e("====", "=====");
                        //加载数据代码
                        DiscountActivity.launch(mContext,getResources().getString(R.string.main_product_title));
                    }
                }
                return false;
            }
        });
    }

    *//**
     * 加载数据
     *//*
    public void setData(List<ForumTagObject> mList) {
        productList.clear();
        productList.addAll(mList);
        productList.add(null);
        if (null != productList && productList.size() > 0) {
            int size = productList.size();
            int length = 90;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            float density = dm.density;
            int gridviewWidth = (int) ((size * (length + 10)+0) * density);
            int itemWidth = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            gvStore.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gvStore.setColumnWidth(itemWidth); // 设置列表项宽
            gvStore.setHorizontalSpacing((int)(10*density)); // 设置列表项水平间距
            gvStore.setStretchMode(GridView.NO_STRETCH);

            gvStore.setNumColumns(size); // 设置列数量=列表集合数
            gvStore.setAdapter(recommendProductAdapter);
        }
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvStoreMore:
                //MobclickAgent.onEvent(mContext, "Home", getResources().getString(R.string.main_product_title));
               // DiscountActivity.launch(mContext,getResources().getString(R.string.main_product_title));
                break;
            default:
                break;
        }
    }
}
