package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.bigkoo.pickerview.OptionsPickerView;
import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.UserObject;
import com.haitao.utils.AddressUtils;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;
import com.haitao.view.HtItemView;
import com.haitao.view.dialog.GenderBSDlg;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haitao.utils.AddressUtils.cityList;
import static com.haitao.utils.AddressUtils.districtList;
import static com.haitao.utils.AddressUtils.provinceList;


/**
 * 编辑个人资料
 */
public class UserInfoUpdateActivity extends BaseActivity {

    @BindView(R.id.hv_title)      HtHeadView      mHvTitle;         // 标题
    @BindView(R.id.item_username) HtItemView      mItemUsername;    // 用户名
    @BindView(R.id.img_avatar)    CustomImageView mImgAvatar;       // 头像
    @BindView(R.id.item_gender)   HtItemView      mItemGender;      // 性别
    @BindView(R.id.item_area)     HtItemView      mItemArea;        // 地区

    private PhotoPickParameterObject mPhotoPickParameterInfo;

    private UserObject  obj;
    private GenderBSDlg mGenderBSDlg;

    private String mGender;
    private String mAvatarBase64;

    private String mProvince;
    private String mCity;
    private String mDisctrict;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, UserInfoUpdateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update);
        ButterKnife.bind(this);

        initVars();
        initViews(savedInstanceState);
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initViews(Bundle savedInstanceState) {
        mHvTitle.setOnRightClickListener(view -> updateUser());
    }

    private void initVars() {
        TAG = "个人资料";
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //        loadAreaData();
        if (HtApplication.isLogin()) {
            obj = UserManager.getInstance().getUser();

            mProvince = obj.province;
            mCity = obj.city;
            mDisctrict = obj.district;
            mGender = obj.gender;

            ImageLoaderUtils.showOnlineGifImage(obj.avatar, mImgAvatar);
            mItemUsername.setContent(obj.username);
            mItemGender.setContent(CommonUtils.getUserGender(obj.gender));
            mItemArea.setContent(obj.province + " " + obj.city + " " + obj.district);
        }
        // 初始化地址信息
        initAddressData();
    }

    /**
     * 用户信息修改请求
     */
    private void updateUser() {
        ProgressDialogUtils.show(mContext, "正在提交修改", true);
        ForumApi.getInstance().userAccountInfoSettingPost(mAvatarBase64, mGender, mProvince, mCity, mDisctrict, null,
                response -> {
                    if (mHvTitle == null)
                        return;
                    ProgressDialogUtils.dismiss();
                    if (TextUtils.equals(response.getCode(), "0")) {
                        ToastUtils.show(mContext, "修改成功");

                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_LOGIN);
                        mContext.sendBroadcast(mIntent);

                        finish();
                    } else {
                        Logger.d(response.toString());
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


    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }


    /**
     * 选择头像
     */
    @OnClick(R.id.rl_avatar)
    public void onAvatarClicked() {
        if (null == mPhotoPickParameterInfo) {
            mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.single_mode = true;
            mPhotoPickParameterInfo.croper_image = true;
        }
        photoPickWrapper();
    }


    /**
     * 选择性别
     */
    @OnClick(R.id.item_gender)
    public void onGenderClicked() {
        final String[] genders = {"男", "女"};

        if (mGenderBSDlg == null) {
            mGenderBSDlg = new GenderBSDlg(mContext).setOnGenderSelectListener(pos -> {
                //                mTvGender.setText(genders[pos]);
                mItemGender.setContent(genders[pos]);
                mGender = pos == 0 ? "1" : "2";
            });
        }
        mGenderBSDlg.show();
    }


    /**
     * 选择地区
     */
    @OnClick(R.id.item_area)
    public void onAreaClicked() {
        // 显示选择框
        if (provinceList.size() > 0 && cityList.size() > 0 && districtList.size() > 0) {
            OptionsPickerView picker = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
                mProvince = provinceList.get(options1);
                mCity = cityList.get(options1).get(options2);
                mDisctrict = options3 >= 0 ? districtList.get(options1).get(options2).get(options3) : "";
                //                mTvArea.setText(mProvince + " " + mCity + " " + mDisctrict);
                mItemArea.setContent(mProvince + " " + mCity + " " + mDisctrict);
            }).build();
            picker.setPicker(provinceList, cityList, districtList);
            picker.show();
        }
    }

    /**
     * 初始化地区数据
     */
    private void initAddressData() {
        try {
            AddressUtils.parseFromJson(mContext);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                finish();
            }
        }
        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(PhotoPickParameterObject.EXTRA_PARAMETER)) {
                    PhotoPickParameterObject photoPickParameterInfo = (PhotoPickParameterObject) data.getExtras().getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
                    String                   avatarPickResult       = photoPickParameterInfo.image_list.get(0);
                    mAvatarBase64 = getUserAvatarBase64(avatarPickResult);
                    ImageLoaderUtils.showLocationImage(avatarPickResult.contains("file:") ? avatarPickResult : "file:///" + avatarPickResult, mImgAvatar);
                }
                break;
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
    public void onBackPressed() {
        ProgressDialogUtils.dismiss();
        super.onBackPressed();
    }
}
