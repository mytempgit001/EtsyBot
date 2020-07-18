package com.digitalartstudio.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTPClient {
	
	public String sendGET(String url) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return sendHttpGet(connection);
	}
	
	public String sendGETUsingProxy(String url, String ip, int port) throws Exception{
		Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(webProxy);

        return sendHttpGet(connection);
	}
	
	private String sendHttpGet(HttpURLConnection connection) throws Exception{
		connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
	}

//	public String sendPost() throws Exception {
//        String url = "http://46.101.113.185/";
//
//        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
//
//        httpClient.setRequestMethod("POST");
////        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
////        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//        String urlParameters = "https://www.etsy.com/listing/832696465/aquamarine-bracelet-raw-stone-jewelry?ref=hp_rf-2&pro=1&frs=1";
////Щ
//        httpClient.setDoOutput(true);
//        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
//            wr.writeBytes(urlParameters);
//            wr.flush();
//        }
//
//        int responseCode = httpClient.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
////        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);
//
//        try (BufferedReader in = new BufferedReader(
//                new InputStreamReader(httpClient.getInputStream()))) {
//
//            String line;
//            StringBuilder response = new StringBuilder();
//
//            while ((line = in.readLine()) != null) {
//                response.append(line);
//            }
//
//            //print result
//            System.out.println(response.toString());
//            return response.toString();
//        }
//
//    }
}
