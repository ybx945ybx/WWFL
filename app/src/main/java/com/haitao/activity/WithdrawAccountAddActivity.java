package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.PhotoUploadPickAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.WithdrawModeKey;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.UserObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.calendar.CustomDate;
import com.haitao.utils.camera.Bimp;
import com.haitao.view.FullGirdView;
import com.haitao.view.HtHeadView;
import com.haitao.view.HtItemEditView;
import com.haitao.view.HtItemTextView;
import com.haitao.view.WithdrawAccountChooseDialog;
import com.haitao.view.dialog.BankSelectBsDlg;
import com.haitao.view.wheel.ChangeValidityDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.BanksIfModelData;
import io.swagger.client.model.OrderBriefModel;
import io.swagger.client.model.WithdrawingModeModel;

/**
 * 添加提现账户
 */
public class WithdrawAccountAddActivity extends BaseActivity {

    @BindView(R.id.hv_title)             HtHeadView     mHvTitle;             // 标题
    @BindView(R.id.gv_order_pics)        FullGirdView   mGvOrderPics;         // 订单列表
    @BindView(R.id.hitv_account_type)    HtItemTextView mHitvAccountType;
    @BindView(R.id.hitv_bank)            HtItemTextView mHitvBank;
    @BindView(R.id.hitv_card_number)     HtItemEditView mHievCardNumber;
    @BindView(R.id.hitv_name)            HtItemEditView mHievName;
    @BindView(R.id.hitv_account)         HtItemEditView mHievAccount;
    @BindView(R.id.hitv_account_confirm) HtItemEditView mHievAccountConfirm;
    @BindView(R.id.hitv_id_number)       HtItemEditView mHievIdNumber;
    @BindView(R.id.hitv_select_order)    HtItemTextView mHitvSelectOrder;
    @BindView(R.id.tv_submit)            TextView       mTvSubmit;          // 提交
    @BindView(R.id.hitv_validate_time)   HtItemTextView mHitvValidateTime;  // 有效期
    @BindView(R.id.tv_order_pic_label)   TextView       mTvOrderPicLabel;   // 订单截图label
    @BindView(R.id.ll_order_pic)         LinearLayout   mLlOrderPic;        // 订单截图容器

    @BindString(R.string.please_input_verify_name) String mStrNameHint; // 认证姓名
    @BindString(R.string.please_input_card_owner)  String mStrCardNameHint;     // 持卡人姓名

    private static final int REQUEST_GET_CARD_ID = 0;
    private static final int REQUEST_GET_ID      = 1;

    //信用卡有效期
    private ChangeValidityDialog        mChangeValidityDialog;
    private OrderBriefModel             mOrderBriefModel;
    private ArrayList<PhotoImageObject> mList;
    private PhotoUploadPickAdapter      mAdapter;
    private PhotoPickParameterObject    mPhotoPickParameterInfo;
    private String month = "", year = "";
    private String mAccount;        // 账号 or 卡号
    private String mAccountConfirm; // 账号确认
    private String mModelId;        // 提现方式id
    private String mBankId;         // 银行id
    private String mIdNumber;       // 身份证号
    private String mName;           // 姓名
    @WithdrawModeKey
    private String mModelKey;

    private WithdrawAccountChooseDialog     dialog; // 提现账户选择
    private ArrayList<WithdrawingModeModel> mModes;
    private List<BanksIfModelData>          mBankList;  // 银行列表
    private BankSelectBsDlg                 mBankSelectBsDlg; // 银行选择对话框
    private Activity                        mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_account_add);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = getString(R.string.add_withdraw_account);
        mActivity = this;
        mModes = new ArrayList<>();
        mList = new ArrayList<>();
        mBankList = new ArrayList<>();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHvTitle.setOnRightClickListener(view -> submit());
        initError();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mGvOrderPics.setOnItemClickListener((parent, view, position, id1) -> {
            if (mList.size() < mAdapter.maxSize && position == mAdapter.getCount() - 1) {
                photoPickWrapper();
            } else {
                openImagePreview(position);
            }
        });
        // 账号 or 卡号
        mHievAccount.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccount = s.toString().trim();
                verifyData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 账号确认
        mHievAccountConfirm.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccountConfirm = s.toString().trim();
                verifyData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 卡号
        mHievCardNumber.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccount = s.toString().trim();
                verifyData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 姓名
        mHievName.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mName = s.toString().trim();
                verifyData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 身份证号码
        mHievIdNumber.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIdNumber = s.toString().trim();
                verifyData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 卡号
        mHievCardNumber.setRightClickListener(v -> {
            CaptureActivity.launch(mActivity, REQUEST_GET_CARD_ID, CaptureActivity.ScanType.BANK_CARD);
        });
        // 身份证号
        mHievIdNumber.setRightClickListener(v -> {
            CaptureActivity.launch(mActivity, REQUEST_GET_ID, CaptureActivity.ScanType.ID_CARD);
        });
    }

    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.max_image = 3;
        mAdapter = new PhotoUploadPickAdapter(mContext, mList);
        // 删除某个图片的监听
        mAdapter.setOnPhotoRemoveListener(position -> {
            mList.remove(position);
            mPhotoPickParameterInfo.image_list.remove(position);
            mAdapter.notifyDataSetChanged();
            verifyData();
        });
        mAdapter.maxSize = mPhotoPickParameterInfo.max_image;
        mGvOrderPics.setAdapter(mAdapter);
        //        verifyData();
        loadAccountList();
    }

    /**
     * 加载账户列表
     */
    private void loadAccountList() {
        ForumApi.getInstance().commonWithdrawingModesGet(response -> {
            if (mHvTitle == null)
                return;
            if ("0".equals(response.getCode())) {
                mModes.clear();
                if (null != response.getData().getModes() && response.getData().getModes().size() > 0) {
                    mModes.addAll(response.getData().getModes());
                    if (mModes.size() > 0) {
                        mModelKey = mModes.get(0).getModeKey();
                        mModelId = mModes.get(0).getModeId();
                        mHitvAccountType.setRightText(mModes.get(0).getModeName());
                        refreshTypeView();
                    }
                    /*for (WithdrawingModeModel mode : mModes) {
                        if (TextUtils.equals(mode.getModeKey(), WithdrawModeKey.BANK_CARD)) {
                            mModelId = mode.getModeId();
                        }
                    }*/
                }
                //                mAdapter.notifyDataSetChanged();
            }
        }, error -> {
            if (mHvTitle == null)
                return;
            showErrorToast(error);
        });
    }

    /**
     * 加载银行列表
     */
    private void loadBankList() {
        ForumApi.getInstance().commonWithdrawingCooperativeBanksGet(
                response -> {
                    if (TextUtils.equals("0", response.getCode())) {
                        List<BanksIfModelData> data = response.getData();
                        if (data != null && data.size() > 0) {
                            mBankList.addAll(data);
                        }
                    }
                },
                error -> {

                });
    }

    /**
     * 提交提现
     */
    private void submit() {
        ProgressDialogUtils.show(mContext, "正在提交……");
        ArrayList<String> picList = compressImage();
        for (int i = picList.size(); i < 3; i++) {
            picList.add(null);
        }
        // 网络请求
        ForumApi.getInstance().userAccountWithdrawingModeIdAccountPost(
                mModelId,
                mAccount,
                mAccountConfirm,
                mOrderBriefModel.getOrderNumber(),
                mBankId,
                mName,
                mIdNumber,
                TextUtils.isEmpty(month) ? "" : month + "/" + year,
                picList.get(0),
                picList.get(1),
                picList.get(2),
                response -> {
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals("0", response.getCode())) {
                        new AlertDialog.Builder(mContext)
                                .setMessage(R.string.withdraw_account_add_success)
                                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                                    setResult(RESULT_OK);
                                    // 设置为已经绑定提现账户
                                    UserObject user = UserManager.getInstance().getUser();
                                    user.hasWithdrawAccount = "1";
                                    UserManager.getInstance().setUser(user);
                                    // dismiss
                                    dialog.dismiss();
                                    finish();
                                }).setCancelable(false)
                                .show();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                },
                error -> {
                    showErrorToast(error);
                    ProgressDialogUtils.dismiss();
                });
    }

    /**
     * 新增提现账户 - 验证
     */
    private void verifyData() {
        if (null == mOrderBriefModel || mList.size() == 0) {
            mTvSubmit.setEnabled(false);
            return;
        }
        if (TextUtils.equals(mModelKey, WithdrawModeKey.ALIPAY)
                || TextUtils.equals(mModelKey, WithdrawModeKey.PAYPAL)) { // 支付宝 & paypal
            mTvSubmit.setEnabled(!TextUtils.isEmpty(mAccount)
                    && !TextUtils.isEmpty(mAccountConfirm)
                    && !TextUtils.isEmpty(mName));
        } else if (TextUtils.equals(mModelKey, WithdrawModeKey.CARD)) { // 信用卡
            mTvSubmit.setEnabled(!TextUtils.isEmpty(mAccount)
                    && !TextUtils.isEmpty(mName)
                    && !TextUtils.isEmpty(month)
                    && !TextUtils.isEmpty(year));
        } else { // 借记卡
            mTvSubmit.setEnabled(!TextUtils.isEmpty(mBankId)
                    && !TextUtils.isEmpty(mAccount)
                    && !TextUtils.isEmpty(mName)
                    && !TextUtils.isEmpty(mIdNumber));
        }
    }

    /**
     * 根据账户类型，调整输入条目视图
     */
    private void refreshTypeView() {
        if (TextUtils.equals(mModelKey, WithdrawModeKey.ALIPAY)
                || TextUtils.equals(mModelKey, WithdrawModeKey.PAYPAL)) { // 支付宝/paypal
            mHievName.setTextHint(mStrNameHint);
            mHievAccount.setVisibility(View.VISIBLE);
            mHievAccountConfirm.setVisibility(View.VISIBLE);
            mHitvBank.setVisibility(View.GONE);
            mHievCardNumber.setVisibility(View.GONE);
            mHitvValidateTime.setVisibility(View.GONE);
            mHievIdNumber.setVisibility(View.GONE);
        } else if (TextUtils.equals(mModelKey, WithdrawModeKey.DEBIT_CARD)) { // 借记卡
            mHitvBank.setVisibility(View.VISIBLE);
            mHievCardNumber.setVisibility(View.VISIBLE);
            mHievName.setTextHint(mStrCardNameHint);
            mHievIdNumber.setVisibility(View.VISIBLE);
            mHitvValidateTime.setVisibility(View.GONE);
            mHievAccount.setVisibility(View.GONE);
            mHievAccountConfirm.setVisibility(View.GONE);
            if (mBankList.size() == 0) {
                loadBankList();
            }
        } else { // 信用卡
            mHievCardNumber.setVisibility(View.VISIBLE);
            mHievName.setTextHint(mStrCardNameHint);
            mHitvValidateTime.setVisibility(View.VISIBLE);
            mHitvBank.setVisibility(View.GONE);
            mHievAccount.setVisibility(View.GONE);
            mHievAccountConfirm.setVisibility(View.GONE);
            mHievIdNumber.setVisibility(View.GONE);
        }
    }

    /**
     * 压缩图片
     */
    private ArrayList<String> compressImage() {
        ArrayList<String> mData = new ArrayList<String>();
        for (int i = 0; i < mList.size(); i++) {
            String path = ImageUtil.getBitmapBase64(mList.get(i).source_image, 408, 800);
            mData.add(path);
        }
        return mData;
    }

    /**
     * 图片预览
     */
    public void openImagePreview(int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        intent.putExtras(b);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(PhotoPickParameterObject.EXTRA_PARAMETER)) {
                    getBundle(data.getExtras());
                }
                break;
            case TransConstant.IS_LOGIN:
                if (!HtApplication.isLogin()) {
                    finish();
                } else {
                    initData();
                }
                break;
            case TransConstant.REFRESH:
                // 选择返利订单
                if (requestCode == resultCode && null != data) {
                    mOrderBriefModel = (OrderBriefModel) data.getSerializableExtra(TransConstant.OBJECT);
                    mHitvSelectOrder.setRightText(mOrderBriefModel.getOrderNumber());
                    // 显示订单模块
                    mLlOrderPic.setVisibility(View.VISIBLE);
                    mTvOrderPicLabel.setVisibility(View.VISIBLE);
                    verifyData();
                }
                break;
            case REQUEST_GET_ID:
                // 身份证号
                if (resultCode == RESULT_OK) {
                    mIdNumber = data.getStringExtra("cardNum");
                    mHievIdNumber.setText(mIdNumber);
                }
                break;
            case REQUEST_GET_CARD_ID:
                // 卡号
                if (resultCode == RESULT_OK) {
                    mAccount = data.getStringExtra("cardNum");
                    mHievCardNumber.setText(mAccount);
                }
                break;
        }
    }

    private void getBundle(Bundle bundle) {
        if (bundle != null) {
            mList.clear();
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
            ArrayList<String> list = mPhotoPickParameterInfo.image_list;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    PhotoImageObject img = new PhotoImageObject();
                    Bitmap           bm;
                    try {
                        bm = Bimp.revitionImageSize(list.get(i));
                        img.source_image = FileUtils.saveBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mList.add(img);
                }
            }
            mAdapter.notifyDataSetChanged();
            verifyData();
        }
    }

    /**
     * 选择订单
     */
    @OnClick(R.id.hitv_select_order)
    public void onClickChooseOrder() {
        if (null != mOrderBriefModel) {
            LogisticsOrderActivity.launch(mContext, mOrderBriefModel);
        } else {
            LogisticsOrderActivity.launch(mContext, LogisticsOrderActivity.OrderSelectType.WITHDRAW);
        }
    }

    /**
     * 提交
     */
    @OnClick(R.id.tv_submit)
    public void onClickSubmit() {
        submit();
    }

    /**
     * 有效期
     */
    @OnClick(R.id.hitv_validate_time)
    public void onClickValidateTime() {
        mChangeValidityDialog = new ChangeValidityDialog(mContext);
        CustomDate date = new CustomDate();
        mChangeValidityDialog.setDate(date.year, date.month, date.day);
        mChangeValidityDialog.show();
        mChangeValidityDialog.setBirthdayListener((y, m, day) -> {
            year = y;
            month = Integer.parseInt(m) < 10 ? "0" + m : m;
            mHitvValidateTime.setRightText(month + "/" + year);
            verifyData();
        });
        WindowManager              windowManager = getWindowManager();
        Display                    display       = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp            = mChangeValidityDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        mChangeValidityDialog.getWindow().setAttributes(lp);
    }

    /**
     * 账户类型
     */
    @OnClick(R.id.hitv_account_type)
    public void onClickAccountType() {
        if (mModes == null || mModes.size() == 0)
            return;
        if (null == dialog) {
            dialog = new WithdrawAccountChooseDialog(mContext);
        }
        dialog.show();
        dialog.initPayStyle(mModes);
        dialog.setCallback(position -> {
            dialog.dismiss();
            WithdrawingModeModel withdrawingModeModel = mModes.get(position);
            mModelKey = withdrawingModeModel.getModeKey();
            mModelId = withdrawingModeModel.getModeId();
            mHitvAccountType.setRightText(withdrawingModeModel.getModeName());
            refreshTypeView();
            // 切换类型之后，需要再做输入验证
            verifyData();
        });
        WindowManager              windowManager = getWindowManager();
        Display                    display       = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp            = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 选择银行
     */
    @OnClick(R.id.hitv_bank)
    public void onBankClicked() {
        if (mBankList != null && mBankList.size() > 0) {
            if (mBankSelectBsDlg == null) {
                mBankSelectBsDlg = new BankSelectBsDlg(mContext, mBankList)
                        .setOnBankSelectListener((dlg, bankId, bankName) -> {
                            mBankId = bankId;
                            mHitvBank.setRightText(bankName);
                            dlg.dismiss();
                        });
            }
            mBankSelectBsDlg.show();
        }
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawAccountAddActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.ADD);
    }
}
