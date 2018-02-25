package com.haitao.common.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Toast类型
 * <p>
 * Created by a55 on 2017/12/8.
 */

@IntDef({ToastType.COMMON_SUCCESS, ToastType.WARNING, ToastType.ERROR, ToastType.COPPY_SUCCESS, ToastType.SIGN_SUCCESS})
@Retention(RetentionPolicy.SOURCE)
public @interface ToastType {
    int COMMON_SUCCESS = 0;      // 通用成功
    int WARNING        = 1;      // 警告
    int ERROR          = 2;      // 错误
    int COPPY_SUCCESS  = 3;      // 复制成功
    int SIGN_SUCCESS   = 4;      // 签到成功

}