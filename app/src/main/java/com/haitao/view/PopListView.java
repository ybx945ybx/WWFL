package com.haitao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.adapter.PopListAdapter;
import com.haitao.model.TagObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class PopListView extends RelativeLayout implements View.OnClickListener{
    Context mContext;
    private ListView lvList;
    public PopListAdapter mAdapter;
    private ArrayList<TagObject> mList;
    public interface OnItemClickLitener
    {
        void onItemClick(int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public PopListView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_list, null);
        lvList = (ListView) mView.findViewById(R.id.lvList);
        addView(mView);
    }

    //初始化数据
    private void initData(){
        mList = new ArrayList<TagObject>();
        mAdapter = new PopListAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
    }
    /**
     * 初始化事件
     */
    @SuppressLint("NewApi")
    private void initEvent(){
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != mOnItemClickLitener){
                    mOnItemClickLitener.onItemClick(position);
                }
            }
        });
    }

    /**
     * 加载数据
     */
    public void setData(ArrayList<TagObject> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }
}
