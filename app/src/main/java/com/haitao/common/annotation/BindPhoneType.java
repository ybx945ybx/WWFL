package com.haitao.common.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 绑定手机号 - 类型
 *
 * @author 陶声
 * @since 2017-08-21
 */

@IntDef({BindPhoneType.NEW, BindPhoneType.UPDATE})
@Retention(RetentionPolicy.SOURCE)
public @interface BindPhoneType {
    int NEW = 0;   // 绑定新号码

    int UPDATE = 1;   // 修改号码
}