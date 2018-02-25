package com.haitao.model;


import java.util.ArrayList;

/**
 * 金币兑换
 * Created by tqy on 15/12/2.
 */
public class ExchangeObject extends BaseObject {
    public String tid = "";
    public String aid = "";
    public String name = "";
    public String hot = "";
    public String timeformat = "";
    public String prices = "";
    public String status = "";
    public String pic = "";
    public String subject = "";
    public String views = "";
    public String replies = "";
    public String number = "";
    public String starttime = "";
    public String starttimeto = "";
    public String thumb = "";
    public String content = "";
    public String is_apply = "";

    public String type = "0";//1-即将开始2-进行中3-已经结束 0-全部.
    public String page = "1";
    public String lpp = "20";

    //免费试用返回
    public int _total = 0;
    public int _pagecount = 0;
    public ExchangeTotalObject _data = null;


}
