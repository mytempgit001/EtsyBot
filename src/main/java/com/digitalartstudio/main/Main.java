package com.digitalartstudio.main;

import com.digitalartstudio.bot.Bot;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		Bot bot = new Bot();
		bot.lookupProxyList("http://api.foxtools.ru/v2/Proxy");
		bot.lookupProxyList("http://api.foxtools.ru/v2/Proxy?page=2");
		
		bot.viewPage();
	}
}
