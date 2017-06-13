package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.SPXData;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/spx")
public class SPXDataResource extends BaseResource{
	
	protected String getCacheName(){
		return Constants.CacheNames.CACHE_SPX;
	}
	
	
	@GET
	@Path("/pe")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getPE() {
		return getData(Constants.SPXTables.PE);
	}
	
	@GET
	@Path("/shillerpe")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getShillerPE() {
		return getData(Constants.SPXTables.SHILLER_PE);
	}
	
	@GET
	@Path("/eps")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getEPS() {
		return getData(Constants.SPXTables.EPS);
	}
	
	@GET
	@Path("/dps")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDPS() {
		return getData(Constants.SPXTables.DPS);
	}
	
	private JSONObject getData(String name){
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_SPX_DATA), name,name);
		//System.out.println(sql);
		JSONObject result = queryList(sql, SPXData.class);
		
		//System.out.println(result);
		return result;
	}
	
}
