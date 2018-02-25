package com.haitao.model;



/**
 * 试用报告
 * Created by tqy on 15/12/2.
 */
public class SampleReportObject extends BaseObject {
    public String id = "";
    public String tid="";
    public String author = "";
    public String authorid = "";
    public String subject = "";
    public String dateline = "";
    public String views = "";
    public String replies = "";
    public String[] pic = null;
    public String avatar = "";
    public String digest = "0";//是否是精华 0-否1-是
    public String class_name = "";

    public PhotoPickParameterObject mPhotoPickParameterInfo;
}
