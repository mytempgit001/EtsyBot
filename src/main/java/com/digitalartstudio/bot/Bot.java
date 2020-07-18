package com.digitalartstudio.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.digitalartstudio.api.ProxyAPI;
import com.digitalartstudio.network.HTTPClient;

public class Bot {
	
	private HTTPClient httpClient;
	private List<ProxyAPI> proxy;
	
	public Bot() {
		this.httpClient = new HTTPClient();
		proxy = new ArrayList<>();
	}
	
	public void viewPage(String... pages) {
		proxy.forEach(proxy -> {
			proxy.getIpAndPort().forEach((ip, port) -> {
				Stream.of(pages).forEach(page -> {
					new Thread(() -> {
						try {
							String responseCode = httpClient.sendGETUsingProxy(page, ip, port);
							System.out.println("yes: " + responseCode + "  " + ip + ":" + port);
						} catch (Exception e) {
							System.out.println("no: " + ip + ":" + port + "   " + e.getMessage());
						}
					}).start();
					
				});
			});
		});
	}
	
	public void addToCart() {}
	
	
	public void lookupProxyList(ProxyAPI api) {
		api.getUrls().forEach(url -> {
			try {
				api.readResponse(httpClient.sendGET(url));
				proxy.add(api);
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public HTTPClient getHttpCliet() {
		return httpClient;
	}
	public void setHttpCliet(HTTPClient httpCliet) {
		this.httpClient = httpCliet;
	}
	
}
