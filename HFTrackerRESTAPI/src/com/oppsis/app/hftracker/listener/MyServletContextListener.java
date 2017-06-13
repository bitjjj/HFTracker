package com.oppsis.app.hftracker.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.oppsis.app.hftracker.cache.EhcacheManager;

public class MyServletContextListener implements ServletContextListener{
	 public void contextInitialized(ServletContextEvent e) {

	  }

	  public void contextDestroyed(ServletContextEvent e) {
		  EhcacheManager.getInstance().shutdown();
	  }
}
