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


public class CrawlerXinhuanet extends WebCrawler {
	
	static final Pattern getSource = Pattern.compile("来源：\n(?<source>.+)\n");
	static final Pattern PageTest = Pattern.compile("^(?:http://)?.+\\.xinhuanet\\.com/(?<block>[^/]+)/(?<year>\\d{4}-\\d{2})/(?<day>\\d{2})/.+\\.htm");


	@Override
	public void siteProcessor(Document d,Dictionary<String,Object> dic) {
		// TODO Auto-generated method stub
			
			
			if (d.getElementById("div_currpage")!=null)
			{
				for (Element e : d.getElementById("div_currpage").getAllElements())
					if (e.text().contains("上一页"))
						return;
			}
			
			UrlProcessor p = new UrlProcessor(d.baseUri());
			dic.put("url", p.getPureurl());
			if (!d.getElementsByTag("title").isEmpty())
			{
				String t = d.getElementsByTag("title").first().text() + "-Unknow";
				dic.put("title", t.split("-")[0]);
				dic.put("possite", t.split("-")[1]);
			}
			else
			{
				dic.put("title",  d.getElementsByTag("h1").first().text());
				dic.put("possite", p.getBlock());
			}
			
			for (Element e : d.getElementsByTag("meta"))
			{
				if (e.attr("name").equals("keywords"))
				{
					dic.put("keywords",e.attr("content").replaceAll("[ ,]", "\t"));
					break;
				}
			}
			
			
			
			if ((d.getElementById("content"))!=null)
			{
				Element e = d.getElementById("content");
				String con = e.text();
				String html = e.html();
				
				if (d.getElementById("div_currpage")!=null)
				{
					for (Element x : d.getElementById("div_currpage").getElementsByTag("a"))
					{
						String h = x.attr("href");
						try
						{
							Document dt = Jsoup.connect(h).get();
							con += dt.getElementById("content").text();
							html += dt.getElementById("content").html();
						}
						catch (Exception ex){}
					}
				}
				dic.put("content", con);
				dic.put("html", html);
			}
			
			if (d.getElementById("source")!=null)
			{
				Matcher m = getSource.matcher(d.getElementById("source").text());
				if (m.find())
				{
					dic.put("fromsite", m.group("source"));
				}
			}
			if (dic.get("fromsite") == null)
			{
				Matcher m = getSource.matcher(d.body().text());
				if (m.find())
					dic.put("fromsite", m.group("source"));
			}
			
			if (d.getElementById("pubtime")!=null)
				dic.put("pubtime",d.getElementById("pubtime").text().replace("年", "-").replace("月","-").replace("日", ""));
			else
			{
				Matcher ma = PageTest.matcher(d.baseUri());
				if (ma.find())
				{
					dic.put("pubtime",ma.group("year") + "-" + ma.group("day"));
				}
			}
			dic.put("posurl", p.getDomain());
			dic.put("lastrefresh", new Date());
			dic.put("processed", false);
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
		if (url.startsWith("javascript:"))
			return false;
		UrlProcessor p = new UrlProcessor(url);
		if (!p.getWebsite().equalsIgnoreCase(getName()))
			return false;
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Xinhuanet";
	}

	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return "http://www.xinhuanet.com/";
	}

}
