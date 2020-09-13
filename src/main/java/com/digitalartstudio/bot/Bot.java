package com.digitalartstudio.bot;

import java.util.HashMap;
import java.util.Map;

import com.digitalartstudio.network.HTTPClient;
import com.digitalartstudio.proxy.ProxyWorker;
import com.digitalartstudio.proxy.providers.ProxyProvider;

public class Bot {
	
	protected Map<String, Integer> whiteListHosts = new HashMap<>();
	protected ProxyWorker pWorker = new ProxyWorker();
	
	public void lookupProxyList(ProxyProvider... proxyProvider) {
		pWorker.reveal(proxyProvider);
	}
	
	public void launchProxyFilter() {
		whiteListHosts = pWorker.filter();
	}
	
	public void launchProxyUpdater() {
		pWorker.launchUpdater();
	}
	
	public void viewPage(HTTPClient client, String... pages) throws Exception {
		for(String destUrl : pages) {
			client.openSecureConnectionProxy(destUrl);
			client.setDeafaultOptions("GET");
			client.setCookiesAutomatically();
			client.readHTTPBodyResponse();
		}
	}
}
