package com.haitao.common.annotation;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 搜索好友类型
 *
 * @author 陶声
 * @since 2017-08-17
 */

@IntDef({SearchFriendType.MESSAGE, SearchFriendType.FRIEND})
@Retention(RetentionPolicy.SOURCE)
public @interface SearchFriendType {

    int MESSAGE = 0;    // 消息

    int FRIEND = 1;     // 好友
}