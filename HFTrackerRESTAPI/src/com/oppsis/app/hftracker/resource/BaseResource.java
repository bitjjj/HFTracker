package com.oppsis.app.hftracker.resource;

import java.util.List;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.cache.EhcacheManager;
import com.oppsis.app.hftracker.db.DBModelMapper;
import com.oppsis.app.hftracker.db.MysqlJdbc;

public class BaseResource {
	protected MysqlJdbc jdbcUtil = new MysqlJdbc();
	
	protected String getCacheName(){
		throw new UnsupportedOperationException("You must implement this method in subclass");
	}
	
	protected JSONObject queryList(String sql,Class model){
		JSONObject result = new JSONObject();
		try {
			Object cacheObj = EhcacheManager.getInstance().get(getCacheName(),sql);
			if(cacheObj == null){
				//System.out.println("first time!!!");
				List<Object> list = jdbcUtil.query(sql);
				result.put("count", list.size());
				result.put("results", DBModelMapper.getModels(list, model));
				
				EhcacheManager.getInstance().put(getCacheName(),sql, result);
			}
			else{
				//System.out.println("got cache!!!");
				result = (JSONObject) cacheObj;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
