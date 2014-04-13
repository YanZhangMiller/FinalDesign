import org.crawler.buaa.Crawler163;
import org.crawler.buaa.WebCrawler;

import net.vidageek.crawler.PageCrawler;
import net.vidageek.crawler.config.CrawlerConfiguration;
import net.vidageek.crawler.visitor.DoesNotFollowVisitedUrlVisitor;


public class CrawControler {
	WebCrawler crawler;
	CrawlerConfiguration cfg;
	public CrawControler(WebCrawler w)
	{
		crawler = w;
		cfg = new CrawlerConfiguration(crawler.getMainPage());
	}
	public void start()
	{
		PageCrawler cp = new PageCrawler(cfg);
		cp.crawl(new DoesNotFollowVisitedUrlVisitor (cfg.beginUrl(), crawler));
	}
	public static void main(String [] args)
	{
		//WebCrawler c163 = new Crawler163();
		//CrawlerConfiguration cfg = new CrawlerConfiguration(c163.getMainPage());
		//PageCrawler crawler = new PageCrawler(cfg);
		//crawler.crawl(new DoesNotFollowVisitedUrlVisitor (cfg.beginUrl(), c163));
		
		//CrawControler cXinhuanet = new CrawControler(new CrawlerXinhuanet());
		//cXinhuanet.start();
		CrawControler c163 = new CrawControler(new Crawler163(5));
		c163.start();
		
	}
}
