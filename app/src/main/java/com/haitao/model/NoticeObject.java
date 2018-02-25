package com.haitao.model;

/**
 * 消息通知
 * Created by tqy on 15/12/2.
 */
public class NoticeObject extends BaseObject {
    private static final long serialVersionUID = 1L;
    /**
     * 消息类型（mypost 帖子动态;system 系统提醒;pm 消息）
     */
    public String type = "";


    public String isnew = "";//是否是新消息(0为否，1为是)
    public String dateline = "";//时间
    public String pmtype = "";//消息类型(1为单聊，2为群聊)
    public String members = "";//参与聊天人数
    public String lastauthorid = "";//发送消息人ID
    public String authorid="";//
    public String author = "";//发送消息人
    public String content = "";//消息
    public String avatar = "";//头像

    public String new_pm = "";
    public String new_notice = "";
    public String total = "";
    public String newpm = "";

    public String news = "0";//新信息条数
    public String type_id;//类型ＩＤ
    public String app_storey;//楼层
    public String app_page;//页数
    public String message;//回复
    public String tid;//贴子ＩＤ
    public String touid;
    /**
     * 帖子动态，回复的图片集
     */
    public String[] pic = null;
    public PhotoPickParameterObject mPhotoPickParameterInfo;


}