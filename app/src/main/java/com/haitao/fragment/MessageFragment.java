package com.haitao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.MyFriendsActivity;
import com.haitao.activity.NoticeDynamicPostActivity;
import com.haitao.activity.NoticeFriendActivity;
import com.haitao.activity.NoticeMessageActivity;
import com.haitao.activity.NoticeRecommendActivity;
import com.haitao.activity.NoticeSystemMessageActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.adapter.MessageNotificationAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.annotation.SearchFriendType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.dialog.ConfirmDlg;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.MsgNoticeModel;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.msv)          MultipleStatusView mMsv;             // 多状态布局
    @BindView(R.id.head_view)    HtHeadView         headView;        // 头部
    @BindView(R.id.content_view) XListView          lvList;          // 消息列表
    @BindView(R.id.img_event)    CustomImageView    mImgActivityFab; // 全局活动入口

    private ConfirmDlg                 confirmDlg;
    private ArrayList<MsgNoticeModel>  mList;
    private MessageNotificationAdapter mAdapter;
    private ChangReceiver              mChangReceiver;
    private boolean                    mIsReceiverRegistered;
    private int page = 1;
    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        unbinder = ButterKnife.bind(this, view);
        initVars();
        initView();
        initEvent();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initVars() {
        TAG = "消息";
        mList = new ArrayList<>();
        mChangReceiver = new ChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransConstant.CHANGE_BROADCAST);
        mContext.registerReceiver(mChangReceiver, filter);
        mIsReceiverRegistered = true;
    }

    private void initView() {
        // 消息列表
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setPullRefreshEnable(true);
        mAdapter = new MessageNotificationAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
    }

    private void initEvent() {
        mMsv.setOnRetryClickListener(v -> {
            if (HtApplication.isLogin()) {
                mMsv.showLoading();
                getData();
            } else {
                QuickLoginActivity.launch(mContext);
            }
        });
        headView.setOnLeftClickListener(view -> MyFriendsActivity.launch(mContext, SearchFriendType.MESSAGE));
        headView.setOnRightClickListener(view -> {
            if (HtApplication.isLogin()) {
                messageReaded();
            } else {
                QuickLoginActivity.launch(mContext);
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
            mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_NOTICE_UPDATE);
            mContext.sendBroadcast(mIntent);
            if (mList.size() <= 0)
                return;
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                MsgNoticeModel object = mList.get(index);
                if (object != null) {
                    if (TransConstant.NoticeType.SYSTEM.equals(object.getNoticeType())) {                   // 系统消息
                        NoticeSystemMessageActivity.launch(mContext, object.getNoticeType());
                    } else if (TransConstant.NoticeType.MY_POST.equals(object.getNoticeType())) {           // 贴子动态
                        NoticeDynamicPostActivity.launch(mContext);
                    } else if (TransConstant.NoticeType.PM.equals(object.getNoticeType())) {                // 消息  5.6版本后添加好友也在消息里面
                        NoticeMessageActivity.launch(mContext, object.getIsFriend(), object.getNoticeIcon(), object.getUserId(), object.getNoticeTitle());
                    } else if (TransConstant.NoticeType.FRIEND.equals(object.getNoticeType())) {            // 好友申请
                        NoticeFriendActivity.launch(mContext);
                    } else if (TransConstant.NoticeType.RECOMMEND_DEAL.equals(object.getNoticeType())) {    // 优惠推荐
                        NoticeRecommendActivity.launch(mContext, object.getNoticeType());
                    }
                }
            }
        });

    }

    public void initData() {
        if (!HtApplication.isLogin()) {
            mMsv.showEmpty(mContext.getResources().getString(R.string.notice_nologin), mContext.getResources().getString(R.string.notice_login));
        } else {
            mMsv.showLoading();
            getData();
        }
    }

    private void getData() {
        ForumApi.getInstance().userMsgsIndexGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    mMsv.showContent();
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            if (1 == page) {
                                mList.clear();
                            }
                            mList.addAll(response.getData().getRows());
                            mAdapter.notifyDataSetChanged();
                            lvList.setPullLoadEnable("1".equals(response.getData().getHasMore()));
                        } else {
                            if (page == 1)
                                mMsv.showError(mContext.getResources().getString(R.string.network_load_error), mContext.getResources().getString(R.string.refresh));
                        }

                    } else {
                        if (lvList == null)
                            return;
                        mMsv.showError(mContext.getResources().getString(R.string.network_load_error), mContext.getResources().getString(R.string.refresh));
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (lvList == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    showErrorToast(error);
                    mMsv.showError(mContext.getResources().getString(R.string.network_load_error), mContext.getResources().getString(R.string.refresh));
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (HtApplication.isLogin() && !mList.isEmpty()) {
            lvList.autoRefresh();
        }
    }

    /**
     * 消息全部标为已读
     */
    private void messageReaded() {
        if (confirmDlg == null) {
            confirmDlg = new ConfirmDlg(mContext, "", getResources().getString(R.string.mark_all_msg_as_read),
                    confirmDlg -> {
                        ForumApi.getInstance()
                                .userMsgsSettingAllReadedPost(response -> {
                                    if ("0".equals(response.getCode())) {
                                        Logger.d("消息标为已读");
                                        // 发送广播更新消息相关
                                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_NOTICE_UPDATE);
                                        mContext.sendBroadcast(mIntent);
                                    }
                                }, error -> {

                                });
                        confirmDlg.dismiss();
                    },
                    confirmDlg -> {
                        confirmDlg.dismiss();
                    });
        }
        confirmDlg.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsReceiverRegistered) {
            mContext.unregisterReceiver(mChangReceiver);
        }
        unbinder.unbind();
    }

    /**
     * 回到顶部
     */
    public void returnTop() {
        if (lvList != null && lvList.getVisibility() == View.VISIBLE) {
            int firstVisiblePosition = lvList.getFirstVisiblePosition();
            if (firstVisiblePosition == 0) {
                lvList.autoRefresh();
            } else {
                lvList.smoothScrollToPosition(0);
            }
        }
    }

    /**
     * 全局活动入口设置事件
     *
     * @param event 事件
     */
    @Subscribe
    public void onActivityFabImgSetEvent(ActivityFabImgSetEvent event) {
        mImgActivityFab.setVisibility(View.VISIBLE);
        mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
        ImageLoaderUtils.showOnlineImage(event.img, mImgActivityFab);
    }

    private class ChangReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (TextUtils.equals(intent.getAction(), TransConstant.CHANGE_BROADCAST)) {
                if (intent.hasExtra(TransConstant.TYPE)) {
                    switch (intent.getIntExtra(TransConstant.TYPE, 0)) {
                        case TransConstant.BROAD_LOGIN:
                        case TransConstant.BROAD_LOGOUT:
                        case TransConstant.BROAD_NOTICE:
                        case TransConstant.BROAD_NOTICE_UPDATE:
                            if (!HtApplication.isLogin()) {
                                Logger.d(TAG + TransConstant.BROAD_LOGOUT);
                                initData();
                            } else {
                                page = 1;
                                getData();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

}
