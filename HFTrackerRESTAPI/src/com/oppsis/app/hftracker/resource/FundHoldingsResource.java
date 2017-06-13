package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/holdings")
public class FundHoldingsResource extends BaseResource{

	protected String getCacheName(){
		return Constants.CacheNames.CACHE_HOLDINGS;
	}
	
	@GET
	@Path("/fund/default")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getHoldingsByDefault(@QueryParam("name") String name,@QueryParam("page") int page) {
		
		if(page == -1)page = 0;

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_HOLDINGS_BY_DEFAULT), 
				name,name,page*Constants.PAGE_SIZE_20);
		System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/fund")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getHoldingsByFundName(@QueryParam("name") String name,@QueryParam("date") String date,
			@QueryParam("sort") String sort,@QueryParam("desc") String desc,@QueryParam("page") int page,
			@QueryParam("securityType") String securityType) {
		
		if(page == -1)page = 0;
		if(sort == null || "".equals(sort))sort = "port";
		if(desc == null || desc.equals(""))desc = "ASC";
		else desc = "DESC";
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_HOLDINGS_BY_NAME), 
				name,date,securityType,sort,desc,page*Constants.PAGE_SIZE_20);
		//System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/mutual")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getGroupHoldings(@QueryParam("security") String security,@QueryParam("ticker") String ticker,
			@QueryParam("date") String date,@QueryParam("securityType") String securityType) {
		
		String where = "";
		if(ticker == null || "".equals(ticker)){
			where = "`security`='" + security + "'";
		}
		else{
			where = "`ticker`='" + ticker + "'";
		}
		
		if(securityType ==null || "".equals(security))securityType = Constants.SecurityTypes.STOCK;
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.GROUP_HOLDINGS), where,date,securityType);
		//System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/history")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getHoldingsByTickerAndName(@QueryParam("security") String security,@QueryParam("ticker") String ticker,
			@QueryParam("name") String name,@QueryParam("securityType") String securityType) {
		
		String where = "";
		if(ticker == null || "".equals(ticker)){
			where = "`security`='" + security + "'";
		}
		else{
			where = "`ticker`='" + ticker + "'";
		}
		
		if(securityType ==null || "".equals(security))securityType = Constants.SecurityTypes.STOCK;
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_HOLDINGS_BY_TICKER_NAME), where,name,securityType);
		//System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Path("/comparator")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getHoldingsByTickerAndNames(@QueryParam("security") String security,@QueryParam("ticker") String ticker,
			@QueryParam("securityType") String securityType,@QueryParam("name") String[] names) {
		
		String where = "";
		if(ticker == null || "".equals(ticker)){
			where = "`security`='" + security + "'";
		}
		else{
			where = "`ticker`='" + ticker + "'";
		}
		
		String name = "";
		for(String n:names){
			name += "fund_name='" + n + "' OR ";
		}
		
		name = name.substring(0, name.lastIndexOf(" OR "));
		
		if(securityType ==null || "".equals(security))securityType = Constants.SecurityTypes.STOCK;
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_HOLDINGS_BY_TICKER_NAMES), where,name,securityType);
		//System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		
		return result;
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getHoldingsByTicker(@QueryParam("ticker") String ticker,@QueryParam("page") int page) {

		if(page == -1)page = 0;
		
		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_HOLDINGS_BY_TICKER), ticker.toUpperCase(),page*Constants.PAGE_SIZE_20);
		//System.out.println(sql);
		
		JSONObject result = queryList(sql, FundHolding.class);
		//System.out.println(result);
		return result;
	}
	
}
