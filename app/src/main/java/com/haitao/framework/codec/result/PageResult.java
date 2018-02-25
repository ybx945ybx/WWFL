package com.haitao.framework.codec.result;

import java.io.Serializable;
import java.util.ArrayList;

/**  
 *   
 * PageResult  
 * 分页查询结果
 */
public class PageResult<T> implements Serializable{
    //总条数
    public int total = 0;
    //总页数
    public int pageCount = 0;
    //查询页数
    public int page = 1;
    //分页大小，手机客户端默认20
    public int pagesize = 20;
    //查询结果
    public ArrayList<T> entityList = null;

}
