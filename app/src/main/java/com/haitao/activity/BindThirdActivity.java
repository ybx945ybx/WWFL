package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.ThirdPartyAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PlatformObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.swagger.client.model.ThirdPartyPlatformModel;


/**
 * 社交账号绑定
 */
public class BindThirdActivity extends BaseActivity {
    private ListView                           lvList;
    private ArrayList<ThirdPartyPlatformModel> mList;
    private ThirdPartyAdapter                  mAdapter;

    private PlatformObject platformObject;
    private int operPosition = -1;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, BindThirdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_third);
        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        TAG = "社交账号绑定";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        lvList = getView(R.id.lvList);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            operPosition = position;
            platformObject = new PlatformObject();
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                final ThirdPartyPlatformModel thirdPartyPlatformModel = mList.get(index);
                if (thirdPartyPlatformModel != null) {
                    if ("1".equals(thirdPartyPlatformModel.getBinded())) {
                        new AlertDialog.Builder(mContext)
                                .setMessage(String.format("确定解除%s绑定?", thirdPartyPlatformModel.getTppName()))
                                .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                                    dialog.dismiss();
                                    unBindThirdParty(thirdPartyPlatformModel);
                                })
                                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                                    dialog.dismiss();
                                }).show();
                    } else {
                        ProgressDialogUtils.show(mContext, "正在加载……");
                        bindThirdParty(thirdPartyPlatformModel);
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new ThirdPartyAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        ProgressDialogUtils.show(mContext, "正在加载……");
        if (HtApplication.isLogin()) {
            ForumApi.getInstance().userAccountThirdPartyPlatformsGet(response -> {
                ProgressDialogUtils.dismiss();
                if ("0".equals(response.getCode())) {
                    mList.addAll(response.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }, error -> {
                ProgressDialogUtils.dismiss();
                showErrorToast(error);
            });
        }
    }

    private void bindThirdParty(ThirdPartyPlatformModel thirdPartyPlatformModel) {
        Platform plat = null;
        if ("wechat".equals(thirdPartyPlatformModel.getTppKey())) {
            plat = ShareSDK.getPlatform(Wechat.NAME);
            plat.SSOSetting(true);
        } else if ("qq".equals(thirdPartyPlatformModel.getTppKey())) {
            plat = ShareSDK.getPlatform(QQ.NAME);
        } else if ("sina_weibo".equals(thirdPartyPlatformModel.getTppKey())) {
            plat = ShareSDK.getPlatform(SinaWeibo.NAME);
        }
        if (null != plat) {
            plat.setPlatformActionListener(new BindThirdActivity.AuthPlatformActionListener());
            plat.authorize();
        }
    }

    /**
     * 绑定三方账号-网络请求
     *
     * @param platformObject 平台信息
     */
    private void bindThirdParty(PlatformObject platformObject) {
        ProgressDialogUtils.show(mContext, "正在绑定……");
        ForumApi.getInstance().userAccountThirdPartyPlatformTppIdPost(mList.get(operPosition).getTppId(), platformObject.userid, platformObject.token, platformObject.unionid,
                response -> {
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "绑定成功");
                        ThirdPartyPlatformModel thirdPartyPlatformModel = mList.get(operPosition);
                        thirdPartyPlatformModel.setBinded("1");
                        mList.set(operPosition, thirdPartyPlatformModel);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    ProgressDialogUtils.dismiss();
                    showErrorToast(error);
                });
    }

    /**
     * 解绑三方账号-网络请求
     *
     * @param thirdPartyPlatformModel 平台信息
     */
    private void unBindThirdParty(final ThirdPartyPlatformModel thirdPartyPlatformModel) {
        ProgressDialogUtils.show(mContext, "正在解绑……");
        ForumApi.getInstance().userAccountThirdPartyPlatformTppIdDelete(thirdPartyPlatformModel.getTppId(),
                response -> {
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "解绑成功");
                        thirdPartyPlatformModel.setBinded("0");
                        mList.set(operPosition, thirdPartyPlatformModel);
                        mAdapter.notifyDataSetChanged();
                    }
                }, error -> {
                    ProgressDialogUtils.dismiss();
                    showErrorToast(error);
                });
    }

    class AuthPlatformActionListener implements PlatformActionListener {

        @Override
        public void onCancel(Platform arg0, int arg1) {
            ProgressDialogUtils.dismiss();
            ToastUtils.show(mContext, "绑定已取消");
        }

        @Override
        public void onComplete(Platform platform, int arg1,
                               HashMap<String, Object> arg2) {
            ProgressDialogUtils.dismiss();
            if (null != platform) {
                Logger.d(platform.getName());
                Logger.d(platform.getDb().getUserId());
                Logger.d(platform.getDb().getUserName());
                Logger.d(platform.getDb().getToken());
                Logger.d(platform.getDb().getUserIcon());
                platformObject.userid = platform.getDb().getUserId();
                platformObject.token = platform.getDb().getToken();
                platformObject.nickname = platform.getDb().getUserName();
                platformObject.icon = platform.getDb().getUserIcon();
                platformObject.platname = platform.getName();
                if (!platform.getName().equals(Wechat.NAME)) {
                    mHandler.sendEmptyMessage(1);
                    //                    bindThirdParty(platformObject);
                    return;
                }
            }
            if (null == arg2) {
                platform.showUser(null);//执行登录，登录后在回调里面获取用户资料
            } else {
                Logger.d(arg2.toString());
                if (arg2.containsKey("email")) {
                    platformObject.email = (String) arg2.get("email");
                }
                if (platform.getName().equals(Wechat.NAME)) {
                    platformObject.unionid = (String) arg2.get("unionid");
                }
                mHandler.sendEmptyMessage(1);
                //                bindThirdParty(platformObject);
            }
        }

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            ProgressDialogUtils.dismiss();
            ToastUtils.show(mContext, "绑定失败");
        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                bindThirdParty(platformObject);
            }
        }
    };

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
