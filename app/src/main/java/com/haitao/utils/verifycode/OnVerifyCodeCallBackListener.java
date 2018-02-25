package com.haitao.utils.verifycode;

import com.haitao.model.UserObject;

/**
 * Created by tqy on 2016/3/30.
 */
public interface OnVerifyCodeCallBackListener {
    void onError();
    void onSuccess();

    /**
     * 图形验证码返回的code type 0:无需验证，1：图片验证
     * @param code
     */
    void onSuccess(int type, String code);

    void onSuccess(UserObject userObject);
}
