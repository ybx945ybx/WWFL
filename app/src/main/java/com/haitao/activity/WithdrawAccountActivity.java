package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.WithdrawAccountAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.WithdrawAccountChooseDialog;
import com.haitao.view.dialog.WithdrawAccountRemoveBsDlg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.UserWithdrawingModeModel;
import io.swagger.client.model.WithdrawingModeModel;

/**
 * 提现账户列表
 */
public class WithdrawAccountActivity extends BaseActivity {

    @BindView(R.id.hv_title) HtHeadView mHvTitle;   // 标题

    private ListView                            lvList;
    private ArrayList<UserWithdrawingModeModel> mList;
    private WithdrawAccountAdapter              mAdapter;

    private String    selectModelId;
    private ViewGroup layoutProgress;

    private ArrayList<WithdrawingModeModel> models;
    //    private ArrayList<WithdrawingModeModel> modelList;

    private WithdrawAccountChooseDialog dialog;

    private List<String> popupMenuItemList = new ArrayList<>();
    private boolean mShowSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_account);
        ButterKnife.bind(this);

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
        TAG = getString(R.string.withdraw_account);
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(TransConstant.ID)) {
            mShowSelect = true;
            selectModelId = intent.getStringExtra(TransConstant.ID);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initError();
        lvList = getView(R.id.lvList);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                UserWithdrawingModeModel userWithdrawingModeModel = mList.get(index);
                if (userWithdrawingModeModel != null && "1".equals(userWithdrawingModeModel.getStatus())) {
                    Intent intent = new Intent();
                    intent.putExtra(TransConstant.ID, userWithdrawingModeModel.getAccountId());
                    intent.putExtra(TransConstant.TITLE, userWithdrawingModeModel.getModeName());
                    intent.putExtra(TransConstant.VALUE, userWithdrawingModeModel.getAccount());
                    intent.putExtra("icon", userWithdrawingModeModel.getIcon());
                    setResult(TransConstant.REFRESH, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        popupMenuItemList.add("删除");
        mList = new ArrayList<>();
        mAdapter = new WithdrawAccountAdapter(mContext, mList);
        mAdapter.setOnMoreClickListener(id -> {
            // 删除提现账户
            new WithdrawAccountRemoveBsDlg(mContext)
                    .setOnRemoveListener((dlg) -> {
                        delData(id);
                        dlg.dismiss();
                    }).show();
        });
        //        mAdapter.setShowSelect(mShowSelect);
        //        mAdapter.mSelectId = selectModelId;
        lvList.setAdapter(mAdapter);

        models = new ArrayList<>();
        //        modelList = new ArrayList<>();
        getData();
    }

    /**
     * 删除
     *
     * @param accountId 账户Id
     */
    private void delData(final String accountId) {
        ProgressDialogUtils.show(mContext, "正在提交……");
        //        UserWithdrawingModeModel userWithdrawingModeModel = mList.get(accountId);
        ForumApi.getInstance().userAccountWithdrawingAccountAccountIdDelete(accountId,
                response -> {
                    if (mHvTitle == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    if ("0".equals(response.getCode())) {
                        //                        ToastUtils.show(mContext, "删除成功");
                        //                        mList.remove(accountId);
                        getData();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    if (mHvTitle == null)
                        return;
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }

    /**
     * 提现账户列表
     */
    private void getData() {
        ForumApi.getInstance().commonWithdrawingModesGet(response -> {
            if (lvList == null)
                return;
            layoutProgress.setVisibility(View.GONE);
            lvList.setVisibility(View.VISIBLE);
            if ("0".equals(response.getCode())) {
                mList.clear();
                List<UserWithdrawingModeModel> userModes = response.getData().getUserModes();
                if (null != userModes && userModes.size() > 0) {
                    mList.addAll(userModes);
                    mAdapter.setFirstCheckingIndex(getFirstCheckPendingIndex(userModes));
                }
                models.clear();
                if (null != response.getData().getModes() && response.getData().getModes().size() > 0) {
                    models.addAll(response.getData().getModes());
                }
                mAdapter.notifyDataSetChanged();
            }
            if (mList.isEmpty()) {
                ll_common_error.setVisibility(View.VISIBLE);
                setErrorType(0);
                setErrorMessage("您还没有添加提现方式");
            } else {
                ll_common_error.setVisibility(View.GONE);
            }
            //            mHvTitle.setRightTextVisible(models.size() > 0);
        }, error -> {
            if (lvList == null)
                return;
            showErrorToast(error);
            layoutProgress.setVisibility(View.GONE);
        });
    }

    /**
     * 第一个审核状态数据的位置
     *
     * @param modes 用户账户列表
     * @return 位置
     */
    private int getFirstCheckPendingIndex(List<UserWithdrawingModeModel> modes) {
        int index = -1;
        for (int i = 0; i < modes.size(); i++) {
            if (TextUtils.equals(modes.get(i).getStatus(), "0")) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.ADD && resultCode == RESULT_OK) {
            getData();
        } else if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                initData();
            }
        }
    }


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawAccountActivity.class);
        context.startActivity(intent);
    }


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, WithdrawAccountActivity.class);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 新增提现账户
     */
    @OnClick(R.id.tv_add)
    public void onClickAdd() {
        WithdrawAccountAddActivity.launch(mContext);
    }
}