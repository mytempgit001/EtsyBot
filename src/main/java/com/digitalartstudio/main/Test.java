package com.digitalartstudio.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(1);
		
		for(int i = 0; i < 30; i++) {
			Runnable r = () -> System.out.print(Thread.currentThread().getName());
			pool.submit(r);
			System.out.println(" " + i);
		}
		
		pool.shutdown();
	}
}
