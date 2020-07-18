package com.digitalartstudio.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTPClient {
	public static void main(String[] args) throws Exception {
		HTTPClient h = new HTTPClient();
		String test = h.sendGETUsingProxy("https://www.etsy.com/listing/822335860/zodiac-sign-digital-leo-printable?ref=shop_home_active_10&pro=1", "80.94.229.172", 3128);
		System.out.println(test);
	}
	public StringBuilder sendGET(String url) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return sendHttpGet(connection);
	}
	
	public String sendGETUsingProxy(String url, String ip, int port) throws Exception{
		Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(webProxy);
        connection.setRequestMethod("GET");
        connection.connect();
    	return String.valueOf(connection.getResponseCode());
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
