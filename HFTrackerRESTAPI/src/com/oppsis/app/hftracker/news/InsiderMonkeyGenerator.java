package com.oppsis.app.hftracker.news;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FuncUtil;
import com.oppsis.app.hftracker.util.PropertyUtil;

public class InsiderMonkeyGenerator implements NewsGenerator{

	private static String newsDir;
	private static String newsBaseUrl;
	private static String cssPath;
	private static String newsTpl;
	
	static{
		newsDir = PropertyUtil.getInstance().getProperty(Constants.NEWS_CONTENT_DIR);
		newsBaseUrl = PropertyUtil.getInstance().getProperty(Constants.NEWS_BASE_URL);
		cssPath = PropertyUtil.getInstance().getProperty("news.css.insidermonkey");
	}
	
	@Override
	public String createNews(int id, String fundName,String url) {
		
		String result = null;
		String dirPathStr = newsDir + FuncUtil.getNewsDir(fundName) + File.separator;
		File dirPath = new File(dirPathStr);
		if(!dirPath.exists()){
			dirPath.mkdirs();
		}
		
		String filePathStr = dirPathStr + id + Constants.HTML_FILE;
		File filePath = new File(filePathStr);
		if(!filePath.exists()){
			if(newsTpl == null){
				InputStream in = this.getClass().getResourceAsStream("/com/oppsis/app/hftracker/news/tpl/" + getTemplateName());
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String  thisLine = null;
				try {
					while((thisLine = br.readLine()) != null){
						sb.append(thisLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				newsTpl = sb.toString();
			}
			
			try {
				Document doc = Jsoup.connect(url + "?singlepage=1").get();
				String postId = doc.select(".blog-content-container .post").first().attr("id"),
						content = "",//doc.select(".entry.single-content").first().html(),
						title = doc.select(".single-post-title h1").first().html(),
						author = doc.select(".author-name a").first().html(),
						date = doc.select(".date-line").first().html().replace("Published:", "");
				
				Element contentEle = doc.select(".entry.single-content").first();
				Elements iframeEles = contentEle.getElementsByTag("iframe"),
						objectEles = contentEle.getElementsByTag("object"),
						embedEles = contentEle.getElementsByTag("embed");
				
				//remove all vedio&audio
				for(Element iframe:iframeEles){
					iframe.remove();
				}
				
				for(Element object:objectEles){
					object.remove();
				}
				
				for(Element embed:embedEles){
					embed.remove();
				}
				
				content = contentEle.html();
				
				String newsHtml = newsTpl.replace("$content$", content)
						.replace("$title$", title).replace("$id$", postId)
						.replace("$author$", author).replace("$date$", date)
						.replace("$css$", cssPath);
				
				PrintWriter out = new PrintWriter(filePath, "UTF-8");
				out.print(newsHtml);
				out.flush();
				out.close();
				//FileOutputStream fos = new FileOutputStream(filePath);
				//fos.write(newsHtml.getBytes());
				//fos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		result = newsBaseUrl + FuncUtil.getNewsDir(fundName) + "/" + id + Constants.HTML_FILE;
		
		return result;
	}

	@Override
	public String getTemplateName() {
		return "insidermonkey.tpl";
	}
	
	public static void main(String[] args) {
		InsiderMonkeyGenerator generator = new InsiderMonkeyGenerator();
		generator.createNews(91799, "Carl Icahn$Icahn Capital Lp", 
				"http://www.insidermonkey.com/blog/sunedison-inc-sune-american-airlines-group-inc-aal-ezcorp-inc-ezpw-some-bullish-picks-from-robin-hood-investors-conference-333007/");
	}

}
