package com.digitalartstudio.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.digitalartstudio.json.ProxyResultItem;
import com.digitalartstudio.json.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FoxtoolsAPI implements ProxyAPI{
	
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	@Override
	public Map<String, Integer> getIpAndPort() {
		return response.getItems().stream().collect(Collectors.toMap(ProxyResultItem::getIp, ProxyResultItem::getPort));
	}

	@Override
	public void readResponse(StringBuilder json) {
		try {
			response =  new ObjectMapper().readValue(json.toString(), FoxtoolsAPI.class).getResponse();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getUrls() {
		return List.of("http://api.foxtools.ru/v2/Proxy",
				"http://api.foxtools.ru/v2/Proxy?page=2");
	}
}
