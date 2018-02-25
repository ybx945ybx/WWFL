package com.haitao.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.SearchHistoryAdapter;
import com.haitao.connection.api.ForumApi;
import com.haitao.db.SearchHistoryDB;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * Created by tqy on 2015/11/30.
 */
public class SearchHistoryView extends RelativeLayout implements View.OnClickListener {
    Context mContext;
    // 热门搜索
    private LinearLayout         llytHot;
    public  TagFlowLayout        tglytHot;
    private TagAdapter<String>   mHotAdapter;
    public  ArrayList<String>    mHotList;
    // 历史搜索
    private LinearLayout         llytHistory;
    //清空缓存
    private ImageView            ivClear;
    //历史记录
    public  FullListView         listSearchHistory;
    private SearchHistoryAdapter mAdapter;
    public  ArrayList<String>    mList;

    private boolean isSearchHistory = true;

    public SearchHistoryView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_search_history, null);
        llytHot = mView.findViewById(R.id.llyt_hot);
        tglytHot = mView.findViewById(R.id.tlyt_hot);

        llytHistory = mView.findViewById(R.id.llythistory);
        listSearchHistory = mView.findViewById(R.id.listSearchHistory);
        ivClear = mView.findViewById(R.id.ivClear);
        addView(mView);


    }

    //初始化数据
    private void initData() {
        // 热门搜索
        mHotList = new ArrayList<>();
        mHotAdapter = new TagAdapter<String>(mHotList) {
            @Override
            public View getView(FlowLayout parent, int position, String keyword) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.search_hot_item, parent, false);
                tv.setText(keyword);
                return tv;
            }
        };
        tglytHot.setAdapter(mHotAdapter);
        loadHotData();

        // 历史搜索
        mList = new ArrayList<>();
        mAdapter = new SearchHistoryAdapter(mContext, mList);
        listSearchHistory.setAdapter(mAdapter);
        loadHistoryData();
    }

    private void loadHotData() {
        ForumApi.getInstance().searchingHotKeywordsGet(response -> {
            Logger.d("search----------" + response.toString());
            if ("0".equals(response.getCode())) {
                Logger.d(response.getData().toString());
                mHotList.clear();
                mHotList.addAll(response.getData());
                llytHot.setVisibility(mHotList.size() > 0 && isSearchHistory ? View.VISIBLE : View.GONE);
                mHotAdapter.notifyDataChanged();
            }
        }, error -> {
        });

    }

    public void addData(String search) {
        if (!mHotList.contains(search)) {
            SearchHistoryDB.add(search);
            mList.clear();
            mList.addAll(SearchHistoryDB.getList());
        }
    }

    /**
     * 加载历史搜索记录
     */
    public void loadHistoryData() {
        mList.clear();
        mList.addAll(SearchHistoryDB.getList());
        llytHistory.setVisibility(mList.size() > 0 && isSearchHistory ? View.VISIBLE : View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ivClear.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClear:
                SearchHistoryDB.clear();
                mList.clear();
                mAdapter.notifyDataSetChanged();
                llytHistory.setVisibility(mList.size() > 0 && isSearchHistory ? View.VISIBLE : View.GONE);
                break;
            default:
                break;
        }
    }
}
