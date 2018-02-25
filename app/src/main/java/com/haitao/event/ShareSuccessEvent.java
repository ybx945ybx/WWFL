package com.haitao.event;

/**
 * 分享成功事件
 *
 * @author 陶声
 * @since 2017-09-29
 */

public class ShareSuccessEvent {
    public String platform;

    public ShareSuccessEvent() {
    }

    public ShareSuccessEvent(String platform) {
        this.platform = platform;
    }
}
