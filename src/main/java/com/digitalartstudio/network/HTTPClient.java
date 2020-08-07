package com.digitalartstudio.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.digitalartstudio.bot.EtsyBot;

public class HTTPClient {
	
	private HttpURLConnection connection;
	
	public void openConnectionUsingProxy(String destUrl, String proxyIp, int proxyPort) throws MalformedURLException, IOException {
		Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
		connection = (HttpURLConnection) new URL(destUrl).openConnection(webProxy);
	}
	
	public void openConnection(String destUrl) throws MalformedURLException, IOException {
		connection = (HttpURLConnection) new URL(destUrl).openConnection();
	}
	
	public void setHTTPMethod(String method) throws ProtocolException {
		connection.setRequestMethod(method);
	}
	
	public boolean usingProxy() {
		return connection.usingProxy();
	}
	
	public void setHeader(String key, String value) {
		connection.setRequestProperty(key, value);
	}
	
	public void connect() throws IOException {
		connection.connect();
	}
	
	public int getResponseCode() throws IOException {
		return connection.getResponseCode();
	}
	
	public Map<String, List<String>> getCookies(){
		return connection == null ? null : connection.getHeaderFields();
	}
	
	public StringBuilder readResponse() throws Exception{
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
	
	public HttpURLConnection getConnection() {
		return connection;
	}
	
	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}
	
	public static void main(String[] args) throws Exception {
		EtsyBot bot = new EtsyBot();
		bot.addToCart();
	}
	
	
	public void sendPost(String url, String urlParameters, String cookie, String proxyIp, int proxyPort) throws Exception {

		HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url)
				.openConnection(/* new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort)) */);

        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpClient.setRequestProperty("cookie", cookie);
        System.out.println(httpClient.getRequestProperties());
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = httpClient.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + System.lineSeparator() + urlParameters);
//        System.out.println("cookie: " + cookie);
        System.out.println("Response Code : " + responseCode + "MESSAGE: " + httpClient.getResponseMessage());

        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            System.out.println(response.toString());
        }
    }
}
