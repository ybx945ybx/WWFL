package com.haitao.model.forum;

import com.haitao.model.BaseObject;

import java.util.List;

/**
 * 达人
 * Created by apple on 17/3/14.
 */

public class ForumTalentObject extends BaseObject {
    /**
     * 用户ID
     **/
    public String uid = null;
    /**
     * 用户名
     **/
    public String username = null;
    /**
     * 头像
     **/
    public String avatar = null;
    /**
     * 达人类别
     **/
    public String category = null;
    /**
     * 类别图片
     **/
    public String categoryPic = null;
    /**
     * 达人等级
     **/
    public String level = null;
    /**
     * 发布主题数
     **/
    public String topicsCount = null;
    /**
     * 精华主题数
     **/
    public String digestTopicsCount = null;
    /**
     * 金币数
     **/
    public String gold = null;
}
