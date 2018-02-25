package com.haitao.imp;


import com.haitao.framework.service.DefaultViewContext;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.framework.service.ServiceFactory;

public class VF {

    static {
        StartUP.StartUp();
    }

    public static <T, S extends IEntityService<T>> IViewContext<T, S> get(Class entityClass) {
        DefaultViewContext<T, S> result = new DefaultViewContext<T, S>();

        try {
            result.setEntity((T) entityClass.newInstance());
        } catch (Exception e) {
        }
        result.setService((S) ServiceFactory.getService(entityClass));

        return result;
    }

    public static <T> IViewContext<T, IEntityService<T>> getDefault(Class entityClass) {
        return get(entityClass);
    }
}
