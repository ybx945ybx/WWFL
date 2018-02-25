package com.haitao.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.adapter.FilterGroupAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.IfFilterModel;

/**
 * 通用筛选面板
 * Created by 陶声 on 2017/08/08.
 */
public class FilterPopView extends RelativeLayout {

    @BindView(R.id.lv_filter) ListView mLvFilter; // Tag分组列表

    private List<IfFilterModel>   mIfFilterModel;         // 筛选分组数据
    private FilterGroupAdapter    mFilterGroupAdapter;    // 筛选分组Adapter
    private String[]              mSelectedIds;
    private OnFilterClickListener mOnFilterClickListener;
    private Context               mContext;

    public FilterPopView(Context context, List<IfFilterModel> ifFilterModel, String[] selectedIds) {
        super(context);
        initVars(context, ifFilterModel, selectedIds);
        Logger.d(mSelectedIds);
        initView();
        initData();
    }

    private void initVars(Context context, List<IfFilterModel> ifFilterModel, String[] selectedIds) {
        mContext = context;
        mIfFilterModel = ifFilterModel;
        mSelectedIds = selectedIds;
    }

    private void initView() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_filter_group, null);
        ButterKnife.bind(this, layout);
        addView(layout);
    }

    //初始化数据
    private void initData() {
        // 设置已选Id
        if (mSelectedIds != null) {
            mFilterGroupAdapter = new FilterGroupAdapter(mContext, mIfFilterModel, mSelectedIds);
        } else {
            mFilterGroupAdapter = new FilterGroupAdapter(mContext, mIfFilterModel);
        }
        mLvFilter.setAdapter(mFilterGroupAdapter);
    }

    /**
     * 重置
     */
    @OnClick(R.id.tv_reset)
    public void onResetClicked() {
        if (null != mOnFilterClickListener) {
            mFilterGroupAdapter.clearSelection();
            mOnFilterClickListener.onReset(new String[]{"", ""});
        }
    }

    /**
     * 确认
     */
    @OnClick(R.id.tv_confirm)
    public void onConfirmClicked() {
        if (null != mOnFilterClickListener) {
            mOnFilterClickListener.onConfirm(mFilterGroupAdapter.getSelectedIds());
        }
    }

    /**
     * 筛选回调
     */
    public interface OnFilterClickListener {
        void onConfirm(String[] positions);

        void onReset(String[] positions);
    }

    /**
     * 设置筛选回调
     *
     * @param onFilterClickListener 筛选回调
     * @return this
     */
    public FilterPopView setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        mOnFilterClickListener = onFilterClickListener;
        return this;
    }
}
