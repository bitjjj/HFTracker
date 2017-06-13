package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/datelist")
public class FundDateListResource extends BaseResource{
	
	protected String getCacheName(){
		return Constants.CacheNames.CACHE_DATE_LIST;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getWholeDateList() {

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_WHOLE_DATE_LIST));
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundDateList.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/fund")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDateListByFundName(@QueryParam("name") String name) {

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_DATE_LIST_BY_NAME), name);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundDateList.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/funds")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDateListByFundNames(@QueryParam("name") String[] names) {
		String name = "";
		for(String n:names){
			name += "fund_name='" + n + "' OR ";
		}
		
		name = name.substring(0, name.lastIndexOf(" OR "));
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_DATE_LIST_BY_NAMES), name);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundDateList.class);
		
		//System.out.println(result);
		return result;
	}
	
}
