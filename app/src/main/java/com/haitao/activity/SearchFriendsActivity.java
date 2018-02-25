package com.haitao.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.adapter.UserListAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.annotation.SearchFriendType;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.ClearEditText;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.FriendModel;

/**
 * 我的好友页面
 *
 * @author tqy
 * @version 5.2.2
 * @since 2017-06-14
 */
public class SearchFriendsActivity extends BaseActivity {

    @BindView(R.id.lvList)    XListView     mLvList;
    @BindView(R.id.et_search) ClearEditText etSearch;
    //    @BindView(R.id.tv_cancel) TextView      mTvCancel;
    //    @BindView(R.id.layoutSearch) ViewGroup     layoutSearch;
    @BindView(R.id.llProgress_common_progress)
    ViewGroup layoutProgress;

    private ArrayList<FriendModel> mList;
    private int mPage = 1;
    private UserListAdapter mAdapter;

    private String search = "";
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        //        loadSearchStoreList();
    }

    private void initVars() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(TransConstant.TYPE, SearchFriendType.MESSAGE);
        }
        TAG = mType == SearchFriendType.MESSAGE ? "发送消息" : "我的好友";

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews(Bundle savedInstanceState) {
        initTop();
        initError();
        layoutProgress.setVisibility(View.GONE);
        etSearch.requestFocus();
        tvTitle.setText(mType == SearchFriendType.MESSAGE ? "发送消息" : "我的好友");
        mAdapter = new UserListAdapter(mContext, mList);
        mLvList.setAdapter(mAdapter);
        mLvList.setAutoLoadEnable(true);
        mLvList.setPullLoadEnable(false);
        mLvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                if (TextUtils.isEmpty(search)) {
                    //                    loadData();
                    mLvList.stopRefresh();
                } else {
                    search();
                }
            }

            @Override
            public void onLoadMore() {
                mPage++;
                if (TextUtils.isEmpty(search)) {
                    loadData();
                } else {
                    search();
                }
            }
        });
        mLvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mLvList.getHeaderViewsCount();
            if (index >= 0) {
                FriendModel obj = mList.get(index);
                if (obj != null) {
                    if (mType == SearchFriendType.MESSAGE) {
                        // 跳转到发消息界面
                        NoticeMessageActivity.launch(mContext, obj.getFriendUid(), obj.getFriendName());
                    } else {
                        // 用户主页
                        TalentDetailActivity.launch(mContext, obj.getFriendUid());
                    }
                    finish();
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //                if (!TextUtils.isEmpty(s.toString().trim())) {
                //                    mTvCancel.setVisibility(View.VISIBLE);
                //                }
                if (!search.equals(s.toString().trim())) {
                    search = s.toString().trim();
                    if (TextUtils.isEmpty(search)) {
                        mPage = 1;
                        loadData();
                    } else {
                        mPage = 1;
                        search();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if((null == filterList || filterList.isEmpty()) && TextUtils.isEmpty(search))
                    return;
                loadSearchStoreList();*/
            }
        });
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);

                }
                if (!TextUtils.isEmpty(search)) {
                    mPage = 1;
                    search();
                }

            }
            return false;
        });
        /*layoutSearch.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                etSearch.requestFocus();
                mTvCancel.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_FORCED);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                etSearch.setLayoutParams(params);
            }
            return false;
        });*/
        etSearch.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                etSearch.requestFocus();

                //                mTvCancel.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                etSearch.setLayoutParams(params);
            }
            return false;
        });

    }


    private void loadData() {
        /*ForumApi.getInstance().userFriendsListGet(String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                        response -> {
                            layoutProgress.setVisibility(View.GONE);
                            mLvList.stopRefresh();
                            mLvList.stopLoadMore();
                            if ("0".equals(response.getCode())) {
                                if (1 == mPage) {
                                    mList.clear();
                                }
                                if (null != response.loadData().getRows() && response.loadData().getRows().size() > 0) {
                                    mList.addAll(response.loadData().getRows());
                                }
                                mLvList.setPullLoadEnable("1".equals(response.loadData().getHasMore()));
                                mAdapter.notifyDataSetChanged();
                            } else {
                                ToastUtils.show(mContext, response.getMsg());
                                finish();
                            }
                            if (mList.isEmpty()) {
                                ll_common_error.setVisibility(View.VISIBLE);
                                setErrorType(0);
                            } else {
                                ll_common_error.setVisibility(View.GONE);
                            }
                        },
                        error -> {
                            layoutProgress.setVisibility(View.GONE);
                            mLvList.stopRefresh();
                            mLvList.stopLoadMore();

                        });*/
    }

    private void search() {
        ForumApi.getInstance().searchingKeywordsFriendsListGet(search, String.valueOf(mPage), String.valueOf(PageConstant.pageSize),
                response -> {
                    ProgressDialogUtils.dismiss();
                    layoutProgress.setVisibility(View.GONE);
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (1 == mPage) {
                            mList.clear();
                        }
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            mList.addAll(response.getData().getRows());
                        }
                        mLvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    ProgressDialogUtils.dismiss();
                    layoutProgress.setVisibility(View.GONE);
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();

                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                loadData();
            }
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context mContext
     */
    public static void launch(Context context, @SearchFriendType int type) {
        Intent intent = new Intent(context, SearchFriendsActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }
}
