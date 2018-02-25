package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.FriendStatChangeEvent;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.TalentTopicFragment;
import com.haitao.model.NoticeObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.StickyNavLayout;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.swagger.client.model.ForumUserInfoModel;

/**
 * 达人详情页（用户详情）
 */
public class TalentDetailActivity extends BaseActivity implements View.OnClickListener {
    //用户信息
    private CustomImageView ivImage;
    private TextView        tvName;
    private TextView        tvLevel;
    private TextView        tvPostNum;
    private ViewGroup       layoutFriend, layoutAdd, layoutMessage;
    private TextView tvAdd;

    private TabLayout        tabLayout;
    private ViewPager        mVpContent;
    private BasePagerAdapter adapter;
    private String[]                tabs      = new String[]{"帖子", "晒单"};
    private ArrayList<BaseFragment> fragments = null;

    private StickyNavLayout layoutContent;
    private ViewGroup       layoutProgress;

    private String id = "";
    //    private UserModel          mUserInfo;
    private ForumUserInfoModel mUserInfo;


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, TalentDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_detail);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(TransConstant.ID)) {
            id = intent.getStringExtra(TransConstant.ID);
        }
        TAG = "达人详情";
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText("");
        ivImage = getView(R.id.ivImage);
        tvName = getView(R.id.tvName);
        tvLevel = getView(R.id.tvLevel);
        tvPostNum = getView(R.id.tvPostNum);
        layoutAdd = getView(R.id.layoutAdd);
        layoutFriend = getView(R.id.layoutFriend);
        tvAdd = getView(R.id.tvAdd);
        layoutMessage = getView(R.id.tab_msgs);

        tabLayout = getView(R.id.tab);
        mVpContent = getView(R.id.id_stickynavlayout_viewpager);

        layoutContent = getView(R.id.layoutContent);
        layoutContent.setVisibility(View.GONE);
        layoutProgress = getView(R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        layoutMessage.setOnClickListener(this);
        layoutAdd.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        layoutContent.setOnTopHiddenListener(status -> {
            if (null != mUserInfo && status) {
                tvTitle.setText(mUserInfo.getUsername());
            } else {
                tvTitle.setText("");
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragments = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString(TransConstant.ID, id);
            bundle.putString(TransConstant.TYPE, String.valueOf(i + 1));
            TalentTopicFragment talentTopicFragment = new TalentTopicFragment();
            talentTopicFragment.setArguments(bundle);
            fragments.add(talentTopicFragment);
        }
        adapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        mVpContent.setAdapter(adapter);
        tabLayout.setupWithViewPager(mVpContent);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(tabs[i]);
        }
        getData();
    }

    private void getData() {
        ForumApi.getInstance().forumUserUserIdIndexGet(id, "0",
                response -> {
                    if (layoutContent == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    layoutContent.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData() && null != response.getData().getUserInfo()) {
                            Logger.d(response.getData().getUserInfo().toString());
                            mUserInfo = response.getData().getUserInfo();
                            renderView(mUserInfo);
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                },
                error -> {
                    if (layoutContent == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                });
    }

    private void renderView(ForumUserInfoModel forumUserInfo) {
        ImageLoaderUtils.showOnlineGifImage(forumUserInfo.getAvatar(), ivImage);
        tvName.setText(forumUserInfo.getUsername());
        tvLevel.setText(forumUserInfo.getGroupName());
        tvPostNum.setText(String.format("%s个帖子    |     %s个精华帖", forumUserInfo.getTopicsCount(), forumUserInfo.getDigestTopicsCount()));
        layoutAdd.setSelected(true);
        tvAdd.setSelected(true);
        layoutFriend.setVisibility(!"2".equals(forumUserInfo.getIsFriend()) ? View.VISIBLE : View.GONE);
        if ("1".equals(forumUserInfo.getIsFriend())) {
            tvAdd.setText("解除好友");
        } else if ("3".equals(forumUserInfo.getIsFriend())) {
            tvAdd.setText("等待验证");
        } else if ("0".equals(forumUserInfo.getIsFriend())) {
            tvAdd.setText("加为好友");
            layoutAdd.setSelected(false);
            tvAdd.setSelected(false);
        } else if ("4".equals(forumUserInfo.getIsFriend())) {
            tvAdd.setText("加为好友");
            layoutAdd.setSelected(false);
            tvAdd.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutAdd:
            case R.id.tvAdd:
                if (!UserManager.getInstance().isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                if ("0".equals(mUserInfo.getIsFriend())) {
                    addFriend();
                } else if ("1".equals(mUserInfo.getIsFriend())) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(String.format("确定要取消和%s的好友关系？", mUserInfo.getUsername()))
                            .setPositiveButton(R.string.confirm, (dialog, which) -> {
                                delFriend();
                                dialog.dismiss();
                            })
                            .setNegativeButton(R.string.cancel, (dialog, which) -> {
                                dialog.dismiss();
                            }).show();


                    /*ConfirmDialogUtils dialogUtils = new ConfirmDialogUtils(mContext);
                    dialogUtils.show(String.format("确定要取消和%s的好友关系？", mUserInfo.getUsername()));
                    dialogUtils.setOnItemClickLitener(new ConfirmDialogUtils.OnItemClickLitener() {
                        @Override
                        public void onLeftClick() {
                            dialogUtils.dismiss();
                        }

                        @Override
                        public void onRightClick() {
                            dialogUtils.dismiss();
                            delFriend();
                        }
                    });*/
                } else if ("2".equals(mUserInfo.getIsFriend())) {
                    ToastUtils.show(mContext, "抱歉，您不能加自己为好友");
                    return;
                } else if ("3".equals(mUserInfo.getIsFriend())) {
                    ToastUtils.show(mContext, "正在等待验证");
                    return;
                } else if ("4".equals(mUserInfo.getIsFriend())) {
                    addFriend();
                    return;
                }
                break;
            case R.id.tab_msgs:
                NoticeObject noticemUserInfo = new NoticeObject();
                noticemUserInfo.type = "pm";
                noticemUserInfo.author = mUserInfo.getUsername();
                noticemUserInfo.type_id = id;
                NoticeMessageActivity.launch(mContext, mUserInfo.getIsFriend(), mUserInfo.getAvatar(), id, mUserInfo.getUsername());
            default:
                break;
        }
    }


    public void addFriend() {
        ForumApi.getInstance().userFriendFriendUidPost(id,
                response -> {
                    if (layoutContent == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "好友请求已发送，请等待对方验证");
                        tvAdd.setText("等待验证");
                        mUserInfo.setIsFriend("3");
                        layoutAdd.setSelected(true);
                        tvAdd.setSelected(true);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (layoutContent == null)
                        return;
                    showErrorToast(error);
                });
    }

    public void delFriend() {
        ForumApi.getInstance().userFriendFriendUidDelete(id,
                response -> {
                    if (layoutContent == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "已解除该好友");
                        tvAdd.setText("加为好友");
                        mUserInfo.setIsFriend("0");
                        layoutAdd.setSelected(false);
                        tvAdd.setSelected(false);
                    }
                }, error -> {
                    if (layoutContent == null)
                        return;
                    showErrorToast(error);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            getData();
        }

    }

    @Subscribe
    public void onFriendStateChange(FriendStatChangeEvent event) {
        Logger.d("onFriendStateChange");
        String isfriend = event.isFreind;
        mUserInfo.setIsFriend(event.isFreind);
        if ("1".equals(isfriend)) {
            tvAdd.setText("解除好友");
        } else if ("3".equals(isfriend)) {
            tvAdd.setText("等待验证");
        }
    }
}
