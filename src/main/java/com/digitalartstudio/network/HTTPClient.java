package com.digitalartstudio.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTPClient {
	public StringBuilder sendGET(String url) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return sendHttpGet(connection);
	}
	
	public String sendGETUsingProxy(String url, String ip, int port) throws Exception{
		Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(webProxy);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Keep-Alive", "300");
//        connection.setRequestProperty("User-Agent", "Chrome/0.2.149.27");
        connection.setRequestMethod("GET");
        connection.connect();
    	return String.valueOf(connection.getResponseCode()) + " - using proxy? " + connection.usingProxy();
//        return sendHttpGet(connection);
        
	}
	
	private StringBuilder sendHttpGet(HttpURLConnection connection) throws Exception{
		connection.setRequestMethod("GET");
		
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line + System.lineSeparator());
            }
            return response;
        }
	}
}
