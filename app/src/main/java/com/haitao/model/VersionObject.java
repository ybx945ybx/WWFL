package com.haitao.model;

/**
 * 版本
 * Created by tqy on 15/12/2.
 */
public class VersionObject extends BaseObject {
    private static final long serialVersionUID = 1L;
    /**
     * 各分类的version
     */
    public String data_dict = "0";
    /**
     * 全部商家的version
     */
    public String stores = "";
    /**
     * 贴子分类
     */
    public String thread_class = "";
    /**
     * 平台相关的信息
     */
    public PlatformObject platform = null;

    public String sms_tel_number = "";
    /**
     * 邀请好友返多少美金
     */
    public String invite_money = "";
    /**
     * 版块分类版本号
     */
    public String thread_class_new = "";

}