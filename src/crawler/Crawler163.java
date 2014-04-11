package crawler;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import tools.MySql;
import tools.UrlProcessor;


public class Crawler163 extends WebCrawler{

	static final Pattern PageTest = Pattern.compile("^(?:http://)?.+\\.163\\.com/\\d{2}/\\d{4}/\\d{2}/.+\\.html");
	static final Pattern getTime = Pattern.compile("(?<time>\\d{4}-\\d{2}-\\d{2}\\s*\\d{2}:\\d{2}:\\d{2})");
	static final Pattern getSource = Pattern.compile("<[Aa][^>]+href=(?<url>\\S+)[^>]+>(?<source>[^<>]+)</[aA]>");
	static final String[] skipList = {"open","v","house","bbs","mall","yuedu","book","music","vip","reg","mail","baoxian","game","a","auto"};
	public Crawler163()
	{
		super(0);
	}
	public Crawler163(int depth)
	{
		super(depth);
	}
	public boolean followUrl(Url url)
	{
		if (!super.followUrl(url))
			return false;
		UrlProcessor p = new UrlProcessor(url.link());
		String b = p.getBlock().toLowerCase();
		for (String x : skipList)
		{
			if (b.startsWith(x + "."))
				return false;
		}
		if (p.getPureurl().toLowerCase().contains("/xunren/"))
			return false;
		return true;
		
	}
	@Override
	public void siteProcessor(Document d,Dictionary<String,Object>dic) {
		// TODO Auto-generated method stub
		for (Element e : d.getElementsByTag("a"))
		{
			if (e.attr("class").equals("ep-pages-all"))
			{
				try
				{
					d = Jsoup.connect(e.absUrl("href")).get();
					break;
				}catch (Exception ex) {}
			}
			
		}
		
		
		
		//content
		for (Element e: d.getElementsByTag("div"))
		{
			if (e.attr("id").equals("endText"))
			{
				dic.put("content", e.text());
				dic.put("html", e.html().replaceAll("<iframe[^>]+>[^<>]+</iframe>", ""));
				if (!dic.get("content").equals(""))
					break;
			}
		}
		
		//time & source
		for (Element e : d.getElementsByTag("div"))
		{
			if (e.attr("class").contains("ep-info"))
			{
				Matcher ma = getTime.matcher(e.html());
				if (ma.find())
					dic.put("pubtime", ma.group("time"));
				
				ma = getSource.matcher(e.html());
				if (ma.find())
				{
					dic.put("fromsite",ma.group("source"));
					dic.put("fromurl", ma.group("url"));
				}
				break;
			}
		}
			
	}

	@Override
	public void onError(Url errorUrl, Status statusError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPage(String url) {
		// TODO Auto-generated method stub
		return PageTest.matcher(url).find();
		
	}

	@Override
	public boolean isLink(String url) {
		// TODO Auto-generated method stub
		return super.isLink(url);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "163";
	}

	@Override
	public String getMainPage() {
		return "http://www.163.com";
	}

}
