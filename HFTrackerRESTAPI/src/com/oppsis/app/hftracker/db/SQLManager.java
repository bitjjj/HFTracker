package com.oppsis.app.hftracker.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SQLManager {

	private static SQLManager instance = new SQLManager();
	private static Document document;
	private static Map<String, String> sqlMap = new HashMap<String,String>();

	static {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(SQLManager.class.getResourceAsStream("/com/oppsis/app/hftracker/db/sql/sqls.xml"));

			List sections = document.getRootElement().elements("section");

			for (Iterator iterSec = sections.iterator(); iterSec.hasNext();) {
				Element secEle = (Element) iterSec.next();
				List sqls = secEle.elements("sql");
				
				for (Iterator iterSql = sqls.iterator(); iterSql.hasNext();) {
					Element sqlEle = (Element) iterSql.next();
					sqlMap.put(sqlEle.attribute("id").getText(), sqlEle.getText());
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static SQLManager getInstance() {
		return instance;
	}
	
	public String getSQL(String id){
		return sqlMap.get(id);
	}
	
	public static void main(String[] args) {
		SQLManager.getInstance();
	}

}
