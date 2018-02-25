package com.haitao.event;

/**
 * 登录状态改变事件
 *
 * @author 陶声
 * @since 2017-12-08
 */

public class LoginStateChangedEvent {
    public boolean isLogin;

    public LoginStateChangedEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
