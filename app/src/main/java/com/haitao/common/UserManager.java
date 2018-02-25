package com.haitao.common;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.haitao.common.Constant.SPConstant;
import com.haitao.model.UserObject;
import com.haitao.utils.SPUtils;
import com.haitao.utils.TextUtil;

/**
 * 用户信息管理，添加、修改、清除
 * Created by Administrator on 2016/4/29.
 */
public class UserManager {
    private static UserManager sInstance;
    private UserObject userObject;

    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    private UserManager() {
    }

    /**
     * 设置用户
     * @param userObject
     */
    public void setUser(UserObject userObject){
        if(null == userObject)
            return;
        this.userObject = userObject;
        SPUtils.put(HtApplication.getInstance(), SPConstant.USER, JSON.toJSONString(userObject));
    }

    /**
     * 获取用户信息
     * @return
     */
    public UserObject getUser(){
        if(null != this.userObject)
            return this.userObject;
        String strUser = (String) SPUtils.get(HtApplication.getInstance(), SPConstant.USER,"");
        if(TextUtils.isEmpty(strUser))
            return null;
        this.userObject = JSON.parseObject(strUser,new TypeReference<UserObject>(){});
        return this.userObject;
    }


    /**
     * 获取用户ID
     * @return
     */
    public String getUserId(){
        if(null != getUser())
            return this.userObject.uid;
        return "";
    }

    /**
     * 获取用户名称
     * @return
     */
    public String getUserName(){
        if(null != getUser())
            return this.userObject.username;
        return "";
    }
    /**
     * 获取海淘token
     * @return
     */
    public String getHtToken(){
        if(null != getUser())
            return this.userObject.ht_token;
        return "";
    }
    /**
     * 获取闪淘token
     * @return
     */
    public String getStToken(){
        if(null != getUser())
            return this.userObject.st_token;
        return "";
    }

    /**
     * 清空用户信息
     */
    public void clearUser(){
        this.userObject = null;
        SPUtils.put(HtApplication.getInstance(),SPConstant.USER,"");
    }

    /**
     * 判断是否登录
     * @return
     */
    public boolean isLogin(){
        return null != getUser() && !TextUtils.isEmpty(this.userObject.ht_token);
    }


}
