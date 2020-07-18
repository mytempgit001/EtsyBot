package com.digitalartstudio.bot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.digitalartstudio.json.JSONResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bot {

	public static void main(String[] args) throws Exception {
		Bot bot = new Bot();
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonProxyList = bot.sendGet();
		JSONResponse response = mapper.readValue(jsonProxyList, JSONResponse.class);
		
		System.out.println(response.toString());
//		bot.sendGetProxy();
	}
	
	
	public String sendGet() throws Exception {

        String url = "http://api.foxtools.ru/v2/Proxy?page=2";
//		String url = "https://www.etsy.com/listing/832696465/aquamarine-bracelet-raw-stone-jewelry?ref=hp_rf-2&pro=1&frs=1";

		
        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("GET");

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            String html = response.toString();
            System.out.println(html);
            return html;
        }

    }

    public String sendPost() throws Exception {
        String url = "http://46.101.113.185/";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("POST");
//        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
//        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "https://www.etsy.com/listing/832696465/aquamarine-bracelet-raw-stone-jewelry?ref=hp_rf-2&pro=1&frs=1";
//
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            System.out.println(response.toString());
            return response.toString();
        }

    }

    
    public String sendGetProxy() throws Exception {

		String url = "https://www.etsy.com/listing/832696465/aquamarine-bracelet-raw-stone-jewelry?ref=hp_rf-2&pro=1&frs=1";
		Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("118.163.65.59", 8080));
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection(webProxy);

        httpClient.setRequestMethod("GET");

//        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + httpClient.getResponseCode());

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            String html = response.toString();
            System.out.println(html);
            return html;
        }

    }
    
}
