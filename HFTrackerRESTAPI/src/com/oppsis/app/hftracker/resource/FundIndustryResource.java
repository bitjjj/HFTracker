package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.FundIndustry;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/industry")
public class FundIndustryResource extends BaseResource{
	
	protected String getCacheName(){
		return Constants.CacheNames.CACHE_INDUSTRY_LIST;
	}
	
	@GET
	@Path("/fund/default")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getIndustrytByDefault(@QueryParam("name") String name) {

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_INDUSTRY_BY_DEFAULT), name,name);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundIndustry.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/fund")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getIndustrytByFundName(@QueryParam("name") String name,@QueryParam("date") String date) {

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_INDUSTRY_BY_NAME), name,date);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundIndustry.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/funds")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDateListByFundNames(@QueryParam("name") String[] names,@QueryParam("date") String date) {
		String name = "";
		for(String n:names){
			name += "fund_name='" + n + "' OR ";
		}
		
		name = name.substring(0, name.lastIndexOf(" OR "));
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_INDUSTRY_BY_NAMES), name,date);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundIndustry.class);
		
		//System.out.println(result);
		return result;
	}
}
