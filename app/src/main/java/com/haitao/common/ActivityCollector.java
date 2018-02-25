package com.haitao.common;

import com.haitao.activity.BaseActivity;

import java.util.Stack;

/**
 * Created by a55 on 2017/11/22.
 */

public class ActivityCollector {

    private static Stack<BaseActivity> stack = new Stack<BaseActivity>();

    /**
     * 注册Activity
     */
    public static void add(BaseActivity activity) {
        if (activity != null) {
            stack.push(activity);
        }
    }

    /**
     * 反注册Handler的引用,防止内存泄漏
     */
    public static void remove(BaseActivity handler) {
        stack.remove(handler);
    }

    public static BaseActivity getTopActivity() {
        return stack.peek();
    }

    public static BaseActivity getSecondActivity() {
        if (stack.size() > 1) {
            return stack.get(stack.size() - 2);
        } else {
            return null;
        }
    }

}
