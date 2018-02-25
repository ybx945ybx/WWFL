package com.haitao.event;

/**
 * Created by a55 on 2017/11/2.
 */

public class FriendStatChangeEvent {
    //* 是否是好友 - 0：不是好友 1：已是好友 2：自己 3:已向对方发送过好友请求 4：对方请求添加我为好友

    public String isFreind;

    public FriendStatChangeEvent(String isFreind) {
        this.isFreind = isFreind;
    }
}
