package com.digitalartstudio.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.digitalartstudio.network.HTTPClient;

public class EtsyBot extends Bot{
	
	private String cart = "https://www.etsy.com/cart/listing.php";
	
	public void addToCart(String... listing){
		proxies.forEach(proxy -> {
			proxy.getRemoteHosts().forEach((ip, port) -> {
				new Thread(() ->  {
					for(String destUrl : listing) {
						try {
							HTTPClient client = new HTTPClient();
							client.openSecureConnectionProxy(destUrl, ip, port);
							client.setDeafaultOptions("GET");
							client.getConnection().setReadTimeout(20000);
							client.getConnection().setConnectTimeout(10000);
							
							String html = client.readHTTPBodyResponse().toString();
							String params = parseAddigToCartPOSTForm(html);
							String cookie = client.getConnection().getHeaderFields().get("Set-Cookie").parallelStream().map(s -> s.split(";")[0]).collect(Collectors.joining("; "));
							
							client.disconnect();
							
							client.openSecureConnectionProxy(cart, ip, port);
							client.setDeafaultOptions("POST");
							client.setHeader("Cookie", cookie);
							client.writeHTTPBodyRequest(params);
							
							System.out.println("OK: " + client.getResponseCode() + ", using proxy? " + client.usingProxy() + ", " + ip + ":" + port);
							
							client.disconnect();
						}catch(Exception e) {
//							e.printStackTrace();
							break;
						}
					}
				}).start();
			});
			
		});
	}
	
	private String parseAddigToCartPOSTForm(String html) {
		Element form = Jsoup.parse(html).getElementsByClass("add-to-cart-form").first();
		return form.getElementsByTag("input").parallelStream().map(input -> input.attr("name") + "=" + input.val()).collect(Collectors.joining("&"));
	}
	
	public void searchListingByTag(String id, String tag) throws Exception {
		String correctTag = tag.replace(" ", "%20");
		final String searchQuery = "https://www.etsy.com/search?q=" + correctTag;
		
		proxies.forEach(proxy -> {
			proxy.getRemoteHosts().forEach((ip, port) -> {
				new Thread(() ->  {
					try {
						Map<String, String> sessCokies = new HashMap<>();                                                                   
						                                                                                                                    
						HTTPClient client = new HTTPClient();                                                                                                                       //
						client.openConnectionProxy("https://www.etsy.com/", ip, port);                                                                       
						client.setDeafaultOptions("GET");         
						client.connect();
						client.separateCookieFromMeta().forEach(cookie -> sessCokies.put(cookie.split("=")[0], cookie.split("=")[1]));      
						client.disconnect();        
						
						String href = findAndParseListingByTag(client, searchQuery, id, sessCokies, ip, port);
						sessCokies.put("search_options", "{\"prev_search_term\":\"" + correctTag + "\",\"item_language\":null,\"language_carousel\":null}");
						
						if(href.length() == 0) {
							int page = 2;
							do {
								href = findAndParseListingByTag(client, searchQuery + "&ref=pagination&page=" + page, id, sessCokies, ip, port);
								page++;
							} while(page < 50 || href.length() > 0);
							
							if(href.length() == 0) 
								throw new IllegalArgumentException("Не удалось найти листинг по заданному тэгу");
						}
						
				    	client.openConnectionProxy(href, ip, port);
				    	client.setDeafaultOptions("GET");
						
						sessCokies.forEach((key, value) -> client.setHeader(key, value));
						
						client.connect();
						System.out.println("OK " + client.getResponseCode() + " " + client.getConnection().getResponseMessage() + ", proxy - " + client.getConnection().usingProxy() + " " + ip + ":" + port + "   " + href);
					}catch(Exception e) {
						if(e instanceof IllegalArgumentException)
							System.out.println(e.getMessage() + " " + ip + ":" + port);
//						e.printStackTrace();
					}
				}).start();
			});
			
		});
	}
	
	private String findAndParseListingByTag(HTTPClient client, String url, String id, Map<String, String> sessCokies, String ip, int port) throws Exception {
		client.openConnectionProxy(url, ip, port);
		client.setDeafaultOptions("GET");
		
		sessCokies.forEach((key, value) -> client.setHeader(key, value));
	
		String html = client.readHTTPBodyResponse().toString();
		
		client.separateCookieFromMeta().forEach(cookie -> sessCokies.put(cookie.split("=")[0], cookie.split("=")[1]));
	    client.disconnect();
	    
	    Element el = Jsoup.parse(html).getElementsByAttributeValue("data-listing-id", id).first();
		if(el == null) 
			return "";
			
		Element el1 = el.select("a").first();
		if(el1 == null) 
			return "";
		
		return el1.attr("href");
	}
}
