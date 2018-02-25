package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BoardTagBaseAdapter;

import java.util.ArrayList;

import io.swagger.client.model.ForumSubBoardModel;

/**
 * Created by Administrator on 2015/11/3.
 */
public class SectionFilterPopView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    private ViewGroup layoutScroll;
    private FullGirdView gvCategory;
    private ArrayList<ForumSubBoardModel> categoryList;
    private BoardTagBaseAdapter categoryAdapter;

    private FullGirdView gvStatus;
    private ArrayList<ForumSubBoardModel> statusList;
    private BoardTagBaseAdapter statusAdapter;

    private TextView tvReset,tvConfirm,tvClose;

    int currentStatusPosition = 0;
    int currentCategoryPosition = 0;




    public interface OnCallbackLitener
    {
        void onConfirm(int catPosition, int statusPosition);
        void onChange(int catPosition, int statusPosition);
        void onClose();
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener)
    {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }



    public SectionFilterPopView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_section_filter, null);
        layoutScroll = (ViewGroup) mView.findViewById(R.id.layoutScroll);
        gvCategory = (FullGirdView) mView.findViewById(R.id.gvCategory);
        gvStatus = (FullGirdView) mView.findViewById(R.id.gvStatus);
        tvReset = (TextView) mView.findViewById(R.id.tvReset);
        tvConfirm = (TextView) mView.findViewById(R.id.tvConfirm);
        tvClose = (TextView) mView.findViewById(R.id.tvClose);
        addView(mView);
    }

    //初始化数据
    private void initData(){
        categoryList = new ArrayList<ForumSubBoardModel>();
        categoryAdapter = new BoardTagBaseAdapter(mContext,categoryList);
        categoryAdapter.isFillParent = true;
        gvCategory.setAdapter(categoryAdapter);


        statusList = new ArrayList<ForumSubBoardModel>();
        statusAdapter = new BoardTagBaseAdapter(mContext,statusList);
        gvStatus.setAdapter(statusAdapter);

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
                    mOnCallbackLitener.onChange(currentCategoryPosition, currentStatusPosition);
                }
            }
        });
        gvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentStatusPosition = position;
                statusAdapter.currentPosition = position;
                statusAdapter.notifyDataSetChanged();
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(currentCategoryPosition, currentStatusPosition);
                }
            }
        });
        tvReset.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvClose.setOnClickListener(this);
    }

    /**
     * 加载数据
     */
    public void setStatusData(ArrayList<ForumSubBoardModel> list) {
        statusList.clear();
        statusList.addAll(list);
        statusAdapter.notifyDataSetChanged();
    }


    /**
     * 加载数据
     */
    public void setCategoryData(ArrayList<ForumSubBoardModel> list) {
        categoryList.clear();
        categoryList.addAll(list);
        categoryAdapter.notifyDataSetChanged();
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
                if(null != statusAdapter){
                    statusAdapter.currentPosition = 0;
                    currentStatusPosition = 0;
                    statusAdapter.notifyDataSetChanged();
                }
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onChange(currentCategoryPosition, currentStatusPosition);
                }
                break;
            case R.id.tvConfirm:
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onConfirm(currentCategoryPosition, currentStatusPosition);
                }
                break;
            case R.id.tvClose:
                if(null != mOnCallbackLitener){
                    mOnCallbackLitener.onClose();
                }
                break;
            default:
                break;
        }
    }
}
