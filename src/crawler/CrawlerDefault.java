package crawler;

import java.util.Dictionary;

import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

import org.jsoup.nodes.Document;

public class CrawlerDefault extends WebCrawler {

	@Override
	public void onError(Url errorUrl, Status statusError) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPage(String url) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void siteProcessor(Document d, Dictionary<String, Object> dic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
