package com.haitao.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.StoreTagBaseAdapter;
import com.haitao.model.TagObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.IfFilterModel;

/**
 * 通用筛选面板
 * Created by 陶声 on 2017/08/08.
 */
public class RebateFilterPopView extends RelativeLayout {

    @BindView(R.id.tv_title_status) TextView     mTvTitleStatus;    // 状态title
    @BindView(R.id.gv_status)       FullGirdView mGvStatus;         // 状态列表
    @BindView(R.id.tv_title_type)   TextView     mTvTitleType;      // 来源类型title
    @BindView(R.id.gv_type)         FullGirdView mGvType;           // 来源类型列表

    private OnCallbackLitener mOnCallbackLitener;

    private Context              mContext;
    private List<IfFilterModel>  mFilterModels;
    private ArrayList<TagObject> categoryList;
    private StoreTagBaseAdapter  categoryAdapter;

    private ArrayList<TagObject> statusList;
    private StoreTagBaseAdapter  statusAdapter;

    private TextView tvReset, tvConfirm;

    int currentStatusPosition   = 0;
    int currentCategoryPosition = 0;


    public RebateFilterPopView(Context context, List<IfFilterModel> filterModels) {
        super(context);
        initVars(context, filterModels);
        initView();
        initData();
    }

    private void initVars(Context context, List<IfFilterModel> filterModels) {
        mContext = context;
        mFilterModels = filterModels;
    }

    private void initView() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_rebate_filter, null);
        ButterKnife.bind(this, layout);
        addView(layout);
    }

    //初始化数据
    private void initData() {
        categoryList = new ArrayList<TagObject>();
        categoryAdapter = new StoreTagBaseAdapter(mContext, categoryList);
        mGvType.setAdapter(categoryAdapter);

        statusList = new ArrayList<TagObject>();
        statusAdapter = new StoreTagBaseAdapter(mContext, statusList);
        mGvStatus.setAdapter(statusAdapter);

    }

    /**
     * 加载数据
     */
    /*public void setStatusData(ArrayList<TagObject> list) {
        statusList.clear();
        statusList.addAll(list);
        statusAdapter.notifyDataSetChanged();
    }*/

    /**
     * 加载数据
     */
    /*public void setCategoryData(ArrayList<TagObject> list) {
        categoryList.clear();
        categoryList.addAll(list);
        categoryAdapter.notifyDataSetChanged();
    }*/

    /**
     * 重置
     */
    @OnClick(R.id.tv_reset)
    public void onResetClicked() {
        if (null != categoryAdapter) {
            categoryAdapter.currentPosition = 0;
            currentCategoryPosition = 0;
            categoryAdapter.notifyDataSetChanged();
        }
        if (null != statusAdapter) {
            statusAdapter.currentPosition = 0;
            currentStatusPosition = 0;
            statusAdapter.notifyDataSetChanged();
        }
        if (null != mOnCallbackLitener) {
            mOnCallbackLitener.onChange(currentCategoryPosition, currentStatusPosition);
        }
    }

    /**
     * 确认
     */
    @OnClick(R.id.tv_confirm)
    public void onConfirmClicked() {
        if (null != mOnCallbackLitener) {
            mOnCallbackLitener.onConfirm(currentCategoryPosition, currentStatusPosition);
        }
    }

    public interface OnCallbackLitener {
        void onConfirm(int catPosition, int statusPosition);

        void onChange(int catPosition, int statusPosition);
    }


    public void setOnCallbackLitener(OnCallbackLitener mOnCallbackLitener) {
        this.mOnCallbackLitener = mOnCallbackLitener;
    }
}
