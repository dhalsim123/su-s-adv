package com.example.imdbg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableScheduling
public class ImdbgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbgApplication.class, args);


		ConcurrentHashMap<Long, Set<String>> pageViews = new ConcurrentHashMap<>();

		pageViews.put(1L, new HashSet<>());

		if (pageViews.get(1L).add("1")) {
			System.out.println("added 1");
		}
		if (pageViews.get(1L).add("1")) {
			System.out.println("added 1");
		}
		if (pageViews.get(1L).add("1")) {
			System.out.println("added 1");
		}

		if (pageViews.get(1L).add("2")) {
			System.out.println("added 2");
		}
	}

}
