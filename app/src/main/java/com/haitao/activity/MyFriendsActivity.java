package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.UserGroupAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.SearchFriendType;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ToastUtils;
import com.haitao.view.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.FriendModel;
import io.swagger.client.model.FriendsSectionsModelData;

/**
 * 我的好友页面
 *
 * @author 陶声
 * @since 2017-06-14
 */
public class MyFriendsActivity extends BaseActivity {

    @BindView(R.id.tvTitle)           TextView           mTvTitle;
    @BindView(R.id.content_view)      ExpandableListView mLvList;
    @BindView(R.id.msv)               MultipleStatusView mMsv;
    @BindView(R.id.btnLeft)           ImageButton        mBtnLeft;
    @BindView(R.id.divider)           View               mDivider;
    @BindView(R.id.ll_search)         RelativeLayout     mLlSearch;
    @BindView(R.id.tv_invite_friends) TextView           tvInviteFriends;

    private ArrayList<FriendsSectionsModelData> mList;
    private UserGroupAdapter                    mAdapter;
    private int                                 mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(TransConstant.TYPE, SearchFriendType.MESSAGE);
        }
        TAG = mType == SearchFriendType.MESSAGE ? "发送消息" : "我的好友";

        mList = new ArrayList<>();
    }

    private void initViews(Bundle savedInstanceState) {
        tvInviteFriends.setVisibility(mType == SearchFriendType.MESSAGE ? View.GONE : View.VISIBLE);
        mTvTitle.setText(TAG);
        mDivider.setVisibility(View.GONE);
        mBtnLeft.setOnClickListener(v -> finish());
        // Adapter
        mAdapter = new UserGroupAdapter(mContext, mList);
        mLvList.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
        mLvList.setAdapter(mAdapter);
        // 跳转到用户详情
        mLvList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            if (mList != null && mList.get(groupPosition) != null) {
                List<FriendModel> friends = mList.get(groupPosition).getFriends();
                if (friends != null && friends.get(childPosition) != null) {
                    if (mType == SearchFriendType.MESSAGE) {
                        NoticeMessageActivity.launch(mContext, friends.get(childPosition).getFriendUid(), friends.get(childPosition).getFriendName());
                    } else {
                        TalentDetailActivity.launch(mContext, friends.get(childPosition).getFriendUid());

                    }
                }
            }
            return true;
        });
        mMsv.setOnRetryClickListener(v -> loadData());
    }

    private void loadData() {
        mMsv.showLoading();
        ForumApi.getInstance().userFriendsListGet(response -> {
            if (TextUtils.equals(response.getCode(), "0")) {
                if (mMsv == null)
                    return;
                mMsv.showContent();
                List<FriendsSectionsModelData> data = response.getData();
                if (data != null && data.size() > 0) {
                    mList.addAll(data);
                    mLlSearch.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                        mLvList.expandGroup(i);
                    }
                } else {
                    mLlSearch.setVisibility(View.GONE);
                    mMsv.showEmpty("暂时还没有好友");
                }
            } else {
                mMsv.showError();
                ToastUtils.show(mContext, response.getMsg());
            }
        }, error -> {
            if (mMsv == null)
                return;
            mMsv.showError();
            showErrorToast(error);
        });
        /*ForumApi.getInstance().userFriendsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore();
                    mMsv.showContent();
                    if ("0".equals(response.getCode())) {
                        if (1 == page) {
                            mList.clear();
                        }
                        if (null != response.loadData()) {
                            if (null != response.loadData().getRows() && response.loadData().getRows().size() > 0) {
                                mList.addAll(response.loadData().getRows());
                            }
                            mLvList.setPullLoadEnable(TextUtils.equals(response.loadData().getHasMore(), "1"));
                        }
                        if (mList.isEmpty()) {
                            mMsv.showEmpty("暂时没有好友");
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                },
                error -> {
                });*/
    }


    /**
     * 跳转到本页面
     *
     * @param context mContext
     */
    public static void launch(Context context, @SearchFriendType int type) {
        if (!UserManager.getInstance().isLogin()) {
            QuickLoginActivity.launch(context);
        } else {
            Intent intent = new Intent(context, MyFriendsActivity.class);
            intent.putExtra(TransConstant.TYPE, type);
            context.startActivity(intent);
        }
    }

    /**
     * 邀请好友
     */
    @OnClick(R.id.tv_invite_friends)
    public void onClickInviteFriends() {
        InviteFriendsActivity.launch(mContext);
    }

    /**
     * 搜索好友
     */
    @OnClick(R.id.ll_search)
    public void onClickSearch() {
        SearchFriendsActivity.launch(mContext, mType);
    }
}
