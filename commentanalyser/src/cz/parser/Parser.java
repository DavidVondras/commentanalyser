package cz.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	 
	
	public Document getPage(String url) {
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public String getLink(Element el){
		Element link = el.select("div.photo").first();
		return link.select("a").attr("abs:href");
	}
	
	public String getImageLink(Element el){
		Element link = el.select("div.photo").first();
		Elements image = el.select("img");
		String imageUrl = "";
		System.out.println(image.size());
		if (image.size() > 0) {
			imageUrl = image.first().absUrl("src");
		} else {
			String str = link.toString().substring(link.toString().indexOf("lazyload-src"),
					link.toString().length());
			imageUrl = str.substring("lazyload-src=\"".length(), str.indexOf("alt") - 2);

		}
		return imageUrl;
	}
	
	public String getFeedbacklink(String link){
		Document doc = getPage(link);
		Elements el = doc.select("div.productReviewsWarp");
		String feedbackLink = doc.getElementById("productReviewsWarp").outerHtml();
		return feedbackLink.substring(feedbackLink.indexOf("src=") + 5, feedbackLink.indexOf("\"> ")).replace("amp;", "");
	}
	
	public List<Feedback> getFeedbackList(String link){
		Document doc = getPage(link);
		Elements el = doc.getElementsByClass("review-detail");
		List<Feedback> f  = new ArrayList<>();
		for( Element e : el ){
			Feedback a = new Feedback();
			a.setFeedback(e.text());
			f.add(a);
		}
		return f;
	}
}