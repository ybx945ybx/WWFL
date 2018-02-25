package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.adapter.StoreTagBaseAdapter;
import com.haitao.model.TagObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class TransportFilterPopView extends RelativeLayout{
    Context mContext;
    private ViewGroup layoutScroll;

    private ViewGroup layoutStore;
    private FullGirdView gvCountry;
    private ArrayList<TagObject> storeList;
    private StoreTagBaseAdapter storeAdapter;


    int currentStorePosition = 0;




    public interface OnCallbackLitener
    {
        void onChange(int countryPosition);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener)
    {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }



    public TransportFilterPopView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_transport_filter, null);
        layoutScroll = (ViewGroup) mView.findViewById(R.id.layoutScroll);
        gvCountry = (FullGirdView) mView.findViewById(R.id.gvCountry);
        layoutStore = (ViewGroup) mView.findViewById(R.id.layoutCountry);
        layoutScroll.requestFocus();
        addView(mView);
    }

    //初始化数据
    private void initData(){


        storeList = new ArrayList<TagObject>();
        storeAdapter = new StoreTagBaseAdapter(mContext,storeList);
//        storeAdapter.isFillParent = true;
        gvCountry.setAdapter(storeAdapter);

    }
    /**
     * 初始化事件
     */
    @SuppressLint("NewApi")
    private void initEvent(){
        gvCountry.setOnItemClickListener((parent, view, position, id) -> {
            currentStorePosition = position;
            storeAdapter.currentPosition = position;
            storeAdapter.notifyDataSetChanged();
            if (null != mOnCallbackLitener) {
                mOnCallbackLitener.onChange(currentStorePosition);
            }
        });
    }

    /**
     * 加载数据
     */
    public void setStoreData(ArrayList<TagObject> list) {
        storeList.clear();
        storeList.addAll(list);
        storeAdapter.notifyDataSetChanged();
        layoutStore.setVisibility(storeList.isEmpty() ? View.GONE : View.VISIBLE);
        setScrollHeight();
    }



    private void setScrollHeight(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutScroll.setLayoutParams(params);
    }


}
