package com.digitalartstudio.bot;

import java.util.ArrayList;
import java.util.List;

import com.digitalartstudio.json.JSONResponse;
import com.digitalartstudio.json.ProxyResultItem;
import com.digitalartstudio.json.Response;
import com.digitalartstudio.network.HTTPClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bot {
	
	private HTTPClient httpCliet;
	private List<ProxyResultItem> proxy;
	
	public Bot() {
		this.httpCliet = new HTTPClient();
	}
	
	public void viewPage() {
		
	}
	
	public void addToCart() {}
	
	
	public void lookupProxyList(String api) {
		try {
			String jsonProxyList = httpCliet.sendGET(api);
			Response response =  new ObjectMapper().readValue(jsonProxyList, JSONResponse.class).getResponse();
			
			if(proxy == null) 
				proxy = new ArrayList<>();
			
			proxy.addAll(response.getItems());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public HTTPClient getHttpCliet() {
		return httpCliet;
	}
	public void setHttpCliet(HTTPClient httpCliet) {
		this.httpCliet = httpCliet;
	}
	
}
