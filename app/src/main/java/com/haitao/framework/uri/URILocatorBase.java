package com.haitao.framework.uri;


import java.util.HashMap;
import java.util.Map;


public class URILocatorBase implements IURILocator {

    private Map<String,String> uriMap = new HashMap<String,String>();
    
    private BaseURIContext baseURI = null;

    
    public URILocatorBase(BaseURIContext baseURI) {
        this.baseURI = baseURI;
    }

    @Override
    public String getURI(Object method){
        return uriMap.get(method.toString());
    }
    
    /**  
     * 添加相对路径
     * @param method
     * @param reletiveURI
     * @return
     * @since  1.0.0  
    */
    public URILocatorBase add(String method,String reletiveURI)
    {
        this.add(method, baseURI.getBaseURI(), reletiveURI);
        return this;
    }
    
    /**  
     * add(这里用一句话描述这个方法的作用)  
     * (这里描述这个方法适用条件 – 可选)  
     * @param method
     * @param baseURI
     * @param reletiveURI
     * @return
     * @since  1.0.0  
    */
    public URILocatorBase add(String method,String baseURI,String reletiveURI)
    {
        uriMap.put(method, String.format("%s/%s", baseURI, reletiveURI));
        return this;
    }
}
