package org.similarity.buaa;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.tools.buaa.News;

public class NgramSimilarity implements Similarity {

	static double[] weights = {0,0,1};
	@Override
	public double getSimScore(News x, News y) {
		// TODO Auto-generated method stub
		double ret = 0;
		for (int i=0;i<weights.length;i++)
		{
			if (weights[i]!=0)
			{
				Dictionary<String,Integer> dicX = countNgram(normalize(x.content),i+1);
				Dictionary<String,Integer> dicY = countNgram(normalize(y.content),i+1);
				ret += weights[i] * cosSim(dicX,dicY);
			}
		}
		return ret;
	}
	public static Dictionary<String,Integer> countNgram(String text,int order)
	{
		Dictionary<String,Integer> ret = new Hashtable<String,Integer>();
		for (String x : text.split(" "))
		{
			for (int i=0;i<=x.length()-order;i++)
			{
				String key = x.substring(i, i+order);
				int c = 0;
					
				try 
				{
					c = ret.get(key);
					ret.remove(key);
				}
				catch (Exception ex){}
				ret.put(key, c+1);
			}
			
		}
		return ret;
		
	}
	public static double cosSim(Dictionary<String,Integer> x,Dictionary<String,Integer> y)
	{
		
		double p = 0,lx=0,ly=0;
		for (Enumeration<String> s = x.keys();s.hasMoreElements();)
		{
			String key = s.nextElement();
			lx += x.get(key) * x.get(key);
			try
			{
				p += x.get(key) * y.get(key);
			}catch (Exception e) {}
		}
		for (Enumeration<String> s = y.keys();s.hasMoreElements();)
		{
			String key = s.nextElement();
			ly += y.get(key) * y.get(key);
		}
		lx = Math.sqrt(lx);
		ly = Math.sqrt(ly);
		try {
			return p/(lx*ly);
		}
		catch (Exception ex){
			return 0;
		}
		
	}
	public static String normalize(String text)
	{
		text = text.replaceAll("[\\pP+~$`^=|<>～`$^+=|<>￥×]" , " ");
		text = text.replaceAll("\\s+", " ");
		return text;
	}
}
