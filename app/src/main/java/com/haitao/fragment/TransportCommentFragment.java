package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.TransportCommentActivity;
import com.haitao.adapter.TransportCommentAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.TransportCommentItemObject;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;
import java.util.Map;

/**
 * 转运详情-评论
 */
public class TransportCommentFragment extends BaseFragment {
    private Context                               mContext;
    private XListView                             mLvContent;
    private ArrayList<TransportCommentItemObject> mList;
    private TransportCommentAdapter               mAdapter;
    private LogisticsCompanyObject                storeObject;

    //顶部
    private RatingBar   rbStar;
    private TextView    tvStar;
    private ProgressBar pb1, pb2, pb3, pb4, pb5;
    private TextView tvAdd, tvCommentCount;

    private int page = 1;

    ViewGroup commentView = null;

    protected IViewContext<TransportCommentItemObject, IEntityService<TransportCommentItemObject>> postContext = VF.<TransportCommentItemObject>getDefault(TransportCommentItemObject.class);


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initVars();
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
        mContext = getActivity();
        TAG = "转运详情 - 评论";
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_store_xlistview, null);
        //        initError(view);
        mLvContent = getView(view, R.id.content_view);
        commentView = (ViewGroup) View.inflate(mContext, R.layout.layout_transport_comment, null);
        rbStar = getView(commentView, R.id.rbStar);
        tvStar = getView(commentView, R.id.tvStar);
        pb1 = getView(commentView, R.id.pb1);
        pb2 = getView(commentView, R.id.pb2);
        pb3 = getView(commentView, R.id.pb3);
        pb4 = getView(commentView, R.id.pb4);
        pb5 = getView(commentView, R.id.pb5);
        tvAdd = getView(commentView, R.id.tvAdd);
        tvCommentCount = getView(commentView, R.id.tvCommentCount);
        mLvContent.addHeaderView(commentView);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        return view;
    }

    public void refresh() {
        page = 1;
        loadData();
    }

    public void initData() {
        mList = new ArrayList<>();
        mAdapter = new TransportCommentAdapter(mContext, mList);
        mAdapter.setOnCallbackLitener(position -> {
            if (!HtApplication.isLogin()) {
                QuickLoginActivity.launch(mContext);
                return;
            }
            TransportCommentItemObject obj = mList.get(position);
            if (!"1".equals(obj.is_my_priase))
                commentLikeReq(position, obj);
        });
        mLvContent.setAdapter(mAdapter);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            storeObject = (LogisticsCompanyObject) bundle.getSerializable(TransConstant.OBJECT);
        }
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullRefreshEnable(false);
        loadData();
    }

    private void initEvent() {
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
        tvAdd.setOnClickListener(v -> TransportCommentActivity.launch(getActivity(), storeObject));
    }

    public void loadData() {
        postContext.getEntity().id = storeObject.id;
        postContext.getEntity().page = String.valueOf(page);
        postContext.getService().asynFunction(MethodConstant.TRANSPORT_COMMENT_LIST, postContext.getEntity(), new IAsynServiceHandler<TransportCommentItemObject>() {
            @Override
            public void onSuccess(TransportCommentItemObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                if (null != entity) {
                    commentView.setVisibility(View.VISIBLE);
                    if (1 == page)
                        mList.clear();
                    if (null != entity.score) {
                        rbStar.setRating(Float.valueOf(entity.score.avg));
                        tvStar.setText(Float.valueOf(entity.score.avg) > 0 ? entity.score.avg + "分" : "暂无评分");
                        if (null != entity.score.ratio) {
                            for (Map.Entry<String, String> entry : entity.score.ratio.entrySet()) {
                                int    key   = Integer.parseInt(entry.getKey());
                                double value = Double.parseDouble(entry.getValue());
                                switch (key) {
                                    case 1:
                                        pb1.setProgress((int) (value * 100));
                                        break;
                                    case 2:
                                        pb2.setProgress((int) (value * 100));
                                        break;
                                    case 3:
                                        pb3.setProgress((int) (value * 100));
                                        break;
                                    case 4:
                                        pb4.setProgress((int) (value * 100));
                                        break;
                                    case 5:
                                        pb5.setProgress((int) (value * 100));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    if (null != entity.list && null != entity.list._data && entity.list._data.size() > 0) {
                        int pageCount = TextUtils.isEmpty(entity.list._pagecount) ? 0 : Integer.parseInt(entity.list._pagecount);
                        if (pageCount > 1) {
                            mLvContent.setPullLoadEnable(true);
                        } else {
                            mLvContent.setPullLoadEnable(false);
                        }
                        mList.addAll(entity.list._data);
                    }
                    if (null != entity.list) {
                        String       pattern = String.format("评价（{%s}） ", entity.list._total);
                        CharSequence chars   = ColorPhrase.from(pattern).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.darkGrey)).format();
                        tvCommentCount.setText(chars);
                    }
                    tvCommentCount.setVisibility(mList.isEmpty() ? View.GONE : View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mLvContent.stopRefresh();
                    mLvContent.stopLoadMore();
                    mLvContent.setRefreshTime();
                }
            }

            @Override
            public void onSuccessPage(PageResult<TransportCommentItemObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
                ProgressDialogUtils.dismiss();
            }
        });
    }

    /**
     * 点赞评论-网络请求
     */
    private void commentLikeReq(final int position, final TransportCommentItemObject commentObject) {
        ForumApi.getInstance().userPraisingPost("5", commentObject.id,
                response -> {
                    ToastUtils.show(mContext, response.getMsg());
                    if (TextUtils.equals("0", response.getCode())) {
                        commentObject.is_my_priase = "1";
                        commentObject.praise_num = String.valueOf(Integer.valueOf(commentObject.praise_num) + 1);
                        mList.set(position, commentObject);
                        mAdapter.notifyDataSetChanged();
                    }
                },
                this::showErrorToast);
    }
}
