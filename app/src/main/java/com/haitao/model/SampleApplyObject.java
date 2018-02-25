package com.haitao.model;


import java.util.ArrayList;

/**
 * 免费申请
 * Created by tqy on 15/12/2.
 */
public class SampleApplyObject extends BaseObject {
    public String id = "";
    /**
     * 试用id
     */
    public String trial_id = "";
    public String uid = "";
    /**
     * 用户名
     */
    public String username = "";
    /**
     * 申请是否成功，1：成功，0：失败
     */
    public String status = "0";
    /**
     * 头像
     */
    public String avatar = "";

    public String reason = "";

    public String real_name = "";

    public String mobile = "";

    public String province = "";

    public String city = "";

    public String address = "";

    public String postcode = "";

    public String content = "";





    public String page = "1";
    public String lpp = "20";

    //免费试用名单返回
    public int _total = 0;
    public int _pagecount = 0;
    public ArrayList<SampleApplyObject> success;
    public ArrayList<SampleApplyObject> fail;
    public ArrayList<SampleApplyObject> all;

}
