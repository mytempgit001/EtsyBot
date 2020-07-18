package com.digitalartstudio.api;

import java.util.List;
import java.util.Map;

public interface ProxyAPI {
	
	List<String> getUrls();
	Map<String, Integer> getIpAndPort();
	void readResponse(StringBuilder response);
}
