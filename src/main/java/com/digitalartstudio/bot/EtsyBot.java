package com.digitalartstudio.bot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.digitalartstudio.network.HTTPClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EtsyBot extends Bot{
	
	private String proxyIp = "157.230.165.31";
	private int proxyPort = 10492;
	
	public void addToCart() throws Exception{
		
//		HTTPClient http = new HTTPClient();
////		http.openConnectionUsingProxy("https://www.etsy.com/listing/836698703/digital-zodiac-sign-libra-printable?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=libra+print&ref=sr_gallery-3-29&organic_search_click=1",
////				proxyIp, proxyPort);
//		http.openConnection("https://www.etsy.com/listing/836698703/digital-zodiac-sign-libra-printable?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=libra+print&ref=sr_gallery-3-29&organic_search_click=1");
//		http.setHeader("User-Agent", "Mozilla/5.0");
//		http.setHeader("Connection", "Keep-Alive");
//		http.setHTTPMethod("GET");
		
		HttpsURLConnection http = (HttpsURLConnection) new URL("https://www.etsy.com/listing/841368407/wash-your-hands-digital-covid-bathroom?ref=shop_home_active_1")
				.openConnection();
		http.setRequestProperty("User-Agent", "Mozilla/5.0");
		http.setRequestProperty("Connection", "Keep-Alive");
		http.setRequestMethod("GET");
		
		StringBuilder response = new StringBuilder();
		 try (BufferedReader in = new BufferedReader(
	                new InputStreamReader(http.getInputStream()))) {
	            String line;
	            while ((line = in.readLine()) != null) {
	                response.append(line + System.lineSeparator());
	            }
	        }
		
		String html = response.toString();
		String cookie = http
					.getHeaderFields()
					.get("Set-Cookie")
					.stream()
					.filter(str -> str.contains("uaid"))
					.map(s -> s.split(";")[0])
					.collect(Collectors.joining());
		
		ObjectMapper mapper = new ObjectMapper();
		POSTForm post = parsePOSTForm(html);
		String params = mapper.writeValueAsString(post);
		
		http.disconnect();
		
		http = (HttpsURLConnection) new URL("https://www.etsy.com/listing/")
				.openConnection();
		http.setRequestMethod("POST");
		http.setRequestProperty("User-Agent", "Mozilla/5.0");
		http.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		http.setRequestProperty("Connection", "Keep-Alive");
		http.setRequestProperty("Cookie", cookie);
		
		
		http.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(http.getOutputStream())) {
            wr.writeBytes(params);
            wr.flush();
        }
        
        System.out.println(cookie);
        System.out.println(params);
        System.out.println("Response Code : " + http.getResponseCode() + " MESSAGE: " + http.getResponseMessage());
        Map<String, List<String>> test = http.getHeaderFields();
        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(http.getInputStream()))) {
            String line2;
            StringBuilder response2 = new StringBuilder();
            while ((line2 = in.readLine()) != null) {
            	response2.append(line2);
            }
            System.out.println(response2.toString());
        }
        
        System.out.println();
	}
	
	private POSTForm parsePOSTForm(String html) {
		Element form = Jsoup.parse(html).getElementsByClass("add-to-cart-form").first();
		Elements inputs = form.getElementsByTag("input");
		
		POSTForm post = new POSTForm();
		
		inputs.forEach(input -> {
			switch(input.attr("name")) {
				case "listing_id": post.setListing_id(input.val()); break;
				case "ref": post.setRef(input.val()); break;
				case "listing_inventory_id": post.setListing_inventory_id(input.val()); break;
				case "shipping_method_id": post.setShipping_method_id(input.val()); break;
				case "quantity": post.setQuantity(Integer.parseInt(input.val())); break;
				case "_nnc": post.set_nnc(input.val()); break;
			}
		});
		return post;
	}
}
