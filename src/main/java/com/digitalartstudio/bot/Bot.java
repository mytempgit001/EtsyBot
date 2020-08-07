package com.digitalartstudio.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.digitalartstudio.api.ProxyAPI;
import com.digitalartstudio.network.HTTPClient;

public abstract class Bot {
	
	private List<ProxyAPI> proxies;
	
	public Bot() {
		proxies = new ArrayList<>();
	}
	
	public void viewPage(String... pages) {
		proxies.forEach(proxy -> {
			new Thread(() -> {
				Map<String, Integer> remotes = proxy.getRemoteHosts();
				remotes.forEach((proxyIp, proxyPort) -> {
					for(String destUrl : pages) {
						try {
							HTTPClient httpClient = new HTTPClient();
							httpClient.openConnectionUsingProxy(destUrl, proxyIp, proxyPort);
							httpClient.setHeader("User-Agent", "Mozilla/5.0");
							httpClient.setHeader("Keep-Alive", "300");
							httpClient.setHTTPMethod("GET");
							httpClient.connect();
							System.out.println("OK: " + httpClient.getResponseCode() + ", using proxy? " + httpClient.usingProxy() + ", " + proxyIp + ":" + proxyPort);
						}catch(Exception e) {
							e.printStackTrace();
							break;
						}
					}
				});
			}).start();
		});
	}
	
	public void lookupProxyList(ProxyAPI api) {
		HTTPClient httpClient = new HTTPClient();
		api.getUrls().forEach(url -> {
			try {
				httpClient.openConnection(url);
				StringBuilder httpResponse = httpClient.readResponse();
				api.readResponse(httpResponse);
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
		proxies.add(api);
	}
}
