package com.digitalartstudio.bot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.digitalartstudio.constants.Constants;
import com.digitalartstudio.network.HTTPClient;

public class EtsyBot extends Bot{
	
	public void executeInPoolThreadBatchBot2(Map<String, List<String>> data) {
		ExecutorService executor = Executors.newFixedThreadPool(800); 
		data.forEach((id, tags) -> {
			tags.forEach(tag -> {
				whiteListHosts.forEach((ip, port) -> {
						Runnable runnable = () -> {
							try {
								HTTPClient client = new HTTPClient(ip, port, "HTTP");
								viewPage(client, Constants.ETSY_HOME);
								
								client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));   
								client.disconnect();
								
								String correctTag = tag.replace(" ", "%20");
								String href = Constants.ETSY_HOME + "search?q=" + correctTag;
								String html;
								System.out.println(href + " " + ip + ":" + port);
								do {
									html = performEtsySearch(client, href);
									href = parseListingOnSearchResult(html, id);
									
									client.disconnect();
									client.getSessCokies().put("search_options", "{\"prev_search_term\":\"" + correctTag + "\",\"item_language\":null,\"language_carousel\":null}");
									client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
									System.out.println(href + " " + ip + ":" + port);
								}while(href.length() != 0 && !href.contains("/listing/" + id));
								
								if(href == null || href.length() == 0) 
									throw new IllegalArgumentException("Не удалось найти листинг по заданному тэгу");
									
//								addToCart(client, href);
								viewPage(client, href);
								System.out.println("DONE: " + client.getSessCokies().get(Constants.ETSY_SESS_UAID) + " " + ip + ":" + port);
							}catch(Exception e) {
								if(e instanceof IllegalArgumentException)
									System.out.println("NOPE: " + tag + " " + e.getMessage() + " " + ip + ":" + port);
								else 
									whiteListHosts.remove(ip, port);
							}
							System.out.println("HOSTS: " + whiteListHosts.size());
						};
					executor.execute(runnable);
				});
			});
		});
		executor.shutdown();
	}
	
	public void executeInPoolThreadBatchBot(String id, String tag) {
		final String correctTag = tag.replace(" ", "%20");

		ExecutorService executor = Executors.newFixedThreadPool(300); 

		whiteListHosts.forEach((ip, port) -> {
				Runnable runnable = () -> {
					try {
						HTTPClient client = new HTTPClient(ip, port, "HTTP");
						viewPage(client, Constants.ETSY_HOME);
						
						client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));   
						client.disconnect();
						
						String href = Constants.ETSY_HOME + "search?q=" + correctTag;
						String html;
						System.out.println(href + " " + ip + ":" + port);
						do {
							html = performEtsySearch(client, href);
							href = parseListingOnSearchResult(html, id);
							
							client.disconnect();
							client.getSessCokies().put("search_options", "{\"prev_search_term\":\"" + correctTag + "\",\"item_language\":null,\"language_carousel\":null}");
							client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
							System.out.println(href + " " + ip + ":" + port);
						}while(href.length() != 0 && !href.contains("/listing/" + id));
						
						if(href == null || href.length() == 0) 
							throw new IllegalArgumentException("Не удалось найти листинг по заданному тэгу");
							
//						addToCart(client, href);
						viewPage(client, href);
						System.out.println("DONE: " + client.getSessCokies().get(Constants.ETSY_SESS_UAID) + " " + ip + ":" + port);
					}catch(Exception e) {
						if(e instanceof IllegalArgumentException)
							System.out.println("NOPE: " + e.getMessage() + " " + ip + ":" + port);
						else
							whiteListHosts.remove(ip, port);
					}
				};
				executor.execute(runnable);
			});
		
		executor.shutdown();
	}
	
	public String performEtsySearch(HTTPClient client, String url) throws Exception {
		client.openSecureConnectionProxy(url);
		client.setDeafaultOptions("GET");
		client.setCookiesAutomatically();
		return client.readHTTPBodyResponse().toString();
	}
	
	public String parseListingOnSearchResult(String html, String id) {
	    Document doc = Jsoup.parse(html);
	    
	    Element div = doc.getElementsByAttributeValue("data-listing-id", id).first();
	    if(div!=null) 
	    	return div.select("a").first().attr("href");
	    
	    Elements elements = doc.select("[href*=" + "/listing/" + id + "]");
    	if(elements!=null && elements.size() > 0) 
    		return elements.attr("href");
	    
		Element li = doc.getElementsByAttributeValue("aria-label", "Review Page Results").last().select("li").last();
		return  li.select("a").first().attr("href");
	}
	
	public void addToCart(HTTPClient client, String... listing) throws Exception{
		for(String destUrl : listing) {
			client.openSecureConnectionProxy(destUrl);
			client.setDeafaultOptions("GET");
			client.setCookiesAutomatically();
			client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
			
			String html = client.readHTTPBodyResponse().toString();
			String params = parseAddigToCartPOSTForm(html);
			
			client.disconnect();
			
			client.openSecureConnectionProxy(Constants.ETSY_CART);
			client.setDeafaultOptions("POST");
			client.setCookiesAutomatically();
			client.writeHTTPBodyRequest(params);
			
			System.out.println("OK: " + client.getResponseCode() + ", using proxy? " + client.usingProxy());
			
			client.disconnect();
		}
	}
	
	public String parseAddigToCartPOSTForm(String html) {
		Element form = Jsoup.parse(html).getElementsByClass("add-to-cart-form").first();
		return form.getElementsByTag("input").parallelStream().map(input -> input.attr("name") + "=" + input.val()).collect(Collectors.joining("&"));
	}
	
	public void writeToFile(String str, String fileName) throws Exception {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    writer.write(str);
	    writer.close();
	}
}