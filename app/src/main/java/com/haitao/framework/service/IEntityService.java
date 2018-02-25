package com.haitao.framework.service;


import android.util.Pair;

import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;

import java.io.File;

public interface IEntityService<T> {
    void asynFunction(String method, T entity, IAsynServiceHandler<T> handler);

    void asynFunctionWidthFile(String method, T entity, Pair<String, File>[] files, IAsynServiceHandler<T> handler);

    void asynQuery(String method, T entity, IAsynServiceHandler<T> handler);

    void asynQuery(PageResult page, String method, T entity, IAsynServiceHandler<T> handler);
}
