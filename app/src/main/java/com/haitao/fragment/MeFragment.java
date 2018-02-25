package com.haitao.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DepreciateActivity;
import com.haitao.activity.GoldActivity;
import com.haitao.activity.InviteFriendsActivity;
import com.haitao.activity.MyFavActivity;
import com.haitao.activity.MyFriendsActivity;
import com.haitao.activity.MyOrderActivity;
import com.haitao.activity.MyPostActivity;
import com.haitao.activity.MyRebateActivity;
import com.haitao.activity.MySampleActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.SettingActivity;
import com.haitao.activity.SignActivity;
import com.haitao.activity.UserInfoUpdateActivity;
import com.haitao.activity.VipStoreActivity;
import com.haitao.activity.WebActivity;
import com.haitao.activity.WithdrawApplyActivity;
import com.haitao.activity.WithdrawUnavailableActivity;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.OrderType;
import com.haitao.common.annotation.SearchFriendType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.ActivityFabImgSetEvent;
import com.haitao.model.UserObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.KFUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.swagger.client.model.UserInfoModel;

/**
 * 我的页面
 */
public class MeFragment extends BaseFragment {
    @BindView(R.id.img_avatar)    CustomImageView  mImgAvatar;         // 头像
    @BindView(R.id.tv_username)   TextView         mTvUsername;        // 用户名
    @BindView(R.id.tv_tag_level)  TextView         mTvTagLevel;        // 等级
    @BindView(R.id.tv_tag_group)  TextView         mTvTagGroup;        // 分组
    @BindView(R.id.ll_tag)        LinearLayout     mLlTag;             // 用户标签容器
    @BindView(R.id.ib_setting)    ImageButton      mIbSetting;         // 设置
    @BindView(R.id.ll_money)      LinearLayout     mLlMoney;           // 金额、金币容器
    @BindView(R.id.tv_currency)   TextView         mTvCurrency;        // 金额
    @BindView(R.id.tv_my_pending) TextView         mTvMyPending;       // 待可用金额
    @BindView(R.id.tv_coin)       TextView         mTvCoin;            // 金币
    @BindView(R.id.cl_pending)    ConstraintLayout mClPending;         // 待可用金额
    @BindView(R.id.cl_coin)       ConstraintLayout mClCoin;            // 金币
    @BindView(R.id.cl_currency)   ConstraintLayout mClCurrency;        // 金额
    @BindView(R.id.tvVipStore)    TextView         mTvVipStore;        // vip返利商家
    @BindView(R.id.tv_vip_rights) TextView         mTvVipRights;       // vip返利权益
    @BindView(R.id.ll_vip)        LinearLayout     mLlVip;             // Vip相关入口
    @BindView(R.id.img_event)     CustomImageView  mImgEvent;          // 活动入口
    @BindView(R.id.img_vip_tag)   ImageView        mImgVipTag;         // VIP头像标签

    private Unbinder   unbinder;
    private UserObject mUserObj;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_me, null);
        unbinder = ButterKnife.bind(this, layout);
        initVars();
        initViews(savedInstanceState);
        return layout;
    }

    private void initVars() {
        TAG = "我的";
        EventBus.getDefault().register(this);
    }

    private void initViews(Bundle savedInstanceState) {
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgEvent.setVisibility(View.VISIBLE);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgEvent);
        }
    }

    public void initData() {
        if (HtApplication.isLogin()) {
            mLlMoney.setVisibility(View.VISIBLE);
            mLlTag.setVisibility(View.VISIBLE);
            getUserInfo();
        } else {
            mLlMoney.setVisibility(View.GONE);
            mLlTag.setVisibility(View.GONE);
            mTvUsername.setText(getString(R.string.my_no_login));
            mTvTagLevel.setVisibility(View.GONE);
            mTvTagGroup.setVisibility(View.GONE);
            mImgAvatar.setImageResource(R.mipmap.ic_avatar_unlogin);
            mImgAvatar.setTag(null);
            mTvCurrency.setText("0.0");
            mTvMyPending.setText("0.0");
            mTvCoin.setText("0");
            // 隐藏VIP返利商家
            mLlVip.setVisibility(View.GONE);
            mImgVipTag.setVisibility(View.INVISIBLE); // 不要设置为GONE
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("onResume");
        initData();
        //        if (HtApplication.isLogin()) {
        //            Logger.d("onResume后去获取用户信息");
        //
        //            getUserInfo();
        //        }
    }

    public void getUserInfo() {
        Logger.d("去获取用户信息");
        ForumApi.getInstance().userAccountInfoGet(response -> {
            if ("0".equals(response.getCode())) {
                if (null != response.getData()) {
                    UserInfoModel userInfoModel = response.getData();
                    mUserObj = UserManager.getInstance().getUser();
                    // 设置用户信息
                    mUserObj.group = userInfoModel.getGroupName();
                    mUserObj.gender = userInfoModel.getSex();
                    mUserObj.username = userInfoModel.getUsername();
                    mUserObj.userLevel = userInfoModel.getUserLevel();
                    mUserObj.is_vip = userInfoModel.getIsVip();
                    mUserObj.current_money = userInfoModel.getBalance();
                    mUserObj.waitcashback = userInfoModel.getFreezedBalance();
                    mUserObj.gold = userInfoModel.getGoldsCount();
                    mUserObj.area = userInfoModel.getAreaCode();
                    mUserObj.hasSetWithdrawPwd = userInfoModel.getHasSettedWithdrawingPwd();
                    mUserObj.hasWithdrawAccount = userInfoModel.getHasWithdrawingAccount();
                    mUserObj.hasBindedPhone = userInfoModel.getHasBindedPhoneNumber();
                    Logger.d("用户信息 = " + mUserObj.toString());
                    // 头像
                    if (mImgAvatar.getTag() == null || TextUtils.equals(userInfoModel.getAvatar(), mImgAvatar.getTag().toString())) {
                        ImageLoaderUtils.showOnlineGifImage(userInfoModel.getAvatar(), mImgAvatar);
                        mImgAvatar.setTag(userInfoModel.getAvatar());
                    }
                    mUserObj.avatar = userInfoModel.getAvatar();
                    // 用户名
                    mTvUsername.setText(mUserObj.username);
                    // 等级
                    if (!TextUtils.isEmpty(mUserObj.userLevel)) {
                        mTvTagLevel.setVisibility(View.VISIBLE);
                        mTvTagLevel.setText(String.format("等级%s", mUserObj.userLevel));
                    } else {
                        mTvTagLevel.setVisibility(View.GONE);
                    }
                    // 分组
                    if (!TextUtils.isEmpty(mUserObj.group)) {
                        mTvTagGroup.setVisibility(View.VISIBLE);
                        mTvTagGroup.setText(mUserObj.group);
                    } else {
                        mTvTagGroup.setVisibility(View.GONE);
                    }
                    // 可用金额
                    mTvCurrency.setText(userInfoModel.getBalance());
                    // 待可用金额
                    mTvMyPending.setText(userInfoModel.getFreezedBalance());
                    // 金币
                    mTvCoin.setText(mUserObj.gold);
                    // 是否VIP
                    mLlVip.setVisibility(TextUtils.equals(mUserObj.is_vip, "1") ? View.VISIBLE : View.GONE);
                    mImgVipTag.setVisibility(TextUtils.equals(mUserObj.is_vip, "1") ? View.VISIBLE : View.INVISIBLE); // 不要设置为GONE
                    // 地址
                    mUserObj.province = userInfoModel.getRegion().getProvince();
                    mUserObj.city = userInfoModel.getRegion().getCity();
                    mUserObj.district = userInfoModel.getRegion().getDistrict();
                    int noticeCount = "".equals(mUserObj.newpm) ? 0 : Integer.parseInt(mUserObj.newpm);
                    UserManager.getInstance().setUser(mUserObj);
                }
            } else {
                Logger.d(response.toString());
                ToastUtils.show(mContext, response.getMsg());
            }
        }, error -> {

        });
    }

    /*@Subscribe
    public void onUserInfoChangeEvent(UserInfoChangeEvent event) {

    }*/

    /**
     * 点击事件
     *
     * @param view view
     */
    @OnClick({R.id.ib_setting, R.id.cl_user_info, R.id.cl_currency, R.id.cl_pending, R.id.cl_coin, R.id.tvVipStore, R.id.tv_vip_rights, R.id.tvRebate, R.id.tvOrder, R.id.tv_withdraw, R.id.tv_ordre_lost_trace, R.id.tvFriend, R.id.tvFav, R.id.tvPost, R.id.tv_my_sample, R.id.tvSign, R.id.tvInvite, R.id.tvDepreciate, R.id.tvService, R.id.img_event})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_setting: // 设置
                SettingActivity.launch(mContext);
                break;
            case R.id.cl_user_info: // 用户信息
                if (HtApplication.isLogin()) {
                    UserInfoUpdateActivity.launch(mContext);
                } else {
                    QuickLoginActivity.launch(mContext);
                }
                break;
            case R.id.cl_currency:     // 可用金额
            case R.id.cl_pending:   // 待可用金额
            case R.id.tvRebate:     // 返利
                MyRebateActivity.launch(mContext);
                break;
            case R.id.cl_coin: // 金币
                GoldActivity.launch(mContext);
                break;
            case R.id.tvVipStore: // VIP返利商家
                VipStoreActivity.launch(mContext);
                break;
            case R.id.tv_vip_rights: // VIP返利权益
                WebActivity.launch(mContext, "", Constant.VIP_RIGHTS);
                break;
            case R.id.tvOrder: // 订单
                MyOrderActivity.launch(mContext, OrderType.ORDER_NORMAL);
                break;
            case R.id.tv_withdraw: // 提现
                if (HtApplication.isLogin()) {
                    if (TextUtils.equals(mUserObj.hasSetWithdrawPwd, "1")
                            && TextUtils.equals(mUserObj.hasWithdrawAccount, "1")
                            && TextUtils.equals(mUserObj.hasBindedPhone, "1")) {
                        WithdrawApplyActivity.launch(mContext);
                    } else {
                        WithdrawUnavailableActivity.launch(mContext);
                    }
                } else {
                    QuickLoginActivity.launch(mContext);
                }
                break;
            case R.id.tv_ordre_lost_trace: // 丢单追踪
                MyOrderActivity.launch(mContext, OrderType.ORDER_LOST);
                break;
            case R.id.tvFriend: // 我的好友
                MyFriendsActivity.launch(mContext, SearchFriendType.FRIEND);
                break;
            case R.id.tvFav: // 收藏
                MyFavActivity.launch(mContext);
                break;
            case R.id.tvPost: // 我的帖子
                MyPostActivity.launch(mContext);
                break;
            case R.id.tv_my_sample: // 我的试用
                MySampleActivity.launch(mContext);
                break;
            case R.id.tvSign: // 签到
                SignActivity.launch(mContext);
                break;
            case R.id.tvInvite: // 邀请
                InviteFriendsActivity.launch(mContext);
                break;
            case R.id.tvDepreciate: //降价提醒
                DepreciateActivity.launch(mContext);
                break;
            case R.id.tvService: // 客服
                if (!UserManager.getInstance().isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                KFUtils.startChat(getActivity());
                break;
            case R.id.img_event: // 活动
                goEvent(mContext);
                break;
            case R.id.tvUnionPay:
                //线下返利
                String defaultHead = Constant.IS_DEBUG ? Constant.SWAGGER_DEBUG_URL : Constant.SWAGGER_PROD_URL;
                WebActivity.launch(mContext, getResources().getString(R.string.wwhaitao), SPUtils.get(mContext, SPConstant.SWAGGER_SETTING_URL, defaultHead) + "/template/offlinerebate/offlinerebate.htm");
                //                WebActivity.launch(mContext, getResources().getString(R.string.wwhaitao), "https://appv6.55haitao.com" + "/template/offlinerebate/offlinerebate.htm");
                break;
        }
    }

    /**
     * 全局活动入口设置事件
     *
     * @param event 事件
     */
    @Subscribe
    public void onActivityFabImgSetEvent(ActivityFabImgSetEvent event) {
        mImgEvent.setVisibility(View.VISIBLE);
        mImgEvent.setOnClickListener(v -> goEvent(mContext));
        ImageLoaderUtils.showOnlineImage(event.img, mImgEvent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
