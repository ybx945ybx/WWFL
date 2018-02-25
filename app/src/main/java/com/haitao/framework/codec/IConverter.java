package com.haitao.framework.codec;

public interface IConverter<T,S> {
	
	S converter(T entity) throws Exception ;
}
