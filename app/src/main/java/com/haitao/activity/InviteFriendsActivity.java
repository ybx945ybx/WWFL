package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.CommonPagerAdapter;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.fragment.InviteRankingFragment;
import com.haitao.fragment.MyInviteFragment;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.HtHeadView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.swagger.client.model.InviteIfModelData;


/**
 * 邀请好友
 */
@MLinkRouter(keys = {"inviteRegisterKey", "inviteLoginKey"})
public class InviteFriendsActivity extends BaseActivity {

    @BindView(R.id.tv_promotion_reward)          TextView   mTvPromotionReward;
    @BindView(R.id.tv_lower_class_rebate)        TextView   mTvLowerClassRebate;
    @BindView(R.id.tv_copy_code)                 TextView   mTvCopyCode;
    @BindView(R.id.tv_reward_money)              TextView   mTvRewardMoney;
    @BindView(R.id.id_stickynavlayout_indicator) TabLayout  mTab;
    @BindView(R.id.id_stickynavlayout_viewpager) ViewPager  mVpContent;
    @BindView(R.id.hv_title)                     HtHeadView mHvTitle;
    @BindView(R.id.tv_invite_desc)               TextView   mTvInviteDesc;

    private InviteIfModelData   mInviteInfo;    // 分享信息
    private ArrayList<Fragment> mFragments;     // 两个Fragment分页
    private ArrayList<String>   mTabNames;      // tab名

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, InviteFriendsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        initVars();
        initViews(savedInstanceState);

        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "邀请好友";
        mFragments = new ArrayList<>();
        mFragments.add(new InviteRankingFragment());
        mFragments.add(new MyInviteFragment());
        mTabNames = new ArrayList<>();
        mTabNames.add(getString(R.string.invite_ranking));
        mTabNames.add(getString(R.string.my_invite));
    }

    private void initViews(Bundle savedInstanceState) {
        mHvTitle.setOnRightClickListener(view -> {
            if (mInviteInfo != null) {
                TopicDetailActivity.launch(mContext, mInviteInfo.getIntroTopicId());
            }
        });
        mVpContent.setAdapter(new CommonPagerAdapter(getSupportFragmentManager(), mFragments, mTabNames));
        mTab.setupWithViewPager(mVpContent);
        for (int i = 0; i < mTabNames.size(); i++) {
            TabLayout.Tab tabItem = mTab.getTabAt(i);
            tabItem.setCustomView(R.layout.custom_tab_layout);
            ((TextView) tabItem.getCustomView().findViewById(R.id.tab_title)).setText(mTabNames.get(i));
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mHvTitle.getWindowToken(), 0);
        }
        mVpContent.setCurrentItem(0);
    }

    private void initData() {
        ForumApi.getInstance().userInviteIndexGet(response -> {
                    if (mHvTitle == null)
                        return;
                    if (TextUtils.equals(response.getCode(), "0")) {
                        mInviteInfo = response.getData();
                        if (mInviteInfo != null) {
                            ImageLoaderUtils.downloadOnlineImage(mContext, mInviteInfo.getSharePic());
                            // 推广奖励金额
                            mTvPromotionReward.setText(mInviteInfo.getRewardCountView());
                            mTvRewardMoney.setText(String.format("%s美元", mInviteInfo.getInviteRewardView()));
                            // 下级用户推广奖励金额
                            mTvLowerClassRebate.setText(mInviteInfo.getSubRewardCountView());
                            // 邀请码
                            mTvCopyCode.setText(mInviteInfo.getInviteCode());
                            // 邀请奖励说明
                            mTvInviteDesc.setText(mInviteInfo.getInviteDescription());
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mHvTitle == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 点击分享
     *
     * @param view view
     */
    @OnClick({R.id.tv_copy_code, R.id.ib_wechat, R.id.ib_wechat_moments, R.id.ib_weibo, R.id.ib_qq, R.id.ib_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_code: // 复制邀请码
                copyToClipboard(mContext, mTvCopyCode.getText().toString().trim());
                break;
            case R.id.ib_wechat:
                doShare(Wechat.NAME);
                break;
            case R.id.ib_wechat_moments:
                doShare(WechatMoments.NAME);
                break;
            case R.id.ib_weibo:
                doShare(SinaWeibo.NAME);
                break;
            case R.id.ib_qq:
                doShare(QQ.NAME);
                break;
            case R.id.ib_more: // 更多
                if (null == mInviteInfo) {
                    ToastUtils.show(mContext, "正在加载，请稍后");
                    return;
                }
                Intent intentMore = new Intent();
                intentMore.setAction(Intent.ACTION_SEND);
                intentMore.putExtra(Intent.EXTRA_TEXT, mInviteInfo.getShareUrl());
                intentMore.setType("text/plain");
                startActivity(Intent.createChooser(intentMore, "分享到"));
                break;
        }
    }


    /**
     * 分享操作
     *
     * @param platform 平台
     */
    private void doShare(String platform) {
        if (null == mInviteInfo) {
            ToastUtils.show(mContext, "正在加载，请稍后");
            return;
        }

        String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(mInviteInfo.getSharePic());
        if (TextUtils.isEmpty(mInviteInfo.getSharePic())) {
            if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                bitmap.recycle();
            } else {
                picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
            }
        }

        if ("".equals(platform)) {
            SPUtils.put(mContext, SPConstant.SHARE_TYPE, "3");
            SPUtils.put(mContext, SPConstant.SHARE_CONTENT, "");
            ShareUtils.showShareDialog(mContext, 1, mInviteInfo.getShareTitle(), mInviteInfo.getShareContent(), mInviteInfo.getShareContentWeibo(), mInviteInfo.getShareUrl(), picUrl, new ShareAnalyticsObject("分享_邀请好友"));
        } else {
            SPUtils.put(mContext, SPConstant.SHARE_TYPE, "3");
            SPUtils.put(mContext, SPConstant.SHARE_CONTENT, "");
            ShareUtils.sharePost(mContext, platform, mInviteInfo.getShareTitle(), mInviteInfo.getShareContent(), mInviteInfo.getShareContentWeibo(), mInviteInfo.getShareUrl(), picUrl, new ShareAnalyticsObject("分享_邀请好友"));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                initData();
            }
        }
    }
}
