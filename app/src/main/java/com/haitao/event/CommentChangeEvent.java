package com.haitao.event;

/**
 * Created by a55 on 2017/12/18.
 */

public class CommentChangeEvent {
    public String  content;
    public boolean isSubmit;

    public CommentChangeEvent(String content, boolean isSubmit) {
        this.content = content;
        this.isSubmit = isSubmit;
    }
}
