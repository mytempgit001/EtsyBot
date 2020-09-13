package com.digitalartstudio.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.digitalartstudio.constants.Constants;
import com.digitalartstudio.network.HTTPClient;
import com.digitalartstudio.proxy.providers.ProxyProvider;

public class ProxyWorker {
	
	private List<ProxyProvider> proxyProviders = new ArrayList<>();
	
	public void reveal(ProxyProvider... proxyProvider) {
		Stream.of(proxyProvider).forEach(proxy -> {
			proxy.updateHosts();
			proxyProviders.add(proxy);
		});
	}
	
	public Map<String, Integer> filter() {
//		TODO дописать
//		HTTPClient httpClient = new HTTPClient();
		Map<String, Integer> whiteListHosts = new HashMap<>();
		
		proxyProviders.forEach(proxy -> {
			proxy.getRemoteHosts().forEach((ip, port) -> {
				whiteListHosts.put(ip, port); 
//				int rcode = 0;
//				try {
//					httpClient.setWebProxy(ip, port);
//					httpClient.openConnectionProxy(Constants.ETSY_HOME);
//					
//					rcode = httpClient.getConnection().getResponseCode();
//					if(rcode == 200)
//						whiteListHosts.put(ip, port); 
//				}catch(Exception e) {
//					e.printStackTrace();
//				}
//				System.out.println(ip + ":" + port + " - " + rcode);
			});
		});
		return whiteListHosts;
	}

	public void launchUpdater() {
//		TODO дописать
		filter();
	}
	
	public List<ProxyProvider> getProxyProviders() {
		return proxyProviders;
	}

	public void setProxyProviders(List<ProxyProvider> proxyProviders) {
		this.proxyProviders = proxyProviders;
	}
}