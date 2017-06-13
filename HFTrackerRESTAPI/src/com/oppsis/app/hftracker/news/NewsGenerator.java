package com.oppsis.app.hftracker.news;

public interface NewsGenerator {
	String createNews(int id,String fundName,String url);
	
	String getTemplateName();
}
