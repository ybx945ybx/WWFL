package com.haitao.framework.service;

import com.haitao.framework.codec.IConverter;
import com.haitao.framework.codec.result.PageResult;


/**  
 *   
 * 服务接口定义类
 * 类作用描述
 */
public interface IServiceDefine<T,S> {
    /**
     * 名称
     * @return
     * @since  1.0.0
     */
    public String getName();

    /**
     * 设置实体对象名称
     * @param name
     * @since  1.0.0
     */
    public void setName(String name);
    
    S getService();
    /**  
     * 业务所属的对象
     * @return
     * @since  1.0.0  
    */
    Class<T> getEntityClass();
    
    /**  
     * 服务地址
     * @return
     * @since  1.0.0  
    */
    String getURL();
    
    /**  
     * 对象转换为请求的参数定义
     * @return
     * @since  1.0.0  
    */
    IConverter<T,String[]> requestParamConverter();
    
    /**  
     * Json对象转换为对象的解码器
     * @return
     * @since  1.0.0  
    */
    IConverter<String,T> decoder();
    
    /**  
     * Json对象转换为对象的解码器
     * @return
     * @since  1.0.0  
    */
    IConverter<String,PageResult<T>> getPageDecoder();
    
}
