package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BoardAdapter;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.ClearEditText;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.ForumBoardModel;


/**
 * 版块搜索
 */
public class BoardSearchActivity extends BaseActivity {
    private ClearEditText etSearch;
    private TextView      tvRight;

    private XListView                  lvList;
    private ArrayList<ForumBoardModel> mList;
    private BoardAdapter               mAdapter;

    private int    page   = 1;
    private String search = "";

    /**
     * 跳转到当前页
     *
     * @param context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, BoardSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_search);
        TAG = "版块搜索";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initError();
        btnLeft = getView(R.id.btnLeft);
        tvRight = getView(R.id.tvRight);
        etSearch = getView(R.id.etSearch);
        lvList = getView(R.id.lvList);
        lvList.setAutoLoadEnable(false);
        lvList.setPullRefreshEnable(false);
        lvList.setPullLoadEnable(false);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                ForumBoardModel forumBoardModel = mList.get(index);
                if (null != forumBoardModel) {
                    BoardDetailActivity.launch(mContext, forumBoardModel.getBoardId());
                }
            }
        });
        tvRight.setOnClickListener(v -> finish());
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                search();

            }

            @Override
            public void onLoadMore() {
                page++;
                search();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (!search.equals(s.toString().trim())) {
                    search = s.toString().trim();
                    search();
                }

                //tvRight.setText("".equals(search) ? R.string.cancel : R.string.search_txt);
                //tvRight.setVisibility(TextUtils.isEmpty(search) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    search();
                }
                return false;
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (hasFocus) {//如果有焦点就显示软件盘

                    imm.showSoftInputFromInputMethod(etSearch.getWindowToken(), 0);

                } else {//否则就隐藏
                    try {
                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    } catch (Exception e) {
                        System.out.println(" null！！！！！");
                    }
                }
            }
        });
        //点击空格键隐藏软键盘
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    search();
                    return true;
                }
                return false;
            }
        });
        btnLeft.setOnClickListener(v -> finish());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<ForumBoardModel>();
        mAdapter = new BoardAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        etSearch.setHint("搜索版块");
    }


    private void search() {
        if (TextUtils.isEmpty(search)) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        ForumApi.getInstance().searchingKeywordsBoardsGet(search,
                response -> {
                    mList.clear();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData() && response.getData().size() > 0) {
                            mList.addAll(response.getData());
                        }
                    }
                    mAdapter.largeSize = mList.size();
                    mAdapter.notifyDataSetChanged();
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                        tvErrorMsg.setText("没有相关版块");
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, this::showErrorToast);
    }


}
