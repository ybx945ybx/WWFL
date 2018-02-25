package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.StoreTagBaseAdapter;
import com.haitao.model.TagObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class OrderFilterPopView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    private FullGirdView gvCategory;
    private ArrayList<TagObject> categoryList;
    private StoreTagBaseAdapter categoryAdapter;


    int currentCategoryPosition = 0;

    private TextView tvTitle;




    public interface OnCallbackLitener
    {
        void onChange(int catPosition);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener)
    {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }



    public OrderFilterPopView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_order_filter, null);
        gvCategory = (FullGirdView) mView.findViewById(R.id.gvCategory);
        tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
        addView(mView);
    }

    //初始化数据
    private void initData(){
        categoryList = new ArrayList<TagObject>();
        categoryAdapter = new StoreTagBaseAdapter(mContext,categoryList);
//        categoryAdapter.isFillParent = true;
        gvCategory.setAdapter(categoryAdapter);

    }
    /**
     * 初始化事件
     */
    @SuppressLint("NewApi")
    private void initEvent(){
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCategoryPosition = position;
                categoryAdapter.currentPosition = position;
                categoryAdapter.notifyDataSetChanged();
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(currentCategoryPosition);
                }
            }
        });
    }



    /**
     * 加载数据
     */
    public void setCategoryData(ArrayList<TagObject> list) {
        categoryList.clear();
        categoryList.addAll(list);
        categoryAdapter.notifyDataSetChanged();
    }

    public void setTitleVisible(boolean isShow){
        tvTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }
}
