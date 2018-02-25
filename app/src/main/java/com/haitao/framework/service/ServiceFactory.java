package com.haitao.framework.service;

import com.haitao.framework.codec.DefaultResultDecoder;
import com.haitao.framework.codec.ParameterConvertor;
import com.haitao.framework.http.HTTPEntityService;

import java.util.HashMap;
import java.util.Map;


public class ServiceFactory {


    private static Map<Class, Map<String, IServiceDefine>> defines       = new HashMap<Class, Map<String, IServiceDefine>>();
    private static Map<Class, Object>                      entityService = new HashMap<Class, Object>();

    public static <T> T getService(Class entityClass) {
        T result = (T) entityService.get(entityClass);
        if (null == result) {
            result = (T) new HTTPEntityService(entityClass);
            entityService.put(entityClass, result);

        }
        return result;
    }

    public static void addService(Class entityClass, Object service) {
        entityService.put(entityClass, service);
    }

    public static <T, S> IServiceDefine<T, S> getServiceDefine(Class<T> entityClass, String method) {
        IServiceDefine<T, S> result = null;

        Map<String, IServiceDefine> entityServices = getEntityServiceDefine(entityClass);

        result = entityServices.get(method);
        if (null == result) {
            result = new ServiceDefineBase(entityClass, method, new ParameterConvertor<T>(), new DefaultResultDecoder(entityClass), new HTTPEntityService(entityClass));
            entityServices.put(method, result);
        }
        return result;
    }

    public static Map<String, IServiceDefine> getEntityServiceDefine(Class entityClas) {
        Map<String, IServiceDefine> entityServices = defines.get(entityClas);
        if (null == entityServices) {
            entityServices = new HashMap<String, IServiceDefine>();
            defines.put(entityClas, entityServices);
        }
        return entityServices;
    }

    public static void addServiceDefine(IServiceDefine sd) {
        getEntityServiceDefine(sd.getEntityClass()).put(sd.getName(), sd);
    }
}
