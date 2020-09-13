package com.digitalartstudio.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.digitalartstudio.bot.EtsyBot;
import com.digitalartstudio.proxy.providers.FoxtoolsProxyProvider;
import com.digitalartstudio.proxy.providers.ProxyElevenProxyProvider;
import com.digitalartstudio.proxy.providers.ProxyListProxyProvider;
import com.digitalartstudio.proxy.providers.PubProxyProxyProvider;

public class Main {
	
	public static void main(String[] args) throws Exception {
		EtsyBot bot = new EtsyBot();
		bot.lookupProxyList(new FoxtoolsProxyProvider(), new ProxyListProxyProvider(),
							new ProxyElevenProxyProvider(), new PubProxyProxyProvider());
		
		bot.launchProxyFilter();
//		bot.launchProxyUpdater();
		
		List<String> list1 = List.of(
				"seasonal sign", 
				"halloween poster", 
				"happy halloween art",
				"pumpkin sign svg");
				
		List<String> list2 = List.of(
				"instant zodiac sign",
				"aquarius birth sign", 
				"zodiac wall art",
				"zodiac sign print");
		
		List<String> list3 = List.of(
				"zodiac digital print",
				"zodiac printable",
				"zodiac sign poster",
				"aries print",
				"aries zodiac");
				
		List<String> list4 = List.of(
				"zodiac digital print",
				"virgo zodiac",
				"zodiac sign poster",
				"virgo poster",
				"virgo print");
		
		List<String> list5 = List.of(
				"zodiac digital print",
				"zodiac printable",
				"libra zodiac",
				"zodiac sign print",
				"zodiac sign poster",
				"libra print");
		
		List<String> list6 = List.of(
				"sagittarius gift",
				"sagittarius zodiac",
				"sagittarius star",
				"sagittarius sign",
				"sagittarius print",
				"sagittarius art");
		
		List<String> list7 = List.of(
				"zodiac sign poster",
				"zodiac printable",
				"pisces zodiac art",
				"pisces sign art",
				"pisces digital");
		
		Map<String, List<String>> map = new HashMap<>();
		map.put("867167949", list1);
		map.put("822847738", list2);
		map.put("821799862", list3);
		map.put("822415042", list4);
		map.put("836698703", list5);
		map.put("836723901", list6);
		map.put("822848230", list7);
		
		bot.executeInPoolThreadBatchBot2(map);
		
//		bot.executeInPoolThreadBatchBot("867167949", "seasonal sign");
//		bot.executeInPoolThreadBatchBot("867167949", "halloween poster");
//		bot.executeInPoolThreadBatchBot("867167949", "happy halloween art");
//		bot.executeInPoolThreadBatchBot("867167949", "pumpkin sign svg");
		
//		bot.executeInPoolThreadBatchBot("822847738", "instant zodiac sign");
//		bot.executeInPoolThreadBatchBot("822847738", "aquarius birth sign");
//		bot.executeInPoolThreadBatchBot("822847738", "zodiac wall art");
//		bot.executeInPoolThreadBatchBot("822847738", "zodiac sign print");
		
	}
}
