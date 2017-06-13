package com.oppsis.app.hftracker.cache;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheManager {
	private static final String path = "/ehcache.xml";
	private URL url;
	private CacheManager manager;
	private static EhcacheManager ehCache;
	
	public static final String CACHE_HOLDINGS = "holdings";
	public static final String CACHE_NEWS_LIST = "news-list";
	public static final String CACHE_NEWS_CONTENT = "news-content";

	private EhcacheManager(String path) {
		url = getClass().getResource(path);
		manager = CacheManager.create(url);
	}

	public static EhcacheManager getInstance() {
		if (ehCache == null) {
			ehCache = new EhcacheManager(path);
		}
		return ehCache;
	}

	public void put(String cacheName, String key, Object value) {
		Cache cache = manager.getCache(cacheName);
		Element element = new Element(key, value);
		cache.put(element);
	}

	public Object get(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache get(String cacheName) {
		return manager.getCache(cacheName);
	}

	public void remove(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		cache.remove(key);
	}
	
	public void shutdown() {
		manager.shutdown();
	}
	
	public void putHoldings(String key, Object value) {
		Cache cache = manager.getCache(CACHE_HOLDINGS);
		Element element = new Element(key, value);
		cache.put(element);
	}

	public Object getHoldings(String key) {
		Cache cache = manager.getCache(CACHE_HOLDINGS);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache getHoldingsCache() {
		return manager.getCache(CACHE_HOLDINGS);
	}

	public void removeHoldings(String key) {
		Cache cache = manager.getCache(CACHE_HOLDINGS);
		cache.remove(key);
	}
	
	public void putNewsList(String key, Object value) {
		Cache cache = manager.getCache(CACHE_NEWS_LIST);
		Element element = new Element(key, value);
		cache.put(element);
	}

	public Object getNewsList(String key) {
		Cache cache = manager.getCache(CACHE_NEWS_LIST);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache getNewsListCache() {
		return manager.getCache(CACHE_NEWS_LIST);
	}

	public void removeNewsListCache(String key) {
		Cache cache = manager.getCache(CACHE_NEWS_LIST);
		cache.remove(key);
	}
	
	public void putNewsContent(String key, Object value) {
		Cache cache = manager.getCache(CACHE_NEWS_CONTENT);
		Element element = new Element(key, value);
		cache.put(element);
	}

	public Object getNewsContent(String key) {
		Cache cache = manager.getCache(CACHE_NEWS_CONTENT);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache getNewsContentCache() {
		return manager.getCache(CACHE_NEWS_CONTENT);
	}

	public void removeNewsContentCache(String key) {
		Cache cache = manager.getCache(CACHE_NEWS_CONTENT);
		cache.remove(key);
	}
	
	
}
