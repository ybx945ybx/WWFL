package com.haitao.framework.codec;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

public class DefaultResultDecoder<T> implements IConverter<String, T> {

    private Class entityClass = null;

    public DefaultResultDecoder(Class entityClass) {
        super();
        this.entityClass = entityClass;
    }

    @Override
    public T converter(String entity) throws Exception {
        if (TextUtils.isEmpty(entity))
            return null;
        return (T) JSON.parseObject(entity, entityClass);
    }
}
