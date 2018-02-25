package com.haitao.model.forum;


import com.haitao.model.BaseObject;

import java.util.ArrayList;

/**
 * 帖子评论
 * Created by apple on 17/3/14.
 */

public class ForumCommentObject extends BaseObject{
    public String tid = "";
    public String pid = "";
    public String author_id = "";
    public String author_name = "";
    public String author_avatar = "";
    public String content = "";
    public String floor_num = "";
    public String praise_count = "";
    public String post_time = "";
    public ArrayList<String> pics = null;
    public String is_praised = "";
    public String is_targeted = "";
    public ForumCommentObject quotation = null;

}
