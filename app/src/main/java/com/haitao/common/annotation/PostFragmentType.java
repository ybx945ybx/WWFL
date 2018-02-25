package com.haitao.common.annotation;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 帖子Fragment类型
 *
 * @author 陶声
 * @since 2017-08-11
 */
@IntDef({PostFragmentType.MY_POST,
        PostFragmentType.MY_FAV,
        PostFragmentType.SEARCH_POST,
        PostFragmentType.SEARCH_UNBOXING})
@Retention(RetentionPolicy.SOURCE)
public @interface PostFragmentType {

    int MY_POST = 0;        // 我发布的

    int MY_FAV = 1;         // 我收藏的

    int SEARCH_POST = 2;    // 搜索帖子

    int SEARCH_UNBOXING = 3;// 搜索晒单
}