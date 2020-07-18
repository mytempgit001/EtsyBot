package com.digitalartstudio.main;

import com.digitalartstudio.api.FoxtoolsAPI;
import com.digitalartstudio.api.ProxyListAPI;
import com.digitalartstudio.bot.Bot;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		Bot bot = new Bot();
		bot.lookupProxyList(new FoxtoolsAPI());
		bot.lookupProxyList(new ProxyListAPI());
		bot.viewPage("https://www.etsy.com/listing/822335860/zodiac-sign-digital-leo-printable?ref=shop_home_active_10&pro=1");
		bot.viewPage("https://www.etsy.com/listing/840837007/set-of-zodiac-signs-digital-files?ref=shop_home_active_1&pro=1");
		bot.viewPage("https://www.etsy.com/listing/840823719/summer-prints-set-of-posters-cocktails?ref=shop_home_active_2&pro=1");
//		bot.viewPage("https://www.pinterest.ru/pin/836965911991191422/feedback/?invite_code=fa03daf459464f54b14369b162ae11c6&sender_id=836966049407813314");
	}
}
