package com.haitao.common.annotation;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单类型
 *
 * @author 陶声
 * @since 2017-08-09
 */

@IntDef({OrderType.ORDER_NORMAL, OrderType.ORDER_LOST})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderType {

    int ORDER_NORMAL = 0;   // 正常订单

    int ORDER_LOST   = 1;   // 丢单
}