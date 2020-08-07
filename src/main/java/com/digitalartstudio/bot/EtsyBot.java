package com.digitalartstudio.bot;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.digitalartstudio.network.HTTPClient;

public class EtsyBot extends Bot{
	
	private String proxyIp = "157.230.165.31";
	private int proxyPort = 10492;
	
	public void addToCart() throws Exception{
		
		HTTPClient http = new HTTPClient();
//		http.openConnectionUsingProxy("https://www.etsy.com/listing/836698703/digital-zodiac-sign-libra-printable?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=libra+print&ref=sr_gallery-3-29&organic_search_click=1",
//				proxyIp, proxyPort);
		http.openConnection("https://www.etsy.com/listing/836698703/digital-zodiac-sign-libra-printable?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=libra+print&ref=sr_gallery-3-29&organic_search_click=1");
		http.setHeader("User-Agent", "Mozilla/5.0");
		http.setHTTPMethod("GET");
		
		List<String> list = http.getCookies().get("Set-Cookie").stream().map(str -> str.split(";")[0]).collect(Collectors.toList());

		list.add(retriveUAcookie());
		list.add("_uetsid=cbf79bf467e342a5f7f60b40689047c7");
		list.add("_uetvid=a373ed4355e137df42fd28f5bb215c48");
		
		String cookie = list.stream().collect(Collectors.joining("; ")) + ";";
		String params = retriveData(http.readResponse().toString());
		http.sendPost("https://www.etsy.com/cart/listing.php", params, cookie, "157.230.165.31", 10492);
	}
	
	public String retriveUAcookie() throws Exception {
//		HTTPClient http = new HTTPClient();
//		http.openConnection("https://site.etsystatic.com/ac/primary/js/ru/base.76536d25b8e204bd5bf2.js");
//		http.setHeader("User-Agent", "Mozilla/5.0");
//		System.out.println(http.readResponse().toString());
		
		return "ua=531227642bc86f3b5fd7103a0c0b4fd6";
	}
	
	public String retriveData(String html) {
		Document doc = Jsoup.parse(html);
		Element form = doc.getElementsByClass("add-to-cart-form").first();
		Elements inputs = form.getElementsByTag("input");
		
		StringBuilder urlParameters = new StringBuilder();
		urlParameters.append("{");
		for(int i = 0; i < inputs.size(); i++) {
			if(i == inputs.size() - 1) {
				urlParameters.append("\"" + inputs.get(i).attr("name") + "\":" + "\"" + inputs.get(i).val()+ "\"");
			}else {
				urlParameters.append("\"" + inputs.get(i).attr("name") + "\":" + "\"" + inputs.get(i).val()+ "\"" + ", " + System.lineSeparator());
			}
		}
		urlParameters.append("}");
		return urlParameters.toString();
	}
}
