package com.haitao.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.BaseActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.adapter.NewCommentAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.view.ClearEditText;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.ToastPopuWindow;
import com.haitao.view.dialog.CommentReplyDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.swagger.client.model.CommentModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 商家 - 评论
 */
public class StoreCommentFragment extends BaseFragment {
    private MultipleStatusView      msv;
    private RecyclerView            lvList;
    private ArrayList<CommentModel> mList;
    private NewCommentAdapter       mAdapter;

    private View         viewHeader;
    private TextView     tvCommentCount;
    private LinearLayout llytSort;
    private TextView     tvSortType;
    private ImageView    ivSortType;

    public ClearEditText etContent;
    public View          layoutBottpm;

    private String mStoreId;
    private String sortType;
    private int page = 1;
    private boolean hasMore;

    private int lastVisibleItemPosition;

    private NewCommentAdapter.OnCallbackLitener mCallBackListener;
    private ToastPopuWindow                     toastPopuWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = "商家详情 - 评论";
        mList = new ArrayList<>();
        sortType = "new";
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            mStoreId = bundle.getString(TransConstant.VALUE);

        }

    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_comment_list, null);
        msv = getView(view, R.id.msv);
        lvList = getView(view, R.id.content_view);
        //        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvList.getLayoutParams();
        //        params.setMargins(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.px75));
        //        lvList.setLayoutParams(params);
        viewHeader = View.inflate(mContext, R.layout.layout_store_comment, null);
        tvCommentCount = getView(viewHeader, R.id.tvCommentCount);
        llytSort = getView(viewHeader, R.id.llyt_sort);
        tvSortType = getView(viewHeader, R.id.tv_sort_type);
        ivSortType = getView(viewHeader, R.id.iv_sort_type);

        etContent = getView(view, R.id.etContent);
        layoutBottpm = getView(view, R.id.layoutComment);

        lvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new NewCommentAdapter(mContext, mList);
        mAdapter.addHeaderView(viewHeader);
        lvList.setAdapter(mAdapter);
        return view;
    }

    private void initEvent() {
        mAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, lvList);

        llytSort.setOnClickListener(v -> {
            if (getResources().getString(R.string.sort_hottest).equals(tvSortType.getText())) {
                tvSortType.setText(getResources().getString(R.string.sort_newest));
                ivSortType.setImageResource(R.mipmap.ic_comment_sort_new);
                sortType = "new";
            } else {
                tvSortType.setText(getResources().getString(R.string.sort_hottest));
                ivSortType.setImageResource(R.mipmap.ic_comment_sort_hot);
                sortType = "hot";
            }
            page = 1;
            loadData();
        });

        msv.setOnRetryClickListener(v -> {
            msv.showLoading();
            loadData();
        });

        lvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 隐藏输入法
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(lvList.getWindowToken(), 0);

                RecyclerView.LayoutManager layoutManager    = recyclerView.getLayoutManager();
                int                        visivleItemCount = layoutManager.getChildCount();
                int                        totalItemCount   = layoutManager.getItemCount();
                if ((visivleItemCount > 0 &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !hasMore) {
                    layoutBottpm.setVisibility(View.INVISIBLE);
                } else {
                    layoutBottpm.setVisibility(View.GONE);

                }
                //                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvList.getLayoutParams();
                //                params.setMargins(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.px75));
                //                lvList.setLayoutParams(params);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        mCallBackListener = new NewCommentAdapter.OnCallbackLitener() {
            @Override
            public void onReplyClick(String commentId, String userName, String content, int commentPosition) {
                showReplyMenu(commentId, userName, content, commentPosition);
            }

            @Override
            public void onAgreeClick(String commentId, int groupPostion) {
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                commentLikeReq(commentId, groupPostion);
            }

            @Override
            public void onAgreeClick(String commentId, int groupPostion, int childPosition) {
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                commentLikeReq(commentId, groupPostion, childPosition);
            }
        };
        mAdapter.setOnCallbackLitener(mCallBackListener);
    }

    public void initData() {
        msv.showLoading();
        loadData();
    }

    private void showReplyMenu(String commentId, String userName, String content, int commentPosition) {
        ((BaseActivity) mContext).runOnUiThread(() -> new CommentReplyDialog(mContext, true)
                .setCommentClickListener(new CommentReplyDialog.CommentClickListener() {
                    @Override
                    public void onReply(CommentReplyDialog dialog) {
                        if (!HtApplication.isLogin()) {
                            QuickLoginActivity.launch(mContext);
                            dialog.dismiss();
                            return;
                        }

                        dialog.dismiss();

                        Observable.timer(300, TimeUnit.MILLISECONDS)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(isFirstLauch -> {
                                    mOnCallbackLitener.onReply(commentId, userName, commentPosition);

                                });
                    }

                    @Override
                    public void onCopyContent(CommentReplyDialog dialog) {
                        // 复制的内容
                        String text = "";
                        text = content;
                        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(text.trim());
                        toastPopuWindow = ToastPopuWindow.makeText((BaseActivity) mContext, ToastType.COMMON_SUCCESS, getResources().getString(R.string.comment_copy_success)).parentView(lvList);
                        toastPopuWindow.show();
                        dialog.dismiss();
                    }
                })
                .show());
    }

    /**
     * 评论点赞-网络请求
     */
    private void commentLikeReq(String commentId, int groupPostion, int childPosition) {
        ForumApi.getInstance().userPraisingPost("2", commentId,
                response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, response.getMsg());
                        try {
                            mAdapter.getData().get(groupPostion).getReplyLists().get(childPosition).setIsPraised("1");
                            int praisecount = Integer.valueOf(mAdapter.getData().get(groupPostion).getReplyLists().get(childPosition).getPraiseCount());
                            praisecount++;
                            mAdapter.getData().get(groupPostion).getReplyLists().get(childPosition).setPraiseCount(String.valueOf(praisecount));
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {

                        }
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                this::showErrorToast);
    }

    /**
     * 评论点赞-网络请求
     */
    private void commentLikeReq(String commentId, int groupPostion) {
        ForumApi.getInstance().userPraisingPost("2", commentId,
                response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        showToast(ToastType.COMMON_SUCCESS, response.getMsg());
                        try {
                            mAdapter.getData().get(groupPostion).setIsPraised("1");
                            int praisecount = Integer.valueOf(mAdapter.getData().get(groupPostion).getPraiseCount());
                            praisecount++;
                            mAdapter.getData().get(groupPostion).setPraiseCount(String.valueOf(praisecount));
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {

                        }
                    } else {
                        showToast(ToastType.ERROR, response.getMsg());
                    }
                },
                this::showErrorToast);
    }

    public void setMargin() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvList.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        lvList.setLayoutParams(params);
    }

    public void updateData(CommentModel commentModel, int commentPosition) {
        if (commentPosition == 0) {
            page = 1;
            loadData();
        } else {
//            mAdapter.getItem(commentPosition).
            boolean isexpnd = mAdapter.getItem(commentPosition).isExpand();
            commentModel.setExpand(isexpnd);
            Collections.reverse(commentModel.getReplyLists());
            mAdapter.getData().set(commentPosition, commentModel);
//            mList.set(commentPosition +  1, commentModel);
            mAdapter.notifyDataSetChanged();
//            mAdapter.not
            lvList.smoothScrollToPosition(commentPosition+ 1);
        }
    }

    public void loadData() {
        ForumApi.getInstance().dealStoreCommentsListGet(mStoreId, "s", sortType, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    Logger.d(response.getData());
                    msv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (response.getData() != null && response.getData().getRows() != null && response.getData().getRows().size() > 0) {

                            // 顶部评论总个数
                            if (page == 1) {
                                if (isAdded()) {
                                    String commentTotal = Integer.valueOf(response.getData().getTotalCount()) > 0 ? String.format(StoreCommentFragment.this.getResources().getString(R.string.store_comment_count), response.getData().getTotalCount()) : StoreCommentFragment.this.getResources().getString(R.string.discount_comment);
                                    tvCommentCount.setText(commentTotal);
                                }
                            }

                            if (page == 1) {
                                mAdapter.setNewData(response.getData().getRows());

                            } else {
                                mAdapter.addData(response.getData().getRows());
                            }

                            if ("1".equals(response.getData().getHasMore())) {
                                mAdapter.loadMoreComplete();
                                hasMore = true;
                            } else {
                                hasMore = false;
                                mAdapter.loadMoreEnd(true);
                            }

                        } else {
                            if (page == 1)
                                msv.showEmpty("暂无相关评论");
                        }
                    } else {
                        if (msv == null)
                            return;
                        msv.showError();
                        showToast(ToastType.ERROR, response.getMsg());

                    }
                },
                error -> {
                    if (msv == null)
                        return;
                    showErrorToast(error);
                    msv.showError();
                });

        //        if (page == 1)
        //            lvList.setSelection(0);
    }

    public interface OnCallbackLitener {
        void onLoaded(int commentTotal);

        void onReply(String username, String commentId, int commentPosition);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener callbackLitener) {
        mOnCallbackLitener = callbackLitener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toastPopuWindow != null && toastPopuWindow.isShowing()) {
            toastPopuWindow.dismiss();
        }
    }
}
