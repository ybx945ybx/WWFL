package com.haitao.model;
/**
 * 转运查询
 * Created by tqy on 15/12/2.
 */
public class LogisticsObject extends BaseObject {
    /**
     * 转运公司ID
     */
    public String tid;
    /**
     * 转运公司
     */
    public String tname;
    public String tlogo;
    /**
     * 运单号
     */
    public String tracking_no;
    /**
     * 订单号
     */
    public String order_id;
    /**
     * 查询结果-时间
     */
    public String time;
    /**
     * 查询结果-描述
     */
    public String content;
	
}