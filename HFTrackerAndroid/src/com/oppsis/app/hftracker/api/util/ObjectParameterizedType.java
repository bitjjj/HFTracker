package com.oppsis.app.hftracker.api.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ObjectParameterizedType<X> implements ParameterizedType{

	private Class<?> wrapped;
	
	public ObjectParameterizedType(Class<X> wrapped) {
        this.wrapped = wrapped;
    }
	
	@Override
	public Type[] getActualTypeArguments() {
		
		return new Type[] {wrapped};
	}

	@Override
	public Type getOwnerType() {
		
		return null;
	}

	@Override
	public Type getRawType() {
		
		return this.wrapped;
	}
	 
	
}