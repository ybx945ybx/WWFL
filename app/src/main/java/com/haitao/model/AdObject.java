package com.haitao.model;

/**
 * 广告图
 * Created by tqy on 15/12/2.
 */
public class AdObject extends BaseObject {
    private static final long   serialVersionUID = 1L;
    /**
     * id
     */
    public               String id               = "";
    /**
     * 标题
     */
    public               String title            = "";
    /**
     * 图片地址
     */
    public               String pic              = "";
    /**
     * 类型 d：优惠,s：商家,st：闪淘,b：帖子,w：网页
     */
    public               String type             = "";
    /**
     * 值
     */
    public               String value            = "";

    @Override
    public String toString() {
        return "AdObject{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}