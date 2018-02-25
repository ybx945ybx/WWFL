package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.StoreMoreAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.db.StoreDB;
import com.haitao.db.v2.StoreModel;

import java.util.ArrayList;

/**
 * 根据首字母查所有商家
 */
public class StoreMoreActivity extends BaseActivity{
    private ListView lvList;
    private ArrayList<StoreModel> mList;
    private StoreMoreAdapter mAdapter;

    private String char_value = "";
    /**
     * 跳转到当前页
     * @param context
     */
    public static void launch(Context context,String val){
        Intent intent = new Intent(context,StoreMoreActivity.class);
        intent.putExtra(TransConstant.VALUE,val);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_more);
        TAG = "商家列表(根据字母查询结果)";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        if(null != getIntent() && getIntent().hasExtra(TransConstant.VALUE)){
            char_value = getIntent().getStringExtra(TransConstant.VALUE);
        }
        lvList = getView(R.id.lvList);
        tvTitle.setText(String.format("全部商家(%s)",char_value));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreDetailActivity.launch(mContext,String.valueOf(mList.get(position).getStore_id()));
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<StoreModel>();
        mAdapter = new StoreMoreAdapter(mContext,mList);
        lvList.setAdapter(mAdapter);
        String sql = "character = ?";
        mList.addAll(StoreDB.getList(sql, char_value));
        mAdapter.notifyDataSetChanged();
    }




}
