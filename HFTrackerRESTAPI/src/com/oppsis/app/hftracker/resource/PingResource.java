package com.oppsis.app.hftracker.resource;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

@Path("/v1/ping")
public class PingResource extends BaseResource{
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject pong() {
		JSONObject result = new JSONObject();
		try {
			result.put("status", "OK");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
