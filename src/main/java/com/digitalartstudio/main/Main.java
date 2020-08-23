package com.digitalartstudio.main;

import com.digitalartstudio.api.FoxtoolsAPI;
import com.digitalartstudio.api.ProxyEleven;
import com.digitalartstudio.api.ProxyListAPI;
import com.digitalartstudio.bot.EtsyBot;

public class Main {
	
	public static void main(String[] args) throws Exception {
		EtsyBot bot = new EtsyBot();
		bot.lookupProxyList(new FoxtoolsAPI());
		bot.lookupProxyList(new ProxyListAPI());
		bot.lookupProxyList(new ProxyEleven());
//		bot.lookupProxyList(new PubProxy());
		
//		bot.viewPage("https://www.etsy.com/listing/822415042/digital-zodiac-sign-virgo-printable?ref=shop_home_active_14");
//		bot.addToCart("https://www.etsy.com/listing/822335860/digital-zodiac-sign-leo-printable?ref=shop_home_active_13");
		bot.searchListingByTag("822848230", "pisces zodiac print");
		
//		pisces zodiac sign print
	}
}
