package com.oppsis.app.hftracker.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.oppsis.app.hftracker.db.SQLManager;
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.hftracker.news.NewsManager;
import com.oppsis.app.hftracker.util.Constants;

@Path("/v1/news")
public class FundNewsResource extends BaseResource{
	
	protected String getCacheName(){
		return Constants.CacheNames.CACHE_NEWS_LIST;
	}
	
	@GET
	@Path("/fund")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNewsByFundName(@QueryParam("name") String name,@QueryParam("page") int page) {
		
		if(page == -1)page = 0;

		String sql = String.format(SQLManager.getInstance().getSQL(Constants.DBSQLs.QUERY_NEWS_BY_NAME), 
				name,Integer.valueOf(page)*Constants.PAGE_SIZE_10);
		//System.out.println(sql);
		JSONObject result = queryList(sql, FundNews.class);
		
		//System.out.println(result);
		return result;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNewsUrl(@QueryParam("id") String id,@QueryParam("name") String name,@QueryParam("url") String url,@QueryParam("source") String source) {
		
		JSONObject result = new JSONObject();
		try {
			JSONObject newsContent = new JSONObject();
			newsContent.put("url", NewsManager.getNewsUrl(Integer.valueOf(id), name, url, source));
			result.put("result", newsContent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return result;
	}
	
}
