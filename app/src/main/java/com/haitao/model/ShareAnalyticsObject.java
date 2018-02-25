package com.haitao.model;

/**
 * 分享统计
 *
 * @author 陶声
 * @since 2017-07-28
 */

public class ShareAnalyticsObject {
    public String type; // 对应eventId
    public String id;   // 对应Id

    public ShareAnalyticsObject(String type) {
        this.type = type;
    }

    public ShareAnalyticsObject(String type, String id) {
        this.type = type;
        this.id = id;
    }
}
