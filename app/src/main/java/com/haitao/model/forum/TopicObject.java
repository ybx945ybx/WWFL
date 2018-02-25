package com.haitao.model.forum;

import com.haitao.model.BaseObject;

import java.util.List;

import io.swagger.client.model.TopicModelTags;


/**
 * 帖子
 * Created by apple on 17/3/14.
 */

public class TopicObject extends BaseObject{
    /**
     * 帖子ID
     **/
    public String tid = null;
    /**
     * 版块ID
     **/
    public String fid = null;
    /**
     * 标题
     **/
    public String title = null;
    /**
     * 发布时间
     **/
    public String postTime = null;
    /**
     * 显示顺序
     **/
    public String displayorder = null;
    /**
     * 作者名字
     **/
    public String authorName = null;
    /**
     * 作者ID
     **/
    public String authorUid = null;
    /**
     * 查看次数
     **/
    public String viewCount = null;
    /**
     * 回复次数
     **/
    public String replyCount = null;
    /**
     * 附件类型
     **/
    public String attachmentType = null;
    /**
     * 主题精华级别
     **/
    public String digest = null;
    /**
     * 主题热度
     **/
    public String heats = null;
    /**
     * 分类名称
     **/
    public String categoryName = null;
    /**
     * 点赞数
     **/
    public String praiseCount = null;
    /**
     * 是否是热帖
     **/
    public String isHot = null;
    /**
     * 头像地址
     **/
    public String avatar = null;
    /**
     * 封面大图
     **/
    public String pic = null;
    /**
     * 帖子包含的图片的地址，用于主题显示
     **/
    public List<String> pics = null;
    /**
     * 相关标签
     **/
    public List<TopicModelTags> tags = null;
    /**
     * 分享内容正文
     **/
    public String shareContent = null;
    /**
     * 分享内容链接
     **/
    public String shareUrl = null;
    /**
     * 分享内容封面图片
     **/
    public String sharePic = null;
    /**
     * 是否推荐帖
     */
    public boolean isRecommendTopic = false;
}
