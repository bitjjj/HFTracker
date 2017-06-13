package com.oppsis.app.hftracker.db;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class DBModelMapper {

	public static List<Object> getModels(List<Object> queryResults, Class clazz) {
		List<Object> results = new ArrayList<Object>();

		for (int i = 0; i < queryResults.size(); i++) {

			results.add(getModel(queryResults.get(i), clazz));

		}

		return results;
	}

	public static Object getModel(Object result, Class clazz) {

		Object o = new Object();
		try {
			o = clazz.newInstance();
			BeanUtils.copyProperties(o, result);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return o;
	}

}
