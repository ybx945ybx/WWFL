package com.haitao.common.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 提现账户 - 类型
 *
 * @author 陶声
 * @since 2017-08-21
 */

@StringDef({WithdrawModeKey.ALIPAY, WithdrawModeKey.PAYPAL, WithdrawModeKey.CARD, WithdrawModeKey.DEBIT_CARD})
@Retention(RetentionPolicy.SOURCE)
public @interface WithdrawModeKey {
    String ALIPAY = "alipay";   // 支付宝

    String PAYPAL = "paypal";   // paypal

    String CARD = "card";   // 信用卡

    String DEBIT_CARD = "debit_card";   // 借记卡
}