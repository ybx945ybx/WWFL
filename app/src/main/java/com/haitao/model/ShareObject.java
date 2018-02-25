package com.haitao.model;

/**
 * 分享数据
 * Created by tqy on 15/12/2.
 */
public class ShareObject extends BaseObject {
    /**
     * 分享类型
     * 1优惠
     * 2帖子
     * 3邀请好友
     */
    public String share_type    = "";
    /**
     * 分享内容
     * 优惠对应优惠ID
     * 帖子对应帖子ID
     */
    public String share_content = "";


    public String title      = "";
    public String desc       = "";
    public String desc_weibo = "";
    public String link       = "";
    public String imgUrl     = "";

    public String remarkFirst = "";
    public String remarkTwo   = "";


}