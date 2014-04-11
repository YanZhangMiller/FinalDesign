package tools;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlProcessor {

	static final String[] countries = { "ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cr", "cu", "cv", "cx", "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "er", "es", "et", "eu", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir", "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sk", "sl", "sm", "sn", "so", "sr", "st", "sv", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tl", "tm", "tn", "to", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu", "za", "zm", "zw" };
    static final String[] departments = { "net", "com", "edu", "org", "gov", "mil" };
    static final Pattern reg = Pattern.compile("^(?<header>http[s]?://)?(?<domain>[^/]+)(?<page>/[^#\\?]+)?.*");
    
    String fullurl = "";
    public String getFullurl() {
		return fullurl;
	}
	public String getCountry() {
		return country;
	}
	public String getBlock() {
		return block;
	}
	public String getWebsite() {
		return website;
	}
	public String getDepartment() {
		return department;
	}
	public String getDomain() {
		return domain;
	}
	public String getPureurl() {
		return pureurl;
	}
	public String getError() {
		return error;
	}
	
	String country = "";
    String block = "";
    String website = "";
    String department = "";
    String domain = "";
    String pureurl = "";
    String error = null;
    
	public UrlProcessor(String url)
	{
		if (url == null || url.equals(""))
		{
			error = "Empty string";
			return;
		}
		fullurl = url;
		Matcher m = reg.matcher(url);
		if (!m.find())
		{
			error = "Invalid Url";
			return;
		}
		domain = m.group("domain");
		if (m.group("header")!=null)
		{
			pureurl = m.group("header") + domain;
		}
		else
		{
			pureurl = "http://" + domain;
		}
		if (m.group("page")!=null)
			pureurl += m.group("page");
		String[] l = domain.split("\\.");
		int last = l.length;
		if (last>0)
			for (String x : countries)
			{
				if (x.equals(l[last -1]))
				{
					country = x;
					last --;
					break;
				}
			}
		if (last>0)
			for (String x : departments)
			{
				if (x.equals(l[last - 1]))
				{
					department = x;
					last --;
					break;
				}
			}
		website = l[last -1];
		last--;
		if (last>0)
		{
			String t = l[0];
			for (int i=1;i<last;i++)
				t += "." + l[i];
			if (!t.equals("www"))
				block = t;
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UrlProcessor u = new UrlProcessor("http://news.163.com/14/0328/04/9OD9NPK700014AED.html?f=resysBvalid2#apage_resys");
		if (u.getError()!=null)
			System.out.println(u.getError());
		else
		{
			System.out.println(u.getFullurl());
			System.out.println(u.getDomain());
			System.out.println(u.getCountry());
			System.out.println(u.getDepartment());
			System.out.println(u.getWebsite());
			System.out.println(u.getBlock());
			System.out.println(u.getPureurl());
			
		}
		
	}

}
