package com.haitao.model;


import java.util.ArrayList;

/**
 * 免费试用
 * Created by tqy on 15/12/2.
 */
public class SampleObject extends BaseObject {

    public String id;
    /**
     * 标题
     */
    public String title = "";
    /**
     * 人气
     */
    public String view_count = "";
    /**
     * 规格
     */
    public String size = "";
    /**
     * 数量
     */
    public String number = "";
    /**
     * 条件
     */
    public String condition = "0";
    /**
     * 开始时间
     */
    public String start_time = "";
    /**
     * 结束时间
     */
    public String end_time = "";
    /**
     * 供应商
     */
    public String provider = "";
    public String apply_count = "";
    /**
     * 状态（1即将开始，2正在进行，3结束）
     */
    public String type;
    /**
     * 描述
     */
    public String describe;
    /**
     * 是否已经申请(0否，1是)
     */
    public String apply = "0";
    /**
     * 是否提交试用报告
     */
    public String report = "1";
    /**
     * 提交试用报告需要的
     */
    public String fid = "";
    public String forum_name = "";
    public String typeid = "";
    /**
     * 帖子id
     */
    public String tid = "";
    public String time;
    public String image;

    public String page = "1";
    public String lpp = "20";

    //免费试用返回
    public int _total = 0;
    public int _pagecount = 0;
    public ArrayList<SampleObject> end;
    public ArrayList<SampleObject> ongoing;



}
