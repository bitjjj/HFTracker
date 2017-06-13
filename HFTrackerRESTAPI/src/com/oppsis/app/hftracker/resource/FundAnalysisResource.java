package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/analysis")
public class FundAnalysisResource extends BaseResource{
	
	protected String getCacheName(){
		return Constants.CacheNames.CACHE_ANALYSIS;
	}
	

	@GET
	@Path("/mutual")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getMutualHoldings(@QueryParam("name") String[] names,@QueryParam("date") String date,
			@QueryParam("page") int page) {

		if(page == -1)page = 0;
		
		String name = "";
		for(String n:names){
			name += "fund_name='" + n + "' OR ";
		}
		
		name = name.substring(0, name.lastIndexOf(" OR "));
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_MUTUAL_HOLDINGS), 
				date, name, names.length-1, page*Constants.PAGE_SIZE_20);
		System.out.println(sql);
		JSONObject result = queryList(sql, FundAnalysis.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/rank")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getMutualHoldingsRank(@QueryParam("date") String date,@QueryParam("securityType") String securityType,@QueryParam("page") int page) {

		if(page == -1)page = 0;
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_MUTUAL_HOLDINGS_RANK), 
				date,securityType, page*Constants.PAGE_SIZE_20);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundAnalysis.class);
		
		//System.out.println(result);
		return result;
	}
	
	
}
