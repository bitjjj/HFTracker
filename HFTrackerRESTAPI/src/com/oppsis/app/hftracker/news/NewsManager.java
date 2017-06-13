package com.oppsis.app.hftracker.news;

import com.oppsis.app.hftracker.cache.EhcacheManager;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.PropertyUtil;

public class NewsManager {

	private static String CACHE_NAME = "news-content";
	
	public static String getNewsUrl(int id,String fundName,String url,String source){
		String cacheUrl = (String)EhcacheManager.getInstance().get(CACHE_NAME, String.valueOf(id));
		if(cacheUrl == null){
			String className = PropertyUtil.getInstance().getProperty(Constants.NEWS_GENERATOR_PREFIX + source);
			try {
				NewsGenerator generator = (NewsGenerator)Class.forName(className).newInstance();
				cacheUrl =  generator.createNews(id, fundName,url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(cacheUrl != null){
				EhcacheManager.getInstance().put(CACHE_NAME, String.valueOf(id), cacheUrl);
			}
		}

		return cacheUrl;
	}
	
}
