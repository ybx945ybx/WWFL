package com.haitao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.adapter.WithdrawAccountAddAddAdapter;

import java.util.ArrayList;

import io.swagger.client.model.WithdrawingModeModel;


/**
 * 更改封面对话框
 *
 * @author ywl
 */
public class WithdrawAccountChooseDialog extends Dialog implements View.OnClickListener {
    private Context                         mContext;
    private FullListView                    lvList;
    private ArrayList<WithdrawingModeModel> mList;
    private WithdrawAccountAddAddAdapter    mAdapter;

    private ViewGroup layoutPage, layoutContent;

    private OnCallBack onCallBack;

    public WithdrawAccountChooseDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_style_choose);

        lvList = findViewById(R.id.lvList);
        layoutPage = findViewById(R.id.layoutPage);
        layoutContent = findViewById(R.id.layoutContent);
        initDatas();
        layoutPage.setOnClickListener(this);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            if (null != onCallBack) {
                onCallBack.onClick(position);
            }
        });
    }


    public void setCallback(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @Override
    public void onClick(View v) {
        if (v == layoutContent) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }


    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnCallBack {
        void onClick(int position);
    }


    /**
     * 解析数据
     */
    private void initDatas() {
        mList = new ArrayList<>();
        mAdapter = new WithdrawAccountAddAddAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
    }


    /**
     * 初始化省会
     */
    public void initPayStyle(ArrayList<WithdrawingModeModel> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}