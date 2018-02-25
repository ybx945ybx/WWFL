package com.haitao.framework.uri;

/**
 * url基类
 */
public class BaseURIContext {

    private String baseURI= null;

    public BaseURIContext(String baseURI) {
        super();
        this.baseURI = baseURI;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }
    
    
}
