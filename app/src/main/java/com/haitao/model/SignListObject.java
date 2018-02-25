package com.haitao.model;

import java.util.ArrayList;

/**
 * 签到列表
 * Created by tqy on 15/12/2.
 */
public class SignListObject extends BaseObject {
    private static final long                  serialVersionUID  = 1L;
    //今天
    public               String                now_time          = "";
    //连续签到天数
    public               String                continue_sign_num = "";
    //签到列表
    public               ArrayList<SignObject> list              = null;
    //成功签到的提示
    public               String                sign_msg          = "";
    //今日签到金币提示
    public               String                desc              = "";

    public String faceicon   = "";//话题
    public String type       = "";//是否话题签到
    public String topic      = "";//当前签到的数据
    public String tid        = "";//话题帖子id
    public String topic_gold = "";
    //今日是否有话题签到
    public String is_topic   = "0";
    public String ad         = "";

    public static class SignAdObject {
        public String url;
        public String img;
    }

    // 广告位
    public SignAdObject sign_ad;

}