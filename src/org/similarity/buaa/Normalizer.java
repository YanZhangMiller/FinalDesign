package org.similarity.buaa;

public class Normalizer {
	public static String normalize(String text)
	{
		text = text.replaceAll("[\\pP+~$`^=|<>～`$^+=|<>￥×]" , " ");
		text = text.replaceAll("\\s+", " ");
		return text;
	}
	public static void main(String[]args)
	{
		String t = "你好！哈，哈，,l!df";
		System.out.println(t + "\n" + normalize(t));
	}
}
