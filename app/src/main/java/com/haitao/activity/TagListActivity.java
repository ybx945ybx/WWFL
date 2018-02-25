package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haitao.R;
import com.haitao.adapter.TagAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.FullGirdView;
import com.haitao.view.HtHeadView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TagModel;

/**
 * 全部标签
 */
public class TagListActivity extends BaseActivity {

    private XListView           lvList;
    private FullGirdView        gvList;
    private TagAdapter          mAdapter;
    private ArrayList<TagModel> mList;

    private ViewGroup layoutProgress;

    private int page = 1;
    private HtHeadView mHtHeadView;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, TagListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        TAG = "热门标签";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
//        initTop();
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText("热门标签");
        View headerView = View.inflate(mContext, R.layout.layout_tag_list, null);
        gvList = getView(headerView, R.id.gv_order_pics);
        lvList.addHeaderView(headerView);
        lvList.setVisibility(View.GONE);

        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
        gvList.setOnItemClickListener((parent, view, position, id) -> {
            TagModel tagModel = mList.get(position);
            TagDetailActivity.launch(mContext, tagModel.getTagName(), tagModel.getTagId());
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        lvList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
        mList = new ArrayList<TagModel>();
        mAdapter = new TagAdapter(mContext, mList);
        gvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        page = 1;
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().tagsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopLoadMore();
                    lvList.stopRefresh();
                    lvList.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    if ("0".equals(response.getCode())) {
                        if (1 == page)
                            mList.clear();
                        if (null != response.getData() && null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mList.addAll(response.getData().getRows());
                            lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            lvList.setPullLoadEnable(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.stopLoadMore();
                    lvList.stopRefresh();
                });
    }
}
