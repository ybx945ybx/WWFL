package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.CodeConstant;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.common.annotation.ToastType;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.LoginStateChangedEvent;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.UserObject;
import com.haitao.utils.CharSequenceFilter;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.LengthFilter;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtEditTextView;
import com.orhanobut.logger.Logger;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.swagger.client.model.LoginSuccessModelData;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 完善个人信息完成注册（即注册页面）
 * Created by a55 on 2017/11/15.
 */

public class CompleteUserInfoActivity extends BaseActivity implements View.OnClickListener, HtEditTextView.textChangedListener {
    private RelativeLayout  rlytSelectPhoto;
    private CustomImageView ivAvatar;
    private HtEditTextView  etAccount, etPassWord, etInvite;
    private TextView tvWoman, tvMan;
    private TextView tvAgree, tvAgreeTerms;
    private TextView tvCommit;
    private TextView tvUseInviteCode;

    private UserObject obj;

    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private String                   mAvatarBase64;

    private int type;   // 1是第三方账号登录绑定新账号  2是新手机账号注册

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, UserObject obj, int type) {
        Intent intent = new Intent(context, CompleteUserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.OBJECT, obj);
        bundle.putInt(TransConstant.TYPE, type);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.IS_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);

        initVars();
        initViews();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "完善个人信息页";
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            obj = (UserObject) bundle.getSerializable(TransConstant.OBJECT);
            type = bundle.getInt(TransConstant.TYPE, 0);
        }
    }

    private void initViews() {
        rlytSelectPhoto = getView(R.id.rlyt_select_photo);
        ivAvatar = getView(R.id.iv_avatar);
        etAccount = getView(R.id.et_account);
        etPassWord = getView(R.id.et_password);
        etPassWord.setRightImgSelected(false);
        etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        etInvite = getView(R.id.et_invite);
        tvMan = getView(R.id.tv_man);
        tvWoman = getView(R.id.tv_woman);

        tvCommit = getView(R.id.tv_commit);

        tvUseInviteCode = getView(R.id.tv_use_invite_code);

        tvAgree = getView(R.id.tvAgree);
        tvAgree.setSelected(true);
        tvAgreeTerms = getView(R.id.tvAgreeTerms);
    }

    private void initEvent() {
        etAccount.setInputFilter(new InputFilter[]{new CharSequenceFilter(), new LengthFilter(mContext, 15)});

        rlytSelectPhoto.setOnClickListener(this);
        etPassWord.setOnRightImgClickListener(view -> {
            etPassWord.setRightImgSelected(!etPassWord.getRightImgSelected());

            etPassWord.setTransformationMethod(etPassWord.getRightImgSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = etPassWord.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        });

        tvMan.setOnClickListener(this);
        tvWoman.setOnClickListener(this);

        tvCommit.setOnClickListener(this);
        tvUseInviteCode.setOnClickListener(this);
        tvAgree.setOnClickListener(this);
        tvAgreeTerms.setOnClickListener(this);

        // 监听快速登录按钮是否可点击
        etAccount.addTextChangedListener(this);
        etPassWord.addTextChangedListener(this);
    }

    private void initData() {
        if (obj != null) {
            // 头像
            if (!TextUtils.isEmpty(obj.avatar)) {
                ivAvatar.setImageURI(obj.avatar);
                new DownAsyncTask().execute(obj.avatar);

            }
            // 性别
            tvWoman.setSelected(true);
            tvMan.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlyt_select_photo:
                selectPhoto();
                break;
            case R.id.tv_woman:
                tvWoman.setSelected(true);
                tvMan.setSelected(false);
                break;
            case R.id.tv_man:
                tvMan.setSelected(true);
                tvWoman.setSelected(false);
                break;
            case R.id.tv_commit:
                doCommit();
                break;
            case R.id.tv_use_invite_code:
                etInvite.setVisibility(View.VISIBLE);
                tvUseInviteCode.setVisibility(View.GONE);
                break;
            case R.id.tvAgree:
                tvAgree.setSelected(!tvAgree.isSelected());
                break;
            case R.id.tvAgreeTerms:
                WebActivity.launch(mContext, "55海淘服务条款", Constant.AGREEMENT_URL);
                break;
        }
    }

    /**
     * 下载头像图片任务
     */
    private class DownAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Bitmap avtBitMap = getImage(obj.avatar);
            if (avtBitMap != null) {
                mAvatarBase64 = ImageUtil.compressImageBase64(avtBitMap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public Bitmap getImage(String path) {
        try {
            URL               url  = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(inStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 去相册选择头像
     */
    private void selectPhoto() {
        if (null == mPhotoPickParameterInfo) {
            mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.single_mode = true;
            mPhotoPickParameterInfo.croper_image = true;
        }
        photoPickWrapper();
    }

    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }

    // 确认完善信息（注册）
    private void doCommit() {
        com.orhanobut.logger.Logger.d(obj.toString());
        if (!tvAgree.isSelected()) {
            showToast(ToastType.ERROR, getResources().getString(R.string.not_allow_agreement));
            return;
        }
        showProgressDialog(R.string.operationg);

        if (type == 1) {   // 第三方账号

            Logger.d("tppid--->" + obj.type);
            Logger.d("uid--->" + obj.open_id);
            Logger.d("token--->" + obj.open_token);
            Logger.d("unionId--->" + obj.open_unionid);
            Logger.d("account--->" + etAccount.getText().toString());
            Logger.d("password--->" + etPassWord.getText().toString());
            Logger.d("sex--->" + (tvWoman.isSelected() ? "2" : "1"));
            Logger.d("inviteCode--->" + etInvite.getText().toString());
            Logger.d("avatar--->" + mAvatarBase64);
            Logger.d("hasAgreedTerms--->" + 1);

            ForumApi.getInstance().userNewAccountBindingTppTppIdPost(obj.type,
                    obj.open_id,
                    obj.open_token,
                    TextUtils.isEmpty(obj.open_unionid) ? null : obj.open_unionid,
                    etAccount.getText().toString(),
                    etPassWord.getText().toString(),
                    tvWoman.isSelected() ? "2" : "1",
                    etInvite.getText().toString(),
                    mAvatarBase64,
                    "1",
                    response -> {
                        dismissProgressDialog();
                        Logger.d(response.toString());
                        if ("0".equals(response.getCode())) {
                            completeRegister(response.getData());

                        } else if (CodeConstant.THIRD_ACCOUNT_HAVE_BIND_55ACCOUNT.equals(response.getCode())) {
                            showToast(ToastType.ERROR, response.getMsg());
                        } else {
                            showToast(ToastType.ERROR, response.getMsg());
                        }
                    },
                    error -> {
                        showErrorToast(error);
                        dismissProgressDialog();
                    });
        } else {
            Logger.d(obj.toString());
            Logger.d("account = " + etAccount.getText().toString()
                    + "\npwd = " + etPassWord.getText().toString()
                    + "\ninvite code = " + etInvite.getText().toString()
                    + "\nsex = " + (tvWoman.isSelected() ? "2" : "1")
                    + "\navatar = " + mAvatarBase64
                    + "\narea" + obj.area
                    + "\nmobile" + obj.mobile
                    + "\naction token" + (TextUtils.isEmpty(obj.actionToken) ? null : obj.actionToken));

            ForumApi.getInstance().userAccountRegisterPost(etAccount.getText().toString(),
                    etPassWord.getText().toString(),
                    null,
                    etInvite.getText().toString(),
                    tvWoman.isSelected() ? "2" : "1",
                    mAvatarBase64,
                    "1",
                    obj.area,
                    obj.mobile,
                    TextUtils.isEmpty(obj.actionToken) ? null : obj.actionToken,
                    null,
                    null,
                    null,
                    response -> {
                        dismissProgressDialog();
                        Logger.d(response.toString());
                        if ("0".equals(response.getCode())) {
                            completeRegister(response.getData());

                        } else if (CodeConstant.THIRD_ACCOUNT_HAVE_BIND_55ACCOUNT.equals(response.getCode())) {
                            showToast(ToastType.ERROR, response.getMsg());
                        } else {
                            showToast(ToastType.ERROR, response.getMsg());
                        }
                    },
                    error -> {
                        showErrorToast(error);
                        dismissProgressDialog();
                    });
        }
    }

    private void completeRegister(LoginSuccessModelData response) {
        wrapUserObject(response);
        if (null != obj) {
            TalkingDataAppCpa.onRegister(obj.uid);
        }
        obj.ht_token = obj.token;
        UserManager.getInstance().setUser(obj);
        showToast(ToastType.COMMON_SUCCESS, getResources().getString(R.string.regist_regist_success));
        EventBus.getDefault().post(new LoginStateChangedEvent(true));
        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
        mContext.sendBroadcast(mIntent);
        setResult(TransConstant.IS_LOGIN);
        finishActivity();
    }

    private void wrapUserObject(LoginSuccessModelData entity) {
        if (entity != null) {
            obj.token = entity.getToken();
            obj.ht_token = entity.getToken();
            obj.uid = entity.getUid();
            obj.username = entity.getUsername();
            obj.mobile = entity.getMobile();
            obj.mobile_unique = entity.getMobileUnique();
            obj.refresh_token = entity.getRefreshToken();

            YbxTrace.getInstance().getTraceCommonBean().mid = entity.getUid() + "";

        }
    }

    private void finishActivity(){
        tvCommit.postDelayed(() -> finish(), 1500);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkCommitEnable();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 检查快速登录按钮是否可点击
     */
    private void checkCommitEnable() {
        if (etAccount.isHasText() && etPassWord.isHasText()) {
            tvCommit.setEnabled(true);
        } else {
            tvCommit.setEnabled(false);
        }
    }

    /**
     * 获取头像Base64编码
     *
     * @param avatar 头像本地url
     * @return 头像Base64编码
     */
    private String getUserAvatarBase64(String avatar) {
        Bitmap bmAvatar = ImageUtil.getSmallBitmap(avatar, 400, 400);
        return ImageUtil.compressImageBase64(bmAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(PhotoPickParameterObject.EXTRA_PARAMETER)) {
                    PhotoPickParameterObject photoPickParameterInfo = (PhotoPickParameterObject) data.getExtras().getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
                    String                   avatarPickResult       = photoPickParameterInfo.image_list.get(0);
                    mAvatarBase64 = getUserAvatarBase64(avatarPickResult);
                    Logger.d(mAvatarBase64);
                    ImageLoaderUtils.showLocationImage(avatarPickResult.contains("file:") ? avatarPickResult : "file:///" + avatarPickResult, ivAvatar);
                }
                break;
        }
    }
}
