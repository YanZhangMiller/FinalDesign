package crawler;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Url;
import net.vidageek.crawler.page.OkPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import tools.MySql;
import tools.UrlProcessor;



public abstract class WebCrawler implements PageVisitor {
	public final int MAX_DEPTH;
	public WebCrawler()
	{
		MAX_DEPTH = 0;
	}
	public WebCrawler(int depth)
	{
		MAX_DEPTH = depth;
	}
	MySql m = new MySql();
	
	public abstract boolean isPage(String url);
	public boolean isLink(String url)
	{
		if (url.startsWith("javascript:"))
			return false;
		UrlProcessor p = new UrlProcessor(url);
		if (!p.getWebsite().equalsIgnoreCase(getName()))
			return false;
		return true;
	}
	public boolean followUrl(Url url) {
		// TODO Auto-generated method stub
		if (!isLink(url.link()))
			return false;
		if (m.isExist("site" + getName(), url.link()))
			return false;
		if (MAX_DEPTH > 0 && url.depth()>MAX_DEPTH)
			return false;
		return true;
	}
	public abstract void siteProcessor(Document d,Dictionary<String,Object> dic);
	public void visit(Page page)
	{
		Document d = Jsoup.parse(page.getContent(),page.getUrl());
		Dictionary<String,Object> dic = new Hashtable<String,Object>();
		System.out.println("crawling: " + d.baseUri());
		UrlProcessor p = new UrlProcessor(d.baseUri());
		
		//title
		if (!d.getElementsByTag("title").isEmpty())
		{
			String t = d.getElementsByTag("title").first().text() + "_Unknow";
			dic.put("title", t.split("_")[0]);
			dic.put("possite", t.split("_")[1]);
		}
		else
		{
			dic.put("title",  d.getElementsByTag("h1").first().text());
			dic.put("possite", p.getBlock());
		}
		//keyword
		for (Element e : d.getElementsByTag("meta"))
		{
			if (e.attr("name").equals("keywords"))
			{
				dic.put("keywords",e.attr("content").replaceAll("[ ,]", "\t"));
				break;
			}
		}
		//processed
		dic.put("processed", false);
		//url
		dic.put("url", p.getPureurl());
		//postUrl
		dic.put("posurl", p.getDomain());
		//lastrefresh
		dic.put("lastrefresh", new Date());
		
		siteProcessor(d,dic);
		m.insertData("site" + getName(), dic);
		System.out.println(d.baseUri() + " crawled.");
		
	}
	public abstract String getName();
	public abstract String getMainPage();
	public void crawOnePage(String url)
	{
		try
		{
			Document dt = Jsoup.connect(url).get();
			Page p = new OkPage(url,dt.html());
			visit(p);
		}
		catch (Exception ex)
		{
			
		}
	}
}
