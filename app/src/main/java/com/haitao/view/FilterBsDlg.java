package com.haitao.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.FilterGroupAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.IfFilterModel;

/**
 * 筛选面板 BottomSheetDlg
 *
 * @author 同事
 * @since 2018-01-25
 */
public class FilterBsDlg extends BottomSheetDialog {

    @BindView(R.id.lv_filter) ListView   mLvFilter; // Tag分组列表
    @BindView(R.id.hv_title)  HtHeadView mHvTitle;  // 标题

    private List<IfFilterModel>   mIfFilterModel;         // 筛选分组数据
    private FilterGroupAdapter    mFilterGroupAdapter;    // 筛选分组Adapter
    private String[]              mSelectedIds;
    private OnFilterClickListener mOnFilterClickListener;
    private Context               mContext;

    public FilterBsDlg(Context context, List<IfFilterModel> ifFilterModel, String[] selectedIds) {
        super(context);
        Logger.d(mSelectedIds);
        initVars(context, ifFilterModel, selectedIds);
        initView();
        initData();
    }

    private void initVars(Context context, List<IfFilterModel> ifFilterModel, String[] selectedIds) {
        mContext = context;
        mIfFilterModel = ifFilterModel;
        mSelectedIds = selectedIds;
    }

    private void initView() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.dlg_filter_group, null);
        ButterKnife.bind(this, layout);
        setContentView(layout);
        // 确定
        mHvTitle.setOnRightClickListener(view -> {
            if (null != mOnFilterClickListener) {
                mOnFilterClickListener.onConfirm(mFilterGroupAdapter.getSelectedIds());
                dismiss();
            }
        });
        // 关闭
        mHvTitle.setOnLeftClickListener(view -> dismiss());
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
    /*@OnClick(R.id.tv_reset)
    public void onResetClicked() {
        if (null != mOnFilterClickListener) {
            mFilterGroupAdapter.clearSelection();
            mOnFilterClickListener.onReset(new String[]{"", ""});
        }
    }*/

    /**
     * 筛选回调
     */
    public interface OnFilterClickListener {
        void onConfirm(String[] positions);
    }

    /**
     * 设置筛选回调
     *
     * @param onFilterClickListener 筛选回调
     * @return this
     */
    public FilterBsDlg setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        mOnFilterClickListener = onFilterClickListener;
        return this;
    }
}
