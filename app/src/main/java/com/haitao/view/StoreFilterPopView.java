package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.StoreTagBaseAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.TagObject;
import com.haitao.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class StoreFilterPopView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    private ViewGroup layoutScroll;
    private ViewGroup layoutCategory;
    private FullGirdView gvCategory;
    private ArrayList<TagObject> categoryList;
    private StoreTagBaseAdapter categoryAdapter;

    private ViewGroup layoutStore;
    private FullGirdView gvCountry;
    private ArrayList<TagObject> storeList;
    private StoreTagBaseAdapter storeAdapter;

    private TextView tvReset,tvConfirm;
    private ImageView ivExpire,ivTransport,ivAlipay,ivSuperRebate;

    int currentStorePosition = 0;
    int currentCategoryPosition = 0;




    public interface OnCallbackLitener
    {
        void onConfirm(String expire, String transport, String alipay,String superRebate, int catPosition, int storePosition);
        void onChange(String expire, String transport, String alipay,String superRebate, int catPosition, int storePosition);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener)
    {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }



    public StoreFilterPopView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_store_filter, null);
        layoutScroll = (ViewGroup) mView.findViewById(R.id.layoutScroll);
        layoutCategory = (ViewGroup) mView.findViewById(R.id.layoutCategory);
        gvCategory = (FullGirdView) mView.findViewById(R.id.gvCategory);
        gvCountry = (FullGirdView) mView.findViewById(R.id.gvCountry);
        layoutStore = (ViewGroup) mView.findViewById(R.id.layoutCountry);
        tvReset = (TextView) mView.findViewById(R.id.tvReset);
        tvConfirm = (TextView) mView.findViewById(R.id.tvConfirm);
        ivExpire = (ImageView) mView.findViewById(R.id.ivDirectMail);
        ivTransport = (ImageView) mView.findViewById(R.id.ivTransport);
        ivAlipay = (ImageView) mView.findViewById(R.id.ivAlipay);
        ivSuperRebate = (ImageView) mView.findViewById(R.id.ivSuperRebate);
        layoutScroll.requestFocus();
        addView(mView);
    }

    //初始化数据
    private void initData(){
        categoryList = new ArrayList<TagObject>();
        categoryAdapter = new StoreTagBaseAdapter(mContext,categoryList);
//        categoryAdapter.isFillParent = true;
        gvCategory.setAdapter(categoryAdapter);


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
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCategoryPosition = position;
                categoryAdapter.currentPosition = position;
                categoryAdapter.notifyDataSetChanged();
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
            }
        });
        gvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentStorePosition = position;
                storeAdapter.currentPosition = position;
                storeAdapter.notifyDataSetChanged();
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
            }
        });
        tvReset.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivExpire.setOnClickListener(this);
        ivTransport.setOnClickListener(this);
        ivAlipay.setOnClickListener(this);
        ivSuperRebate.setOnClickListener(this);
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


    /**
     * 加载数据
     */
    public void setCategoryData(ArrayList<TagObject> list) {
        categoryList.clear();
        categoryList.addAll(list);
        categoryAdapter.notifyDataSetChanged();
        layoutCategory.setVisibility(categoryList.isEmpty() ? View.GONE : View.VISIBLE);
        setScrollHeight();
    }

    private void setScrollHeight(){
        if(categoryList.size() + storeList.size() > 10){
            int height = (int)(ScreenUtils.getScreenHeight(mContext)*0.6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TextUtils.isEmpty(TransConstant.HEIGHT) ? ViewGroup.LayoutParams.WRAP_CONTENT : height);
            layoutScroll.setLayoutParams(params);
        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutScroll.setLayoutParams(params);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReset:
                if(null != categoryAdapter){
                    categoryAdapter.currentPosition = 0;
                    currentCategoryPosition = 0;
                    categoryAdapter.notifyDataSetChanged();
                }
                if(null != storeAdapter){
                    storeAdapter.currentPosition = 0;
                    currentStorePosition = 0;
                    storeAdapter.notifyDataSetChanged();
                }
                ivExpire.setSelected(false);
                ivTransport.setSelected(false);
                ivAlipay.setSelected(false);
                ivSuperRebate.setSelected(false);
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            case R.id.tvConfirm:
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onConfirm(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            case R.id.ivDirectMail:
                ivExpire.setSelected(!ivExpire.isSelected());
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            case R.id.ivTransport:
                ivTransport.setSelected(!ivTransport.isSelected());
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            case R.id.ivAlipay:
                ivAlipay.setSelected(!ivAlipay.isSelected());
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            case R.id.ivSuperRebate:
                ivSuperRebate.setSelected(!ivSuperRebate.isSelected());
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(ivExpire.isSelected() ? "1" : "0", ivTransport.isSelected() ? "1" : "0", ivAlipay.isSelected() ? "1" : "0",ivSuperRebate.isSelected() ? "1" : "0",currentCategoryPosition, currentStorePosition);
                }
                break;
            default:
                break;
        }
    }
}
